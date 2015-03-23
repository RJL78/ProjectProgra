package ch.epfl.imhof;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import ch.epfl.imhof.geometry.PolyLine;
import ch.epfl.imhof.geometry.Polygon;

public final class Map {

   List<Attributed<PolyLine>> polyLines; 
   List<Attributed<Polygon>> polygons;
    
    public Map(List<Attributed<PolyLine>> polyLines, List<Attributed<Polygon>> polygons) {
        this.polyLines = new ArrayList<Attributed<PolyLine>> (polyLines);
        this.polygons = new ArrayList<Attributed<Polygon>> (polygons);
    }
    
    public List<Attributed<PolyLine>> polyLines(){
        return Collections.unmodifiableList(polyLines);
    }
    
    public List<Attributed<Polygon>> polygons(){
        return Collections.unmodifiableList(polygons);
    }
    
    public static class Builder{
        List<Attributed<PolyLine>> polyLines; 
        List<Attributed<Polygon>> polygons; 
        
        
        public Builder(){
            polyLines = new ArrayList<Attributed<PolyLine>> ();
            polygons = new ArrayList<Attributed<Polygon>>();
        }
        
        public void addPolyLine(Attributed<PolyLine> newPolyLine){
            polyLines.add(newPolyLine);
        }
        
        public void addPolygon(Attributed<Polygon> newPolygon){
            polygons.add(newPolygon);
        }
        
        public Map build(){
            return new Map(polyLines,polygons);
        }
        
    }
    
}
