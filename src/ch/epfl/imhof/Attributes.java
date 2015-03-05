package ch.epfl.imhof;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

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
        int output; 
        return attributes.get
        try{ 
            attributes.getOrDefault(key, defaultValue); 
        }
        catch{ 
            
        }

    }

}
