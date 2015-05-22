package ch.epfl.imhof.painting;
import java.util.Arrays;
import java.util.function.Predicate;

import ch.epfl.imhof.*;

/** 
 * Classe non-instantiable avec une méthode statique qui fournit le peintre du réseau routier
 * 
 * @author Raphael Laporte (251209) / Romain Leteurtre (238162)
 *
 */
// !!! ASK TA !!! final ? abstract ? for non-instantiable class
public class RoadPainterGenerator {
    
    private static final Predicate<Attributed<?>> IS_BRIDGE = Filters.tagged("highway").and(Filters.tagged("bridge"));
    private static final Predicate<Attributed<?>> IS_TUNNEL = Filters.tagged("highway").and(Filters.tagged("tunnel")); 
    private static final Predicate<Attributed<?>> IS_ROAD = Filters.tagged("highway").and((IS_BRIDGE.or(IS_TUNNEL).negate()));

            
    private RoadPainterGenerator() {
    }
    
    /** 
     * Méthode créant et retourant un peintre du réseau routier. 
     * 
     * @param args : Les roadspec indiquant comment le peintre à retourner doit se comporter ( attention - dans chacune des couches de ce peintre
     * ie. pont interieur, pont exterieur, route interieur, route exterieur, tunnel : la sous-couche correpondant au premier argument sera peinte
     *  au dessus de la sous-couche correspondant au deuxieme et ainsi de suite )
     *              
     * @return : Le peintre avec les caractéristiques voulues 
     *
     */
    public static Painter painterForRoads(RoadSpec ... args){
        LineStyle templateA = new LineStyle(null, 0f, LineStyle.LINE_END.ROUND,LineStyle.JOINT.ROUND,LineStyle.DEFAULT_LINE_SEQUENCE);
        LineStyle templateB = templateA.withLineEnd(LineStyle.LINE_END.BUTT);
        return recursivePainterForRoads(args,false,true, templateA).when(IS_BRIDGE)
                .above(recursivePainterForRoads(args,false, false, templateB).when(IS_BRIDGE))
                .above(recursivePainterForRoads(args,false,true, templateA).when(IS_ROAD))
                .above(recursivePainterForRoads(args,false,false, templateA).when(IS_ROAD))
                .above(recursivePainterForRoads(args,true, false, templateB).when(IS_TUNNEL));
    }
    
    // méthode privée, retourne les peintres correspondant aux elements du 1er paramètre, empilés dans l'ordre ( plus petit indice en haut) 
    // utile pour pouvoir séparer les 5 niveaux pour peindre les route ( cette méthode est appelé par PainterForRoads une fois pour les ponts, une fois pour l'exterieur des tunnels, une fois pour l'interieur des tunnels... et ainsi de suite
    private static Painter recursivePainterForRoads(RoadSpec[] a, boolean tunnel, boolean interior, LineStyle l){
        //RoadSpec spec = a[0] - !!! ASK TA !!! should we add a variable for curr? or is a[0] already a direct memory access?
        float wi = a[0].getWi();
        float[] tunnelSeq = {2*wi,2*wi};
        l = l.withColor(interior? a[0].getCi() : a[0].getCc())
                .withWidth(tunnel? wi/2 : interior? wi : wi+2*a[0].getWc())
                .withLineSequence(tunnel? tunnelSeq: LineStyle.DEFAULT_LINE_SEQUENCE);
         
        Painter curr = Painter.line(l).when(a[0].getPredicate());
        // Ci dessous: appel recursif pour gérer les autres RoadSpec du tableau mis en paramètre 
        return (a.length==1) ? curr : curr.above(recursivePainterForRoads(Arrays.copyOfRange(a, 1, a.length), tunnel, interior, l));
    }
    
    /** 
     * Classe imbriquée statiquement servant de holder pour les différentes variables qui pourraient décrire comment peindre une route
     * 
     * @author Raphael Laporte (251209) / Romain Leteurtre (238162)
     *
     */
    public static class RoadSpec{
        
        private final Predicate<Attributed<?>> predicate;
        private final float wi;
        private final Color ci;
        private final float wc;
        private final Color cc;
        
        /**Constructeur pour le holder 
         * 
         * @param predicate : le prédicat servant à indiquer quand es-ce que une route doit être peinte avec ces caractéristiques 
         * @param wi : largeur de l'intérieur de la route
         * @param ci : couleur de l'intérieur de la route
         * @param wc : largeur de la bordure de la route   
         * @param cc : couleur de la bordure de la route 
         */
        
        public RoadSpec(Predicate<Attributed<?>> predicate, float wi, Color ci, float wc, Color cc){
            this.predicate = predicate; 
            this.wi = wi;
            this.wc = wc; 
            this.ci = ci; 
            this.cc = cc; 
        }
        
        /**
         * Getter
         * 
         * @return le prédicat servant à indiquer quand es-ce que une route doit être peinte avec ces caractéristiques
         */
        public Predicate<Attributed<?>> getPredicate(){
            return predicate;
        }
        
        /**
         * Getter
         * 
         * @return largeur de l'intérieur de la route
         */
        public float getWi(){
            return wi;
        }
        
        /**
         * Getter
         * 
         * @return couleur de l'intérieur de la route
         */
        public Color getCi(){
            return ci;
        }
        
        /**
         * Getter
         * 
         * @return largeur de la bordure de la route 
         */
        public float getWc(){
            return wc;
        }
        
        /**
         * Getter
         * 
         * @return couleur de la bordure de la route 
         */
        public Color getCc(){
            return cc;
        }
        
        
    }
    
    

}
