package ch.epfl.imhof.geometry;

import java.util.Collections;
import java.util.List;
import java.util.ArrayList;

/** 
 * Classe abstraite et immuable représentant un ensemble de segments continus 
 * 
 * @author Raphael Laporte (251209) / Romain Leteurtre (238162)
 */
public abstract class PolyLine {

    private final List<Point> points;

    /**
     * Constructeur de PolyLine
     * 
     * @param points : La liste de points formant l'ensemble de segments continus
     * 
     * @throws IllegalArgumentException : Exception levée si la liste des points mis en paramètre est vide
     */
    public PolyLine(List<Point> points) throws IllegalArgumentException {
        if (points.size()==0){
            throw new IllegalArgumentException(); 
        }
        this.points = new ArrayList<> (points); 
    }

    /**
     * Methode abstraite destinée à permettre d'établir si la polyligne est ouverte ou fermée
     * 
     * @return : true si la polyligne est fermée, sinon false
     */
    public abstract boolean isClosed();

    /**
     * Getter
     *  
     * @return Une liste immuable des points formant la polyligne
     */
    public List<Point> points(){
        return Collections.unmodifiableList(points);
    }

    /**
     * Getter 
     * @return Le premier point de la liste des points formant la polyligne
     */
    public Point firstPoint() {
        return points.get(0);
    }

    /**
     * Classe imbriquee statiquement qui facilite la construction d'instances dont le type effectif hérite de Polyline
     */
    public static final class Builder{ 

        private final List<Point> points;

        /**
         * Constructeur par default pour la classe Builder 
         */
        public Builder(){
            points = new ArrayList<>();
        }

        /**
         * Methode qui ajoute un point à la liste des points qui sera utilisée pour construire une Polyline
         * 
         * @param : Le Point à ajouter à la liste
         */
        public void addPoint (Point newPoint){
            points.add(newPoint); 
        }

        /**
         * Methode qui construit une OpenPolyLine à partir de la liste des points ajoutés
         * 
         * @return : une instance de OpenPolyLine
         */
        public OpenPolyLine buildOpen(){
            return new OpenPolyLine(points);
        }

        /**
         * Methode qui construit une ClosedPolyLine a partir de la liste des points ajoutés
         * 
         * @return : une instance de ClosedPolyLine
         */
        public ClosedPolyLine buildClosed(){
            return new ClosedPolyLine(points);
        }
    }
}
