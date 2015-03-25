package ch.epfl.imhof.osm;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import ch.epfl.imhof.Attributed;
import ch.epfl.imhof.Attributes;
import ch.epfl.imhof.Graph;
import ch.epfl.imhof.Map;
import ch.epfl.imhof.geometry.ClosedPolyLine;
import ch.epfl.imhof.geometry.OpenPolyLine;
import ch.epfl.imhof.geometry.Point;
import ch.epfl.imhof.geometry.PolyLine;
import ch.epfl.imhof.geometry.Polygon;
import ch.epfl.imhof.projection.Projection;

/**
 *  classe finale et immuable représentant un convertisseur de données OSM en carte 
 *  
 * @author Raphael Laporte (251209) / Romain Leteurtre (238162)
 *
 */

public final class OSMToGeoTransformer {
    
    Projection projection;

    /**
     * Constructeur 
     * @param projection : projection utilisée pour la conversion
     */
    
    public OSMToGeoTransformer(Projection projection) {
        this.projection = projection;
    }
    
    /**
     * Convertit une carteOSM en une carte géométrique projetée
     * 
     * @param map : carteOSM à convertir
     * @return une carte (Map) convertie selon la projection utilisée
     */
    public Map transform(OSMMap map){
        
        List<Attributed<PolyLine>> polylines = new ArrayList<>();
        List<Attributed<Polygon>> polygones = new ArrayList<>();
        List<OSMRelation> multiPolygons = new ArrayList<>(map.relations());
        
        
        for (OSMWay way : map.ways()) {
            List<Point> points = new ArrayList<>();
            for (OSMNode node :way.nodes()) {
                Point newPoint = projection.project(node.position());
                points.add(newPoint);
            }
            if (way.isClosed()) {
                if (isSurface(way)) {
                    polygones.add(new Attributed<Polygon>(new Polygon(new ClosedPolyLine(points),new ArrayList<ClosedPolyLine>()) ,way.attributes()));
                }
                else {
                    polylines.add(new Attributed<PolyLine>(new ClosedPolyLine(points),way.attributes()));
                }
            }
            else {
                polylines.add(new Attributed<PolyLine>(new OpenPolyLine(points),way.attributes()));
            }
        }
        multiPolygons.removeIf(relation -> !relation.hasAttribute("type"));
        multiPolygons.removeIf(relation -> !relation.attributeValue("type").equals("multipolygon"));
        for (OSMRelation relation : multiPolygons) {
            polygones.addAll(this.assemblePolygon(relation,relation.attributes()));
        }
        
        
        return new Map(polylines,polygones);
    }
    
    private boolean isSurface(OSMWay way) {
        if (way.hasAttribute("area")) {
                List<String> possibilities1 = new ArrayList<>(Arrays.asList("yes","1","true"));
                return (possibilities1.contains(way.attributeValue("area")));
            }
        List<String> possibilities2 = new ArrayList<>(Arrays.asList("aeroway", "amenity", "building", "harbour", "historic",
                "landuse", "leisure", "man_made", "military", "natural",
                "office", "place", "power", "public_transport", "shop",
                "sport", "tourism", "water", "waterway", "wetland"));
        for (String s : possibilities2) {
            if (way.hasAttribute(s)) return true;
        }
        return false;
        
    }
    
    
    private List<ClosedPolyLine> ringsForRole(OSMRelation relation, String role){

        Graph.Builder<Point> graphBuilder = new Graph.Builder<Point>(); 
        Set<Point> visitedPoints = new HashSet<Point> ();
        
        for (OSMRelation.Member aMember: relation.members()){
            if (aMember.type().equals(OSMRelation.Member.Type.WAY) && aMember.role().equals(role)){
                Point prevNode = null;
                for (OSMNode node: ((OSMWay)aMember.member()).nodes()){                  
                    Point currNode = projection.project(node.position());
                    graphBuilder.addNode(currNode);
                    if (prevNode != null){
                        graphBuilder.addEdge(currNode, prevNode); 
                    }
                    prevNode = currNode;
                }
             }                         
        }
        
        Graph<Point> graph = graphBuilder.build();
        
        List<ClosedPolyLine> lineList = new ArrayList<ClosedPolyLine>();
        List<Point> currentLine;
        Point currentPoint;
        for (Point aPoint: graph.nodes()){
            if(graph.neighborsOf(aPoint).size()!=2){
                return new ArrayList<ClosedPolyLine>();               
            }
            if(visitedPoints.contains(aPoint)==false){
                currentLine = new ArrayList<Point>(); 
                currentPoint = aPoint;
                boolean endRing =false;
                while (endRing==false){
                    currentLine.add(currentPoint);
                    visitedPoints.add(currentPoint);
                    List<Point> unvisitedNeighbors = new ArrayList<>(graph.neighborsOf(currentPoint));
                    unvisitedNeighbors.removeIf(x -> visitedPoints.contains(x));
                    if (unvisitedNeighbors.size()==0) endRing=true;
                    else currentPoint=unvisitedNeighbors.get(0);                           
                }
                lineList.add(new ClosedPolyLine(currentLine));    
           }
       }
       return lineList;
    }
    
    private List<Attributed<Polygon>> assemblePolygon(OSMRelation relation, Attributes attributes){
        
        List<Attributed<Polygon>> polygonList = new ArrayList<Attributed<Polygon>>();
        HashMap<ClosedPolyLine,List<ClosedPolyLine>> ringMap = new HashMap <ClosedPolyLine,List<ClosedPolyLine>>();
        List<ClosedPolyLine> outerLines = this.ringsForRole(relation, "outer");
        List<ClosedPolyLine> innerLines = this.ringsForRole(relation, "inner"); 
        
        for (ClosedPolyLine outerLine: outerLines){
            ringMap.put(outerLine, new ArrayList<ClosedPolyLine>()); 
        }
        Collections.sort(outerLines, (p1, p2) -> ((Double)p1.area()).compareTo((Double)p2.area()));
        
        
        for (ClosedPolyLine innerLine: innerLines){
            Point pointToTest = innerLine.firstPoint();
            int index=0;
            while (index<outerLines.size()&&!outerLines.get(index).containsPoint(pointToTest)) index++;
            if (index<outerLines.size()) {
                ringMap.get(outerLines.get(index)).add(innerLine);
            }
        }
        
        for (Entry<ClosedPolyLine, List<ClosedPolyLine>> entry: ringMap.entrySet()){
            Polygon polygon;
            if(entry.getValue().size()==0){
                polygon = new Polygon(entry.getKey());
            }
            else{
                polygon = new Polygon(entry.getKey(),entry.getValue());
            }
            polygonList.add(new Attributed<Polygon>(polygon,attributes));
        }        
        return polygonList;
    }
    

}
