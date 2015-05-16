package ch.epfl.imhof.painting;

import ch.epfl.imhof.geometry.PolyLine;
import ch.epfl.imhof.geometry.Polygon;

/** 
 * Interface représentant une toile
 * 
 * @author Raphael Laporte (251209) / Romain Leteurtre (238162)
 */
public interface Canvas {
    
    /**
     * Permet de dessiner sur la toile une polyligne donnée avec un style de ligne donné
     * 
     * @param polyline : la polyligne à dessiner
     * @param lineStyle : le style de ligne à utiliser
     */
    public void drawPolyline(PolyLine polyline, LineStyle lineStyle);
    
    /**
     * Permet de dessiner sur la toile un polygone donnée avec une couleur donnée
     * 
     * @param polygon : le polygone à dessiner
     * @param color : la couleur à utiliser
     */
    public void drawPolygon(Polygon polygon, Color color);

}
