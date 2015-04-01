package ch.epfl.imhof;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;


/**
 *  Classe immuable représentant un ensemble d'attributs et de valeurs associées respectives
 *  Classe construite autour d'une java.util.Map  
 *  
 * @author Raphael Laporte (251209) / Romain Leteurtre (238162)
 *
 */
public final class Attributes {
    
    private final Map<String,String> attributes; 
    
    /**
     * Constructeur 
     * 
     * @param attributes : une java.util.Map qui contient les paires ( attribut / valeur ) souhaitées. 
     */
    public Attributes(Map<String, String> attributes){
        this.attributes = new HashMap<String, String> (attributes); 
    }
    
   /**
    * Indique si oui ou non l'objet contient au moins une paire ( attribut / valeur ) 
    * 
    * @return: false si l'objet ne contient aucune paire, sinon oui
    */
    public boolean isEmpty(){
        return attributes.isEmpty(); 
    }
    
    /**
     * Indique si oui ou non l'objet contient une paire associé a l'attribut mis en parametre.
     *  
     * @param key: L'attribut pour lequel on cherche à savoir si il existe une paire
     * 
     * @return: True si il existe une valeur associé à cet attribut, false sinon 
     */    
    public boolean contains(String key){
        return attributes.containsKey(key); 
    }
    
    /**
     * Getter pour la valeur associée à un attribut donné
     * 
     * @param key : l'attribut pour lequel on cherche la valeur associé
     * 
     * @return: La valeur associé à cet attribut, null si l'objet ne contient pas une paire avec l'attribut mis en paramètre  
     */    
    public String get(String key){
        return attributes.get(key); 
    }
    
    /**
     * Getter pour la valeur (sous forme de String) associée à un attribut donné
     * 
     * @param key : l'attribut pour lequel on cherche la valeur
     * @param defaultValue : le String à retourner si l'objet ne contient pas de paire avec l'attribut donne en parametre
     * 
     * @return : La valeur associé à cet attribut, defaultValue si l'objet ne contient pas une paire avec l'attribut mis en paramètre  
     */   
    public String get(String key, String defaultValue){
        return attributes.getOrDefault(key, defaultValue); 
    }
    
    /**
     * Getter pour la valeur ( numerique, retournee sous forme de int ) associé à un attribut donné
     * 
     * @param key : l'attribut pour lequel on cherche la valeur
     * @param defaultValue : Le int à retourner si l'objet ne contient pas de paire avec l'attribut donné en paramètre, ou si la valeure associé à l'attribut ne peut pas etre parsé en int.
     *  
     * @return : la valeur associe au parametre "key" si possible, defaultValue sinon (voir param defaultValue) 
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
     * Retourne une nouvelle instance d'Attributes qui ne contient qu'un sous-ensemble des paires attribut/valeur de l'objet qui appelle la methode
     * 
     * @param keysToKeep: les attributs associés aux paires attribut/valeur que l'on souhaite garder dans le nouvel objet 
     * ( Si un element du Set mis en parametre ne correspond pas à un attribut contenu dans l'objet appelant la méthode, cet element sera ignoré ) 
     * 
     * @return Un objet Attributes contenant seulement les paires attribut/valeur retenus
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
     * Classe imbriqéee statiquement, servant à la construction d'un objet Attributes
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
         * Methode servant à ajouter les paires attributs/valeurs desirées aux builder 
         * 
         * @param key : l'attribut desirée 
         * @param value : la valeur associée desirée
         */
        public void put(String key, String value){
            mapToBuild.put(key, value);
        }
        
        /**
         * Sert à construire un objet Attributes contenant les paires attribut/valeur ajoutées au builder via la methode put(String key, String value)
         * 
         * @return L'objet Attributs avec les paires desirées
         */        
        public Attributes build(){
            return new Attributes(mapToBuild);
        }
    }
}
