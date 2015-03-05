package ch.epfl.imhof.geometry;

import java.util.List;

/** 
 * Classe représentant une polyligne ouverte
 * Hérite de PolyLine
 * 
 * @author Raphael Laporte (251209) / Romain Leteurtre (238162)
 *
 */

public final class OpenPolyLine extends PolyLine {

    /**
     * Constructeur
     * 
     * @param points: liste de points formant la polyligne ouverte
     * 
     * @return Instance représentant une polyligne ouverte
     */
    
    public OpenPolyLine(List<Point> points){
        super(points);
    }  
    
    /**
     * Retourne false car un Polyligne ouverte est ... (suspense) ouverte !
     * 
     * @return false
     */
    
    public boolean isClosed() {
        return false;
    }

}
