package ch.epfl.imhof;

import ch.epfl.imhof.geometry.Point;

/** 
 * Classe modelisant un point dans le repere WGS84
 * 
 * @author Raphael Laporte (251209) / Romain Leteurtre (238162)
 *
 */


public final class PointGeo {
    
    private final double longitude, latitude;
    
    /**
     * Constructeur 
     * 
     * @param longitude : La longitude du point, en radians
     * @param latitude : La latitude du point, en radians
     * @throws IllegalArgumentException : Lancee si la longitude et la latitude mis en parametre n'appartiennent pas
     *       aux intervalles respectifs [-PI;PI] et [-PI/2;PI/2]
     *       
     * @return Instance modelisant le point desiree.
     */ 
    
    public PointGeo( double longitude, double latitude) throws IllegalArgumentException{
        if (longitude>Math.PI || longitude < -Math.PI || latitude > Math.PI/2 || latitude < -Math.PI/2 ){
            throw new IllegalArgumentException("lat " + latitude + " and lon " + longitude);
        }
        this.longitude = longitude;
        this.latitude = latitude;
    }
    /** Getter pour la longitude
     * 
     * @ return: la longitude du point, en radians 
     */
    public double longitude(){
        return longitude;
    }
    /**
     * Getter pour la latitude
     * @ return: la latitude du point, en radians 
     */
    
    public double latitude(){
        return latitude;
    }
    
    public boolean equals(Object o){
        return ((o.getClass()==this.getClass())? (((PointGeo)o).longitude()==longitude && ((PointGeo)o).latitude()==latitude ) : false);
    }

}





