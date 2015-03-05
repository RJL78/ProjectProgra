package ch.epfl.imhof.geometry;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/** 
 * Classe représentant un polygone avec son enveloppe et ses trous
 * 
 * @author Raphael Laporte (251209) / Romain Leteurtre (238162)
 *
 */

public final class Polygon {
    
   private final ClosedPolyLine shell; 
   private List<ClosedPolyLine> holes;
    
   /**
    * Constructeur
    * 
    * @param shell: polyligne fermée représentant enveloppe ; holes: liste de
    * polylignes fermées représentant les trous du polygone
    * 
    * @return Instance représentant un polygone
    */
   
   public Polygon (ClosedPolyLine shell, List<ClosedPolyLine> holes){ 
       this.shell = shell;
       this.holes = Collections.unmodifiableList(new ArrayList<ClosedPolyLine> (holes));
   }
   
   /**
    * Constructeur 
    * pour un polygone sans trou
    * 
    * @param shell: polyligne fermée représentant enveloppe ;
    *
    * @return Instance représentant un polygone
    */
   
   public Polygon(ClosedPolyLine shell){
       this.shell = shell; 
       this.holes = Collections.unmodifiableList(new ArrayList<ClosedPolyLine>());
   }
   
   /**
    * Retourne l'enveloppe du polygone
    * 
    * 
    * @return enveloppe du polygone
    */
   
   public ClosedPolyLine shell(){
       return shell;
   }
   
   /**
    * Retourne la liste des trous du polygone
    * 
    * 
    * @return liste des trous du polygone
    */
   
   public List<ClosedPolyLine> holes(){
       return holes;
   }
   
   

}
