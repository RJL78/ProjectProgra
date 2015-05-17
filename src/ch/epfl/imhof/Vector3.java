package ch.epfl.imhof;

/**
 * Classe imuable représentant un vecteur de dimension 3 
 * 
 * @author Raphael Laporte (251209) / Romain Leteurtre (238162)
 */
public class Vector3 {
    
    private final double x, y, z; 
    
    /**
     * Constructeur
     * 
     * @param x : La coordonnée en x du vecteur
     * @param y : La coordonnée en y du vecteur 
     * @param z : La coordonnée en z du vecteur
     */
    public Vector3(double x,double y,double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }
    
    /**
     * Méthode donnant la norme du vecteur appelant cette méthode
     * 
     * @return la norme du vecteur 
     */
    public double norm(){
        return Math.sqrt(x*x+y*y+z*z);
    }
    
    
    /**
     * Méthode retournant une version normalisée de ce vecteur
     * 
     * @return un Vecteur3 colinéaire au vecteur sur lequel la méthode à été appelé, mais evec une norme de 1
     */
    public Vector3 normalized(){
        double n = norm();
        return  new Vector3(x/n,y/n,z/n);
    }
    
    /**
     * Méthode permettant le calcul du produit scalaire de deux Vecteur3
     * 
     * @return un Vecteur3 colinéaire au vecteur sur lequel la méthode à été appelé, mais evec une norme de 1
     */
    public double scalarProduct(Vector3 that){
        return this.x*that.x + this.y*that.y +this.z*that.z;
    }

}
