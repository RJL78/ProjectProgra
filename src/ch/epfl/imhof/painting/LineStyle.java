package ch.epfl.imhof.painting;

public class LineStyle {
    
    private final LINE_END lineEnd;
    private final JOINT joint; 
    private final float thickness; 
    private final float[] lineSequence;
    private final Color color;
    
    public enum LINE_END{
        BUTT,ROUND,SQUARE;
    }
    
    public enum JOINT{
        BEVEL, MITER, ROUND;
    }

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
        
     public LineStyle (Color color, float thickness){
         this (color, thickness, LINE_END.BUTT, JOINT.MITER, new float [0]);
     }
     
     public Color getColor(){
         return color;
     }
     
     public LINE_END  getLineEnd(){
         return lineEnd;
     }
     
     public JOINT getJoint(){
         return joint;
     }
     
     public float getThickness(){
         return thickness; 
     }
     
     // a voir si plus efficace possible ci-dessous - je crois pas
     public float[] lineSequence(){
         float [] copy = new float[lineSequence.length]; 
         for (int i=0; i <lineSequence.length; i++){
             copy[i] = lineSequence[i];
         }
         return copy;
     }
     
     public LineStyle withWidth(float newThickness){
         return  new LineStyle (color, newThickness, lineEnd, joint, lineSequence);
     }
     
     public LineStyle withColor(Color newColor){
         return  new LineStyle (newColor, thickness, lineEnd, joint, lineSequence);
     }
     
     public LineStyle withLineEnd(LINE_END newLineEnd){
         return  new LineStyle (color, thickness, newLineEnd, joint, lineSequence);
     }
     
     public LineStyle withJoint(JOINT newJoint){
         return  new LineStyle (color, thickness, lineEnd, newJoint, lineSequence);
     }
     
     public LineStyle withLineSequence(float [] newLineSequence){
         return  new LineStyle (color, thickness, lineEnd, joint, newLineSequence);
     }
     
     
        
        


}
