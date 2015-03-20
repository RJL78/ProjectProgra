package ch.epfl.imhof.osm;

import java.util.ArrayList;
import java.util.List;

import ch.epfl.imhof.Map;
import ch.epfl.imhof.geometry.ClosedPolyLine;
import ch.epfl.imhof.projection.Projection;

public final class OSMToGeoTransformer {
    
    Projection projection;

    public OSMToGeoTransformer(Projection projection) {
        this.projection = projection;
    }
    
    public Map transform(OSMMap map){
        
        
        return null;
    }
    
    private List<ClosedPolyLine> ringsForRole(OSMRelation relation, String role){
        List<OSMWay> outerWays = new ArrayList<OSMWay>();
        List<OSMWay> innerWays = new ArrayList<OSMWay>();
        for (OSMRelation.Member aMember: relation.members()){
            if (aMember.type()==OSMRelation.Member.Type.WAY){
                switch(aMember.role()){
                    case "outer":
                        outerWays.add((OSMWay)aMember.member());
                        break;
                    case "inner":
                        innerWays.add((OSMWay)aMember.member());
                        break;
                    default:
                        return null;
                }
                
            }
        }
        return null; 
    }
    

}
