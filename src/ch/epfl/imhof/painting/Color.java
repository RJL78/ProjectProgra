package ch.epfl.imhof.painting;


/** 
 * Classe immuable représentant une couleur, décrite par ses trois composantes rouge, verte et bleue
 * 
 * @author Raphael Laporte (251209) / Romain Leteurtre (238162)
 */
public class Color {
    /**
     * Constantes publiques, représentant chacune une couleur différente ( Rouge, vert, bleu, noir, blanc ) 
     */
    public static final Color RED = new Color(1,0,0);
    public static final Color GREEN = new Color(0,1,0);
    public static final Color BLUE = new Color(0,0,1);
    public static final Color BLACK = new Color(0,0,0);
    public static final Color WHITE = new Color (1,1,1);
    
    private final double red, green, blue;

    private Color(double red, double green, double blue) throws IllegalArgumentException {
        if (red>1 || red<0 || green>1 || green<0 || blue>1 || blue<0) throw new IllegalArgumentException();
        this.red = red;
        this.green = green; 
        this.blue = blue;
    }
    
    /**
     * Retourne une nuance de gris
     * 
     * @param scale : pourcentage de la nuance de gris
     * 
     * @return une couleur correspondant à la nuance de gris donnée
     */
    public static Color gray(double scale){
        return new Color(scale,scale,scale); 
    }
    
    /**
     * Construit une couleur avec les composantes données
     * 
     * @param red : composante rouge 
     * @param green : composante verte
     * @param blue : composante bleue
     * 
     * @return couleur ayant les composantes données
     */
    public static Color rgb(double red, double green, double blue){
        return new Color (red,green,blue);    
    }
    
    /**
     * Convertit la couleur this en couleur de l'API Java
     * 
     * @return la couleur de l'API Java
     */
    public java.awt.Color toAPIColor(){
        return new java.awt.Color((float)red,(float)green,(float)blue);
    }
    
    /**
     * Construit une couleur avec les composantes empaquetées dans l'entier donné
     * 
     * @param bits : entier empaquetant les composantes rouge, verte, et bleue
     * 
     * @return une couleur avec les composantes données
     */
    public static Color rgb(int bits){
        return new Color(((bits >> 16) & 0xFF) / 255d,
                        ((bits >>  8) & 0xFF) / 255d,
                        ((bits >>  0) & 0xFF) / 255d);
    }
    
    /**
     * Retourne le produit de deux couleurs : les composantes sont multipliées entre elles
     * 
     * @param that : couleur avec laquelle on multiplie this
     * 
     * @return le produit des deux couleurs
     */
    public Color multiply(Color that){
        return new Color(red*that.red,green*that.green,blue*that.blue);
    }
    
    /**
     * Getter pour la composante rouge
     * 
     * @return la composante rouge
     */
    public double getRed(){
        return red;
    }
    
    /**
     * Getter pour la composante bleue
     * 
     * @return la composante bleue
     */
    public double getBlue(){
        return blue;
    }
    
    /**
     * Getter pour la composante verte
     * 
     * @return la composante verte
     */
    public double getGreen(){
        return green;
    }

}
