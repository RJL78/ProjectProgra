package ch.epfl.imhof.osm;

import ch.epfl.imhof.Attributes;

/**
 * Classe abstraite et immuable représentant une entité OSM
 *  
 * @author Raphael Laporte (251209) / Romain Leteurtre (238162)
 */
public abstract class OSMEntity {

    private final long id;
    private final Attributes attributes;

    /**
     * Constructeur pour OSM Entity - utilisé par le builder
     * 
     * @param id : numéro d'identification de l'objet OSM
     * @param attributes : les attributs liés a cette entité
     */
    public OSMEntity(long id, Attributes attributes) {
        this.id=id;
        this.attributes=attributes;
    }

    /**
     * Getter pour l'id de l'entité
     * 
     * @return: l'id de l'entité
     */
    public long id() {
        return id;
    }

    /**
     * Getter pour les attributs de l'entité
     * 
     * @return: les attributs de l'entité
     */
    public Attributes attributes() {
        return attributes;
    }

    /**
     * Retourne un boolean pour voir si l'entité contient un certain attribut
     * 
     * @param key : clé de l'attribut cherché
     * 
     * @return: true si l'entité a cet attribut ; sinon false
     */
    public boolean hasAttribute(String key) {
        return attributes.contains(key);
    }

    /**
     * Retourne la valeur de l'attribut demandé
     * 
     * @param key : clé de l'attribut cherché
     * 
     * @return: La valeur (String) de l'attribut demandé
     */
    public String attributeValue(String key) {
        return attributes.get(key);
    }

    /**
     * Classe imbriquée statiquement dans OSMEntity
     * Builder de la classe
     * 
     * @author Raphael Laporte (251209) / Romain Leteurtre (238162)
     */
    public static abstract class Builder {
        protected final long id;
        protected final Attributes.Builder attributes;
        private boolean incomplete;

        /**
         * Constructeur du builder 
         * 
         * @param id :  numéro d'identification de l'objet OSM
         */
        public Builder(long id) {
            this.id=id;
            incomplete=false;
            attributes = new Attributes.Builder();
        }

        /**
         * Ajoute un attribut à ceux de l'entité en construction
         * 
         * @param key : clé de l'attribut
         * @param value : valeur de l'attribut
         */
        public void setAttribute(String key, String value) {
            attributes.put(key, value);
        }

        /**
         * Déclare l'entite en construction incomplète
         */
        public void setIncomplete() {
            incomplete=true;
        }

        /**
         * Retourne un boolean regardant si l'entité en construction est complète
         * 
         * @return true si l'entité en construction est incomplète; sinon false
         */
        public boolean isIncomplete() {
            return incomplete;
        }
    }
}
