package ch.epfl.imhof.dem;


import java.awt.image.BufferedImage;
import java.awt.image.ConvolveOp;
import java.awt.image.Kernel;
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
    
    /**
     * Retourne une image représentant la topographie de la zone dans le rectangle formé par bottomLeftPoint et par topRightPoint
     * 
     * @param bottomLeftPoint : Coin sud-ouest de la zone à représenter  
     * @param topRightPoint : Coin nord-est de la zone à représenter
     * @param pixelHeight : Hauteur de l'image qui va être retournée ( en pixels )
     * @param pixelWidth : Largeur de l'image qui va être retournée ( en pixels ) 
     * @param blurRadius : Le rayon de floutage qui doit être appliqué à l'image
     * @return une image représentant la topographie de la zone 
     * 
     * @throws IllegalArgumentException si blurRadius<0
     * @throws Exception : exception remontée de HGTDigitalElevationModel.close() 
     */

    public BufferedImage shadedRelief(Point bottomLeftPoint, Point topRightPoint, int pixelHeight, int pixelWidth, double blurRadius) throws Exception   {
       
        if (blurRadius<0) throw new IllegalArgumentException (); 
       
        int r = (int) Math.ceil(blurRadius);
        Function<Point,Point> coordinateChange = Point.alignedCoordinateChange( new Point (r,pixelHeight+r), bottomLeftPoint, new Point(pixelWidth+r,r), topRightPoint);
        BufferedImage topo = shadedReliefRaw(pixelHeight+2*r,pixelWidth+2*r, coordinateChange);
        
        model.close();
        
        return (blurRadius == 0) ? topo : applyKernel (topo, gaussianVerticalKernel(blurRadius)).getSubimage(r, r, pixelWidth, pixelHeight);
    }
  
    // Crée la carte topographique sans floutage
    private BufferedImage shadedReliefRaw(int pixelHeight, int pixelWidth, Function<Point,Point> coordinateChange) {
        
        BufferedImage imageRelief = new BufferedImage(pixelWidth,pixelHeight,BufferedImage.TYPE_INT_RGB);
        
        for (int i=0;i<pixelWidth;i++) {
            for (int j=0;j<pixelHeight;j++) {
                
                Point mapPoint = coordinateChange.apply(new Point(i,j));
                PointGeo geoPoint = projection.inverse(mapPoint);
                Vector3 pointVector = model.normalAt(geoPoint);
                
                double angleCos = pointVector.scalarProduct(lightVector)/(pointVector.norm()*lightVector.norm());
                double redAndGreen = 0.5*(angleCos+1);
                double blue =  0.5*(0.7*angleCos+1);
                Color newColor = Color.rgb(redAndGreen, redAndGreen,blue);
                
                imageRelief.setRGB(i, j, newColor.toAPIColor().getRGB());
            }
        }
        return imageRelief;
    }
    
    // Retourne la matrice definissant le floutage vertical (à partir duquel peut etre extrapolé)
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
                
        return new ConvolveOp (new Kernel(1,n,data), ConvolveOp.EDGE_NO_OP , null);   
    }
    
    // Applique le floutage à la carte 
    private BufferedImage applyKernel(BufferedImage image, ConvolveOp verticalConvolution) {
       
        Kernel k = new Kernel(verticalConvolution.getKernel().getHeight(),
                              verticalConvolution.getKernel().getWidth(),
                              verticalConvolution.getKernel().getKernelData(null));
        
        ConvolveOp horizontalConvolution = new ConvolveOp(k,ConvolveOp.EDGE_NO_OP,null);
        return horizontalConvolution.filter(verticalConvolution.filter(image, null),null);
    }
}
