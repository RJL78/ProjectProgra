package ch.epfl.imhof.dem;

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

public class ReliefShader {
    
    // manage blur radius = 0; 
    
    private final Projection projection;
    private final HGTDigitalElevationModel model;
    private final Vector3 lightVector;
    
    public ReliefShader(Projection projection, HGTDigitalElevationModel model, Vector3 lightVector) {
       this.projection=projection;
       this.model=model;
       this.lightVector=lightVector;
    }
    
    // Points en argument a mettre en Radians - change input type?

    public BufferedImage shadedRelief(Point bottomLeftPoint, Point topRightPoint, int pixelHeight, int pixelWidth, double blurRadius) {
        int r = (int)Math.ceil(blurRadius); 
  //      return shadedReliefRaw(pixelHeight+2*r, pixelWidth+2*r, Point.alignedCoordinateChange( new Point (r,pixelHeight+r), bottomLeftPoint, new Point(pixelWidth+r,r), topRightPoint));
        return applyKernel (
                 shadedReliefRaw(pixelHeight+2*r, pixelWidth+2*r, Point.alignedCoordinateChange( new Point (r,pixelHeight+r), bottomLeftPoint, new Point(pixelWidth+r,r), topRightPoint)),
                gaussianVerticalKernel(blurRadius)).getSubimage(r, r, pixelWidth, pixelHeight);  
    }
   
  
    private BufferedImage shadedReliefRaw(int pixelHeight, int pixelWidth, Function<Point,Point> coordinateChange) {
        BufferedImage imageRelief = new BufferedImage(pixelWidth,pixelHeight,BufferedImage.TYPE_INT_RGB);
        for (int i=0;i<pixelWidth;i++) {
            for (int j=0;j<pixelHeight;j++) {
                // unnessary conversions? 
                Point mapPoint = coordinateChange.apply(new Point(i,j));
                PointGeo pointGeo = projection.inverse(mapPoint);
                Vector3 pointVector = model.normalAt(pointGeo);
                double angleCos = pointVector.scalarProduct(lightVector)/(pointVector.norm()*lightVector.norm());
                Color newColor = Color.rgb(0.5*(angleCos+1),0.5*(angleCos+1),0.5*(0.7*angleCos+1));
                imageRelief.setRGB(i, j, newColor.toAPIColor().getRGB());
            }
        }
        return imageRelief;
    }
    
    private ConvolveOp gaussianVerticalKernel(double radius) {
        double o = radius/3;
        int r = (int) Math.ceil(radius);
        int n =  (2*r+1);
        float[] data = new float[n];
        float sum = 0;
        for (int i= -r; i<=r; i++){
           float a = (float)Math.exp(-(i*i)/(2*o*o));
           data[i+r]= (float)a;
           sum += a*a ;           
        }
        sum =(float)Math.sqrt(sum);
        for (int i=0; i< n; i++){
             data[i] /= sum;
             System.out.println(data[i]);
        }
        
        
        
        return new ConvolveOp (new Kernel(1,n,data), ConvolveOp.EDGE_NO_OP ,null);   
    }
    
    private BufferedImage applyKernel(BufferedImage image, ConvolveOp verticalConvolution) {
   
        ConvolveOp horizontalConvolution = new ConvolveOp(new Kernel(verticalConvolution.getKernel().getHeight(),verticalConvolution.getKernel().getWidth(),verticalConvolution.getKernel().getKernelData(null)),ConvolveOp.EDGE_NO_OP,null);
        return horizontalConvolution.filter(verticalConvolution.filter(image,null), null);
    }
}
