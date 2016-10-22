package mainPackage;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

import com.jogamp.opengl.awt.GLCanvas;

/**
 * Class providing the link between the {@link Scene} and the {@link Camera}.
 * @author alan
 * @see Scene
 * @see Camera
 */
public class MouseCameraManager implements MouseListener, MouseMotionListener, MouseWheelListener
{
    private float scrollSensitivity = 2.0f;
    private GLCanvas panel;
    private Camera camera;
    private double width;
    private double height;
    private double xOld;
    private double yOld;
    private double xNew;
    private double yNew;
    private int mode;
    static int DRAG;
    static int ROTATE;
    static int ZOOM;
    
    static {
        MouseCameraManager.DRAG = 1;
        MouseCameraManager.ROTATE = 2;
        MouseCameraManager.ZOOM = 3;
    }
    
    /**
     * Constructor. Create a mouse camera manager.
     * @param pan the GLCanvas to handle.
     */
    public MouseCameraManager(final GLCanvas pan) {
        this.panel = pan;
        this.updateData();
    }
    
    /**
     * Update the camera.
     */
    public void updateData() {
        this.camera = ((Scene)this.panel).getCamera();
        this.width = this.panel.getWidth();
        this.height = this.panel.getHeight();
    }
    
    public void setSensitivity(float sensitivity)
    {
    	this.scrollSensitivity = sensitivity;
    }
    
    @Override
    public void mouseClicked(final MouseEvent e) {
    }
    
    @Override
    public void mouseEntered(final MouseEvent e) {
    }
    
    @Override
    public void mouseExited(final MouseEvent e) {
    }
    
    @Override
    public void mousePressed(final MouseEvent e) {
    	
        this.panel.requestFocus();
        this.xOld = e.getX();
        this.yOld = e.getY();
        final int button = e.getButton();
        if (button == 1) {
            this.mode = MouseCameraManager.ROTATE;
        }
        else if (button == 3) {
            this.mode = MouseCameraManager.DRAG;
        }
        else
        	this.mode = MouseCameraManager.ZOOM;
    }
    
    @Override
    public void mouseReleased(final MouseEvent e) {
    }
    
    @Override
    public void mouseDragged(final MouseEvent e) {
    	this.updateData();
        this.xNew = e.getX();
        this.yNew = e.getY();
        if (this.mode == MouseCameraManager.DRAG) {
            this.camera.translateX((this.xNew - this.xOld) / this.width * 100.0);
            this.camera.translateY(-(this.yNew - this.yOld) / this.height * 100.0);
        }
        else if (this.mode == MouseCameraManager.ROTATE) {
            this.camera.rotateY(-(this.xNew - this.xOld) / this.width * 360.0);
            this.camera.rotateX(-(this.yNew - this.yOld) / this.height * 360.0);
        }
        this.xOld = this.xNew;
        this.yOld = this.yNew;
    }
    
    @Override
    public void mouseMoved(final MouseEvent e) {
    }
    
    @Override
    public void mouseWheelMoved(final MouseWheelEvent e) {
        if (e.getWheelRotation() < 0) {
            this.camera.translateZ(scrollSensitivity);
        }
        else {
            this.camera.translateZ(-scrollSensitivity);
        }
    }
}
