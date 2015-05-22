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
     * Redefinition de la methode equals(): utile dans OSMToGeoTransformer pour bien identifier deux noeuds équivalents
     * 
     * @param o: Objet auquel on compare l'instance sur lequel la methode est appelée
     * 
     * @return True si les deux objets comparés sont équivalents, false sinon
     */
    public boolean equals(Object o){
        return ((o.getClass()==this.getClass()) ? (((Point)o).y()==y && ((Point)o).x()==x ) : false);
    }
    
    /**
     * Redéfinition de hashcode : necessaire pour verifier que deux elements égaux selon la methode equals présentent le meme hashcode
     */
    public int hashCode(){
        return Objects.hash(x,y);
    }
    
    
    
    /**
     * Renvoie une fonction de changement de repère (repères alignés)
     * 
     * @param firstPointOld : premier point dans repère initial
     * @param firstPointNew : premier point dans nouveau repère
     * @param secondPointOld : deuxième point dans repère initial
     * @param secondPointNew : deuxième point dans nouveau repère
     * 
     * @return fonction de changement de repères alignés 
     */
    public static Function<Point,Point> alignedCoordinateChange( Point firstPointOld, Point firstPointNew, Point secondPointOld, Point secondPointNew){
       if (firstPointOld.x() == secondPointOld.x() || firstPointOld.y() == secondPointOld.y()){
            throw new IllegalArgumentException("The two points used as arguments are on the same vertical or horizontal line");
        }
        
       // !!! ASK TA !!! Should we be dividing more into local variables? 
        double a = (firstPointNew.x() - secondPointNew.x())/(firstPointOld.x() - secondPointOld.x());
        double b = firstPointNew.x()-a*firstPointOld.x();
        double c = (firstPointNew.y() - secondPointNew.y())/(firstPointOld.y() - secondPointOld.y());
        double d = firstPointNew.y()-c*firstPointOld.y();
        
        return  point -> new Point(point.x()*a+b,point.y()*c+d);
    }
}
