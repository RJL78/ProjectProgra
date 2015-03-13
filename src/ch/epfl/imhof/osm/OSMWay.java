package ch.epfl.imhof.osm;

import java.util.ArrayList;
import java.util.List;

import ch.epfl.imhof.Attributes;
import ch.epfl.imhof.osm.OSMRelation.Member;

public final class OSMWay extends OSMEntity {

    private List<OSMNode> nodes;
    
    public OSMWay(long id, List<OSMNode> nodes, Attributes attributes) throws IllegalArgumentException{
        super(id,attributes);
        if (nodes.size()<2) throw new IllegalArgumentException();
        this.nodes=nodes;
    }
    
    public int nodesCount() {
        return nodes.size();
    }
    
    public List<OSMNode> nodes() {
       List<OSMNode> output = new ArrayList <OSMNode> ();
       for (OSMNode aNode: nodes){
           output.add(aNode);
       }
       return output;
    }
    
    public List<OSMNode> nonRepeatingNodes() {
        if (nodes.get(0).equals(nodes.get(nodes.size()-1))) return nodes.subList(0, nodes.size()-1);
        return nodes;
    }
    
    public OSMNode firstNode() {
        return nodes.get(0);
    }
    
    public OSMNode lastNode() {
        return nodes.get(nodes.size()-1);
    }
    
    public boolean isClosed() {
        return nodes.get(0).equals(nodes.get(nodes.size()-1));
    }
    
    public static final class Builder extends OSMEntity.Builder {
        
        private List<OSMNode> nodes;
        
        public Builder(long id) {
            super(id);
            nodes = new ArrayList<>();
        }
        
        public void addNode(OSMNode newNode) {
            nodes.add(newNode);
        }
        
        public OSMWay build() throws IllegalStateException {
            if (isIncomplete()) throw new IllegalStateException();
            return new OSMWay(id,nodes,attributes.build());//Comment faire pour id et attributes?Protected?
        }
        
        public boolean isIncomplete() {
            return (super.isIncomplete()||nodes.size()<2);
        }
    }
}
