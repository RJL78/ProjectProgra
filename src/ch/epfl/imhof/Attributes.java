package ch.epfl.imhof;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;


/**
 *  Classe immuable representant un ensemble d'attributs et de valeurs associes
 *  Classe construite autrour d'un java.util.Map
 *  
 * @author Raphael Laporte (251209) / Romain Leteurtre (238162)
 *
 */

public final class Attributes {
    
    private Map<String,String> attributes; 
    
    /**
     * Constructeur 
     * @param attributes : une java.util.Map qui contient les paires ( attribut / valeur ) souhaités. 
     */
    public Attributes(Map<String, String> attributes){
        this.attributes = Collections.unmodifiableMap( new HashMap<String, String> (attributes)); 
    }
    
   /**
    * indique si oui ou non l'objet contient au moins une paire ( attribut / valeur ) 
    * 
    * @return: false si l'objet ne contient aucune paire, sinon oui
    */
    
    public boolean isEmpty(){
        return attributes.isEmpty(); 
    }
    
    /**
     * indique si oui ou non l'objet contient une paire associe a l'attribut mis en parametre. 
     * @param key: L'attribut pour lequel on cherche a savoir si il existe une paire,
     * @return: True si l'objet contient une paire avec cet attribut, false sinon 
     */
    
    public boolean contains(String key){
        return attributes.containsKey(key); 
    }
    /**
     * Getter pour la valeur associe a un attribut donne
     * @param key : l'attribut pour lequel on cherche la valeure
     * @return: La valeure associe a cet attribut, null si l'objet ne contient pas une paire avec l'attribut donne en parametre  
     */
    
    public String get(String key){
        return attributes.get(key); 
    }
    
    /**
     * Getter pour la valeur associe a un attribut donne
     * @param key : l'attribut pour lequel on cherche la valeure
     * @param defaultValue : le string a retourner si l'objet ne contient pas de paire avec l'attribut donne en parametre.
     * @return : La valeure associe a cet attribut, defaultValue si l'objet ne contient pas une paire avec l'attribut donne en parametre  
     */
    
    public String get(String key, String defaultValue){
        return attributes.getOrDefault(key, defaultValue); 
    }
    
    
    /**
     * Getter pour la valeur ( numerique, retournee sous forme de int ) associe a un attribut donne
     * @param key : l'attribut pour lequel on cherche la valeure
     * @param defaultValue : Le int a retourner si l'objet ne contient pas de paire avec l'attribut donne en parametre, ou si la valeure associe a l'attribut ne peut pas etre parse en int. 
     * @return : la valeur associe au parametre "key" si possible, defaultValue sinon ( voir ci-dessus ) 
     */
    
    public int get(String key, int defaultValue){
        int output; 
        try{ 
            output = Integer.parseInt(attributes.get(key)); 
        }
        catch(NumberFormatException e){
            return defaultValue;
        }
        return output; 
    }
    
    /**
     * Retourne un Attributes immutable qui ne contient qu'un sous-ensemble des paires attribut/valeure de l'objet qui appelle la methode
     * @param keysToKeep: les attributs associes aux paires que l'on souhaite garder dans le nouvel objet 
     * ( Si un element du Set mis en parametre ne correspond pas a un attribut contenu dans l'objet appelant la methode, cet element sera ignore ) 
     * @return Un objet Attributes contenant seuls les paires attribut/valeure retenus.
     */
    
    public Attributes keepOnlyKeys(Set<String> keysToKeep){
        HashMap<String,String> newMap = new HashMap<String,String>(); 
        for (String keyToKeep: keysToKeep){
            if (attributes.containsKey(keyToKeep)){
                newMap.put(keyToKeep,attributes.get(keyToKeep));
            }
        }
        return new Attributes(newMap);
    }
    /**
     * 
     * Classe imbriquee statiquement, servant a la construction d'un objet Attributes. 
     *
     */
    public static final class Builder {
        
        private HashMap<String,String> mapToBuild;
        /**
         * Constructeur par default pour le builder
         * 
         */
        public Builder (){
            mapToBuild = new HashMap<String,String>(); 
        }
        
        /**
         * Methode servant a ajouter les paires attributs/valeurs desirees aux builder 
         * @param key : l'attribut desiree 
         * @param value : la valeure associee desiree
         */
        
        public void put(String key, String value){
            mapToBuild.put(key, value);
        }
        
        /**
         * Sert a construire un objet Attributes contenant les paires attribut/valeure ajoutees au builder via la methode put(String key, String value)
         * @return L'objet Attributs avec les paires desirees. 
         */
        
        public Attributes build(){
            return new Attributes(mapToBuild);
        }
    }
}
