package ch.epfl.imhof.osm;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;

import ch.epfl.imhof.Attributed;
import ch.epfl.imhof.Attributes;
import ch.epfl.imhof.Graph;
import ch.epfl.imhof.Map;
import ch.epfl.imhof.geometry.ClosedPolyLine;
import ch.epfl.imhof.geometry.Point;
import ch.epfl.imhof.geometry.Polygon;
import ch.epfl.imhof.projection.Projection;

public final class OSMToGeoTransformer {
    
    Projection projection;

    public OSMToGeoTransformer(Projection projection) {
        this.projection = projection;
    }
    
    public Map transform(OSMMap map){
        
        
        return null;
    }
    
    
    private List<ClosedPolyLine> ringsForRole(OSMRelation relation, String role){

        Graph.Builder<Point> graphBuilder = new Graph.Builder<Point>(); 
        Set<Point> visitedPoints = new HashSet<Point> ();
        
        for (OSMRelation.Member aMember: relation.members()){
            if (aMember.type()==OSMRelation.Member.Type.WAY && aMember.role()==role){
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
                boolean endRing = false;
                while (endRing == false){
                    currentLine.add(currentPoint);
                    visitedPoints.add(currentPoint);
                    endRing = true;
                    for (Point neighbor: graph.neighborsOf(currentPoint)){
                        if (visitedPoints.contains(neighbor)==false){
                            endRing = false;
                            currentPoint = neighbor;
                        }
                    }                  
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
        
        for (ClosedPolyLine innerLine: innerLines){
            TreeMap<Double,ClosedPolyLine> containing = new TreeMap <Double,ClosedPolyLine>();
            for (ClosedPolyLine outerLine: outerLines){
                boolean contains = true; 
                for (Point aPoint: innerLine.points()){
                    if (outerLine.containsPoint(aPoint)==false){
                        contains = false;
                    }
                }
                if(contains){
                    containing.put(innerLine.area(), innerLine); // you mean outerline right ?
                    // since you're adding all the outers that contains that inner
                }
            }
            ringMap.get(containing.lastEntry().getValue()).add(innerLine); // if outers without inners ?
            // and why choose the biggest one ? isn't it the smallest one we have to choose? thus firstentry?
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
