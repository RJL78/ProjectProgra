package ch.epfl.imhof.painting;
import java.util.Arrays;
import java.util.function.Predicate;

import ch.epfl.imhof.*;

public abstract class RoadPainterGenerator {
    
    private static Predicate<Attributed<?>> IS_ROAD = Filters.tagged("highway");
    private static Predicate<Attributed<?>> IS_BRIDGE = IS_ROAD.and(Filters.tagged("bridge"));
    private static Predicate<Attributed<?>> IS_TUNNEL = IS_ROAD.and(Filters.tagged("tunnel")); 
    private static float[] def = {};
   
            
    private RoadPainterGenerator() {
    }
    
    public static Painter painterForRoads(RoadSpec ... args){
        LineStyle templateA = new LineStyle(null, 0f, LineStyle.LINE_END.ROUND,LineStyle.JOINT.ROUND,def);
        LineStyle templateB = templateA.withLineEnd(LineStyle.LINE_END.BUTT);
        return recursivePainterForRoads(args,false,true, templateA).when(IS_BRIDGE)
                .above(recursivePainterForRoads(args,false, false, templateB).when(IS_BRIDGE))
                .above(recursivePainterForRoads(args,false,true, templateA).when(IS_ROAD))
                .above(recursivePainterForRoads(args,false,false, templateA).when(IS_ROAD))
                .above(recursivePainterForRoads(args,true, false, templateB).when(IS_TUNNEL));
    }
    
    private static Painter recursivePainterForRoads(RoadSpec[] a, boolean tunnel, boolean interior, LineStyle l){
        float wi = a[0].getWi();
        float[] seq = {2*wi,2*wi};
        l = l.withColor(interior? a[0].getCi() : a[0].getCc())
                .withWidth(tunnel? wi/2 : interior? wi : wi+a[0].getWc())
                .withLineSequence(tunnel? seq: def);
         
        Painter curr = Painter.line(l).when(a[0].getPredicate());
        return (a.length==1) ? curr : curr.above(recursivePainterForRoads(Arrays.copyOfRange(a, 1, a.length), tunnel, interior, l));
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
