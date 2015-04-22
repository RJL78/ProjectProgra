package ch.epfl.imhof.painting;

import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;

import ch.epfl.imhof.Attributed;

/** 
 * Classe finale et non-instanciable permettant de retourner certains filtres pour les Attributed<?>
 *  
 * @author Raphael Laporte (251209) / Romain Leteurtre (238162)
 */
public final class Filters {

    private Filters() {
    }
    
    /**
     * Retourne un predicat qui lui retourne vrai si l'élément possède un attribut
     * 
     * @param attribute : attribut qui filtre
     * 
     * @return predicat qui lui retourne vrai si l'élément possède un attribut
     */
    public static Predicate<Attributed<?>> tagged(String attribute){
        return x -> x.hasAttribute(attribute);
    }
    
    /**
     * Retounrne un prédicat qui lui retourne vrai si l'élément possède un attribut et
     * dont la valeur figure dans celles données 
     * 
     * @param attribute : attribut qui filtre
     * 
     * @param values : valeurs que l'élément doit avoir pour l'attribut
     * 
     * @return prédicat qui retourne vrai si l'élément possède un attribut et
     * dont la valeur figure dans celles données 
     */
    public static Predicate<Attributed<?>> tagged(String attribute, String... values){
        List<String> valuesList = Arrays.asList(values);
        return x -> x.hasAttribute(attribute) && valuesList.contains(x.attributeValue(attribute));
    }
    
    /**
     * Retourne un prédicat qui lui retourne vrai si l'attribut "layer" de l'élément est égal
     * à l'entier passé en argument
     * 
     * @param layer : entier layer filtrant
     * 
     * @return prédicat qui lui retourne vrai si l'attribut "layer" de l'élément est égal
     * à l'entier passé en argument
     */
    public static Predicate<Attributed<?>> onLayer(int layer) {
        return x -> Integer.parseInt(x.attributeValue("layer", "0")) == layer;
    } 
}
