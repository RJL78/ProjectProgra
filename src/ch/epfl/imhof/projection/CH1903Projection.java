package ch.epfl.imhof.projection;

import ch.epfl.imhof.PointGeo;
import ch.epfl.imhof.geometry.Point;

/** 
 * Classe permettant la projection CH1903 d'un PointGeo dans le repère WGS84
 *  en un Point dans le repère CH1903, et inversement
 * Implémente Projection
 * 
 * @author Raphael Laporte (251209) / Romain Leteurtre (238162)
 */
public final class CH1903Projection implements Projection{
    
    /**
     *Projette un point du repère WGS84 au repère CH1903
     * 
     * @param L'instance de PointGeo que l'on souhaite projeter
     *  
     * @return La projection du paramètre
     */
    
    // stocker les variables mises au carre. 
    // Faut virer les Math.pow 
    @Override 
    public Point project(PointGeo point){
        double longi = Math.toDegrees(point.longitude());
        double latit =  Math.toDegrees(point.latitude()); 
        double longi1 = Math.pow(10.0, -4.0)*(longi*3600-26782.5);
        double latit1 =  Math.pow(10.0, -4.0)*(latit*3600-169028.66);
        double latit1Carre = latit1*latit1;
        double longi1Carre = longi1*longi1;
        return new Point ( 600072.37 + 211455.93*longi1 - 10938.51*longi1*latit1 - 0.36*longi1*latit1Carre - 44.54*longi1Carre*longi1,
                200147.07+308807.95*latit1+3745.25*longi1Carre+76.63*latit1Carre-194.56*longi1Carre*latit1+119.79*latit1Carre*latit1);
    }
    
    /**
     *Projette un point du repère CH1903 au repère WGS84
     * 
     * @param L'instance de Point que l'on souhaite projeter
     *  
     * @return La projection du paramètre
     */
    @Override 
    public PointGeo inverse(Point point){
        double x1 = (point.x() -600000)/1000000;
        double y1 = (point.y() -200000)/1000000;
        double x1Carre = x1*x1;
        double y1Carre = y1*y1;
        double longi0 = 2.6779094 + 4.728982*x1 + 0.791484*x1*y1 + 0.1306*x1*y1Carre - 0.0436*x1Carre*x1;
        double latit0 = 16.9023892 + 3.238272*y1 - 0.270978*x1Carre - 0.002528*y1Carre - 0.0447*x1Carre*y1 - 0.0140*y1Carre*y1; 
        return new PointGeo(Math.toRadians(longi0*100/36),Math.toRadians(latit0*100/36));
    }
}
