package ch.epfl.imhof;

import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.junit.Test;

import ch.epfl.imhof.osm.OSMNode;


public class GraphTest {
    private static OSMNode n1;
    private static OSMNode n2;
    private static OSMNode.Builder b1;
    private static OSMNode.Builder b2;
    private static Map<OSMNode,Set<OSMNode>> testMap;
    private static Graph<OSMNode> testGraph;
    
    static{ 
        b1 = new OSMNode.Builder(1,new PointGeo(0,0)); 
        b2 = new OSMNode.Builder(2,new PointGeo(0,0));
        b1.setAttribute("String1.1","String1.2");
        b1.setAttribute("String1.3","String1.4");
        b2.setAttribute("String2.1","String2.2");
        n1 = b1.build();
        n2 = b2.build();
        testMap = new HashMap<>();
        testMap.put(n1, new HashSet<OSMNode>());
        testMap.get(n1).add(n2);
        testGraph = new Graph<>(testMap);
    }
    
    
    @Test 
    public void getterImmuable() {
        Set<OSMNode> set = testGraph.nodes();
        set.clear();
        assertTrue(testGraph.nodes().contains(n1));
    }
    
    @Test (expected = IllegalArgumentException.class)
    public void neighborsOfExceptionTest() {
        testGraph.neighborsOf(n2);
    }
    
    @Test 
    public void neighborsOfTest() {
        assertTrue(testGraph.neighborsOf(n1).contains(n2));
        assertTrue(testGraph.neighborsOf(n1).size()==1); 
    }
    
    @Test
    public void neighborsOfImmuable() {
        Set<OSMNode> set = testGraph.nodes();
        set.clear();
        assertTrue(testGraph.neighborsOf(n1).contains(n2));
    }
    
    @Test (expected = IllegalArgumentException.class)
    public void addEdgeExceptionTest() {
       Graph.Builder<OSMNode> testGraphBuilder = new Graph.Builder<>();
       testGraphBuilder.addNode(n1);
       testGraphBuilder.addEdge(n1, n2);
    }
    
    @Test
    public void addEdgeTest() {
        Graph.Builder<OSMNode> testGraphBuilder = new Graph.Builder<OSMNode>();
        testGraphBuilder.addNode(n1);
        testGraphBuilder.addNode(n2);
        testGraphBuilder.addEdge(n1, n2);
        testGraph = testGraphBuilder.build();
        assertTrue(testGraph.neighborsOf(n1).contains(n2));
        assertTrue(testGraph.neighborsOf(n2).contains(n1));
    }
}
