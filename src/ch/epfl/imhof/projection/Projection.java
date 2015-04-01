package ch.epfl.imhof.projection;

import ch.epfl.imhof.PointGeo;
import ch.epfl.imhof.geometry.Point;

/** 
 * Interface destinée aux projections d'un PointGeo dans le repère WGS84 
 * en un Point dans le repère CH1903, et inversement
 * 
 * @author Raphael Laporte (251209) / Romain Leteurtre (238162)
 */
public interface Projection {
    
    /**
     * Projette un PointGeo en Point géométrique
     * 
     * @param point : le PointGeo à projeter
     * 
     * @return Le Point géométrique projeté
     */
    public Point project(PointGeo point);
    
    /**
     * Projette un Point géométrique en PointGeo
     * 
     * @param point : le Point géométrique à projeter
     * 
     * @return Le PointGeo projeté
     */
    public PointGeo inverse(Point point);
}
