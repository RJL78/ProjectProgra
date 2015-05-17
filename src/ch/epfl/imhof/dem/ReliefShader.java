package ch.epfl.imhof.dem;

import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.awt.image.ConvolveOp;
import java.awt.image.Kernel;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;

import ch.epfl.imhof.PointGeo;
import ch.epfl.imhof.Vector3;
import ch.epfl.imhof.geometry.Point;
import ch.epfl.imhof.painting.Color;
import ch.epfl.imhof.projection.Projection;

/**
 * Classe permettant la création de cartes topographiques, utilisant des variations de couleur pour représenter des différences d'inclinaison du terrain
 * 
 * @author Raphael Laporte (251209) / Romain Leteurtre (238162)
 *
 */

public class ReliefShader {
    
    private final Projection projection;
    private final DigitalElevationModel model;
    private final Vector3 lightVector;
    
    /**
     * Constructeur pour la classe
     * 
     * @param projection : 
     * @param model : le DigitalElevationModel décrivant la topographie de la zone qui est à représenter
     * @param lightVector : le vecteur montrant la direction de l'éclairage artificiel 
     */
    public ReliefShader(Projection projection, DigitalElevationModel model, Vector3 lightVector) {
       this.projection=projection;
       this.model=model;
       this.lightVector=lightVector;
    }
    
    // Does our shit work for something on the other side of the world? 
    // Points en argument a mettre en Radians - change input type?
    /**
     * Retourne une image représentant la topographie de la zone dans le rectangle formé par bottomLeftPoint et par topRightPoint
     * 
     * @param bottomLeftPoint : Coin sud-ouest de la zone à représenter  
     * @param topRightPoint : Coin nord-est de la zone à représenter
     * @param pixelHeight : Hauteur de l'image qui va être retournée ( en pixels )
     * @param pixelWidth : Largeur de l'image qui va être retournée ( en pixels ) 
     * @param blurRadius : Le rayon de floutage qui doit être appliqué à l'image
     * @return une image représentant la topographie de la zone 
     */
    public BufferedImage shadedRelief(Point bottomLeftPoint, Point topRightPoint, int pixelHeight, int pixelWidth, double blurRadius) throws Exception {
        if (blurRadius<0) throw new IllegalArgumentException (); // I dunno if I should be doing this ... 
        int r = (int) Math.ceil(blurRadius);
        BufferedImage topo = shadedReliefRaw(pixelHeight+2*r, pixelWidth+2*r,Point.alignedCoordinateChange( new Point (r,pixelHeight+r), bottomLeftPoint, new Point(pixelWidth+r,r), topRightPoint));
        return (blurRadius == 0) ? topo : applyKernel (topo, gaussianVerticalKernel(blurRadius)).getSubimage(r, r, pixelWidth, pixelHeight);
    }
   
  
    private BufferedImage shadedReliefRaw(int pixelHeight, int pixelWidth, Function<Point,Point> coordinateChange) throws Exception {
        BufferedImage imageRelief = new BufferedImage(pixelWidth,pixelHeight,BufferedImage.TYPE_INT_RGB);
        for (int i=0;i<pixelWidth;i++) {
            for (int j=0;j<pixelHeight;j++) {
                // unnessary conversions? 
                Vector3 pointVector = model.normalAt(projection.inverse(coordinateChange.apply(new Point(i,j))));
                double angleCos = pointVector.scalarProduct(lightVector)/(pointVector.norm()*lightVector.norm());
                Color newColor = Color.rgb(0.5*(angleCos+1),0.5*(angleCos+1),0.5*(0.7*angleCos+1));
                imageRelief.setRGB(i, j, newColor.toAPIColor().getRGB());
            }
        }
        return imageRelief;
    }
    
    private ConvolveOp gaussianVerticalKernel(double radius) {
        double o = radius/3d;
        int r = (int) Math.ceil(radius);
        int n =  (2*r+1);
        float[] data = new float[n];
        float sum = 0;
        
        for (int i= -r; i<=r; i++){
           float a = (float)Math.exp(-(i*i)/(2*o*o));
           data[i+r] = a;
           sum += a ;           
        }
        
        for (int i=0; i< n; i++){
             data[i] /=sum;
        }
                
        return new ConvolveOp (new Kernel(1,n,data), ConvolveOp.EDGE_NO_OP , new RenderingHints(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON));   
    }
    
    private BufferedImage applyKernel(BufferedImage image, ConvolveOp verticalConvolution) {
        ConvolveOp horizontalConvolution = new ConvolveOp(new Kernel(verticalConvolution.getKernel().getHeight(),verticalConvolution.getKernel().getWidth(),verticalConvolution.getKernel().getKernelData(null)),ConvolveOp.EDGE_NO_OP,new RenderingHints(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON));
        return horizontalConvolution.filter(verticalConvolution.filter(image, null),null);
    }
}
