package ch.epfl.imhof.osm;

import ch.epfl.imhof.Attributes;
import ch.epfl.imhof.PointGeo;
import ch.epfl.imhof.geometry.Point;


/**
 * classe representant un noeud OSM
 * Herite de OSMEnitity - Classe Immuable 
 * 
 * 
 * @author Raphael Laporte (251209) / Romain Leteurtre (238162)
 *
 */
public final class OSMNode extends OSMEntity {
    
   private PointGeo position;
    /**
     * Constructeur pour OSM node - utilise par le builder
     * 
     * 
     * @param id : numero d'identification de l'objet OSM
     * @param position : un PointGeo representant la position du Noeud 
     * @param attributes : les attributs lies a ce Noeud
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
    
    public boolean equals (Object o){
        return ((o.getClass()==this.getClass())? (((OSMNode)o).position().equals(this.position())): false);
    }
    
    /**
     * Classe imbriquee statiquement servant de builder
     *
     */
    
    public final static class Builder extends OSMEntity.Builder {
        private PointGeo position;
        
        /**
         * Constructeur du builder 
         * @param id :  numero d'identification de l'objet OSM
         * @param position : un PointGeo representant la position du Noeud 
         */
        public Builder(long id,PointGeo position) {
            super(id);
            this.position=position;
        }
        /**
         * methode retournant le noeud OSM a construire 
         * @return le Noeud OSM avec les caracteristiques desirees 
         * @throws IllegalStateException : exception levee si le Builder est dans un etat "incomplete == true" 
         */
        
        public OSMNode build() throws IllegalStateException {
            if (isIncomplete()){
                throw new IllegalStateException();
            }
            return new OSMNode(id,position,attributes.build()); 
        }
    }
}
