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

/** 
 * Interface fonctionnelle représentant un peintre 
 * 
 * @author Raphael Laporte (251209) / Romain Leteurtre (238162)
 */
public interface Painter {
     
    /**
     * Permet de dessiner la carte sur la toile
     * 
     * @param canvas
     * @param map
     */
    public void drawMap(Canvas canvas, Map map);
    
    
    /**
     * Crée un peintre qui dessine l'interieur de tous les polygons de la carte avec une couleur donnée
     * 
     * @param color : couleur de dessin
     * 
     * @return un peintre dessinant l'interieur de tous les polygons de la carte avec la couleur donnée
     */
    public static Painter polygon(Color color) {
        return (canvas,map) -> {
            for (Attributed<Polygon> attributedPolygon : map.polygons()) {
                canvas.drawPolygon(attributedPolygon.value(),color);
            }
        };
    }
    
    /**
     * Crée un peintre dessinant toutes les lignes de la carte qu'on lui fournit avec un style donné
     * 
     * @param style : style des lignes
     * 
     * @return peintre dessinant toutes les lignes de la carte avec le style donné
     */
    public static Painter line(LineStyle style) {
        return (canvas,map) -> {
            for (Attributed<PolyLine> attributedPolyline : map.polyLines()) {
                canvas.drawPolyline(attributedPolyline.value(), style);
            }
        };
    }
    
    /**
     * Crée un peintre dessinant toutes les lignes de la carte qu'on lui fournit avec un style correspondant
     * aux éléments de style donnés
     * 
     * @param color : couleur de la ligne
     * @param thickness : épaisseur de la ligne
     * @param lineEnd : terminaision de la ligne
     * @param joint : jointure de la ligne
     * @param lineSequence : séquence d'alternance des segments  
     * 
     * @return peintre dessinant toutes les lignes de la carte avec le style correspondant aux éléments donnés
     */
    public static Painter line(Color color, float thickness, LINE_END lineEnd, JOINT joint, float[] lineSequence) {
        return line(new LineStyle(color, thickness, lineEnd, joint, lineSequence));
    }
    
    /**
     * Crée un peintre dessinant toutes les lignes de la carte qu'on lui fournit avec un style correspondant
     * aux éléments de style donnés
     * 
     * @param color : couleur de la ligne
     * @param thickness : épaisseur de la ligne
     * 
     * @return peintre dessinant toutes les lignes de la carte avec le style correspondant aux éléments donnés
     */
    public static Painter line(Color color, float thickness) {
        return line(new LineStyle(color,thickness));
    }
    
    /**
     * Crée un peintre dessinant les pourtours de l'enveloppe et des trous de tous les polygones de la carte
     * qu'on lui fournit avec un style donné
     * 
     * @param style : style des lignes
     * 
     * @return peintre dessinant les pourtours de l'enveloppe et des trous de tous les polygones de la carte avec le style donné
     */
    public static Painter outline(LineStyle style) {
        return (canvas,map) -> {
            for (Attributed<Polygon> attributedPolygon : map.polygons()) {
                canvas.drawPolyline(attributedPolygon.value().shell(), style);
                for (PolyLine polylineHole : attributedPolygon.value().holes()) {
                    canvas.drawPolyline(polylineHole, style);
                }
            }
        };
    }
    
    /**
     * Crée un peintre dessinant les pourtours de l'enveloppe et des trous de tous les polygones de la carte qu'on lui fournit
     * avec un style correspondant aux éléments de style donnés
     * 
     * @param color : couleur de la ligne
     * @param thickness : épaisseur de la ligne
     * @param lineEnd : terminaision de la ligne
     * @param joint : jointure de la ligne
     * @param lineSequence : séquence d'alternance des segments  
     * 
     * @return peintre dessinant les pourtours de l'enveloppe et des trous de tous les polygones de la carte
     * avec le style correspondant aux éléments de style donnés
     */
    public static Painter outline(Color color, float thickness, LINE_END lineEnd, JOINT joint, float[] lineSequence) {
        return outline(new LineStyle(color, thickness, lineEnd, joint, lineSequence));
    }
    
    /**
     * Crée un peintre dessinant les pourtours de l'enveloppe et des trous de tous les polygones de la carte qu'on lui fournit
     * avec un style correspondant aux éléments de style donnés
     * 
     * @param color : couleur de la ligne
     * @param thickness : épaisseur de la ligne  
     * 
     * @return peintre dessinant les pourtours de l'enveloppe et des trous de tous les polygones de la carte
     * avec le style correspondant aux éléments de style donnés
     */
    public static Painter outline(Color color, float thickness) {
        return outline(new LineStyle(color,thickness));
    }
    
    /**
     * Retourne un peintre se comportant comme celui auquel on l'applique,
     * mais considèrent que les éléments de la carte satisfaisant le prédicat donné
     * 
     * @param pred : le prédicat qui filtre les élément de la carte
     * 
     * @return peintre comme celui auquel on l'applique qui ne considère que les éléments satisfaisant le prédicat donné
     */
    public default Painter when(Predicate<Attributed<?>> pred) {
        return (canvas,map) -> {
            List<Attributed<PolyLine>> polylines = new ArrayList<>(map.polyLines());
            List<Attributed<Polygon>> polygons = new ArrayList<>(map.polygons());
            polygons.removeIf(pred.negate());
            polylines.removeIf(pred.negate());
            drawMap(canvas,new Map(polylines,polygons));
        };
    }
    /**
     * Retourne un peintre dessinant d'abord la carte produite par le peintre pris en argument et,
     * par dessus, la carte produite par le peintre auquel on l'applique
     * 
     * @param p : peintre utilisé en premier
     * 
     * @return un peintre qui est la composition des deux peintres
     */
    public default Painter above(Painter p) {
        return (canvas,map) -> {
            p.drawMap(canvas,map);
            this.drawMap(canvas,map);
        };
    }
    
    /**
     * Retourne un peintre utilisant l'attribut layer attaché aux entités de la carte pour la dessiner par couches
     * 
     * @return le peintre dessinant par couche
     */
    public default Painter layered() {
        return layered(-5,5);
    }
    
    public default Painter layered(int n, int m) {
        if (n==m) return when(Filters.onLayer(n));
        return layered(n+1,m).above(when(Filters.onLayer(n)));
    }
}
