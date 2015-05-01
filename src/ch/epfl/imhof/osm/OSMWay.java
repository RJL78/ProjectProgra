package ch.epfl.imhof.osm;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import ch.epfl.imhof.Attributes;

public final class OSMWay extends OSMEntity {
    
    /**
     * Classe representant un chemin OSM 
     * Herite de OSMEnitity - Classe Immuable 
     * 
     * @author Raphael Laporte (251209) / Romain Leteurtre (238162)
     */
    private final List<OSMNode> nodes;
    
    /**
     * Constructeur, utilisé par le OSMWAY.Builder 
     * 
     * @param id : identifiant de l'objet OSM 
     * @param nodes : Liste des noeuds qui composent le chemin 
     * @param attributes :les attributs lies a ce chemin
     * @throws IllegalArgumentException: lancee si la liste des noeuds est trop petite pour consituter un chemin 
     */
    public OSMWay(long id, List<OSMNode> nodes, Attributes attributes) throws IllegalArgumentException{
        super(id,attributes);
        this.nodes = new ArrayList<>();
        if (nodes.size()<2) throw new IllegalArgumentException();
        for (OSMNode aNode: nodes){
            this.nodes.add(aNode);
        }
    }
    
    /**
     * Getter pour le nombre de noeuds
     * 
     * @return : Le nombre de noeuds qui constituent le chemin 
     */
    public int nodesCount() {
        return nodes.size();
    }
    
    /**
     * Getter pour la liste de noeuds
     * 
     * @return : La liste de noeuds qui constituent le chemin
     */
    public List<OSMNode> nodes() {
       return Collections.unmodifiableList(nodes);
    }
    
   /**
    * Getter pour la liste des noeuds (sans le dernier noeud si celui-ci est égal au premier
    * 
    * @return: La liste des noeuds qui constituent le chemin sans le dernier noeud si lastNode.equals(firstNode)
    */
    public List<OSMNode> nonRepeatingNodes() {
        return (isClosed()) ? Collections.unmodifiableList(nodes.subList(0, nodes.size()-1)): Collections.unmodifiableList(nodes); 
    }
    
    /**
     * Getter pour le premier noeud
     * 
     * @return : Le premier noeud qui constitue le chemin
     */
    public OSMNode firstNode() {
        return nodes.get(0);
    }
    
    /**
     * Getter pour le dernier noeud
     * 
     * @return : Le dernier noeud qui constitue le chemin 
     */
    public OSMNode lastNode() {
        return nodes.get(nodes.size()-1);
    }
    
    /**
     * Méthode qui indique si le chemin est fermé ou non 
     * 
     * @return True si le dernier noeud est égal au premier, false sinon 
     */
    public boolean isClosed() {
        return firstNode().equals(lastNode());
    }
    
    /**
     * Classe imbriquee statiquement servant de Builder pour OSMWay 
     * Hérite de OSMEntity.Builder
     */    
    public static final class Builder extends OSMEntity.Builder {
        
        private final List<OSMNode> nodes;
        
        /**
         * Constructeur 
         * 
         * @param id: identifiant du chemin à créer avec ce Builder 
         */ 
        public Builder(long id) {
            super(id);
            nodes = new ArrayList<>();
        }
        
        /**
         * Methode qui permet d'ajouter des OSMNodes succesivement de facon à former un chemin
         * @param newNode: le noeud à rajouter
         */      
        public void addNode(OSMNode newNode) {
            nodes.add(newNode);
        }
        
        /**
         * Méthode retournant le OSMWay à construire
         *  
         * @return le chemin avec les caractéristiques voulues 
         * 
         * @throws IllegalStateException : exception levée si le Builder est tel que "this.IsIncomplete() == true" 
         */    
        public OSMWay build() throws IllegalStateException {
            if (isIncomplete()) throw new IllegalStateException();
            return new OSMWay(id,nodes,attributes.build());
        }
        
        /**
         * Override de la methode isIncomplete() de OSMEntity.Builder - Cet override permet de plus de verifier qu'il y a assez de noeuds pour construire un chemin 
         * 
         * @return: false si le Builder peut construire une tel chemin, true sinon
         * (ie. retourne false si et seulement si la méthode setIncomplete() [héritée de OSMEntity.Builder] est appellé préalablement par ce Builder, ou si moins de deux noeuds on été ajoutés à ce Builder)
         */ 
        public boolean isIncomplete() {
            return (super.isIncomplete()||nodes.size()<2);
        }
    }
}
