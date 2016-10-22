package util;

import java.io.Serializable;

public class Vector3d implements Serializable
{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1300969774589382222L;
	private double x;
    private double y;
    private double z;
    private double angle;
    
    public Vector3d() {
        this.init();
    }
    
    public Vector3d(final double x, final double y, final double z) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.angle = 0.0;
    }
    
    public Vector3d(final Vector3d vec) {
        this.x = vec.x;
        this.y = vec.y;
        this.z = vec.z;
        this.angle = vec.angle;
    }
    
    public Vector3d(final Point3d a, final Point3d b) {
        this.x = b.getX() - a.getX();
        this.y = b.getY() - a.getY();
        this.z = b.getZ() - a.getZ();
        this.angle = 0.0;
    }
    
    public Vector3d(final Point3d a) {
        this.x = a.getX();
        this.y = a.getY();
        this.z = a.getZ();
        this.angle = 0.0;
    }
    
    public void init() {
        this.setX(0.0);
        this.setY(0.0);
        this.setZ(0.0);
        this.setAngle(0.0);
    }
    
    public void normalize() {
        final double norm = Math.sqrt(this.getX() * this.getX() + this.getY() * this.getY() + this.getZ() * this.getZ());
        if (norm > 0.0) {
            this.setX(this.getX() / norm);
            this.setY(this.getY() / norm);
            this.setZ(this.getZ() / norm);
        }
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
    
    public double getAngle() {
        return this.angle;
    }
    
    public void setAngle(final double angle) {
        this.angle = angle;
    }
    
    public double getComponent(final int i) {
        if (i < 0 || i >= 3) {
            return 0.0;
        }
        if (i == 0) {
            return this.getX();
        }
        if (i == 1) {
            return this.getY();
        }
        return this.getZ();
    }
    
    public void setComponent(final int i, final double val) {
        if (i == 0) {
            this.setX(val);
        }
        else if (i == 1) {
            this.setY(val);
        }
        else if (i == 2) {
            this.setZ(val);
        }
    }
    
    public void add(final Vector3d vect) {
        this.setX(this.getX() + vect.getX());
        this.setY(this.getY() + vect.getY());
        this.setZ(this.getZ() + vect.getZ());
        this.setAngle(this.getAngle() + vect.getAngle());
    }
    
    public void add(final double x, final double y, final double z) {
        this.setX(this.getX() + x);
        this.setY(this.getY() + y);
        this.setZ(this.getZ() + z);
    }
    
    public void mult(final double value) {
        this.x *= value;
        this.y *= value;
        this.z *= value;
    }
    
    public static double scalarProduct(final Vector3d a, final Vector3d b) {
        return a.x * b.x + a.y * b.y + a.z * b.z;
    }
    
    public static Vector3d crossProduct(final Vector3d a, final Vector3d b) {
        final Vector3d temp = new Vector3d();
        temp.setX(a.getY() * b.getZ() - a.getZ() * b.getY());
        temp.setY(a.getZ() * b.getX() - a.getX() * b.getZ());
        temp.setZ(a.getX() * b.getY() - a.getY() * b.getX());
        temp.setAngle(0.0);
        return temp;
    }
    
    public static Vector3d getCombination(final Vector3d vectA, final double valA, final Vector3d vectB, final double valB) {
        final double compX = vectA.x * valA + vectB.x * valB;
        final double compY = vectA.y * valA + vectB.y * valB;
        final double compZ = vectA.z * valA + vectB.z * valB;
        return new Vector3d(compX, compY, compZ);
    }
    
    @Override
    public String toString() {
        return String.valueOf(this.getX()) + ", " + this.getY() + ", " + this.getZ() + ", " + this.getAngle();
    }
    
    public Vector3d copy() {
        return new Vector3d(this.x, this.y, this.z);
    }
}
