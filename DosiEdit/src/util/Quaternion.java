package util;

public class Quaternion
{
    private double x;
    private double y;
    private double z;
    private double w;
    
    public Quaternion() {
        this.init();
    }
    
    public Quaternion(final double x, final double y, final double z, final double w) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.w = w;
    }
    
    public Quaternion(final Matrix3 mat) {
        final double t = mat.getTrace() + 1.0;
        if (t > 1.0E-5) {
            final double s = 1.0 / (2.0 * Math.sqrt(t));
            this.x = (mat.getValue(2, 1) - mat.getValue(1, 2)) * s;
            this.y = (mat.getValue(0, 2) - mat.getValue(2, 0)) * s;
            this.z = (mat.getValue(1, 0) - mat.getValue(0, 1)) * s;
            this.w = 1.0 / (4.0 * s);
            this.normalize();
        }
        else if (Math.abs(t) <= 1.0E-5) {
            final double m00 = mat.getValue(0, 0);
            final double m2 = mat.getValue(1, 1);
            final double m3 = mat.getValue(2, 2);
            final double m4 = mat.getValue(0, 1);
            final double m5 = mat.getValue(1, 0);
            final double m6 = mat.getValue(0, 2);
            final double m7 = mat.getValue(2, 0);
            final double m8 = mat.getValue(1, 2);
            final double m9 = mat.getValue(2, 1);
            final double max = Math.max(m00, Math.max(m2, m3));
            if (Math.abs(max - m00) < 1.0E-5) {
                final double s = 2.0 * Math.sqrt(1.0 + m00 - m2 - m3);
                this.x = s / 4.0;
                this.y = (m4 + m5) / s;
                this.z = (m6 + m7) / s;
                this.w = 0.0;
                this.normalize();
            }
            else if (Math.abs(max - m2) < 1.0E-5) {
                final double s = 2.0 * Math.sqrt(1.0 - m00 + m2 - m3);
                this.x = (m4 + m5) / s;
                this.y = s / 4.0;
                this.z = (m8 + m9) / s;
                this.w = 0.0;
                this.normalize();
            }
            else if (Math.abs(max - m3) < 1.0E-5) {
                final double s = 2.0 * Math.sqrt(1.0 - m00 - m2 + m3);
                this.x = (m6 + m7) / s;
                this.y = (m8 + m9) / s;
                this.z = s / 4.0;
                this.w = 0.0;
                this.normalize();
            }
        }
        else {
            this.init();
            System.out.println("erreur d'initialisation du quaternion!");
        }
    }
    
    public void init() {
        this.x = 1.0;
        this.y = 1.0;
        this.z = 1.0;
        this.w = 1.0;
        this.normalize();
    }
    
    public void normalize() {
        final double norm = Math.sqrt(this.x * this.x + this.y * this.y + this.z * this.z + this.w * this.w);
        if (norm > 0.0) {
            this.x /= norm;
            this.y /= norm;
            this.z /= norm;
            this.w /= norm;
        }
    }
    
    public Quaternion getConjugate() {
        return new Quaternion(-this.x, -this.y, -this.z, this.w);
    }
    
    public Quaternion mult(final Quaternion quat) {
        final Quaternion result = new Quaternion(this.w * quat.x + this.x * quat.w + this.y * quat.z - this.z * quat.y, this.w * quat.y + this.y * quat.w + this.z * quat.x - this.x * quat.z, this.w * quat.z + this.z * quat.w + this.x * quat.y - this.y * quat.x, this.w * quat.w - this.x * quat.x - this.y * quat.y - this.z * quat.z);
        result.normalize();
        return result;
    }
    
    public Vector3d mult(final Vector3d vec) {
        final Vector3d vn = new Vector3d(vec);
        vn.normalize();
        final Quaternion vecQuat = new Quaternion();
        vecQuat.x = vn.getX();
        vecQuat.y = vn.getY();
        vecQuat.z = vn.getZ();
        vecQuat.w = 0.0;
        Quaternion resQuat = vecQuat.getConjugate();
        resQuat = this.mult(resQuat);
        return new Vector3d(resQuat.x, resQuat.y, resQuat.z);
    }
    
    public void FromAxis(final Vector3d v, double angle) {
        angle *= 0.5;
        final Vector3d vn = new Vector3d(v);
        vn.normalize();
        final double sinAngle = Math.sin(angle);
        this.x = vn.getX() * sinAngle;
        this.y = vn.getY() * sinAngle;
        this.z = vn.getZ() * sinAngle;
        this.w = Math.cos(angle);
        this.normalize();
    }
    
    public Vector3d getAxisAngle() {
        final Vector3d axis = new Vector3d();
        final double scale = Math.sqrt(this.x * this.x + this.y * this.y + this.z * this.z);
        axis.setX(this.x / scale);
        axis.setY(this.y / scale);
        axis.setZ(this.z / scale);
        axis.setAngle(Math.acos(this.w) * 2.0);
        return axis;
    }
    
    public void FromEuler(final double pitch, final double yaw, final double roll) {
        final double PIOVER180 = 0.017453292519943295;
        final double pdeg = pitch * PIOVER180 / 2.0;
        final double ydeg = yaw * PIOVER180 / 2.0;
        final double rdeg = roll * PIOVER180 / 2.0;
        final double sinp = Math.sin(pdeg);
        final double siny = Math.sin(ydeg);
        final double sinr = Math.sin(rdeg);
        final double cosp = Math.cos(pdeg);
        final double cosy = Math.cos(ydeg);
        final double cosr = Math.cos(rdeg);
        this.x = sinr * cosp * cosy - cosr * sinp * siny;
        this.y = cosr * sinp * cosy + sinr * cosp * siny;
        this.z = cosr * cosp * siny - sinr * sinp * cosy;
        this.w = cosr * cosp * cosy + sinr * sinp * siny;
        this.normalize();
    }
    
    public Matrix4 getMatrix() {
        final double x2 = this.x * this.x;
        final double y2 = this.y * this.y;
        final double z2 = this.z * this.z;
        final double xy = this.x * this.y;
        final double xz = this.x * this.z;
        final double yz = this.y * this.z;
        final double wx = this.w * this.x;
        final double wy = this.w * this.y;
        final double wz = this.w * this.z;
        return new Matrix4(1.0 - 2.0 * (y2 + z2), 2.0 * (xy - wz), 2.0 * (xz + wy), 0.0, 2.0 * (xy + wz), 1.0 - 2.0 * (x2 + z2), 2.0 * (yz - wx), 0.0, 2.0 * (xz - wy), 2.0 * (yz + wx), 1.0 - 2.0 * (x2 + y2), 0.0, 0.0, 0.0, 0.0, 1.0);
    }
    
    public Matrix4 getMatrixInv() {
        final Quaternion temp = new Quaternion(this.x, this.y, this.z, -this.w);
        return temp.getMatrix();
    }
    
    public Matrix3 getRotMatrix() {
        final double x2 = this.x * this.x;
        final double y2 = this.y * this.y;
        final double z2 = this.z * this.z;
        final double xy = this.x * this.y;
        final double xz = this.x * this.z;
        final double yz = this.y * this.z;
        final double wx = this.w * this.x;
        final double wy = this.w * this.y;
        final double wz = this.w * this.z;
        return new Matrix3(1.0 - 2.0 * (y2 + z2), 2.0 * (xy - wz), 2.0 * (xz + wy), 2.0 * (xy + wz), 1.0 - 2.0 * (x2 + z2), 2.0 * (yz - wx), 2.0 * (xz - wy), 2.0 * (yz + wx), 1.0 - 2.0 * (x2 + y2));
    }
    
    public Matrix3 getRotMatrixInv() {
        final Quaternion quatInv = new Quaternion(this.x, this.y, this.z, -this.w);
        return quatInv.getRotMatrix();
    }
    
    @Override
    public String toString() {
        return String.valueOf(this.x) + " / " + this.y + " / " + this.z + " / " + this.w;
    }
}
