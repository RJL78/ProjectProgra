package ch.epfl.imhof.geometry;

import java.util.Objects;

/** 
 * Classe immuable modélisant un point dans le repère CH1903
 * 
 * @author Raphael Laporte (251209) / Romain Leteurtre (238162)
 */
public final class Point {
    
    private final double x, y;
    
    /**
     * Constructeur de Point
     * 
     * @param x : Coordonnée horizontale desirée
     * @param y : Coordonnée verticale desirée 
     */
    public Point (double x, double y){
        this.x = x;
        this.y = y; 
    }
    
    /**
     * Getter de la coordonnée x 
     * 
     * @return  Coordonnée horizontale du point
     */
    public double x(){
        return x; 
    }
    
    /**
     * Getter de la coordonnée y
     * 
     * @return  Coordonnée verticale du point
     */
    public double y(){
        return y; 
    }
    
    public boolean equals(Object o){
        return ((o.getClass()==this.getClass()) ? (((Point)o).y()==y && ((Point)o).x()==x ) : false);
    }
    
    public int hashCode(){
        return Objects.hash(x,y);
    }
}
