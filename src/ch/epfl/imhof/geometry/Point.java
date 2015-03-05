package ch.epfl.imhof.geometry;

/** 
 * Classe modelisant un point dans le repere CH1903
 * 
 * @author Raphael Laporte (251209) / Romain Leteurtre (238162)
 *
 */


public final class Point {
    
    private final double x, y;
    /**
     * Constructeur
     * 
     * @param x : Coordonnee horizontale desiree
     * @param y : Coordonee verticale desiree
     * 
     * @return Instance modelisant le point desiree.
     */

    public Point (double x, double y){
        this.x = x;
        this.y = y; 
    }
    /**
     * 
     * @return  Coordonnee horizontale du point
     */
    
    public double x(){
        return x; 
    }
    
    /**
     * 
     * @return  Coordonnee verticale du point
     */
    
    public double y(){
        return y; 
    }

}
