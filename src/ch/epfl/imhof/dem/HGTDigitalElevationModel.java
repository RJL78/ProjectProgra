package ch.epfl.imhof.dem;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ShortBuffer;
import java.nio.channels.FileChannel.MapMode;

import ch.epfl.imhof.PointGeo;
import ch.epfl.imhof.Vector3;

public class HGTDigitalElevationModel implements DigitalElevationModel {
    
    // seperation = resolution angulaire du fichier 
    
    private final  ShortBuffer buffer; 
    private final double pointsPerSide;
    private final double seperation;
    private final double lowerLeftLongitude;
    private final double lowerLeftLatitude; 

    
    public HGTDigitalElevationModel(File f) throws IllegalArgumentException, IOException {
        String name = f.getName();
        pointsPerSide = Math.sqrt(f.length()/2.0);
        seperation = (1/(pointsPerSide-1.0))*Math.PI/180.0;
        try{
            lowerLeftLatitude = (Integer.parseInt(name.substring(1,3)) * ((name.startsWith("N"))? 1: -1))*Math.PI/180.0;
            lowerLeftLongitude = (Integer.parseInt(name.substring(4,7)) * ((name.startsWith("W", 3))? -1: 1))*Math.PI/180.0; 
        }
        catch (NumberFormatException e){
            throw new IllegalArgumentException("Invalid file");
        }
        if (!name.endsWith(".hgt") || !(name.startsWith("N")||name.startsWith("S")) || !(name.startsWith("W", 3)|| name.startsWith("E", 3)) || name.length()!=11
               || !(lowerLeftLongitude>= -180 && lowerLeftLongitude<=179) || !(lowerLeftLatitude>=-90 && lowerLeftLatitude<=89) || pointsPerSide%1 !=0 ) throw new IllegalArgumentException("Invalid file");
    
        try (FileInputStream stream =new FileInputStream(f)) {
           buffer = stream.getChannel().map(MapMode.READ_ONLY, 0, f.length()).asShortBuffer();
        }       
    }
    
    public void close(){
    }
    
    private double get(int latIndex, int lonIndex){
        return buffer.get(latIndex*(int)pointsPerSide+lonIndex);
    }
    
    public Vector3 normalAt(PointGeo point){
        double lat = point.latitude();
        double lon = point.longitude();
        if (lat<lowerLeftLatitude || lat>lowerLeftLatitude + Math.PI/180.0 || lon < lowerLeftLongitude || lon > lowerLeftLongitude+Math.PI/180.0) throw new IllegalArgumentException();
        int latIndex = (lat==lowerLeftLatitude)? (int)pointsPerSide-1 :  (int)Math.floor((Math.PI/180.0-(lat-lowerLeftLatitude))/seperation);
        int lonIndex = (lon==(lowerLeftLongitude+Math.PI/180.0))? (int)pointsPerSide-1 : (int)Math.floor((lon-lowerLeftLongitude)/seperation);
        double a = get (latIndex,lonIndex); 
        double b = get (latIndex+1,lonIndex);
        double c = get (latIndex, lonIndex+1); 
        double d = get (latIndex+1, lonIndex+1);
        double s = seperation*Earth.RADIUS;
        return new Vector3(0.5*s*(a-b+c-d),0.5*s*(a+b-c-d),s*s);
    }
    
    

}
