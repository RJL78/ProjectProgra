package ch.epfl.imhof;

import java.io.IOException;

import org.junit.Test;
import org.xml.sax.SAXException;

import ch.epfl.imhof.osm.OSMMap;
import ch.epfl.imhof.osm.OSMMapReader;
import ch.epfl.imhof.osm.OSMToGeoTransformer;
import ch.epfl.imhof.projection.CH1903Projection;

public class MapTest {
    
    private static Map map;
    private static OSMToGeoTransformer transformer;
    
    static{
        transformer = new OSMToGeoTransformer(new CH1903Projection());

        try {
            map = transformer.transform(OSMMapReader.readOSMFile("/Users/raphael/Sites/ProjectSemester2/src/ch/epfl/imhof/osm/testFile1.osm", false));
        } catch (IOException | SAXException e) {
            e.printStackTrace();
            map = null;
        }
    }

    public MapTest() {
    }
    
    @Test (expected = UnsupportedOperationException.class )
    public void immuabilityTest1() throws IOException, SAXException{
        map.polygons().clear();
    }
    
    @Test (expected = UnsupportedOperationException.class )
    public void immuabilityTest2() throws IOException, SAXException{
        map.polyLines().clear();
    }
    
    @Test
    public void mapTest() throws IOException, SAXException{
        assert(map.polygons().size()==0);
        assert(map.polyLines().size()==3);
        map = transformer.transform(OSMMapReader.readOSMFile("/Users/raphael/Sites/ProjectSemester2/src/ch/epfl/imhof/osm/testFile2.osm", false));
        assert (map.polygons.size()==2);
        assert(map.polyLines().size()==2);
    }
    
    @Test
    public void longRun() throws IOException, SAXException{
        map = transformer.transform(OSMMapReader.readOSMFile("/Users/raphael/Sites/ProjectSemester2/src/ch/epfl/imhof/osm/berne.osm.gz", true));
    }

}
