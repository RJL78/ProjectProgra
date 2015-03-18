package ch.epfl.imhof.osm;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public final class OSMMap {
    
    private List<OSMWay> ways;
    private List<OSMRelation> relations;

    public OSMMap(Collection<OSMWay> ways, Collection<OSMRelation> relations) {
        this.ways = Collections.unmodifiableList(new ArrayList <OSMWay> (ways));
        this.relations = Collections.unmodifiableList(new ArrayList <OSMRelation> (relations));
    }
    
    public List<OSMWay> ways(){
        return ways; 
    }
    
    public List<OSMRelation> relations(){
        return relations;
    }
    
    public static final class Builder{ 
        
        private HashMap <Long,OSMNode> nodes;
        private HashMap <Long,OSMRelation> relations;
        private HashMap <Long,OSMWay> ways;
        
        public Builder(){
            nodes = new HashMap <Long,OSMNode>();
            relations = new HashMap <Long,OSMRelation>();
            ways = new HashMap <Long,OSMWay>();
        }
        
        public void addNode(OSMNode newNode){
            nodes.put(newNode.id(), newNode);
        }
        
        public OSMNode nodeForId(long id){
            return nodes.get(id); 
        }
        
        public void addWay(OSMWay newWay){
            ways.put(newWay.id(), newWay); 
        }
        
        public OSMWay wayForId (long id){
            return ways.get(id); 
        }
        
        public void addRelation(OSMRelation newRelation){
            relations.put(newRelation.id(), newRelation); 
        }
        
        public OSMRelation relationForId(long id){
            return relations.get(id);
        }
        
        public OSMMap build(){
            return new OSMMap(ways.values(),relations.values());
        }
        
    }

}
