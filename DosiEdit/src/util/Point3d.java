package util;

public class Point3d
{
    public String id;
    private double x;
    private double y;
    private double z;
    
    public Point3d() {
        this.init();
    }
    
    public Point3d(final double a, final double b, final double c) {
        this.init();
        this.x = a;
        this.y = b;
        this.z = c;
    }
    
    public final void init() {
        this.id = "noname";
        this.setX(0.0);
        this.setY(0.0);
        this.setZ(0.0);
    }
    
    public void add(final double a, final double b, final double c) {
        this.setX(this.getX() + a);
        this.setY(this.getY() + b);
        this.setZ(this.getZ() + c);
    }
    
    public void add(final Vector3d vector) {
        this.setX(this.getX() + vector.getX());
        this.setY(this.getY() + vector.getY());
        this.setZ(this.getZ() + vector.getZ());
    }
    
    public void ratio(final double r) {
        this.setX(this.getX() / r);
        this.setY(this.getY() / r);
        this.setZ(this.getZ() / r);
    }
    
    @Override
    public String toString() {
        final String _chaine = "x=" + this.getX() + " y=" + this.getY() + " z=" + this.getZ();
        return _chaine;
    }
    
    public double getX() {
        return this.x;
    }
    
    public void setX(final double x) {
        this.x = x;
    }
    
    public double getY() {
        return this.y;
    }
    
    public void setY(final double y) {
        this.y = y;
    }
    
    public double getZ() {
        return this.z;
    }
    
    public void setZ(final double z) {
        this.z = z;
    }
    
    public Point3d translate(final Vector3d vector) {
        final Point3d temp = new Point3d();
        temp.setX(this.getX() + vector.getX());
        temp.setY(this.getY() + vector.getY());
        temp.setZ(this.getZ() + vector.getZ());
        return temp;
    }
    
    public Point3d translate(final double tx, final double ty, final double tz) {
        final Point3d temp = new Point3d();
        temp.setX(this.getX() + tx);
        temp.setY(this.getY() + ty);
        temp.setZ(this.getZ() + tz);
        return temp;
    }
}
