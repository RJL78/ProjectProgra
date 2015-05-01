package ch.epfl.imhof.geometry;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/** 
 * Classe immuable représentant un polygone avec son enveloppe et ses trous
 * 
 * @author Raphael Laporte (251209) / Romain Leteurtre (238162)
 */
public final class Polygon {

    private final ClosedPolyLine shell; 
    private final List<ClosedPolyLine> holes;

    /**
     * Constructeur de Polygon
     * 
     * @param shell: Polyligne fermée représentant enveloppe
     * @param holes: Liste de polylignes fermées représentant les trous du polygone 
     */
    public Polygon (ClosedPolyLine shell, List<ClosedPolyLine> holes){ 
        this.shell = shell;
        this.holes = new ArrayList<> (holes);
    }

    /**
     * Constructeur de Polygon pour un polygone sans trous
     * 
     * @param shell: Polyligne fermée représentant l'enveloppe ;
     *
     * @return Instance représentant un polygone
     */
    public Polygon(ClosedPolyLine shell){
        this (shell, new ArrayList<ClosedPolyLine>());
    }

    /**
     * Retourne la ClosedPolyLine représentant l'enveloppe du polygone
     * 
     * @return enveloppe du polygone
     */
    public ClosedPolyLine shell(){
        return shell;
    }

    /**
     * Retourne la liste des trous du polygone
     * 
     * @return Liste des trous du polygone; liste vide s'il n'en a pas
     */
    public List<ClosedPolyLine> holes(){
        return Collections.unmodifiableList(holes);
    }
}
