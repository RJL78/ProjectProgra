package ch.epfl.imhof.osm;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import org.junit.Test;

import ch.epfl.imhof.PointGeo;
import ch.epfl.imhof.osm.OSMRelation.Member;


public class OSMRelationTest extends OSMEntityTest {

   private static OSMRelation.Builder b1, b2;
   private static OSMRelation r; 
   private static OSMNode.Builder nBuilder; 
   private static OSMNode n1, n2;
   static{ 
       nBuilder = new OSMNode.Builder(2,new PointGeo(0,0)); 
       b1= new OSMRelation.Builder(1);
       b2= new OSMRelation.Builder(2);
       b2.setAttribute("String1","String4");
       b2.setAttribute("String2","String5");
       b2.setAttribute("String3","String6");
       n1 = nBuilder.build();
       n2 = nBuilder.build();
       b2.addMember(Member.Type.NODE, "testNode1", n1);
       b2.addMember(Member.Type.NODE, "testNode2", n2);
   }
   
   @Test (expected = IllegalStateException.class)
   public void IncompleteBuilderTest(){
       b1.setIncomplete(); 
       r = b1.build();
   }
   
   @Test
   public void BuilderTest(){
       r = b2.build();
       this.hasAttributesTest(r,"String1");
       this.hasAttributesTest(r,"String2");
       this.hasAttributesTest(r,"String3");
   }
   
   @Test
   public void memberTest(){
       r = b2.build();
       r.members().clear();
       assertEquals(r.members().size(),2);
       
       OSMEntity aMember = r.members().get(0).member();
       OSMRelation.Member.Type aType = r.members().get(0).type();

       
       OSMNode.Builder nBuilder2 = new OSMNode.Builder(4,new PointGeo(0,0)); 
       aMember = nBuilder2.build();
       aType = OSMRelation.Member.Type.WAY;

       
       System.out.println(r.members().get(0).member().id());
       for (int i=0; i<r.members().size(); i++){
           assertFalse( r.members().get(i).member().id()==4);
           assertFalse( r.members().get(i).type() == OSMRelation.Member.Type.WAY);
       }
   }
   
   
   
   
   
}
