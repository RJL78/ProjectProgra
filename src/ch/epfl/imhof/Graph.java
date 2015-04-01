package ch.epfl.imhof;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Classe servant à modéliser un graphe non-orienté
 * 
 * @author Raphael Laporte (251209) / Romain Leteurtre (238162)
 *
 * @param <N> la classe dont les instances serviront à représenter les noeuds du graph.
 */
public final class Graph<N> {

    private final Map<N,Set<N>> map;

    /**
     * Constructeur pour le Graph 
     * 
     * @param neighbors: Une map qui a pour clefs les noeuds du graph, et pour valeures associés les listes des noeuds connectés correspondantes 
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
     * Getter retournant un set de tous les noeuds reliés au noeud mis en paramètre 
     * 
     * @param node : noeud pour lequel on cherche les voisins
     * 
     * @return : un HashSet contenant tous les voisins du noeud mis en paramètre 
     * 
     * @throws IllegalArgumentException : lancée lorsque le noeud mis en parametre n'est pas contenu dans le Graph
     */  
    public Set<N> neighborsOf(N node) throws IllegalArgumentException{
        if (map.containsKey(node)==false){
            throw new IllegalArgumentException();
        }
        return Collections.unmodifiableSet(map.get(node));
    }

    /**
     * 
     * Classe imbriquée statiquement servant de builder pour la class Graph
     *
     * @param <N> la classe generique servant à représenter les noeuds du graph.
     */    
    public static final class Builder<N>{

        private HashMap<N,Set<N>> map;

        /**
         * Constucteur pour le Builder
         * 
         */
        public Builder(){
            map = new HashMap<>();
        }

        /** 
         * Méthode permettant d'ajouter un noeud desire dans le graph au Builder
         * 
         * @param Le noeud que l'on souhaite voire dans le Graph construit par ce Builder
         */
        public void addNode(N n){
            if (!map.keySet().contains(n)){
                map.put(n, new HashSet<N>());
            }
        }

        /**
         * Méthode permettant d'ajouter une arrete desiree dans le graph au Builder
         * 
         * @param n1 : Un des deux noeuds constituant l'arête desiéee
         * @param n2 : Le second noeud constituant l'arête desirée 
         * 
         * @throws IllegalArgumentException : lancée si l'un des deux noeuds mis en parametre n'a pas ete préalablement ajouté au builder 
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
         * Méthode retournant le graph avec les noeuds et les arêtes qui ont ete ajoutés au builder 
         * 
         * @return le graph avec les caracteristiques desirées. 
         */
        public Graph<N> build(){
            return new Graph<N>(map);
        }
    }
}
