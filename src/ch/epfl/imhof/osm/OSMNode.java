package ch.epfl.imhof.osm;

import ch.epfl.imhof.Attributes;
import ch.epfl.imhof.PointGeo;

public final class OSMNode extends OSMEntity {
    
   private PointGeo position;
    
    public OSMNode(long id, PointGeo position, Attributes attributes) {
        super(id,attributes);
        this.position=position;
    }
    
    public PointGeo position() {
        return position;
    }
    
    public final static class Builder extends OSMEntity.Builder {
        private PointGeo position;
        
        public Builder(long id,PointGeo position) {
            super(id);
            this.position=position;
        }
        
        public OSMNode build() throws IllegalStateException {
            if (isIncomplete()) throw new IllegalStateException();
            return new OSMNode(id,position,attributes.build()); //Comment faire pour id et attributes?Protected?
        }
    }
}
