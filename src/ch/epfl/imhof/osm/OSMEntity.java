package ch.epfl.imhof.osm;

import ch.epfl.imhof.Attributes;

public abstract class OSMEntity {
    
    private long id;
    private Attributes attributes;
    
    public OSMEntity(long id, Attributes attributes) {
        this.id=id;
        this.attributes=attributes;
    }
    
    public long id() {
        return id;
    }
    
    public Attributes attributes() {
        return attributes;
    }
    
    public boolean hasAttributes(String key) {
        return attributes.contains(key);
    }
    
    public String attributeValue(String key) {
        return attributes.get(key);
    }
    
    public static abstract class Builder {
        protected long id;
        protected Attributes.Builder attributes;
        private boolean incomplete;

        public Builder(long id) {
            this.id=id;
            incomplete=false;
            attributes = new Attributes.Builder();
        }
        
        public void setAttribute(String key, String value) {
            attributes.put(key, value);
        }
        
        public void setIncomplete() {
            incomplete=true;
        }
        
        public boolean isIncomplete() {
            return incomplete;
        }
    }
}
