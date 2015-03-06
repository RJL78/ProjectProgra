package ch.epfl.imhof;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNull;


import org.junit.Test;

public class AttributedTest {
    
   private static Attributes.Builder b = new Attributes.Builder();
   private static Attributes a;
    static {
        b.put("natural", "water");
        b.put("name", "Lac Léman");
        b.put("ele", "372");
        a = b.build();
    }
   private static Attributed<String> test = new Attributed<>("yolo",a);
 
  @Test
  public void hasAttributesTest() {
      assertTrue(test.hasAttribute("natural"));
  }
  
  @Test
  public void hasAttributeValueTest() {
      assertNull(test.attributeValue("oups"));
      assertEquals("water",test.attributeValue("natural"));
      assertEquals("water",test.attributeValue("natural","mistake"));
      assertEquals("mistake",test.attributeValue("uh-oh","mistake"));
      assertEquals(0,test.attributeValue("uh-oh",0));
  }
}
