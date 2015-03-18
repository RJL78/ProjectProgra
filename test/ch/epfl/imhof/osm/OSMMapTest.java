
package ch.epfl.imhof.osm;

import static org.junit.Assert.*;

import org.junit.Test;

import ch.epfl.imhof.Attributes;
import ch.epfl.imhof.PointGeo;
import ch.epfl.imhof.osm.OSMRelation.Member;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class OSMMapTest {
    private static List<OSMWay> testWays;
    private static List<OSMRelation> testRelations;
    private static OSMMap testOSMMap;
    private static OSMNode n1;
    private static OSMWay w1;
    private static OSMRelation r1;
    
    static {
      Map<String,String> attriMap = new HashMap<>();
      attriMap.put("motha", "fuka");
      Attributes attributes = new Attributes(attriMap);
      n1 = new OSMNode(20 ,new PointGeo(1,1),attributes);
      List<OSMNode> nodes = new ArrayList<>();
      nodes.add(n1);
      System.out.println("boo");
      w1 = new OSMWay(10,nodes,attributes);
      System.out.println("heya");
      List<OSMRelation.Member> members = new ArrayList<>();
      members.add(new Member(Member.Type.NODE,"sucker",n1));
      r1 = new OSMRelation(30,members,attributes);
      testWays = new ArrayList<>();
      testWays.add(w1);
      testRelations = new ArrayList<>();
      testRelations.add(r1);
      testOSMMap = new OSMMap(testWays,testRelations);
    }
    
    @Test
    public void gettersImmuableTest() {
        List<OSMWay> ways= testOSMMap.ways();
        List<OSMRelation> relations = testOSMMap.relations();
        relations.clear();
        ways.clear();
        assertTrue(testOSMMap.ways().size()>0);
        assertTrue(testOSMMap.relations().size()>0);
    }
    
    @Test
    public void buildertest() {
        OSMMap.Builder builder = new OSMMap.Builder();
        builder.addNode(n1);
        builder.addRelation(r1);
        builder.addWay(w1);
        assertTrue(builder.nodeForId(20).equals(n1));
        assertTrue(builder.wayForId(10).equals(w1));
        assertTrue(builder.relationForId(30).equals(r1));
    }  
}
