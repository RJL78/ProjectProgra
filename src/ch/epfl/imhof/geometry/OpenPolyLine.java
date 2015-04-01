package ch.epfl.imhof.geometry;

import java.util.List;

/** 
 * Classe immuable représentant une polyligne ouverte
 * Hérite de PolyLine
 * 
 * @author Raphael Laporte (251209) / Romain Leteurtre (238162)
 */
public final class OpenPolyLine extends PolyLine {

    /**
     * Constructeur de OpenPolyLine retournant une instance d'une polyligne ouverte
     * 
     * @param points: Liste de points formant la polyligne ouverte 
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
