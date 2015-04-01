package ch.epfl.imhof;

/** 
 * Classe générique représentant un objet de type T doté d'attributs
 * Englobe à la fois l'objet ainsi que ses attributs associés (wrapper class) 
 * Classe Immuable
 * 
 * @author Raphael Laporte (251209) / Romain Leteurtre (238162)
 * 
 * @param <T> La classe de l'objet doté d'attributs 
 */
public final class Attributed<T> {

    private final T inputValue;
    private final Attributes attributes; 

    /**
     * Constructeur d'une instance englobant à la fois un objet doté d'attributs et ses attributs associées
     * 
     * @param value: objet doté d'attributs
     * @param attributes: attributs associés au parametre value
     */
    public Attributed (T value, Attributes attributes){
        inputValue = value;
        this.attributes = attributes;
    }

    /**
     * Getter pour l'objet englobé
     * 
     * @return l'Objet de type T englobé par l'instance sur laquelle la methode est appellé
     */
    public T value (){
        return inputValue;
    }

    /**
     * Getter pour les attributs associés à l'objet englobé. 
     * 
     * @return les attributs de l'entité englobé
     */
    public Attributes attributes(){
        return attributes; 
    }

    /**
     * Verifie si l'objet englobé est associé avec un attribut ayant pour nom le string mis en parametre
     * 
     * @param name: le nom de l'attribut dont on cherche a verifier l'existance
     * 
     * @return true si un attribut ayant pour nom "name" est associé à l'objet englobé, sinon false
     */
    public boolean hasAttribute(String name){ 
        return attributes.contains(name);
    }

    /**
     * Retourne la valeur de l'attribut attributeName
     * 
     * @param attributeName: le nom de l'attribut dont on cherche la valeur associée
     * 
     * @return la valeur de l'attribut dont le nom est mis en paramètre, null si cet attribut n'est pas associé à l'objet englobé
     */
    public String attributeValue(String attributeName){ 
        return attributes.get(attributeName); 
    }

    /**
     * Retourne la valeur de l'attribut ont le nom est mis en parametre, ou une valeur par default si cet attribut n'existe pas
     * 
     * @param attributeName: le nom de l'attribut dont on cherche la valeur associée
     * @param defaultValue: le String servant de valeur par default 
     * 
     * @return la valeur de l'attribut dont le nom est mis en paramètre, defaultValue si cet attribut n'est pas associé à l'objet englobé
     */
    public String attributeValue(String attributeName, String defaultValue){ 
        return attributes.get(attributeName, defaultValue);
    }

    /**
     * Retourne la valeur de l'attribut attributeName ou (defaultValue si elle n'existe
     * pas ou si la valeur n'est pas un entier
     * 
     * @param attributeName: nom de l'attribut dont on cherche la valeur associée
     * @param defaultValue: l'entier servant de valeur par default
     * 
     * @return  la valeur de l'attribut dont le nom est mis en paramètre, defaultValue si cet attribut n'est pas associé à l'objet englobé
     */
    public int attributeValue(String attributeName, int defaultValue){ 
        return attributes.get(attributeName, defaultValue);
    }
}
