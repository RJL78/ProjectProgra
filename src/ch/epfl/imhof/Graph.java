package ch.epfl.imhof;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Classe servant a modelier un graphe non-oriente
 * 
 * @author Raphael Laporte (251209) / Romain Leteurtre (238162)
 *
 * @param <N> la classe generique servant a representer les noeuds du graph.
 */

public final class Graph<N> {
    
    private final Map<N,Set<N>> map;
    
    /**
     * Constructeur pour le Graph 
     * 
     * @param neighbors: Une map qui a pour clefs les noeuds du graph, et pour valeures associes les listes des noeuds connectes correspondants 
     */
    public Graph(Map<N, Set<N>> neighbors) {
        map = new HashMap<>();
        for (Map.Entry<N,Set<N>> entry: neighbors.entrySet()){
            map.put(entry.getKey(), new HashSet<N>(entry.getValue()));
        }
    }
    
    /**
     * Getter pour un Set de tous les noeuds du graph 
     * 
     * @return Un set de tous les noeuds du graph
     */
    
    public Set<N> nodes(){
        return Collections.unmodifiableSet(map.keySet());
    }
    
    /**
     * Getter retournant un set de tous les noeuds relies au noeud mis en parametre
     * 
     * @param node : noeud pour lequel on cherche les voisins
     * @return : un HashSet contenant tous les voisins du noeud mis en parametre 
     * @throws IllegalArgumentException : lancee lorsque le noeud mis en parametre n'est pas contenu dans le Graph
     */
    
    public Set<N> neighborsOf(N node) throws IllegalArgumentException{
        if (map.containsKey(node)==false){
            throw new IllegalArgumentException();
        }
        return Collections.unmodifiableSet(map.get(node));
    }
    
    /**
     * 
     * Classe imbriquee statiquement servant de builder pour la class Graph
     *
     * @param <N> la classe generique servant a representer les noeuds du graph.
     */
    
    public static final class Builder<N>{
        
        private HashMap<N,Set<N>> map;
        
        /**
         * Constreur pour le Builder
         * 
         */
        public Builder(){
            map = new HashMap<>();
        }
        
        /** 
         * Methode permettant d'ajouter un noeud desire dans le graph au Builder
         * @param Le noeud que l'on souhaite voire dans le Graph construit par ce Builder
         */
        
        public void addNode(N n){
            if (!map.keySet().contains(n)){
                map.put(n, new HashSet<N>());
            }
        }
        
        /**
         * Methode permettant d'ajouter une arrete desiree dans le graph au Builder
         * @param n1 : Un des deux noeuds constituant l'arrete desiree
         * @param n2 : Le second noeud constituant l'arrete desiree 
         * @throws IllegalArgumentException : lancee si l'un des deux noeuds mis en parametre n'a pas ete prealablement ajoutee au builder 
         */
        
        public void addEdge(N n1, N n2) throws IllegalArgumentException{
            if(map.containsKey (n1)&& map.containsKey(n2)){
                map.get(n1).add(n2);
                map.get(n2).add(n1);
            }
            else{
                throw new IllegalArgumentException();
            }
        }
        /**
         * Methode retournant le graph avec les noeuds et les arretes qui ont ete ajoutes aux builder 
         * @return le graph avec les caracteristiques desirees. 
         */
        
        public Graph<N> build(){
            return new Graph<N>(map);
        }
    }
}
