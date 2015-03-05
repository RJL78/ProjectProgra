package ch.epfl.imhof.geometry;

import java.util.List;
import java.lang.Math;

/** 
 * Classe représentant une polyligne fermée
 * Hérite de PolyLine
 * 
 * @author Raphael Laporte (251209) / Romain Leteurtre (238162)
 *
 */

public final class ClosedPolyLine extends PolyLine {
    
    /**
     * Constructeur
     * 
     * @param points: liste de points formant la polyligne
     * 
     * @return Instance représentant une polyligne fermée
     */
    
    public ClosedPolyLine(List<Point> points) {
        super(points);
    }
    
    /**
     * Retourne true car un Polyligne fermé est ... (suspense) fermé !
     * 
     * @return true
     */
    
    public boolean isClosed() {
        return true;
    }
    
    private boolean isLeftof(Point point1, Point point2, Point p) {
        if ((point1.x()-p.x())*(point2.y()-p.y())>(point2.x()-p.x())*(point1.y()-p.y())) {
            return true; // si c'est sur la ligne ? !!!!!
        }
        return false;
    }

    /**
     * Calcule l'aire de la polyligne fermée
     * 
     * @return l'aire (double)
     * 
     */
    public double area() {
       List<Point> liste=points();
       double A=0.0;
       int n= liste.size();
       for (int i=0;i<liste.size();i++) {
           A+=liste.get(i).x()*(liste.get(IndexCor(i+1,n)).y()-liste.get(IndexCor(i-1,n)).y());
       }
       A=Math.abs(A)/2.0;
       return A;
    }
    
    private int IndexCor(int i, int n) {
        return Math.floorMod(i,n);
    }
    
    /**
     * Permet de savoir si le point p est à l'intérieur de la polyligne
     * 
     * @param Point p en question
     * @return True si le point est à l'intérieur de la polyligne
     * 
     */
    
    public boolean containsPoint(Point p) {
        int indice = 0;
        List<Point> liste=points();
        int n =liste.size();
        for (int i=0;i<liste.size();i++) {
            if (liste.get(i).y()<=p.y()) {
                if (liste.get(IndexCor(i+1,n)).y()>p.y()&&isLeftof(liste.get(i),liste.get(IndexCor(i+1,n)),p)) {
                    indice++;
                }
            }
            else {
                if (liste.get(IndexCor(i+1,n)).y()<=p.y()&&isLeftof(liste.get(IndexCor(i+1,n)),liste.get(i),p)) {
                    indice--;
                }
            }
        }
        return (indice!=0);
    }
}


// test marker 123
// test market 456