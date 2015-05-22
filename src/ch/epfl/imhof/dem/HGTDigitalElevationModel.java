package ch.epfl.imhof.dem;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ShortBuffer;
import java.nio.channels.FileChannel.MapMode;

import ch.epfl.imhof.PointGeo;
import ch.epfl.imhof.Vector3;

/**
 * Classe permetant de lire un fichier HGT pour en extraire une un Modèle Numérique du terrain
 * 
 * @author Raphael Laporte (251209) / Romain Leteurtre (238162)
 *
 */
public class HGTDigitalElevationModel implements DigitalElevationModel {
    
    private ShortBuffer buffer; 
    private final double pointsPerSide, seperation, lowerLeftLongitude,lowerLeftLatitude;
    private final FileInputStream stream; 

    /**
     * Constructeur pour la classe
     * 
     * @param f: le fichier HGT à lire, qui contient les données nécéssaires pour construire le modèle numérique du terrain 
     * @throws IllegalArgumentException : lorsque le nom du fichier n'est pas valide
     * @throws IOException : lorsque le fichier ne peut pas être lu 
     */
    public HGTDigitalElevationModel(File f) throws IllegalArgumentException, IOException {
        
        // !!! ASK TA !!! Seperating expression for expressions
        
        String name = f.getName();
        pointsPerSide = Math.sqrt(f.length()/2.0);
        if (pointsPerSide<=1) throw new IOException("File too small"); 
            // Pour éviter une division par 0 ou une séparation negative 
        seperation =  Math.toRadians(1/(pointsPerSide-1.0));
        
        if (!name.endsWith(".hgt") ||
            name.length()!=11  || 
            !(name.startsWith("N")||name.startsWith("S")) || 
            !(name.startsWith("W", 3)|| name.startsWith("E", 3)))
            throw new IllegalArgumentException("Invalid filename");
        
        try{
            lowerLeftLatitude = Math.toRadians((Integer.parseInt(name.substring(1,3)) * ((name.startsWith("N"))? 1: -1)));
            lowerLeftLongitude = Math.toRadians((Integer.parseInt(name.substring(4,7)) * ((name.startsWith("W", 3))? -1: 1))); 
        }
        catch (NumberFormatException e){
            throw new IllegalArgumentException("Invalid filename");
        }        
            
        if (!(lowerLeftLongitude>= -180 && lowerLeftLongitude<=179) || 
            !(lowerLeftLatitude>=-90 && lowerLeftLatitude<=89) || 
            pointsPerSide%1 !=0 ) 
                throw new IllegalArgumentException("Invalid filename");
    
        stream = new FileInputStream(f);
        buffer = stream.getChannel().map(MapMode.READ_ONLY, 0, f.length()).asShortBuffer();
    }       
    
    /**
     * Méthode vide: est nécéssaire car DigitalEvationModel implémente AutoCloseable - cependant, le constructeur de 
     * cette classe assure déjà que les ressources seront fermées. 
     */
    public void close() throws IOException{
        stream.close();
        buffer = null; 
    }
   
    /**
     * Methode qui prend un point en coordonnées WGS 84 en argument et qui retourne le vecteur normal à la Terre en ce point, 
     * 
     * @param point : Point à la suface de la Terre
     * @return : Le vecteur normal à la surface en le point pris en argument. 
     * @throws IllegalArgumentException: lancée si le point pris en argument n'est pas dans la zone couverte par le fichier HGT mis en paramètre dans le constructeur. 
     */
    public Vector3 normalAt(PointGeo point) throws IllegalArgumentException{
        double lat = point.latitude();
        double lon = point.longitude();
        
        // !!! ASK TA !!! Math.PI/180.0  or Math.toRadians (1) ? 
        if (lat < lowerLeftLatitude || 
            lat > lowerLeftLatitude + Math.toRadians(1)|| 
            lon < lowerLeftLongitude || 
            lon > lowerLeftLongitude + Math.toRadians(1)) 
                throw new IllegalArgumentException("Point is outside zone convered by file passed to instance's constructor");
      

        // Quadrillions la zone couverte par le fichier mis en paramètre dans le constructeur, de facon à ce que chaque noeud corresponde à un point de donnée; 
        // LatIndex et lonIndex corresponded aux coordonnées du noeud le plus proche au nord-ouest du point mis en paramètre. 
        int latIndex = lat==lowerLeftLatitude ? (int) pointsPerSide-1 :  (int) Math.floor((Math.toRadians(1)-(lat-lowerLeftLatitude))/seperation);
        int lonIndex = lon==lowerLeftLongitude+Math.toRadians(1) ? (int) pointsPerSide-1 : (int) Math.floor((lon-lowerLeftLongitude)/seperation);
        
        double a = get (latIndex,lonIndex); 
        double b = get (latIndex+1,lonIndex);
        double c = get (latIndex, lonIndex+1); 
        double d = get (latIndex+1, lonIndex+1);
        double s = seperation*Earth.RADIUS;
        
        return new Vector3(0.5*s*(a-b+c-d),-0.5*s*(a+b-c-d),s*s);
    }
    
    // Méthode privée permettant de passer d'une paire de coordonnées (du noeud dans le quadrillage metionnée ci dessus) à une altitude. 
    private double get(int latIndex, int lonIndex){
        return buffer.get(latIndex*(int)pointsPerSide+lonIndex);
    }
    
    

}
