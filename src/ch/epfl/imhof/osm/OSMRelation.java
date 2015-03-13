package ch.epfl.imhof.osm;

import java.util.ArrayList;
import java.util.List;

import ch.epfl.imhof.Attributes;

public final class OSMRelation extends OSMEntity {

    private List<Member> members;
    
    public OSMRelation(long id, List<Member> members,Attributes attributes) {
        super(id,attributes);
        this.members=members;
    }
    
    public List<Member> members() {
        List<Member> output = new ArrayList <Member> ();
        for (Member aMember: members){
            output.add(aMember);
        }
        return output;
    }
    
    public final static class Member {
        
        private String role;
        private OSMEntity member;
        private Type type;
        
        public Member(Type type, String role, OSMEntity member) {
            this.member=member;
            this.type=type;
            this.role=role;
        }
        
        public Type type() {
            return type;
        }
        
        public OSMEntity member() {
            return member;
        }
        
        public String role() {
            return role;
        }
        
        public enum Type { 
            NODE,WAY,RELATION 
        }
    }
    
    public final static class Builder extends OSMEntity.Builder {
        
        private List<Member> members;
        
        public Builder(long id) {
            super(id);
            members = new ArrayList<Member>();
        }
        
        public void addMember(Member.Type type, String role, OSMEntity newMember) {
            members.add(new Member(type,role,newMember));
        }
        
        public OSMRelation build() throws IllegalStateException {
            if (isIncomplete()) throw new IllegalStateException();
            return new OSMRelation(id,members,attributes.build());//Comment faire pour id et attributes?Protected?
        }
    }
}
