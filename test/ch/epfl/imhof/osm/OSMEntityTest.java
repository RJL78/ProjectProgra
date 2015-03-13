package ch.epfl.imhof.osm;
import static org.junit.Assert.*;


import ch.epfl.imhof.Attributes;

public class OSMEntityTest {


    private static Attributes.Builder b = new Attributes.Builder();
    static {
        b.put("testing", "water");
    }
    
    public void hasAttributesTest(OSMEntity e, String s){
        Attributes a = e.attributes();
        assertFalse(a.get(s)==null);
        assertEquals(e.attributeValue(s),(a.get(s)));
        a = b.build();
        assert(e.hasAttribute(s));
        assertFalse(e.hasAttribute("testing"));
    }
    

}
