package ch.epfl.imhof;

public final class Attributed<T> {

    private T inputValue;
    private Attributes attributes; 
    
    public Attributed (T value, Attributes attributes){
        inputValue = value;
        this.attributes = attributes;
    }
    
    public T value (){
        return inputValue;
    }
    
    public Attributes attributes(){
        return attributes; 
    }
    
    public boolean hasAttribute(String name){ 
        return attributes.contains(name);
    }
    
    public String attributeValue(String attributeName){ 
        return attributes.get(attributeName); 
    }
    
    public String attributeValue(String attributeName, String defaultValue){ 
        return attributes.get(attributeName, defaultValue);
    }
    
    public int attributeValue(String attributeName, int defaultValue){ 
        return attributes.get(attributeName, defaultValue);
    }
    
}
