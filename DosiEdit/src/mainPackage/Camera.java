package mainPackage;

import java.nio.FloatBuffer;

import util.Matrix3;
import util.Matrix4;
import util.Point3d;
import util.Quaternion;
import util.Vector3d;

/**
 * Handle the 2D camera.
 * @author alan
 */
public class Camera
{
    private Quaternion quatRot;
    private Vector3d transVect;
    private Vector3d transCDR;
    private Point3d location;
    private Point3d target;
    private Point3d rotCenter;
    private Vector3d upVector;
    
    /**
     * Constructor. Create a camera with the value passed in parameters.
     * @param loc location of the point of view.
     * @param targ the target the camera sees.
     * @param up the up vector.
     * @param rotCenter the center of the world's rotation.
     */
    public Camera(final Point3d loc, final Point3d targ, final Vector3d up, final Point3d rotCenter) {
        this.location = loc;
        this.target = targ;
        this.upVector = up;
        this.rotCenter = rotCenter;
        this.updateQuaternion();
        this.updateTranslations();
    }
    
    /**
     * Update the quaternion with current matrices.
     */
    public void updateQuaternion() {
        final Vector3d y1 = this.upVector;
        final Vector3d z1 = new Vector3d(this.target, this.location);
        z1.normalize();
        final Vector3d x1 = Vector3d.crossProduct(y1, z1);
        x1.normalize();
        final Vector3d y2 = Vector3d.crossProduct(z1, x1);
        this.quatRot = new Quaternion(new Matrix3(x1, y2, z1));
    }
    
    /**
     * Update the translation matrices.
     */
    public void updateTranslations() {
        final Point3d origin = new Point3d(0.0, 0.0, 0.0);
        Vector3d transVectInit = new Vector3d(this.location, this.rotCenter);
        transVectInit = this.quatRot.getRotMatrix().mult(transVectInit);
        this.setTranslation(transVectInit);
        this.setTranslationToRotCenter(new Vector3d(this.rotCenter, origin));
    }
    
    /**
     * Set the the vector translation with a new vector.
     * @param vect the vector to set.
     */
    public void setTranslation(final Vector3d vect) {
        this.transVect = vect;
    }
    
    /**
     * Set the the vector translation with a new vector.
     * @param x the x value of the vector.
     * @param y the y value of the vector.
     * @param z the z value of the vector.
     */
    public void setTranslation(final double x, final double y, final double z) {
        this.transVect = new Vector3d(x, y, z);
    }
    
    /**
     * Set the translation to rot center with doubles coordinates.
     * @param vect the translation vector.
     */
    public void setTranslationToRotCenter(final Vector3d vect) {
        this.transCDR = vect;
    }
    
    /**
     * Set the translation to rot center with doubles coordinates.
     * @param x the x value of the translation vector.
     * @param y the y value of the translation vector.
     * @param z the z value of the translation vector.
     */
    public void setTranslationToRotCenter(final double x, final double y, final double z) {
        this.transCDR = new Vector3d(x, y, z);
    }
    
    /**
     * @return the matrices resulting of the current vector.
     */
    public FloatBuffer getCameraMatrix() {
        final Matrix4 matCDR = Matrix4.getTranslationMatrix(this.transCDR);
        final Matrix4 matRot = this.quatRot.getMatrixInv();
        final Matrix4 matTrans = Matrix4.getTranslationMatrix(this.transVect);
        return matTrans.mult(matRot).mult(matCDR).toFloatBuffer();
    }
    
    /**
     * Transform the current quaternion, rotating it with the specified X angle.
     * @param angle the angle to rotate the view with.
     */
    public void rotateX(final double angle) {
        final Quaternion rot = new Quaternion();
        rot.FromAxis(new Vector3d(1.0, 0.0, 0.0), angle / 180.0 * 3.141592653589793);
        (this.quatRot = this.quatRot.mult(rot)).normalize();
    }
    
    /**
     * Transform the current quaternion, rotating it with the specified Y angle.
     * @param angle the angle to rotate the view with.
     */
    public void rotateY(final double angle) {
        final Quaternion rot = new Quaternion();
        rot.FromAxis(new Vector3d(0.0, 1.0, 0.0), angle * 3.141592653589793 / 180.0);
        (this.quatRot = this.quatRot.mult(rot)).normalize();
    }
    
    /**
     * Transform the current quaternion, translating it with the specified X value.
     * @param tx The value to translate the x value.
     */
    public void translateX(final double tx) {
        this.transVect.add(tx, 0.0, 0.0);
    }
    
    /**
     * Transform the current quaternion, translating it with the specified Y value.
     * @param tx The value to translate the y value.
     */
    public void translateY(final double ty) {
        this.transVect.add(0.0, ty, 0.0);
    }
    
    /**
     * Transform the current quaternion, translating it with the specified Z value.
     * @param tx The value to translate the z value.
     */
    public void translateZ(final double tz) {
        this.transVect.add(0.0, 0.0, tz);
    }
    
    /**
     * Set a new target point to the camera.
     * @param targ the new target point.
     */
    public void setTarget(final Point3d targ) {
        this.target = targ;
    }
    
    /**
     * Set a new rotation center to the camera.
     * @param rotC the new rotation center.
     */
    public void setRotCenter(final Point3d rotC) {
        this.rotCenter = rotC;
    }
    
    /**
     * Set a new location to the camera.
     * @param loc the new location.
     */
    public void setLocation(final Point3d loc) {
        this.location = loc;
    }
    
    /**
     * Reset the matrices and vector of the camera at its first state.
     */
    public void reset() {
        this.quatRot.init();
        this.transVect.init();
        this.transCDR.init();
        this.updateQuaternion();
        this.updateTranslations();
    }
}
