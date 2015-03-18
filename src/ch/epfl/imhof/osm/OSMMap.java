package ch.epfl.imhof.osm;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

/**
 *  Classe immuable representant une Carte OSM, ensemble de chemins et relations
 *  sous forme de listes
 * @author Raphael Laporte (251209) / Romain Leteurtre (238162)
 *
 */

public final class OSMMap {
    
    private List<OSMWay> ways;
    private List<OSMRelation> relations;

    /**
     * Constructeur 
     * 
     * @param ways : Collection des chemins de la carte
     * @param relations : Collection des relations de la carte  
     */
    
    public OSMMap(Collection<OSMWay> ways, Collection<OSMRelation> relations) {
        this.ways = Collections.unmodifiableList(new ArrayList <OSMWay> (ways));
        this.relations = Collections.unmodifiableList(new ArrayList <OSMRelation> (relations));
    }
    
    /**
     * getter de la List ways (chemins) - unmodifiableList
     * 
     * @return: ways
     */
    
    public List<OSMWay> ways(){
        return ways; 
    }
    
    /**
     * getter de la List relations (relations) - unmodifiableList
     * 
     * @return: relations
     */
    
    public List<OSMRelation> relations(){
        return relations;
    }
    
    /**
     * Builder -
     * Classe imbriquee statiquement, servant a la construction d'un objet OSMMap. 
     *
     */
    
    public static final class Builder{ 
        
        private HashMap <Long,OSMNode> nodes;
        private HashMap <Long,OSMRelation> relations;
        private HashMap <Long,OSMWay> ways;
        
        /**
         * Constructeur du Builder 
         * 
         */
        
        public Builder(){
            nodes = new HashMap <Long,OSMNode>();
            relations = new HashMap <Long,OSMRelation>();
            ways = new HashMap <Long,OSMWay>();
        }
        
        /**
         * ajoute un noeud à la carte OSM en construction
         * 
         * @param newNode : nouveau noeud à ajouter
         */
        
        public void addNode(OSMNode newNode){
            nodes.put(newNode.id(), newNode);
        }
        
        /**
         * retourne le noeud avec l'id correspondant 
         * 
         * @param id : id du noeud à chercher
         * @return noeud avec le id correspondant
         * (retourne null si aucun noeud dans la liste a l'id demandé)
         */
        
        public OSMNode nodeForId(long id){
            return nodes.get(id); 
        }
        
        /**
         * ajoute un chemin à la carte OSM en construction
         * 
         * @param newWay : nouveau chemin à ajouter
         */
        
        public void addWay(OSMWay newWay){
            ways.put(newWay.id(), newWay); 
        }
        
        /**
         * retourne le chemin avec l'id correspondant 
         * 
         * @param id : id du chemin à chercher
         * @return chemin avec le id correspondant
         * (retourne null si aucun chemin dans la liste a l'id demandé)
         */
        
        public OSMWay wayForId (long id){
            return ways.get(id); 
        }
        
        /**
         * ajoute une relation à la carte OSM en construction
         * 
         * @param newNode : nouvelle relation à ajouter
         */
        
        public void addRelation(OSMRelation newRelation){
            relations.put(newRelation.id(), newRelation); 
        }
        
        /**
         * retourne la relation avec l'id correspondant 
         * 
         * @param id : id de la relation à chercher
         * @return relation avec le id correspondant
         * (retourne null si aucune relation dans la liste a l'id demandé)
         */
        
        public OSMRelation relationForId(long id){
            return relations.get(id);
        }
        
        /**
         * retourne une carte OSM construite 
         * 
         * @return la carte OSM construite
         */
        
        public OSMMap build(){
            return new OSMMap(ways.values(),relations.values());
        }
        
    }

}
