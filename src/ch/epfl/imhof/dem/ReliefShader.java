package ch.epfl.imhof.dem;

import java.awt.image.BufferedImage;
import java.awt.image.ConvolveOp;
import java.awt.image.Kernel;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;

import ch.epfl.imhof.PointGeo;
import ch.epfl.imhof.Vector3;
import ch.epfl.imhof.painting.Color;
import ch.epfl.imhof.projection.Projection;

public class ReliefShader {
    
    private final Projection projection;
    private final HGTDigitalElevationModel model;
    private final Vector3 lightVector;
    
    public ReliefShader(Projection projection, HGTDigitalElevationModel model, Vector3 lightVector) {
       this.projection=projection;
       this.model=model;
       this.lightVector=lightVector;
    }
    
    public Color[][] shadedRelief(PointGeo bottomLeftPoint, PointGeo topRightPoint, int pixelHeight, int pixelWidth, int blurRadius) {
        return null;
    }
   
    private Color[][] shadedReliefRaw(int pixelHeight, int pixelWidth,Projection proj) {
        return null;
    }
    
    private ConvolveOp gaussianVerticalKernel(double radius) {
        double o = radius/3;
        int r = (int) Math.ceil(radius);
        int n = (int) (2*r+1);
        float[] data = new float[n];
        float sum = 0;
        List<Double> gausFilter = new ArrayList<>();
        for (int i= -r; i<=r; i++){
           float a = (float)Math.exp(-(i*i)/(2*o));
           data[i+r]= a;
           sum += a*a ;           
        }
        for (int i=0; i<= 2*r+1; i++){
             data[i] /= sum;
        }
        return new ConvolveOp (new Kernel(1, n,data));
           
    }

    
    private Object wtf(Color[][] map, int radius) {
        return null;
    }
}
