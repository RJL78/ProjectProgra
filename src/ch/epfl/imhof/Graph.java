package ch.epfl.imhof;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public final class Graph<N> {
    
    private Map<N,Set<N>> map;

    public Graph(Map<N, Set<N>> neighbors) {
        map = Collections.unmodifiableMap(new HashMap<N,Set<N>> (neighbors));
    }
    
    public Set<N> nodes(){
        return new HashSet<N>(map.keySet());
    }
    
    public Set<N> neighborsOf(N node) throws IllegalArgumentException{
        if (map.containsKey(node)==false){
            throw new IllegalArgumentException();
        }
        return map.get(node);
    }
    
    public static final class Builder<N>{
        
        private HashMap<N,Set<N>> map;
        
        public Builder(){
            map = new HashMap<>();
        }
        
        public void addNode(N n){
            map.putIfAbsent(n, null);
        }
        
        public void addEdge(N n1, N n2) throws IllegalArgumentException{
            if(map.containsKey (n1)&& map.containsKey(n2)){
                Set<N> n1Neighbors = map.get(n1);
                n1Neighbors.add(n2);
                Set<N> n2Neighbors = map.get(n2);
                n2Neighbors.add(n1);
                map.put(n1, n1Neighbors);
                map.put(n2, n2Neighbors);
                //map.get(n1).add(n2);
                //map.get(n2).add(n1);
            }
            else{
                throw new IllegalArgumentException();
            }
        }
        
        public Graph<N> build(){
            return new Graph<N>(map);
        }
    }
}
