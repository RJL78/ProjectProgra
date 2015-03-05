package ch.epfl.imhof.projection;
import ch.epfl.imhof.PointGeo;
import ch.epfl.imhof.geometry.Point;

/** 
 * Interface destinee aux projections d'un PointGeo dans le repere WGS84 en un Point dans le repere CH1903, et inversement.
 * 
 * @author Raphael Laporte (251209) / Romain Leteurtre (238162)
 *
 */

public interface Projection {
    public Point project(PointGeo point);
    public PointGeo inverse(Point point);
}
