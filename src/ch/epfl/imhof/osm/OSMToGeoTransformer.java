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
 */
public final class OSMToGeoTransformer {

    private final Projection projection;

    /**
     * Constructeur 
     * 
     * @param projection : projection utilisée pour la conversion
     */
    public OSMToGeoTransformer(Projection projection) {
        this.projection = projection;
    }

    /**
     * Convertit une carteOSM en une carte géométrique projetée
     * 
     * @param map : carteOSM à convertir
     * 
     * @return une carte (Map) convertie selon la projection utilisée
     */
    public Map transform(OSMMap map){

        List<Attributed<PolyLine>> polylines = new ArrayList<>();
        List<Attributed<Polygon>> polygones = new ArrayList<>();
        List<OSMRelation> multiPolygons = new ArrayList<>(map.relations());
        Set<String> keysToKeepLine = new HashSet<>(Arrays.asList("bridge", "highway", "layer", "man_made", "railway", "tunnel", "waterway"));
        Set<String> keysToKeepGon = new HashSet<>(Arrays.asList("building", "landuse", "layer", "leisure", "natural","waterway"));

        for (OSMWay way : map.ways()) {
            List<Point> points = new ArrayList<>();
            for (OSMNode node : way.nonRepeatingNodes()) {
                Point newPoint = projection.project(node.position());
                points.add(newPoint);
            }
            if (way.isClosed()) {
                if (isSurface(way) && shouldKeep(keysToKeepGon, way)) {
                    polygones.add(new Attributed<Polygon>(new Polygon(new ClosedPolyLine(points),new ArrayList<ClosedPolyLine>()) ,way.attributes().keepOnlyKeys(keysToKeepGon)));
                }
                else if (shouldKeep(keysToKeepLine, way)&& !isSurface(way)){
                    polylines.add(new Attributed<PolyLine>(new ClosedPolyLine(points),way.attributes().keepOnlyKeys(keysToKeepLine)));
                }
            }
            else if (shouldKeep(keysToKeepLine, way)) {
                polylines.add(new Attributed<PolyLine>(new OpenPolyLine(points),way.attributes().keepOnlyKeys(keysToKeepLine)));
            }
        }

        multiPolygons.removeIf(relation -> !relation.hasAttribute("type"));
        multiPolygons.removeIf(relation -> !relation.attributeValue("type").equals("multipolygon"));
        for (OSMRelation relation : multiPolygons) {

            if (shouldKeep(keysToKeepGon, relation)){
                polygones.addAll(this.assemblePolygon(relation,relation.attributes().keepOnlyKeys(keysToKeepGon)));
            }
        }
        return new Map(polylines,polygones);
    }

    private boolean shouldKeep(Set<String> keysToKeep, OSMEntity ent){
        for (String key: keysToKeep){
            if(ent.hasAttribute(key)){
                return true;
            }
        }
        return false; 
    }

    private boolean isSurface(OSMWay way) {
        String[] possibilities1 = {"yes","1","true"};
        String[] possibilities2 = {"aeroway", "amenity", "building", "harbour", "historic","landuse", "leisure", "man_made", "military", "natural","office", "place", "power", "public_transport", "shop","sport", "tourism", "water", "waterway", "wetland"};
        if (way.hasAttribute("area")){
            for (int i=0; i<3; i++){
                if (way.attributeValue("area").equals(possibilities1[i])){
                    return true;
                }
            }
        }
        for (String s : possibilities2) {
            if (way.hasAttribute(s)){
                return true;
            }
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
