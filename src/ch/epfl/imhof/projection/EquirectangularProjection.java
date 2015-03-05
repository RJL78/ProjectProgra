package ch.epfl.imhof.projection;

import ch.epfl.imhof.PointGeo;
import ch.epfl.imhof.geometry.Point;

/** 
 * Classe permettant la projection equirectangulaire d'un PointGeo dans le repere WGS84 en un Point dans le repere CH1903, et inversement.
 * 
 * @author Raphael Laporte (251209) / Romain Leteurtre (238162)
 *
 */
public final class EquirectangularProjection implements Projection{
    
    /**
     *Projette un point du repere WGS84 au repere CH1903
     * 
     * @param L'instance de PointGeo que l'on souhaite projeter. 
     * @return La projection du parametre.
     * 
     */
    public Point project(PointGeo point){
        return new Point(point.longitude(),point.latitude());
    }
    /**
     *Projette un point du repere CH1903 au repere WGS84
     * 
     * @param L'instance de Point que l'on souhaite projeter. 
     * @return La projection du parametre.
     * 
     */
    public PointGeo inverse(Point point){
        return new PointGeo(point.x(),point.y());
    }
}
