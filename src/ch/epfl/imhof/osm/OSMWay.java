package ch.epfl.imhof.osm;

import java.util.ArrayList;
import java.util.List;

import ch.epfl.imhof.Attributes;
import ch.epfl.imhof.osm.OSMRelation.Member;

public final class OSMWay extends OSMEntity {
    
    /**
     * Classe representant un chemin OSM 
     * Herite de OSMEnitity - Classe Immuable 
     * 
     * @author Raphael Laporte (251209) / Romain Leteurtre (238162)
     */

    private List<OSMNode> nodes;
    
    /**
     * Constructeur, utilise par le builder 
     * 
     * @param id : identifiant de l'objet OSM 
     * @param nodes : Liste des noeuds qui composent le chemin 
     * @param attributes :les attributs lies a ce chemin
     * @throws IllegalArgumentException: lancee si la liste des noeuds est trop petite pour consituter un chemin 
     */
    
    public OSMWay(long id, List<OSMNode> nodes, Attributes attributes) throws IllegalArgumentException{
        super(id,attributes);
        this.nodes = new ArrayList<OSMNode>();
        if (nodes.size()<2) throw new IllegalArgumentException();
        for (OSMNode aNode: nodes){
            this.nodes.add(aNode);
        }
    }
    /**
     * Getter
     * @return : Le nombre de noeuds qui constituent le chemin 
     */
    public int nodesCount() {
        return nodes.size();
    }
    
    /**
     * Getter
     * @return : La liste de noeuds qui constituent le chemin
     */
    
    public List<OSMNode> nodes() {
       List<OSMNode> output = new ArrayList <OSMNode> ();
       for (OSMNode aNode: nodes){
           output.add(aNode);
       }
       return output;
    }
   /**
    * Getter 
    * @return: La liste des noeuds qui constituent le chemin sans le dernier noeud si celui-ci est egal au premier 
    */
    
    public List<OSMNode> nonRepeatingNodes() {
        List<OSMNode> output = new ArrayList<OSMNode>();
        if (nodes.get(0).equals(nodes.get(nodes.size()-1))){
            for (OSMNode aNode: nodes.subList(0, nodes.size()-1)){ 
                output.add(aNode);
            }
            return output; 
        }
        for (OSMNode aNode: nodes){
            output.add(aNode);
        }
        return output;
    }
    
    /**
     * Getter
     * @return : Le premier noeud qui constitue le chemin
     */
    public OSMNode firstNode() {
        return nodes.get(0);
    }
    
    /**
     * Getter
     * @return : Le dernier noeud qui constitue le chemin 
     */
    
    public OSMNode lastNode() {
        return nodes.get(nodes.size()-1);
    }
    
    /**
     * Methode qui indique si le chemin est ferme ou non 
     * @return True si le dernier noeud est egal au premier, false sinon 
     */
    
    public boolean isClosed() {
        return nodes.get(0).equals(nodes.get(nodes.size()-1));
    }
    
    /**
     * 
     * Classe imbriquee statiquement servant de Builder pour OSMWay 
     * Herite de OSMEntity.Builder
     */
    
    public static final class Builder extends OSMEntity.Builder {
        
        private List<OSMNode> nodes;
        
        /**
         * Constructeur 
         * @param id: identifiant du chemin a creer avec ce Builder 
         */
        
        public Builder(long id) {
            super(id);
            nodes = new ArrayList<>();
        }
        /**
         * Methode qui permet d'ajouter des OSMNodes succesivement de facon a former un chemin
         * @param newNode: le noeud a rajouter
         */
        
        public void addNode(OSMNode newNode) {
            nodes.add(newNode);
        }
        /**
         *  methode retournant le OSMWay a construire
         * @return le chemin avec les caracteristiques voulues 
         * @throws IllegalStateException : exception levee si le Builder est dans un etat "incomplete == true"
         */
        
        public OSMWay build() throws IllegalStateException {
            if (isIncomplete()) throw new IllegalStateException();
            return new OSMWay(id,nodes,attributes.build());
        }
        /**
         * Override de la methode isIncomplete() de OSMEntity.Builder - Cet override permet de plus de verifier qu'il y a assez de noeuds pour construire un chemin 
         * @return: false si le Builder peut construire une tel chemin, true sinon 
         */ 
        
        public boolean isIncomplete() {
            return (super.isIncomplete()||nodes.size()<2);
        }
    }
}
