package ch.epfl.imhof.painting;
import java.util.Arrays;
import java.util.function.Predicate;

import ch.epfl.imhof.*;

public abstract class RoadPainterGenerator {
    
    private static Predicate<Attributed<?>> IS_ROAD = Filters.tagged("highway");
    private static Predicate<Attributed<?>> IS_BRIDGE = IS_ROAD.and(Filters.tagged("bridge"));
    private static Predicate<Attributed<?>> IS_TUNNEL = IS_ROAD.and(Filters.tagged("tunnel")); 
    private static float[] defaultLineSequence = {100000000000000f};
            
    private RoadPainterGenerator() {
    }
    
    public static Painter painterForRoads(RoadSpec ... args){
        return recursivePainterForRoads(args); 
    }
    
    private static Painter recursivePainterForRoads(RoadSpec[] a){
        Painter curr = specificPainter(a[0].getWi(),a[0].getCi(),a[0].getWc(),a[0].getCc()).when(a[0].getPredicate());
        return (a.length==1) ? curr : curr.above(recursivePainterForRoads(Arrays.copyOfRange(a, 1, a.length)));
    }
    
    private static Painter specificPainter(float wi, Color ci, float wc, Color cc){
        LineStyle IntStyle = new LineStyle(ci,wi,LineStyle.LINE_END.ROUND,LineStyle.JOINT.ROUND,defaultLineSequence);
        LineStyle BorderStyle = IntStyle.withWidth(wi+2*wc).withColor(cc).withLineEnd(LineStyle.LINE_END.BUTT);
        float[] array = {2*wi, 2*wi};
        
        return (Painter.line(IntStyle).when(IS_BRIDGE))
                .above(Painter.line(BorderStyle).when(IS_BRIDGE))
                .above(Painter.line(IntStyle).when(IS_ROAD))
                .above(Painter.line(BorderStyle.withLineEnd(LineStyle.LINE_END.ROUND)).when(IS_ROAD))
                .above(Painter.line(BorderStyle.withWidth(wi/2).withLineSequence(array)).when(IS_TUNNEL));       
    }
    
    public static class RoadSpec{
        
        private final Predicate<Attributed<?>> predicate;
        private final float wi;
        private final Color ci;
        private final float wc;
        private final Color cc;
        
        public RoadSpec(Predicate<Attributed<?>> predicate, float wi, Color ci, float wc, Color cc){
            this.predicate = predicate; 
            this.wi = wi;
            this.wc = wc; 
            this.ci = ci; 
            this.cc = cc; 
        }
        
        public Predicate<Attributed<?>> getPredicate(){
            return predicate;
        }
        
        public float getWi(){
            return wi;
        }
        
        public Color getCi(){
            return ci;
        }
        
        public float getWc(){
            return wc;
        }
        
        public Color getCc(){
            return cc;
        }
        
        
    }
    
    

}
