package ch.epfl.imhof.painting;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

import ch.epfl.imhof.Attributed;
import ch.epfl.imhof.Map;
import ch.epfl.imhof.geometry.Polygon;
import ch.epfl.imhof.geometry.PolyLine;
import ch.epfl.imhof.painting.LineStyle.JOINT;
import ch.epfl.imhof.painting.LineStyle.LINE_END;

public interface Painter {
     
    public void drawMap(Canvas c, Map map);
    
    public static Painter polygon(Color color) {
        return (c,m) -> {
            for (Attributed<Polygon> attributedPolygon : m.polygons()) {
                c.drawPolygon(attributedPolygon.value(),color);
            }
        };
    }
    
    public static Painter line(LineStyle style) {
        return (c,m) -> {
            for (Attributed<PolyLine> attributedPolyline : m.polyLines()) {
                c.drawPolyline(attributedPolyline.value(), style);
            }
        };
    }
    
    public static Painter line(Color color, float thickness, LINE_END lineEnd, JOINT joint, float[] lineSequence) {
        return line(new LineStyle(color, thickness, lineEnd, joint, lineSequence));
    }
    
    public static Painter line(Color color, float thickness) {
        return line(new LineStyle(color,thickness));
    }
    
    public static Painter outline(LineStyle style) {
        return (c,m) -> {
        
            for (Attributed<Polygon> attributedPolygon : m.polygons()) {
                c.drawPolyline(attributedPolygon.value().shell(), style);
                for (PolyLine polyline : attributedPolygon.value().holes()) {
                    c.drawPolyline(polyline, style);
                }
            }
        };
    }
    
    public static Painter outline(Color color, float thickness, LINE_END lineEnd, JOINT joint, float[] lineSequence) {
        return outline(new LineStyle(color, thickness, lineEnd, joint, lineSequence));
    }
    
    public static Painter outline(Color color, float thickness) {
        return outline(new LineStyle(color,thickness));
    }
    
    public default Painter when(Predicate<Attributed<?>> pred) {
        return (c,m) -> {
            List<Attributed<PolyLine>> polylines = new ArrayList<>(m.polyLines());
            List<Attributed<Polygon>> polygons = new ArrayList<>(m.polygons());
            polygons.removeIf(pred.negate());
            polylines.removeIf(pred.negate());
            this.drawMap(c,new Map(polylines,polygons));
        };
    }
    
    public default Painter above(Painter p) {
        return (c,m) -> {
            p.drawMap(c,m);
            this.drawMap(c,m);
        };
    }
    
    public default Painter layered() {
        return layered(-5,5);
    }
    
    public default Painter layered(int n, int m) {
        Painter curr = this.when(Filters.onLayer(m));
        for (int i=m-1; i>=n; i--){
            curr = curr.above(this.when(Filters.onLayer(i)));
        }
        return curr;
    }
}
