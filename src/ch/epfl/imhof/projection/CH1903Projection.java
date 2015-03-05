package ch.epfl.imhof.projection;

import ch.epfl.imhof.PointGeo;
import ch.epfl.imhof.geometry.Point;


/** 
 * Classe permettant la projection CH1903 d'un PointGeo dans le repere WGS84 en un Point dans le repere CH1903, et inversement.
 * 
 * @author Raphael Laporte (251209) / Romain Leteurtre (238162)
 *
 */

public final class CH1903Projection implements Projection{
    
    /**
     *Projette un point du repere WGS84 au repere CH1903
     * 
     * @param L'instance de PointGeo que l'on souhaite projeter. 
     * @return La projection du parametre.
     * 
     */
    public Point project(PointGeo point){
        double longi = Math.toDegrees(point.longitude());
        double latit =  Math.toDegrees(point.latitude()); 
        double longi1 = Math.pow(10.0, -4.0)*(longi*3600-26782.5);
        double latit1 =  Math.pow(10.0, -4.0)*(latit*3600-169028.66);
        return new Point ( 600072.37 + 211455.93*longi1 - 10938.51*longi1*latit1 - 0.36*longi1*Math.pow(latit1,2) - 44.54*Math.pow(longi1,3),
                200147.07+308807.95*latit1+3745.25*Math.pow(longi1, 2)+76.63*Math.pow(latit1,2)-194.56*Math.pow(longi1,2)*latit1+119.79*Math.pow(latit1,3));
    }
    /**
     *Projette un point du repere CH1903 au repere WGS84
     * 
     * @param L'instance de Point que l'on souhaite projeter. 
     * @return La projection du parametre.
     * 
     */
    public PointGeo inverse(Point point){
        double x1 = (point.x() -600000)/1000000;
        double y1 = (point.y() -200000)/1000000;
        double longi0 = 2.6779094 + 4.728982*x1 + 0.791484*x1*y1 + 0.1306*x1*Math.pow(y1,2) - 0.0436*Math.pow(x1,3);
        double latit0 = 16.9023892 + 3.238272*y1 - 0.270978*Math.pow(x1,2) - 0.002528*Math.pow(y1,2) - 0.0447*Math.pow(x1,2)*y1 - 0.0140*Math.pow(y1,3); 
        return new PointGeo(Math.toRadians(longi0*100/36),Math.toRadians(latit0*100/36));
    }
}
