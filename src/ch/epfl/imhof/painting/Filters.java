package ch.epfl.imhof.painting;

import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;

import ch.epfl.imhof.Attributed;

public final class Filters {

    private Filters() {
    }
    
    public static Predicate<Attributed<?>> tagged(String attribute){
        return x -> x.hasAttribute(attribute);
    }
    
    public static Predicate<Attributed<?>> tagged(String attribute, String... values){
        List<String> valuesList = Arrays.asList(values);
        return x -> x.hasAttribute(attribute) && valuesList.contains(x.attributeValue(attribute));
    }
    
    public static Predicate<Attributed<?>> onLayer(int layer) {
        return x -> Integer.parseInt(x.attributeValue("layer", "0")) == layer;
    }
    
}
