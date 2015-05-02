package ch.epfl.imhof.painting;

/** 
 * Classe immuable regroupant tous les paramètres de style utiles au dessin d'une ligne
 * 
 * @author Raphael Laporte (251209) / Romain Leteurtre (238162)
 */
public class LineStyle {

    private final LINE_END lineEnd;
    private final JOINT joint; 
    private final float thickness; 
    private final float[] lineSequence;
    private final Color color;
    private final static float[] defaultLineSequence = {};

    /**
     * Enumération des terminaisons des lignes
     */
    public enum LINE_END{
        BUTT,ROUND,SQUARE;
    }

    /**
     * Enumération des jointures des lignes
     */
    public enum JOINT{
        BEVEL, MITER, ROUND;
    }

    /**
     * Constructeur d'un style de ligne
     * 
     * @param color : couleur de la ligne
     * @param thickness : épaisseur de la ligne
     * @param lineEnd : terminaision de la ligne
     * @param joint : jointure de la ligne
     * @param lineSequence : séquence d'alternance des segments
     */
    public LineStyle(Color color, float thickness, LINE_END lineEnd, JOINT joint, float[] lineSequence) {
        if (thickness<0)throw new IllegalArgumentException();
        this.color = color; 
        this.thickness = thickness; 
        this.lineEnd = lineEnd;
        this.joint = joint; 
        this.lineSequence = new float[lineSequence.length]; 
        for (int i=0; i <lineSequence.length; i++){
            if (lineSequence[i] <= 0 ) throw new IllegalArgumentException();
            this.lineSequence[i] = lineSequence[i];
        }
    }

    /**
     * Constructeur d'un style de ligne mettant la terminaison, les jointures, et la 
     * séquence d'alternance des segments à un valeur par défaut
     * 
     * @param color : couleur de la ligne
     * @param thickness : épaisseur de la ligne
     */
    public LineStyle (Color color, float thickness){
        this (color, thickness, LINE_END.BUTT, JOINT.MITER, defaultLineSequence);
    }


    /** 
     * Getter de la couleur 
     * 
     * @return la couleur
     */
    public Color getColor(){
        return color;
    }
    
    /** 
     * Getter de la terminaison de la ligne 
     * 
     * @return la terminaison de la ligne
     */
    public LINE_END  getLineEnd(){
        return lineEnd;
    }

    /** 
     * Getter de la jointure de la ligne
     * 
     * @return la jointure de la ligne
     */
    public JOINT getJoint(){
        return joint;
    }

    /** 
     * Getter de l'épaisseur 
     * 
     * @return l'épaisseur
     */
    public float getThickness(){
        return thickness; 
    }

    /** 
     * Getter de la séquence d'alternance des segments 
     * 
     * @return la séquence d'alternance des segments
     */
    public float[] lineSequence(){
        float [] copy = new float[lineSequence.length]; 
        for (int i=0; i <lineSequence.length; i++){
            copy[i] = lineSequence[i];
        }
        return copy;
    }

    /**
     * Retourne un style de ligne dérivé, avec une nouvelle épaisseur
     * 
     * @param newThickness : nouvelle épaisseur 
     * 
     * @return style de ligne avec nouvelle épaisseur
     */
    public LineStyle withWidth(float newThickness){
        return  new LineStyle (color, newThickness, lineEnd, joint, lineSequence);
    }
    
    /**
     * Retourne un style de ligne dérivé, avec une nouvelle couleur
     * 
     * @param newColor : nouvelle couleur 
     * 
     * @return style de ligne avec nouvelle couleur
     */
    public LineStyle withColor(Color newColor){
        return  new LineStyle (newColor, thickness, lineEnd, joint, lineSequence);
    }

    /**
     * Retourne un style de ligne dérivé, avec une nouvelle terminaison
     * 
     * @param newLineEnd : nouvelle terminaison 
     * 
     * @return style de ligne avec nouvelle terminaison
     */
    public LineStyle withLineEnd(LINE_END newLineEnd){
        return  new LineStyle (color, thickness, newLineEnd, joint, lineSequence);
    }

    /**
     * Retourne un style de ligne dérivé, avec une nouvelle jointure
     * 
     * @param newJoint : nouvelle jointure 
     * 
     * @return style de ligne avec nouvelle jointure
     */
    public LineStyle withJoint(JOINT newJoint){
        return  new LineStyle (color, thickness, lineEnd, newJoint, lineSequence);
    }

    /**
     * Retourne un style de ligne dérivé, avec une nouvelle séquence d'alternance des segments
     * 
     * @param newLineSequence : nouvelle séquence d'alternance des segments 
     * 
     * @return style de ligne avec nouvelle séquence d'alternance des segments
     */
    public LineStyle withLineSequence(float [] newLineSequence){
        return  new LineStyle (color, thickness, lineEnd, joint, newLineSequence);
    }






}
