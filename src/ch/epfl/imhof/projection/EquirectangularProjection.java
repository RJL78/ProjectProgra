package ch.epfl.imhof.projection;

import ch.epfl.imhof.PointGeo;
import ch.epfl.imhof.geometry.Point;

/** 
 * Classe permettant la projection équirectangulaire d'un PointGeo dans le repère WGS84 
 * en un Point dans le repère CH1903, et inversement
 * Implémente Projection
 * 
 * @author Raphael Laporte (251209) / Romain Leteurtre (238162)
 */
public final class EquirectangularProjection implements Projection{

    /**
     *Projette un point du repère WGS84 au repère CH1903
     * 
     * @param L'instance de PointGeo que l'on souhaite projeter
     * 
     * @return La projection du paramètre
     */
    public Point project(PointGeo point){
        return new Point(point.longitude(),point.latitude());
    }

    /**
     *Projette un point du repère CH1903 au repère WGS84
     * 
     * @param L'instance de Point que l'on souhaite projeter
     * @return La projection du paramètre.
     */
    public PointGeo inverse(Point point){
        return new PointGeo(point.x(),point.y());
    }
}
