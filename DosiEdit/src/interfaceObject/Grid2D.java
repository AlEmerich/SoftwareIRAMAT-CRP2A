package interfaceObject;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.NoninvertibleTransformException;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import mainPackage.ExternalVoxelizedWorld;
import mainPackage.Material;
import mainPackage.Scene;
import mainPackage.VoxelObject;
import mainPackage.VoxelizedObject;

/**
 * This class provides a non-editable 2D view to manipulate the voxelized world.
 * @author alan
 * @see JPanel
 * @see ChangeListener
 * @see MouseListener
 * @see MouseMotionListener
 * @see ActionListener
 */
public class Grid2D extends JPanel implements ChangeListener, MouseListener, MouseMotionListener, ActionListener
{
    private static final long serialVersionUID = 6058823273740955967L;
    
    /**
     * Backgrounds to easily recognize the XY Layer.
     */
    public Rectangle2D.Float redBackground;
    
    /**
     * Backgrounds to easily recognize the YZ Layer.
     */
    public Rectangle2D.Float greenBackground;
    
    /**
     * 	Backgrounds to easily recognize the XZ Layer.
     */
    public Rectangle2D.Float blueBackground;
    
    /**
     * The font of texts.
     */
    private Font font;
    
    /**
     * Rectangle for rectangle selection.
     */
    private Rectangle2D.Float cursorRectSelection;
    
    /**
     * The 2D camera.
     */
    private ZoomAndPanListener zoomManager;
    
    /**
     * Viewer 2D and 3D.
     */
    private ViewGrid2D gridlistener;
    private Scene scene;
    private Color probeColor;
    
    /**
     * Brute data.
     */
    private VoxelizedObject Voxy;
    private Layer mode;
    private int lastXY;
    private int lastXZ;
    private int lastYZ;
    private int activeLayer;
    private RadMode showMode;
    private int editorMode;
    private Material currentMaterial;
    private boolean setTobeUpdate;
    private boolean leftButtonPushed;
    
    /**
     * Constructor. Save the world into field, call the constructor of the camera, and do the linking listener.
     * @param vox the voxelized world.
     * @see ZoomAndPanListener
     * @see VoxelizedObject
     */
    public Grid2D(final VoxelizedObject vox) {
        this.redBackground = new Rectangle2D.Float();
        this.greenBackground = new Rectangle2D.Float();
        this.blueBackground = new Rectangle2D.Float();
        this.font = null;
        this.cursorRectSelection = null;
        this.mode = Layer.XY;
        this.lastXY = 0;
        this.lastXZ = 0;
        this.lastYZ = 0;
        this.activeLayer = 0;
        this.showMode = RadMode.Mat;
        this.editorMode = 0;
        this.currentMaterial = null;
        this.setTobeUpdate = true;
        this.leftButtonPushed = false;
        this.gridlistener = null;
        this.probeColor = new Color(255, 0, 0);
        this.Voxy = vox;
        this.zoomManager = new ZoomAndPanListener(this, 1, vox.getNbVoxelX() * vox.getNbVoxelY() * 10, 1.2);
        this.font = new Font("Serif", 0, 20);
        this.addMouseListener(this.zoomManager);
        this.addMouseMotionListener(this.zoomManager);
        this.addMouseWheelListener(this.zoomManager);
        this.addMouseListener(this);
        this.addMouseMotionListener(this);
        this.updateBackground();
       /* this.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(final ComponentEvent e) {
                Grid2D.this.revalidate();
                Grid2D.this.repaint();
            }
        });*/
        this.validate();
    }
    
    /**
     * Change the background when the vie is changed.
     */
    private void updateBackground() {
    	if(VoxelObject.KEEP_RATIO_ON_GRID)
    	{
    		this.redBackground.setRect(-this.Voxy.getVoxelDimensionX() * VoxelObject.FACTOR_DISPLAY_GRID, -this.Voxy.getVoxelDimensionY() * VoxelObject.FACTOR_DISPLAY_GRID, (this.Voxy.getNbVoxelX() + 2) * this.Voxy.getVoxelDimensionX() * VoxelObject.FACTOR_DISPLAY_GRID, (this.Voxy.getNbVoxelY() + 2) * this.Voxy.getVoxelDimensionY() * VoxelObject.FACTOR_DISPLAY_GRID);
            this.blueBackground.setRect(-this.Voxy.getVoxelDimensionX() * VoxelObject.FACTOR_DISPLAY_GRID, -this.Voxy.getVoxelDimensionZ() * VoxelObject.FACTOR_DISPLAY_GRID, (this.Voxy.getNbVoxelX() + 2) * this.Voxy.getVoxelDimensionX() * VoxelObject.FACTOR_DISPLAY_GRID, (this.Voxy.getNbVoxelZ() + 2) * this.Voxy.getVoxelDimensionZ() * VoxelObject.FACTOR_DISPLAY_GRID);
            this.greenBackground.setRect(-this.Voxy.getVoxelDimensionY() * VoxelObject.FACTOR_DISPLAY_GRID, -this.Voxy.getVoxelDimensionZ() * VoxelObject.FACTOR_DISPLAY_GRID, (this.Voxy.getNbVoxelY() + 2) * this.Voxy.getVoxelDimensionY() * VoxelObject.FACTOR_DISPLAY_GRID, (this.Voxy.getNbVoxelZ() + 2) * this.Voxy.getVoxelDimensionZ() * VoxelObject.FACTOR_DISPLAY_GRID);
    	}
    	else
    	{
    		this.redBackground.setRect(-VoxelObject.FACTOR_DISPLAY_GRID, -VoxelObject.FACTOR_DISPLAY_GRID, (this.Voxy.getNbVoxelX() + 2) * VoxelObject.FACTOR_DISPLAY_GRID, (this.Voxy.getNbVoxelY() + 2) * VoxelObject.FACTOR_DISPLAY_GRID);
            this.blueBackground.setRect(-VoxelObject.FACTOR_DISPLAY_GRID, -VoxelObject.FACTOR_DISPLAY_GRID, (this.Voxy.getNbVoxelX() + 2) *  VoxelObject.FACTOR_DISPLAY_GRID, (this.Voxy.getNbVoxelZ() + 2) * VoxelObject.FACTOR_DISPLAY_GRID);
            this.greenBackground.setRect(-VoxelObject.FACTOR_DISPLAY_GRID, -VoxelObject.FACTOR_DISPLAY_GRID, (this.Voxy.getNbVoxelY() + 2) *  VoxelObject.FACTOR_DISPLAY_GRID, (this.Voxy.getNbVoxelZ() + 2) * VoxelObject.FACTOR_DISPLAY_GRID);
    	}
    }
   
    /**
     * @return the current layer displayed.
     */
    public int getActiveLayer() {
        return this.activeLayer;
    }
    
    /**
     * paint the UI components, like {@link Rectangle2D}.
     */
    public void paintComponent(final Graphics g) {
        super.paintComponent(g);
        if (this.setTobeUpdate) {
            this.Voxy.updateAccordingView(this.mode, this.activeLayer);
            this.setTobeUpdate = false;
        }
        if (!this.Voxy.isUpdateForScene) {
            this.updateBackground();
            this.Voxy.updateAccordingView(this.mode, this.activeLayer);
            this.gridlistener.updateMode(this.mode, this.activeLayer);
            this.scene.setToUpdate();
        }
        this.setBackground(Color.white);
        g.setFont(this.font);
        this.drawGrid(g);
        if (this.cursorRectSelection != null && this.currentMaterial != null) {
            final Color c = new Color(this.currentMaterial.getColor().getRed(), this.currentMaterial.getColor().getGreen(), this.currentMaterial.getColor().getBlue(), 120);
            ((Graphics2D)g).setColor(c);
            ((Graphics2D)g).fill(this.cursorRectSelection);
        }
        if (this.gridlistener != null) {
            this.gridlistener.updateMaterial();
        }
    }
    
    /**
     * Draw the black strips of the grid, and all voxels.
     * @param g the current context graphics.
     */
    private void drawGrid(final Graphics g) {
        final Graphics2D g2d = (Graphics2D)g;
        final RenderingHints rh = new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        rh.put(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g2d.setRenderingHints(rh);
        g2d.setTransform(this.zoomManager.getCoordTransform());
        switch (this.mode) {
            case XY: {
                g2d.setColor(Color.red);
                g2d.fill(this.redBackground);
                g2d.setColor(Color.black);
                for (int y = 0; y < this.Voxy.getNbVoxelY(); ++y) {
                    for (int x = 0; x < this.Voxy.getNbVoxelX(); ++x) {
                        final VoxelObject vox = this.Voxy.getVoxelToIndices(x, y, this.activeLayer);
                        if (y == 0) {
                            g.drawString(new StringBuilder(String.valueOf(x)).toString(), (int)(x * vox.width + vox.width / 3.0f), (int)(y * vox.height - vox.height / 3.0f));
                        }
                        if (vox != null && vox.isVisible(this.getVisibleRect(), this.zoomManager.getCoordTransform())) {
                            vox.draw(g2d, this.showMode);
                        }
                        if (this.Voxy instanceof ExternalVoxelizedWorld && x == ((ExternalVoxelizedWorld)this.Voxy).getProbeLocationX() && y == ((ExternalVoxelizedWorld)this.Voxy).getProbeLocationY()) {
                            g2d.setColor(this.probeColor);
                            g2d.setStroke(new BasicStroke(4.0f));
                            g2d.draw(vox);
                            g2d.setColor(Color.black);
                        }
                        else {
                            g2d.setColor(Color.black);
                            g2d.setStroke(new BasicStroke(2.0f));
                            g2d.draw(vox);
                        }
                        if (x == 0) {
                            g.drawString(new StringBuilder(String.valueOf(y)).toString(), (int)(x * vox.width - vox.width), (int)(y * vox.height + vox.height / 2.0f));
                        }
                    }
                }
                break;
            }
            case XZ: {
                g2d.setColor(Color.blue);
                g2d.fill(this.blueBackground);
                g2d.setColor(Color.black);
                for (int z = 0; z < this.Voxy.getNbVoxelZ(); ++z) {
                    for (int x = 0; x < this.Voxy.getNbVoxelX(); ++x) {
                        final VoxelObject vox = this.Voxy.getVoxelToIndices(x, this.activeLayer, z);
                        if (x == 0) {
                            g.drawString(new StringBuilder(String.valueOf(z)).toString(), (int)(x * vox.width - vox.width), (int)(z * vox.height + vox.height / 2.0f));
                        }
                        if (z == 0) {
                            g.drawString(new StringBuilder(String.valueOf(x)).toString(), (int)(x * vox.width + vox.width / 3.0f), (int)(z * vox.height - vox.height / 3.0f));
                        }
                        if (vox != null && vox.isVisible(this.getBounds(), this.zoomManager.getCoordTransform())) {
                            vox.draw(g2d, this.showMode);
                        }
                        if (this.Voxy instanceof ExternalVoxelizedWorld && x == ((ExternalVoxelizedWorld)this.Voxy).getProbeLocationX() && this.activeLayer == ((ExternalVoxelizedWorld)this.Voxy).getProbeLocationY()) {
                            g2d.setColor(this.probeColor);
                            g2d.setStroke(new BasicStroke(4.0f));
                            g2d.draw(vox);
                            g2d.setColor(Color.black);
                        }
                        else {
                            g2d.setColor(Color.black);
                            g2d.setStroke(new BasicStroke(2.0f));
                            g2d.draw(vox);
                        }
                    }
                }
                break;
            }
            case YZ: {
                g2d.setColor(Color.green);
                g2d.fill(this.greenBackground);
                g2d.setColor(Color.black);
                for (int y = 0; y < this.Voxy.getNbVoxelY(); ++y) {
                    for (int z2 = 0; z2 < this.Voxy.getNbVoxelZ(); ++z2) {
                        final VoxelObject vox = this.Voxy.getVoxelToIndices(this.activeLayer, y, z2);
                        if (y == 0) {
                            g.drawString(new StringBuilder(String.valueOf(z2)).toString(), (int)(z2 * vox.width + vox.width / 3.0f), (int)(y * vox.height - vox.height / 3.0f));
                        }
                        if (vox != null && vox.isVisible(this.getBounds(), this.zoomManager.getCoordTransform())) {
                            vox.draw(g2d, this.showMode);
                        }
                        if (this.Voxy instanceof ExternalVoxelizedWorld && y == ((ExternalVoxelizedWorld)this.Voxy).getProbeLocationY() && this.activeLayer == ((ExternalVoxelizedWorld)this.Voxy).getProbeLocationX()) {
                            g2d.setColor(this.probeColor);
                            g2d.setStroke(new BasicStroke(4.0f));
                            g2d.draw(vox);
                            g2d.setColor(Color.black);
                        }
                        else {
                            g2d.setColor(Color.black);
                            g2d.setStroke(new BasicStroke(2.0f));
                            g2d.draw(vox);
                        }
                        if (z2 == 0) {
                            g.drawString(new StringBuilder(String.valueOf(y)).toString(), (int)(z2 * vox.width - vox.width), (int)(y * vox.height + vox.height / 2.0f));
                        }
                    }
                }
                break;
            }
        }
    }
    
    /**
     * Change the layer view mode (XY, XZ or YZ) and repaint.
     * @param mode the view mode.
     */
    public void setLayerViewMode(final Layer mode) {
        if (this.mode != mode) {
            this.mode = mode;
            this.setTobeUpdate = true;
        }
        this.repaint();
    }
    
    /**
     * @return the current view mode.
     */
    public Layer getLayerViewMode() {
        return this.mode;
    }
    
    /**
     * Change the show mode (Material, Uranium, Thorium or Potassium values) and repaint.
     * @param mode the view mode to set.
     */
    public void setShowMode(final RadMode mode) {
        if (this.showMode != mode) {
            this.showMode = mode;
        }
        this.repaint();
    }
    
    /**
     * Change the current material.
     * @param m the new current material.
     */
    public void setCurrentMaterial(final Material m) {
        this.currentMaterial = m;
    }
    
    /**
     * Triggered when the state of the object has changed.
     */
    @Override
    public void stateChanged(final ChangeEvent e) {
        this.activeLayer = ((JSlider)e.getSource()).getValue();
        switch (this.mode) {
            case XY: {
                this.lastXY = this.activeLayer;
                if (this.scene != null) {
                    this.scene.setXYLayer(this.activeLayer);
                    break;
                }
                break;
            }
            case XZ: {
                this.lastXZ = this.activeLayer;
                if (this.scene != null) {
                    this.scene.setXZLayer(this.activeLayer);
                    break;
                }
                break;
            }
            case YZ: {
                this.lastYZ = this.activeLayer;
                if (this.scene != null) {
                    this.scene.setYZLayer(this.activeLayer);
                    break;
                }
                break;
            }
        }
        this.Voxy.updateAccordingView(this.mode, this.activeLayer);
        ((EditorPane)this.getParent().getParent().getParent()).updateYourself();
        this.repaint();
    }
    
    
    @Override
    public void mouseClicked(final MouseEvent e) {
    }
    
    @Override
    public void mouseEntered(final MouseEvent arg0) {
    }
    
    @Override
    public void mouseExited(final MouseEvent arg0) {
    }
    
    /**
     * Triggered when a mouse button is pressed within the grid.
     * If left-button, fill voxels according to selection mode.
     * If right-button, translate the view.
     */
    @Override
    public void mousePressed(final MouseEvent e) {
        Point2D pTransform = null;
        try {
            pTransform = this.zoomManager.transformPoint(e.getPoint());
        }
        catch (NoninvertibleTransformException e2) {
            e2.printStackTrace();
        }
        if (e.getButton() == 1) {
            if (this.editorMode == 0) {
                final ArrayList<Integer> list = this.Voxy.checkAndFill(pTransform.getX(), pTransform.getY(), this.mode, this.activeLayer, this.currentMaterial);
                if (list != null) {
                    if (list.isEmpty()) {
                        this.scene.setToUpdate();
                    }
                    else {
                        this.scene.setListToUpdate(list);
                    }
                }
            }
            else {
                this.cursorRectSelection = new Rectangle2D.Float();
                this.cursorRectSelection.x = (float)pTransform.getX();
                this.cursorRectSelection.y = (float)pTransform.getY();
            }
            this.leftButtonPushed = true;
        }
        this.repaint();
    }
    
    /**
     * Triggered when a mouse button is released within the grid.
     */
    @Override
    public void mouseReleased(final MouseEvent arg0) {
        if (this.cursorRectSelection != null) {
            final ArrayList<Integer> list = this.Voxy.checkAndFill(this.cursorRectSelection, this.mode, this.activeLayer, this.currentMaterial);
            this.cursorRectSelection = null;
            if (list != null) {
                if (list.isEmpty()) {
                    this.scene.setToUpdate();
                }
                else {
                    this.scene.setListToUpdate(list);
                }
            }
            this.repaint();
        }
        this.leftButtonPushed = false;
    }
    
    /**
     * Triggered when an action is performed, like clicking on a button.
     */
    @Override
    public void actionPerformed(final ActionEvent e) {
        DuplicateWindow dupWin = null;
        if (e.getActionCommand().equals("pen mode selection")) {
            this.editorMode = 0;
        }
        else if (e.getActionCommand().equals("rectangle mode selection")) {
            this.editorMode = 1;
        }
        else if (e.getActionCommand().equals("fill the selected layer")) {
            if (this.currentMaterial == null) {
                return;
            }
            final int option = JOptionPane.showConfirmDialog(null, "Do you really want to fill all the selected layer with " + this.currentMaterial.getName() + " ? Even filled voxels will be erase and fill with " + this.currentMaterial.getName(), "Filling the selected layer", 1, 3);
            if (option != 1 && option != 2 && option != -1) {
                this.fillLayer(false);
            }
        }
        else if (e.getActionCommand().equals("filling all")) {
            if (this.currentMaterial == null) {
                return;
            }
            final int option = JOptionPane.showConfirmDialog(null, "Do you really want to fill all the object with " + this.currentMaterial.getName() + "? Even filled voxels will be erase and fill with " + this.currentMaterial.getName(), "Filling all voxel in the object", 1, 3);
            if (option != 1 && option != 2 && option != -1) {
                this.fillObject(false);
            }
        }
        else if (e.getActionCommand().equals("fill empty voxel in the selected layer")) {
            this.fillLayer(true);
        }
        else if (e.getActionCommand().equals("fill empty voxel in the object")) {
            this.fillObject(true);
        }
        else if (e.getActionCommand().equals("duplicate")) {
            int nbLayer = 0;
            switch (this.mode) {
                case XY: {
                    nbLayer = this.Voxy.getNbVoxelZ();
                    break;
                }
                case XZ: {
                    nbLayer = this.Voxy.getNbVoxelY();
                    break;
                }
                case YZ: {
                    nbLayer = this.Voxy.getNbVoxelX();
                    break;
                }
            }
            dupWin = new DuplicateWindow(null, "Duplicate", true, this.activeLayer, nbLayer);
            dupWin.setVisible(true);
            final List<Integer> list = dupWin.getSelectedLayer();
            this.Voxy.duplicate(this.mode, this.activeLayer, list);
            this.scene.setToUpdate();
            this.repaint();
        }
    }
    
    /**
     * Triggered when the mouse is moved with a button pressed.
     * If it is the left-button, fill voxels according to selection mode.
     * If it is the right-button, translate the view.
     */
    @Override
    public void mouseDragged(final MouseEvent e) {
        Point2D pTransform = null;
        try {
            pTransform = this.zoomManager.transformPoint(e.getPoint());
        }
        catch (NoninvertibleTransformException e2) {
            e2.printStackTrace();
        }
        if (this.leftButtonPushed) {
            if (this.editorMode == 0) {
                final ArrayList<Integer> list = this.Voxy.checkAndFill(pTransform.getX(), pTransform.getY(), this.mode, this.activeLayer, this.currentMaterial);
                if (list != null) {
                    if (list.isEmpty()) {
                        this.scene.setToUpdate();
                    }
                    else {
                        this.scene.setListToUpdate(list);
                    }
                }
            }
            else {
                this.cursorRectSelection.width = (float)pTransform.getX() - this.cursorRectSelection.x;
                this.cursorRectSelection.height = (float)pTransform.getY() - this.cursorRectSelection.y;
            }
        }
        this.repaint();
    }
    
    @Override
    public void mouseMoved(final MouseEvent e) {
    }
    
    /**
     * Fill layer with the current material.
     * @param justEmpty if true, only empty voxels will be filled. If false, all voxels will be.
     */
    private void fillLayer(final boolean justEmpty) {
        if (this.currentMaterial == null) {
            return;
        }
        switch (this.mode) {
            case XY: {
                for (int y = 0; y < this.Voxy.getNbVoxelY(); ++y) {
                    for (int x = 0; x < this.Voxy.getNbVoxelX(); ++x) {
                        final VoxelObject v = this.Voxy.getVoxelToIndices(x, y, this.activeLayer);
                        if (justEmpty) {
                            if (v.isEmpty()) {
                                v.setMaterialIndex(this.currentMaterial);
                            }
                        }
                        else {
                            v.setMaterialIndex(this.currentMaterial);
                        }
                    }
                }
                break;
            }
            case XZ: {
                for (int z = 0; z < this.Voxy.getNbVoxelZ(); ++z) {
                    for (int x = 0; x < this.Voxy.getNbVoxelX(); ++x) {
                        final VoxelObject v = this.Voxy.getVoxelToIndices(x, this.activeLayer, z);
                        if (justEmpty) {
                            if (v.isEmpty()) {
                                v.setMaterialIndex(this.currentMaterial);
                            }
                        }
                        else {
                            v.setMaterialIndex(this.currentMaterial);
                        }
                    }
                }
                break;
            }
            case YZ: {
                for (int z = 0; z < this.Voxy.getNbVoxelZ(); ++z) {
                    for (int y2 = 0; y2 < this.Voxy.getNbVoxelY(); ++y2) {
                        final VoxelObject v = this.Voxy.getVoxelToIndices(this.activeLayer, y2, z);
                        if (justEmpty) {
                            if (v.isEmpty()) {
                                v.setMaterialIndex(this.currentMaterial);
                            }
                        }
                        else {
                            v.setMaterialIndex(this.currentMaterial);
                        }
                    }
                }
                break;
            }
        }
        this.scene.setToUpdate();
        this.repaint();
    }
    
    /**
     * Fill object with the current material.
     * @param justEmpty if true, only empty voxels will be filled. If false, all voxels will be.
     */
    private void fillObject(final boolean justEmpty) {
        if (this.currentMaterial == null) {
            return;
        }
        for (int z = 0; z < this.Voxy.getNbVoxelZ(); ++z) {
            for (int y = 0; y < this.Voxy.getNbVoxelY(); ++y) {
                for (int x = 0; x < this.Voxy.getNbVoxelX(); ++x) {
                    final VoxelObject v = this.Voxy.getVoxelToIndices(x, y, z);
                    if (justEmpty) {
                        if (v.isEmpty()) {
                            v.setMaterialIndex(this.currentMaterial);
                        }
                    }
                    else {
                        v.setMaterialIndex(this.currentMaterial);
                    }
                }
            }
        }
        this.scene.setToUpdate();
        this.repaint();
    }
    
    /**
     * Do the link between the editable Grid2D and the non-editable {link ViewGrid2D}, to update it when voxels are modified.
     * @param vg the non-editable grid.
     */
    public void addGridListener(final ViewGrid2D vg) {
        (this.gridlistener = vg).addLayerListener(this);
    }
    
    /**
     * Do the linking between the editable Grid2D and the {@link Scene}.
     * @param voxelizedScene the 3D scene.
     */
    public void addObjectChangedListener(final Scene voxelizedScene) {
        this.scene = voxelizedScene;
    }
    
    /**
     * @return the XY background.
     */
    public Rectangle2D.Float getRedBackground() {
        return this.redBackground;
    }
    
    /**
     * @return the YZ background.
     */
    public Rectangle2D.Float getgreenBackground() {
        return this.greenBackground;
    }
    
    /**
     * @return the XZ background.
     */
    public Rectangle2D.Float getblueBackground() {
        return this.blueBackground;
    }
    
    /**
     * @param lastXY the lastXY to set.
     */
    public void setLastXY(final int lastXY) {
        this.lastXY = lastXY;
    }
    
    /**
     * @return the lastXY.
     */
    public int getLastXY() {
        return this.lastXY;
    }
    
    /**
     * @param lastXZ the lastXZ to set
     */
    public void setLastXZ(final int lastXZ) {
        this.lastXZ = lastXZ;
    }
    
    /**
     * @return the lastXZ.
     */
    public int getLastXZ() {
        return this.lastXZ;
    }
    
    /**
     * @param lastYZ the lastYZ to set
     */
    public void setLastYZ(final int lastYZ) {
        this.lastYZ = lastYZ;
    }
    
    /**
     * @return the lastYZ.
     */
    public int getLastYZ() {
        return this.lastYZ;
    }
    
    /**
     * @return the camera handler.
     */
    public ZoomAndPanListener getZoomManager() {
        return this.zoomManager;
    }
    
    /**
     * Change the color of the probe.
     * @param color the new color to set.
     */
    public void setProbeColor(final Color color) {
        this.probeColor = color;
    }
    
    /**
     * Describe the mode of view.
     * @author alan
     */
    public enum Layer
    {
        XY("XY", 0), 
        YZ("YZ", 1), 
        XZ("XZ", 2);
        
        private Layer(final String name, final int ordinal) {
        }
    }
    
    /**
     * Describes what to show, radiation value or the color of the material.
     * @author alan
     */
    public enum RadMode
    {
        Mat("Mat", 0), 
        U("U", 1), 
        Th("Th", 2), 
        K("K", 3), 
        Ud("Ud", 4);
        
        private RadMode(final String name, final int ordinal) {
        }
    }
}
