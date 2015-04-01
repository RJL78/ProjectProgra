package ch.epfl.imhof.osm;

import ch.epfl.imhof.Attributes;
import ch.epfl.imhof.PointGeo;


/**
 * classe représentant un noeud OSM
 * Hérite de OSMEnitity - Classe Immuable 
 * 
 * @author Raphael Laporte (251209) / Romain Leteurtre (238162)
 */
public final class OSMNode extends OSMEntity {

    private final PointGeo position;

    /**
     * Constructeur pour OSM node - utilisé par le builder
     * 
     * @param id : numéro d'identification de l'objet OSM
     * @param position : un PointGeo représentant la position du Noeud 
     * @param attributes : les attributs liés à ce Noeud
     */
    public OSMNode(long id, PointGeo position, Attributes attributes) {
        super(id,attributes);
        this.position=position;
    }

    /**
     * Getter pour la position du noeud
     * @return: la position du noeud
     */
    public PointGeo position() {
        return position;
    }

    /**
     * Classe imbriquée statiquement servant de Builder
     */   
    public final static class Builder extends OSMEntity.Builder {
        
        private final PointGeo position;

        /**
         * Constructeur du builder 
         * 
         * @param id :  numéro d'identification de l'objet OSM
         * 
         * @param position : un PointGeo représentant la position du Noeud 
         */
        public Builder(long id,PointGeo position) {
            super(id);
            this.position=position;
        }

        /**
         * methode retournant le noeud OSM à construire 
         * 
         * @return le Noeud OSM avec les caracteristiques désirées 
         * 
         * @throws IllegalStateException : exception levée si la méthode setIncomplete() [héritée de OSMEntity.Builder] est appellé préalablement par ce Builder  
         */        
        public OSMNode build() throws IllegalStateException {
            if (isIncomplete()){
                throw new IllegalStateException();
            }
            return new OSMNode(id,position,attributes.build()); 
        }
    }
}
