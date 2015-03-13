package ch.epfl.imhof.osm;
import org.junit.Test;
import ch.epfl.imhof.PointGeo;

public class OSMNodeTEST extends OSMEntityTest {

    
    
    private static OSMNode n; 
    private static OSMNode.Builder b;
    private static OSMNode.Builder b1;
    static{ 
        b = new OSMNode.Builder(2,new PointGeo(0,0)); 
        b1 = new OSMNode.Builder(2,new PointGeo(0,0)); 
        b.setAttribute("String1","String4");
        b.setAttribute("String2","String5");
        b.setAttribute("String3","String6");
        n = b.build();
    }
    
    @Test (expected = IllegalStateException.class)
    public void IncompleteBuilderTest(){
        b1.setIncomplete(); 
        n = b1.build();
    }
    
    @Test 
    public void BuilderTest(){ 
  
        this.hasAttributesTest(n,"String1");
        this.hasAttributesTest(n,"String2");
        this.hasAttributesTest(n,"String3");
    }
    
    @Test
    public void PositionTest(){
        assert(n.position().latitude()==0);
        assert(n.position().longitude()==0);
    }

}
