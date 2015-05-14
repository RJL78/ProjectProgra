package ch.epfl.imhof.dem;

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
    
    private Object gaussianKernel(int radius) {
        return null;
    }
    
    private Object wtf(Color[][] map, int radius) {
        return null;
    }
}
