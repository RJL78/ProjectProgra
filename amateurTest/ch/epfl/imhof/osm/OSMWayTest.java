package ch.epfl.imhof.osm;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import java.util.List;

import org.junit.Test;

import ch.epfl.imhof.Attributes;
import ch.epfl.imhof.PointGeo;

public class OSMWayTest extends OSMEntityTest {

    private static OSMWay.Builder b1, b2, b3;
    private static OSMWay w; 
    private static OSMNode.Builder nBuilder; 
    private static OSMNode n1, n2;
    static{ 
        nBuilder = new OSMNode.Builder(2,new PointGeo(0,0)); 
        b1 = new OSMWay.Builder(1);
        b2 = new OSMWay.Builder(2);
        b3 = new OSMWay.Builder(3);

        b2.setAttribute("String1","String4");
        b2.setAttribute("String2","String5");
        b2.setAttribute("String3","String6");
        n1 = nBuilder.build();
        n2 = nBuilder.build();
        b2.addNode(n1);
        b2.addNode(n2); 
        b3.addNode(n1);
        
    }
    
    @Test (expected = IllegalStateException.class)
    public void IncompleteBuilderTest(){
        b1.setIncomplete(); 
        w = b1.build();
    }
    
    @Test (expected = IllegalStateException.class)
    public void IncompleteBuilderTest2(){
        w = b3.build();
    }
    
    @Test
    public void BuilderTest(){
        w = b2.build();
        this.hasAttributesTest(w,"String1");
        this.hasAttributesTest(w,"String2");
        this.hasAttributesTest(w,"String3");
    }
    
   @Test 
    public void OSMWay(){
        w = b2.build();
        List<OSMNode> nodes = w.nodes();
        nodes.clear(); 
        assertEquals(w.nodes().size(),2);
        
        b2.addNode(n1);
        w =b2.build(); 
        assertEquals(w.nodes().size(),3); 
        assertEquals(w.nonRepeatingNodes().size(),2);   
        assert(w.isClosed()); 
        
        b2.addNode(n2);
        w =b2.build(); 
        assertEquals(w.lastNode(),n2);
    }
    
    @Test
    public void OSMWayConstructorTest(){
        Attributes.Builder b = new Attributes.Builder();
        b.put("1", "2");
        List<OSMNode> nodes = b2.build().nodes();
        OSMWay way = new OSMWay(10, nodes, b.build());
        nodes.clear();
        assertFalse(way.nodes().size()==0); 
        
    }
    
    
    

}
