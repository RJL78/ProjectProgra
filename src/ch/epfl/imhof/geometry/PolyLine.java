package ch.epfl.imhof.geometry;

import java.util.Collections;
import java.util.List;
import java.util.ArrayList;

/** 
 * Classe representant un ensemble de segments continus 
 * Cette classe est abstraite et immuable
 * 
 * @author Raphael Laporte (251209) / Romain Leteurtre (238162)
 *
 */

public abstract class PolyLine {
    
    private final List<Point> points;
    /**
     * Constructeur
     * 
     * @param points : La liste de points formant l'ensemble de segments continus
     * @throws IllegalArgumentException : Exception leve si la liste des points mis en parametre est nulle
     */

    public PolyLine(List<Point> points) throws IllegalArgumentException {
        if (points.size()==0){
            throw new IllegalArgumentException(); 
        }
        this.points = Collections.unmodifiableList( new ArrayList<Point> (points)); 
    }
    
    /**
     * Methode abstraite destinee a permettre d'etablir si la polyline est ouverte ou fermmee
     * @return : Si oui ou non l'ensemble de segments est fermee.
     */
    public abstract boolean isClosed();
    
    /**
     * Getter 
     * @return Une liste immuable des points formant la polyline
     */
    public List<Point> points(){
        return points;
    }
    /**
     * Getter 
     * @return le premier point de la liste des points formant la polyline
     */
    public Point firstPoint() {
        return points.get(0);
    }
    
    /**
     * Classe imbriquee statiquement qui facilite la construction d'instances dont le type effectif herite de Polyline
     */
    public static final class Builder{ 
      
        
          private final ArrayList<Point> points;
          /**
           * Constructeur par default pour la Classe Builder 
           *
           */
          public Builder(){
              points = new ArrayList<Point>();
          }
          
          /**
           * Methode qui ajoute un point a la liste des points qui sera utilise pour construire une Polyline
           * @param : Le Point a ajouter a la liste
           */
          public void addPoint (Point newPoint){
              points.add(newPoint); 
          }
          
          /**
           * Methode qui construit une OpenPolyLine a partir de la liste des points ajoutees.
           * @return : une instance de OpenPolyLine
           */
          public OpenPolyLine buildOpen(){
              return new OpenPolyLine(points);
          }
          
          /**
           * Methode qui construit une ClosedPolyLine a partir de la liste des points ajoutees.
           * @return : une instance de ClosedPolyLine
           */
          public ClosedPolyLine buildClosed(){
              return new ClosedPolyLine(points);
          }
          
          
    }
    
}