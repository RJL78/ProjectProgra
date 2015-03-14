package ch.epfl.imhof.osm;

import java.util.ArrayList;
import java.util.List;

import ch.epfl.imhof.Attributes;
/**
 * classe representant une relation OSM
 * Herite de OSMEntity - Classe Immuable 
 * 
 * 
 * @author Raphael Laporte (251209) / Romain Leteurtre (238162)
 *
 */


public final class OSMRelation extends OSMEntity {

    private List<Member> members;
    
    /**
     * Constructeur pour OSM Relation - utilise par le builder
     * 
     * 
     * @param id : numero d'identification de l'objet OSM
     * @param members : liste des membres immliqués dans la relation 
     * @param attributes : les attributs lies a cette realtion
     */
    
    public OSMRelation(long id, List<Member> members,Attributes attributes) {
        super(id,attributes);
        this.members = new ArrayList<Member> (members);
    }
    
    /**
     * Getter pour la liste des membres
     * @return: la liste des membres
     */
    
    public List<Member> members() {
        return new ArrayList <Member> (members);
    }
    
    /**
     * classe imbriquée statiquement représentant un Membre de la relation
     * Classe immuable
     * 
     * @author Raphael Laporte (251209) / Romain Leteurtre (238162)
     *
     */
    
    public final static class Member {
        
        private String role;
        private OSMEntity member;
        private Type type;
        
        /**
         * Constructeur pour OSMRelation.Member
         * 
         * 
         * @param type : correspond au Type du Membre
         * @param role : role du membre (String)
         * @param member : Entité du membre désigné
         */
        
        public Member(Type type, String role, OSMEntity member) {
            this.member=member;
            this.type=type;
            this.role=role;
        }
        
        /**
         * Getter pour le type du membre
         * @return: le type du membre
         */
        
        public Type type() {
            return type;
        }
        
        /**
         * Getter pour l'entité du Membre
         * @return: l'entité du membre
         */
        
        public OSMEntity member() {
            return member;
        }
        
        /**
         * Getter pour le role du membre
         * @return: le role du membre
         */
        
        public String role() {
            return role;
        }
        
        /**
         * Imbriqué (statiquement) dans OSMRelation.Member
         * Enumeration de Type - Les 3 différents type possibles d'un
         * membre de la relation
         * 
         * @author Raphael Laporte (251209) / Romain Leteurtre (238162)
         */
        
        public enum Type { 
            NODE,WAY,RELATION 
        }
    }
    
    /**
     * Imbriqué statiquement dans OSMRelation - sert de Builder
     * 
     * @author Raphael Laporte (251209) / Romain Leteurtre (238162)
     */
    
    public final static class Builder extends OSMEntity.Builder {
        
        private List<Member> members;
        
        /**
         * Constructeur du builder 
         * @param id :  numero d'identification de l'objet OSM
         */
        
        public Builder(long id) {
            super(id);
            members = new ArrayList<Member>();
        }
        
        /**
         * Ajoute un membre à la liste  
         * @param type :  type du membre
         * @param role : role du membre (String)
         * @param newMember : Entité du membre
         */
        
        public void addMember(Member.Type type, String role, OSMEntity newMember) {
            members.add(new Member(type,role,newMember));
        }
        
        /**
         * Build une instance de OSMRelation 
         * 
         * @throws IllegalStateException : exception levee si le Builder est dans un etat "incomplete == true" 
         * @return Une instance construite de OSMRelation
         */
        
        public OSMRelation build() throws IllegalStateException {
            if (isIncomplete()) throw new IllegalStateException();
            return new OSMRelation(id,members,attributes.build());//Comment faire pour id et attributes?Protected?
        }
    }
}
