package ch.epfl.imhof.dem;

import ch.epfl.imhof.PointGeo;
import ch.epfl.imhof.Vector3;

/**
 * Interface pour une classe permettant de représenter un model d'altitude du terrain 
 * 
 * @author Raphael Laporte (251209) / Romain Leteurtre (238162)
 *
 */
public interface DigitalElevationModel extends AutoCloseable {
    
    /**
     * Methode qui prend un point en coordonnées WGS 84 en argument et qui retourne le vecteur normal à la Terre en ce point, 
     * 
     * @param point : Point à la suface de la Terre
     * @return : Le vecteur normal à la surface en le point pris en argument. 
     */
    public Vector3 normalAt(PointGeo point);
    
    /**
     * Méthode héritée de AutoCloseable. 
     * Garanti la fermeture du fichier qui est lu.
     * 
     * @throws: Exception - si les ressources ouvertes ne peuvent pas etre fermées
     */
    public void close() throws Exception;
}
