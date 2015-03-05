package ch.epfl.imhof;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public final class Attributes {
    
    private Map<String,String> attributes; 
    
    public Attributes(Map<String, String> attributes){
        this.attributes = Collections.unmodifiableMap( new HashMap (attributes)); 
    }
    
    public boolean isEmpty(){
        return attributes.isEmpty(); 
    }
    
    public boolean contains(String key){
        return attributes.containsKey(key); 
    }
    
    public String get(String key){
        return attributes.get(key); 
    }
    
    public String get(String key, String defaultValue){
        return attributes.getOrDefault(key, defaultValue); 
    }
    
    public int get(String key, int defaultValue){
        Integer output; 
        try{ 
            output = Integer.parseInt(attributes.get(key)); 
        }
        catch(ClassCastException e){
            return defaultValue; 
        }
        if (output == null){
            return defaultValue; 
        }
        else{
            return output; 
        }
    }
    
    public Attributes keepOnlyKeys(Set<String> keysToKeep){
        HashMap<String,String> newMap = new HashMap<String,String>(); 
        for (String keyToKeep: keysToKeep){
            newMap.put(keyToKeep,attributes.get(keyToKeep));
        }
        return new Attributes(newMap);
    }

}
