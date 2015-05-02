package ch.epfl.imhof;

public class Vector3 {
    
    private final double x; 
    private final double y; 
    private final double z;

    public Vector3(double x,double y,double z) {
        this.x = x;
        this.y = y;
        this.z = z;
        // TODO Auto-generated constructor stub
    }
    
    public double norm(){
        return Math.sqrt(x*x+y*y+z*z);
    }
    
    public Vector3 normalized(){
        double n = norm();
        return  new Vector3(x/n,y/n,z/n);
    }
    
    public double scalarProduct(Vector3 that){
        return this.x*that.x + this.y*that.y +this.z*that.z;
    }

}
