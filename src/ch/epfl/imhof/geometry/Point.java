package ch.epfl.imhof.geometry;

import java.util.Objects;
import java.util.function.Function;

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
    
    /**
     * redefinition de la methode equals(): utile dans OSMToGeoTransformer pour bien identifier deux noeuds equivalents
     * 
     * @param o: Objet auquel on compare l'instance sur lequel la methode est appelée
     * 
     * @return True si les deux objets comparés sont equivalents, false sinon
     */
    public boolean equals(Object o){
        return ((o.getClass()==this.getClass()) ? (((Point)o).y()==y && ((Point)o).x()==x ) : false);
    }
    
    /**
     * Redifinition de hashcode : necessaire pour verifier que deux elements egaux selon la methode equals presentent le meme hashcoe
     */
    public int hashCode(){
        return Objects.hash(x,y);
    }
    // auuuuucune idee de si ca marche ou pas ! 
    public static Function<Point,Point> alignedCoordinateChange( Point firstPoint1, Point firstPoint2, Point secondPoint1, Point secondPoint2){
        if (firstPoint1.x() == secondPoint1.x() || firstPoint1.y() == secondPoint1.y()) throw new IllegalArgumentException();
        return  point -> {
            double a = (firstPoint2.x() - secondPoint2.x())/(firstPoint1.x() - secondPoint1.x());
            double b = (firstPoint2.y() - secondPoint2.y())/(firstPoint1.y() - secondPoint1.y());
            double c = firstPoint2.x()-a*firstPoint1.x();
            double d = firstPoint2.y()-b*firstPoint1.y();
            return new Point(point.x()*a+c,point.y()*b+d);
        };
    }
}
