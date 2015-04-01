package ch.epfl.imhof.geometry;

import java.util.List;
import java.lang.Math;

/** 
 * Classe représentant immuable une polyligne fermée
 * Hérite de PolyLine
 * 
 * @author Raphael Laporte (251209) / Romain Leteurtre (238162)
 */
public final class ClosedPolyLine extends PolyLine {

    /**
     * Constructeur de ClosedPolyLine
     * 
     * @param points: liste de points formant la polyligne 
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

    /**
     * Calcule l'aire de la polyligne fermée
     * 
     * @return l'aire (double)
     * 
     */
    public double area() {
        List<Point> list=points();
        double A=0.0;
        int n= list.size();
        for (int i=0;i<list.size();i++) {
            A+=list.get(i).x()*(list.get(IndexCor(i+1,n)).y()-list.get(IndexCor(i-1,n)).y());
        }
        A=Math.abs(A)/2.0;
        return A;
    }

    /**
     * Permet de savoir si le point p est à l'intérieur de la polyligne
     * Si le point est sur la ClosedPolyline, il est considéré à l'intérieur
     * 
     * @param Point p en question
     * 
     * @return True si le point est à l'intérieur de la polyligne
     */
    public boolean containsPoint(Point p) {
        int index = 0;
        List<Point> list=points();
        int n =list.size();
        for (int i=0;i<n;i++) {
            if (list.get(i).y()<=p.y()) {
                if (list.get(IndexCor(i+1,n)).y()>p.y()&&isLeftof(list.get(i),list.get(IndexCor(i+1,n)),p)) {
                    index++;
                }
            }
            else {
                if (list.get(IndexCor(i+1,n)).y()<=p.y()&&isLeftof(list.get(IndexCor(i+1,n)),list.get(i),p)) {
                    index--;
                }
            }
        }
        return (index!=0);
    }
    
    private boolean isLeftof(Point point1, Point point2, Point p) {
        if ((point1.x()-p.x())*(point2.y()-p.y())>(point2.x()-p.x())*(point1.y()-p.y())) {
            return true;
        }
        return false;
    }
    
    private int IndexCor(int i, int n) {
        return Math.floorMod(i,n);
    }
}
