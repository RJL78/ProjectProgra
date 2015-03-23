package ch.epfl.imhof.osm;

import java.io.IOException;

import org.junit.Test;
import org.xml.sax.SAXException;

public class OSMMapReaderTest {
    
    public OSMMapReaderTest(){}
    
    @Test
    public void OSMMapReaderTest1() throws IOException, SAXException {
        OSMMap map = OSMMapReader.readOSMFile("/Users/Romain/Documents/Eclipse-workspace/github/ProjectProgra/src/ch/epfl/imhof/osm/berne.osm.gz", true);
        System.out.println("Yay");
        OSMMapReader.readOSMFile("/Users/Romain/Documents/Eclipse-workspace/github/ProjectProgra/src/ch/epfl/imhof/osm/interlaken.osm.gz", true);
        System.out.println("Yay");
        OSMMapReader.readOSMFile("/Users/Romain/Documents/Eclipse-workspace/github/ProjectProgra/src/ch/epfl/imhof/osm/lausanne.osm.gz", true);
        System.out.println("Yay");
        System.out.println(map.ways().size());
        System.out.println(map.relations().size());
        for (OSMWay way: map.ways()){
            assert(way.nodesCount()>=2);
        }
        
    }

}
