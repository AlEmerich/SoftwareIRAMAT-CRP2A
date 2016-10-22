package interfaceObject;

import java.awt.Component;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.geom.AffineTransform;
import java.awt.geom.NoninvertibleTransformException;
import java.awt.geom.Point2D;

/**
 * Listener that can be attached to a Component to implement Zoom and Pan functionality.
 *
 * @author Sorin Postelnicu
 * @since Jul 14, 2009
 */
public class ZoomAndPanListener implements MouseListener, MouseMotionListener, MouseWheelListener
{
    public static final int DEFAULT_MIN_ZOOM_LEVEL = -20;
    public static final int DEFAULT_MAX_ZOOM_LEVEL = 10;
    public static final double DEFAULT_ZOOM_MULTIPLICATION_FACTOR = 1.2;
    private Component targetComponent;
    private int zoomLevel;
    private int minZoomLevel;
    private int maxZoomLevel;
    private double zoomMultiplicationFactor;
    private Point dragStartScreen;
    private Point dragEndScreen;
    private AffineTransform coordTransform;
    private boolean isRightButton;
    
    public ZoomAndPanListener(final Component targetComponent) {
        this.zoomLevel = 0;
        this.minZoomLevel = -20;
        this.maxZoomLevel = 10;
        this.zoomMultiplicationFactor = 1.2;
        this.coordTransform = new AffineTransform();
        this.isRightButton = false;
        this.targetComponent = targetComponent;
    }
    
    public ZoomAndPanListener(final Component targetComponent, final int minZoomLevel, final int maxZoomLevel, final double zoomMultiplicationFactor) {
        this.zoomLevel = 0;
        this.minZoomLevel = -20;
        this.maxZoomLevel = 10;
        this.zoomMultiplicationFactor = 1.2;
        this.coordTransform = new AffineTransform();
        this.isRightButton = false;
        this.targetComponent = targetComponent;
        this.minZoomLevel = minZoomLevel;
        this.maxZoomLevel = maxZoomLevel;
        this.zoomMultiplicationFactor = zoomMultiplicationFactor;
    }
    
    @Override
    public void mouseClicked(final MouseEvent e) {
    }
    
    @Override
    public void mousePressed(final MouseEvent e) {
        if (e.getButton() == 3) {
            this.dragStartScreen = e.getPoint();
            this.dragEndScreen = null;
            this.isRightButton = true;
        }
        if (e.getButton() == 2) {
            this.coordTransform = new AffineTransform();
        }
    }
    
    @Override
    public void mouseReleased(final MouseEvent e) {
        if (e.getButton() == 3) {
            this.isRightButton = false;
        }
    }
    
    @Override
    public void mouseEntered(final MouseEvent e) {
    }
    
    @Override
    public void mouseExited(final MouseEvent e) {
    }
    
    @Override
    public void mouseMoved(final MouseEvent e) {
    }
    
    @Override
    public void mouseDragged(final MouseEvent e) {
        if (this.isRightButton) {
            this.moveCamera(e);
        }
    }
    
    @Override
    public void mouseWheelMoved(final MouseWheelEvent e) {
        this.zoomCamera(e);
    }
    
    private void moveCamera(final MouseEvent e) {
        try {
            this.dragEndScreen = e.getPoint();
            final Point2D.Float dragStart = this.transformPoint(this.dragStartScreen);
            final Point2D.Float dragEnd = this.transformPoint(this.dragEndScreen);
            final double dx = dragEnd.getX() - dragStart.getX();
            final double dy = dragEnd.getY() - dragStart.getY();
            this.coordTransform.translate(dx, dy);
            this.dragStartScreen = this.dragEndScreen;
            this.dragEndScreen = null;
            this.targetComponent.repaint();
        }
        catch (NoninvertibleTransformException ex) {
            ex.printStackTrace();
        }
    }
    
    private void zoomCamera(final MouseWheelEvent e) {
        try {
            final int wheelRotation = e.getWheelRotation();
            final Point p = e.getPoint();
            if (wheelRotation > 0) {
                if (this.zoomLevel < this.maxZoomLevel) {
                    ++this.zoomLevel;
                    final Point2D p2 = this.transformPoint(p);
                    this.coordTransform.scale(1.0 / this.zoomMultiplicationFactor, 1.0 / this.zoomMultiplicationFactor);
                    final Point2D p3 = this.transformPoint(p);
                    this.coordTransform.translate(p3.getX() - p2.getX(), p3.getY() - p2.getY());
                    this.targetComponent.repaint();
                }
            }
            else if (this.zoomLevel > this.minZoomLevel) {
                --this.zoomLevel;
                final Point2D p2 = this.transformPoint(p);
                this.coordTransform.scale(this.zoomMultiplicationFactor, this.zoomMultiplicationFactor);
                final Point2D p3 = this.transformPoint(p);
                this.coordTransform.translate(p3.getX() - p2.getX(), p3.getY() - p2.getY());
                this.targetComponent.repaint();
            }
        }
        catch (NoninvertibleTransformException ex) {
            ex.printStackTrace();
        }
    }
    
    public Point2D.Float transformPoint(final Point p1) throws NoninvertibleTransformException {
        final AffineTransform inverse = this.coordTransform.createInverse();
        final Point2D.Float p2 = new Point2D.Float();
        inverse.transform(p1, p2);
        return p2;
    }
    
    public int getZoomLevel() {
        return this.zoomLevel;
    }
    
    public void setZoomLevel(final int zoomLevel) {
        this.zoomLevel = zoomLevel;
    }
    
    public AffineTransform getCoordTransform() {
        return this.coordTransform;
    }
    
    public void setCoordTransform(final AffineTransform coordTransform) {
        this.coordTransform = coordTransform;
    }
}
