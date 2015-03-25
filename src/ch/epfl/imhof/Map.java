package ch.epfl.imhof;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import ch.epfl.imhof.geometry.PolyLine;
import ch.epfl.imhof.geometry.Polygon;

/**
 *  classe finale et immuable représentant une carte projetée, 
 *  composée d'entités géométriques attribuées. 
 *  
 * @author Raphael Laporte (251209) / Romain Leteurtre (238162)
 *
 */

public final class Map {

   List<Attributed<PolyLine>> polyLines; 
   List<Attributed<Polygon>> polygons;
    
   /**
    * Constructeur 
    * @param polyLines : liste de polylignes attribuées
    * @param polygons :  liste de polygones attribués
    */
   
    public Map(List<Attributed<PolyLine>> polyLines, List<Attributed<Polygon>> polygons) {
        this.polyLines = new ArrayList<Attributed<PolyLine>> (polyLines);
        this.polygons = new ArrayList<Attributed<Polygon>> (polygons);
    }
    
    /**
     * Getter de polyLines 
     * @return la liste des polylignes attribuées de la carte
     */
    
    public List<Attributed<PolyLine>> polyLines(){
        return Collections.unmodifiableList(polyLines);
    }
    
    /**
     * Getter de polygons 
     * @return la liste des polygones attribués de la carte
     */
    
    public List<Attributed<Polygon>> polygons(){
        return Collections.unmodifiableList(polygons);
    }
    
    /**
     * 
     * Classe imbriquée statiquement, servant a la construction d'un objet Map. 
     *
     */
    
    public static class Builder{
        List<Attributed<PolyLine>> polyLines; 
        List<Attributed<Polygon>> polygons; 
        
        /**
         * Constructeur 
         * 
         */
        
        public Builder(){
            polyLines = new ArrayList<Attributed<PolyLine>> ();
            polygons = new ArrayList<Attributed<Polygon>>();
        }
        
        /**
         *  Ajoute une polyligne à la liste des polylignes en construction
         * @param newPolyLine : nouvelle polyligne à ajouter à la liste
         */
        
        public void addPolyLine(Attributed<PolyLine> newPolyLine){
            polyLines.add(newPolyLine);
        }
        
        /**
         *  Ajoute une polygon à la liste des polygones en construction
         * @param newPolygon : nouveau polygone à ajouter à la liste
         */
        
        public void addPolygon(Attributed<Polygon> newPolygon){
            polygons.add(newPolygon);
        }
        
        /**
         * Sert a construire un objet Map contenant les polylignes et polygones
         * ajoutés lors de la construction
         * 
         * @return L'objet Map avec les polylignes et polygones souhaités
         */
        
        public Map build(){
            return new Map(polyLines,polygons);
        }
        
    }
    
}
