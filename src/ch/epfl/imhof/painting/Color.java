package ch.epfl.imhof.painting;


public class Color {
    
    public static final Color RED = new Color(1,0,0);
    public static final Color GREEN = new Color(0,1,0);
    public static final Color BLUE = new Color(0,0,1);
    public static final Color BLACK = new Color(1,1,1);
    public static final Color WHITE = new Color (0,0,0);
    
    private final double red, green, blue;

    private Color(double red, double green, double blue) {
        if (red>1 || red<0 || green>1 || green<0 || blue>1 || blue<0) throw new IllegalArgumentException();
        this.red = red;
        this.green = green; 
        this.blue = blue;
    }
    
    public static Color gray(double scale){
        return new Color(scale,scale,scale); 
    }
    
    public static Color rgb(double red, double green, double blue){
        return new Color (red,green,blue);    
    }
    
    public java.awt.Color toAPIColor(){
        return new java.awt.Color((float)red,(float)green,(float)blue);
    }
    
    public static Color rgb(int bits){
        return null; 
        // TODO 
    }
    
    public Color multiply(Color that){
        return new Color(red*that.red,green*that.green,blue*that.blue);
    }
    
    
    public double getRed(){
        return red;
    }
    
    public double getBlue(){
        return blue;
    }
    
    public double getGreen(){
        return green;
    }

}
