package ch.epfl.imhof;

/** 
 * Classe générique représentant une entité de type T dotée d'attributs
 * 
 * @author Raphael Laporte (251209) / Romain Leteurtre (238162)
 */

public final class Attributed<T> {

    private T inputValue;
    private Attributes attributes; 
    
    /**
     * Constructeur
     * 
     * @param value: entité représenté par la classe
     * @param attributes: attributs associés à l'entité value
     * 
     * @return Instance représentant un Attributed
     */
    public Attributed (T value, Attributes attributes){
        inputValue = value;
        this.attributes = attributes;
    }
    /**
     * retourne l'entité représenté par la classe
     * 
     * @return entité de type T
     */
    public T value (){
        return inputValue;
    }
    
    /**
     * Retourne les attributs de l'entité
     * 
     * @return les attributs de l'entité
     */
    public Attributes attributes(){
        return attributes; 
    }
    
    /**
     * regarde si l'entité à l'attribut name
     * 
     * @param name: attribut cherché
     * 
     * @return true si name fait partie des attributs de l'entité sinon false
     */
    public boolean hasAttribute(String name){ 
        return attributes.contains(name);
    }
    /**
     * retourne la valeur de l'attribut attributeName
     * 
     * @param attributeName: nom de l'attribut dont on cherche la valeur
     * 
     * @return la valeur de l'attribut (String)
     */
    public String attributeValue(String attributeName){ 
        return attributes.get(attributeName); 
    }
    
    /**
     * retourne la valeur de l'attribut attributeName et une valeur default si elle n'existe pas
     * 
     * @param attributeName: nom de l'attribut dont on cherche la valeur
     * @param defaultValue: le String par default qu'on renvoit en cas de problème
     * 
     * @return la valeur de l'attribut (String) ou la valeur pas défault en cas de problème
     */
    public String attributeValue(String attributeName, String defaultValue){ 
        return attributes.get(attributeName, defaultValue);
    }
    
    /**
     * retourne la valeur (int) de l'attribut attributeName et une valeur default si elle n'existe
     * pas ou si la valeur n'est pas un entier
     * 
     * @param attributeName: nom de l'attribut dont on cherche la valeur
     * @param defaultValue: l'entier par default qu'on renvoit en cas de problème
     * 
     * @return la valeur de l'attribut (int) ou la valeur pas défault en cas de problème
     */
    public int attributeValue(String attributeName, int defaultValue){ 
        return attributes.get(attributeName, defaultValue);
    }
    
}
