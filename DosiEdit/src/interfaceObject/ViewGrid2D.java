package interfaceObject;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.util.HashMap;

import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import mainPackage.ExternalVoxelizedWorld;
import mainPackage.Material;
import mainPackage.VoxelObject;
import mainPackage.VoxelizedObject;

/**
 * This class provides an editable 2D view to manipulate the voxelized world and edit it.
 * @author alan
 * @see JPanel
 * @see ChangeListener
 */
public class ViewGrid2D extends JPanel implements ChangeListener
{
    private static final long serialVersionUID = -8250559212514780972L;
    public static float FACTOR_DISPLAY_GRID;
    private ZoomAndPanListener zoomManager;
    private Grid2D.Layer mode;
    private Grid2D.RadMode showMode;
    private Font font;
    private VoxelizedObject Voxy;
    private HashMap<Integer, HashMap<Integer, MinimalVoxel>> grid;
    private int activeLayer;
    private int lastXY;
    private int lastXZ;
    private int lastYZ;
    private Grid2D layerlistener;
    private Color probeColor;
    
    static {
        ViewGrid2D.FACTOR_DISPLAY_GRID = 40.0f;
    }
    
    /**
     * Constructor initializing the camera and the grid to render.
     * @param vox the voxelized object of the project.
     */
    public ViewGrid2D(final VoxelizedObject vox) {
        this.zoomManager = null;
        this.mode = Grid2D.Layer.XZ;
        this.showMode = Grid2D.RadMode.Mat;
        this.font = null;
        this.Voxy = null;
        this.grid = null;
        this.activeLayer = 0;
        this.lastXY = 0;
        this.lastXZ = 0;
        this.lastYZ = 0;
        this.probeColor = new Color(255, 0, 0);
        this.Voxy = vox;
        this.zoomManager = new ZoomAndPanListener(this, 1, vox.getNbVoxelX() * vox.getNbVoxelY() * 10, 1.2);
        this.grid = new HashMap<Integer, HashMap<Integer, MinimalVoxel>>();
        this.font = new Font("Serif", 0, 20);
        this.addMouseListener(this.zoomManager);
        this.addMouseMotionListener(this.zoomManager);
        this.addMouseWheelListener(this.zoomManager);
        this.updateMode(this.mode, this.activeLayer);
    }
    
    @Override
    public void paintComponent(final Graphics g) {
        super.paintComponent(g);
        this.setBackground(Color.gray);
        g.setFont(this.font);
        this.drawGrid(g);
    }
    
    /**
     * Draw black strips and voxels according the view.
     * @param g the current context graphics.
     */
    private void drawGrid(final Graphics g) {
        final Graphics2D g2d = (Graphics2D)g;
        final RenderingHints rh = new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        rh.put(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g2d.setRenderingHints(rh);
        g2d.setTransform(this.zoomManager.getCoordTransform());
        int nbX = 0;
        int nbY = 0;
        switch (this.mode) {
            case XY: {
                if (this.activeLayer == this.layerlistener.getLastXY()) {
                    g2d.setColor(Color.red);
                    g2d.fill(this.layerlistener.getRedBackground());
                    g2d.setColor(Color.black);
                }
                nbX = this.Voxy.getNbVoxelX();
                nbY = this.Voxy.getNbVoxelY();
                break;
            }
            case XZ: {
                if (this.activeLayer == this.layerlistener.getLastXZ()) {
                    g2d.setColor(Color.blue);
                    g2d.fill(this.layerlistener.getblueBackground());
                    g2d.setColor(Color.black);
                }
                nbX = this.Voxy.getNbVoxelX();
                nbY = this.Voxy.getNbVoxelZ();
                break;
            }
            case YZ: {
                if (this.activeLayer == this.layerlistener.getLastYZ()) {
                    g2d.setColor(Color.green);
                    g2d.fill(this.layerlistener.getgreenBackground());
                    g2d.setColor(Color.black);
                }
                nbX = this.Voxy.getNbVoxelZ();
                nbY = this.Voxy.getNbVoxelY();
                break;
            }
        }
        switch (this.showMode) {
            case Mat: {
                for (int y = 0; y < nbY; ++y) {
                    for (int x = 0; x < nbX; ++x) {
                        final MinimalVoxel mv = this.grid.get(y).get(x);
                        if (mv != null && mv.isVisible(this.getVisibleRect(), this.zoomManager.getCoordTransform())) {
                            this.drawNumber(g2d, mv, true, this.mode, x, y);
                            if (mv.m != null) {
                                if (mv.m.getTexture() == null) {
                                    g2d.setColor(mv.m.getColor());
                                }
                                else {
                                    g2d.setPaint(mv.m.getTexture());
                                }
                            }
                            else {
                                g2d.setColor(Color.white);
                            }
                            g2d.fill(mv);
                            if (this.Voxy instanceof ExternalVoxelizedWorld && x == ((ExternalVoxelizedWorld)this.Voxy).getProbeLocationX() && y == ((ExternalVoxelizedWorld)this.Voxy).getProbeLocationY()) {
                                g2d.setColor(this.probeColor);
                                g2d.setStroke(new BasicStroke(4.0f));
                                g2d.draw(mv);
                                g2d.setColor(Color.black);
                            }
                            else {
                                g2d.setColor(Color.black);
                                g2d.setStroke(new BasicStroke(2.0f));
                                g2d.draw(mv);
                            }
                            this.drawNumber(g2d, mv, false, this.mode, x, y);
                        }
                    }
                }
                break;
            }
            case U: {
                for (int y = 0; y < nbY; ++y) {
                    for (int x = 0; x < nbX; ++x) {
                        final MinimalVoxel mv = this.grid.get(y).get(x);
                        this.drawNumber(g2d, mv, true, this.mode, x, y);
                        g2d.setColor(Color.white);
                        g2d.fill(mv);
                        g2d.setColor(Color.black);
                        if (mv != null && mv.isVisible(this.getVisibleRect(), this.zoomManager.getCoordTransform()) && mv.m != null) {
                            g2d.drawString(new StringBuilder(String.valueOf(mv.u)).toString(), mv.x + mv.width / 5.0f, mv.y + mv.height * 2.0f / 3.0f);
                        }
                        if (this.Voxy instanceof ExternalVoxelizedWorld && x == ((ExternalVoxelizedWorld)this.Voxy).getProbeLocationX() && y == ((ExternalVoxelizedWorld)this.Voxy).getProbeLocationY()) {
                            g2d.setColor(this.probeColor);
                            g2d.setStroke(new BasicStroke(4.0f));
                            g2d.draw(mv);
                            g2d.setColor(Color.black);
                        }
                        else {
                            g2d.setColor(Color.black);
                            g2d.setStroke(new BasicStroke(2.0f));
                            g2d.draw(mv);
                        }
                        this.drawNumber(g2d, mv, false, this.mode, x, y);
                    }
                }
                break;
            }
            case Th: {
                for (int y = 0; y < nbY; ++y) {
                    for (int x = 0; x < nbX; ++x) {
                        final MinimalVoxel mv = this.grid.get(y).get(x);
                        this.drawNumber(g2d, mv, true, this.mode, x, y);
                        g2d.setColor(Color.white);
                        g2d.fill(mv);
                        g2d.setColor(Color.black);
                        if (mv != null && mv.isVisible(this.getVisibleRect(), this.zoomManager.getCoordTransform()) && mv.m != null) {
                            g2d.drawString(new StringBuilder(String.valueOf(mv.th)).toString(), mv.x + mv.width / 5.0f, mv.y + mv.height * 2.0f / 3.0f);
                        }
                        if (this.Voxy instanceof ExternalVoxelizedWorld && x == ((ExternalVoxelizedWorld)this.Voxy).getProbeLocationX() && y == ((ExternalVoxelizedWorld)this.Voxy).getProbeLocationY()) {
                            g2d.setColor(this.probeColor);
                            g2d.setStroke(new BasicStroke(4.0f));
                            g2d.draw(mv);
                            g2d.setColor(Color.black);
                        }
                        else {
                            g2d.setColor(Color.black);
                            g2d.setStroke(new BasicStroke(2.0f));
                            g2d.draw(mv);
                        }
                        this.drawNumber(g2d, mv, false, this.mode, x, y);
                    }
                }
                break;
            }
            case K: {
                for (int y = 0; y < nbY; ++y) {
                    for (int x = 0; x < nbX; ++x) {
                        final MinimalVoxel mv = this.grid.get(y).get(x);
                        this.drawNumber(g2d, mv, true, this.mode, x, y);
                        g2d.setColor(Color.white);
                        g2d.fill(mv);
                        g2d.setColor(Color.black);
                        if (mv != null && mv.isVisible(this.getVisibleRect(), this.zoomManager.getCoordTransform()) && mv.m != null) {
                            g2d.drawString(new StringBuilder(String.valueOf(mv.k)).toString(), mv.x + mv.width / 5.0f, mv.y + mv.height * 2.0f / 3.0f);
                        }
                        if (this.Voxy instanceof ExternalVoxelizedWorld && x == ((ExternalVoxelizedWorld)this.Voxy).getProbeLocationX() && y == ((ExternalVoxelizedWorld)this.Voxy).getProbeLocationY()) {
                            g2d.setColor(this.probeColor);
                            g2d.setStroke(new BasicStroke(4.0f));
                            g2d.draw(mv);
                            g2d.setColor(Color.black);
                        }
                        else {
                            g2d.setColor(Color.black);
                            g2d.setStroke(new BasicStroke(2.0f));
                            g2d.draw(mv);
                        }
                        this.drawNumber(g2d, mv, false, this.mode, x, y);
                    }
                }
                break;
            }
			default:
				break;
        }
    }
    
    /**
     * Draw the line numbers.
     * @param g the current context graphics.
     * @param mv the voxel represented just by its rectangle.
     * @param before 
     * @param mode
     * @param x
     * @param y
     */
    private void drawNumber(final Graphics g, final MinimalVoxel mv, final boolean before, final Grid2D.Layer mode, final int x, final int y) {
        if (before) {
            switch (mode) {
                case XY: {
                    if (y == 0) {
                        g.drawString(new StringBuilder(String.valueOf(x)).toString(), (int)(x * mv.width + mv.width / 3.0f), (int)(y * mv.height - mv.height / 3.0f));
                        break;
                    }
                    break;
                }
                case YZ: {
                    if (y == 0) {
                        g.drawString(new StringBuilder(String.valueOf(x)).toString(), (int)(x * mv.width + mv.width / 3.0f), (int)(y * mv.height - mv.height / 3.0f));
                        break;
                    }
                    break;
                }
			default:
				break;
            }
        }
        else {
            switch (mode) {
                case XY: {
                    if (x == 0) {
                        g.drawString(new StringBuilder(String.valueOf(y)).toString(), (int)(x * mv.width - mv.width), (int)(y * mv.height + mv.height / 2.0f));
                        break;
                    }
                    break;
                }
                case XZ: {
                    if (x == 0) {
                        g.drawString(new StringBuilder(String.valueOf(this.Voxy.getNbVoxelZ() - y - 1)).toString(), (int)(x * mv.width - mv.width), (int)(y * mv.height + mv.height / 2.0f));
                    }
                    if (y == 0) {
                        g.drawString(new StringBuilder(String.valueOf(x)).toString(), (int)(x * mv.width + mv.width / 3.0f), (int)(y * mv.height - mv.height / 3.0f));
                        break;
                    }
                    break;
                }
                case YZ: {
                    if (x == 0) {
                        g.drawString(new StringBuilder(String.valueOf(y)).toString(), (int)(x * mv.width - mv.width), (int)(y * mv.height + mv.height / 2.0f));
                        break;
                    }
                    break;
                }
            }
        }
    }
    
    /**
     * Update the grid depending of the Layer view mode.
     * @param mode Layer to display.
     * @param newLayer the number of the new layer displayed.
     */
    public void updateMode(final Grid2D.Layer mode, final int newLayer) {
        this.mode = mode;
        this.activeLayer = newLayer;
        final int nbVoxelZ = this.Voxy.getNbVoxelZ();
        final int nbVoxelY = this.Voxy.getNbVoxelY();
        final int nbVoxelX = this.Voxy.getNbVoxelX();
        switch (mode) {
            case XY: {
                for (int y = 0; y < nbVoxelY; ++y) {
                    final HashMap<Integer, MinimalVoxel> array = new HashMap<Integer, MinimalVoxel>();
                    for (int x = 0; x < nbVoxelX; ++x) {
                        final VoxelObject v = this.Voxy.getVoxelToIndices(x, y, this.activeLayer);
                        array.put(x, new MinimalVoxel(v.getMaterial(), v.getUranium(), v.getThorium(), v.getPotassium(), x * this.Voxy.getVoxelDimensionX(), y * this.Voxy.getVoxelDimensionY(), this.Voxy.getVoxelDimensionX(), this.Voxy.getVoxelDimensionY()));
                    }
                    this.grid.put(y, array);
                }
                break;
            }
            case XZ: {
                for (int z = 0; z < nbVoxelZ; ++z) {
                    final HashMap<Integer, MinimalVoxel> array = new HashMap<Integer, MinimalVoxel>();
                    for (int x = 0; x < nbVoxelX; ++x) {
                        final VoxelObject v = this.Voxy.getVoxelToIndices(x, this.activeLayer, z);
                        array.put(x, new MinimalVoxel(v.getMaterial(), v.getUranium(), v.getThorium(), v.getPotassium(), x * this.Voxy.getVoxelDimensionX(), z * this.Voxy.getVoxelDimensionZ(), this.Voxy.getVoxelDimensionX(), this.Voxy.getVoxelDimensionZ()));
                    }
                    this.grid.put(z, array);
                }
                break;
            }
            case YZ: {
                for (int z = nbVoxelZ - 1; z >= 0; --z) {
                    final HashMap<Integer, MinimalVoxel> array = new HashMap<Integer, MinimalVoxel>();
                    for (int y2 = 0; y2 < nbVoxelY; ++y2) {
                        final VoxelObject v = this.Voxy.getVoxelToIndices(this.activeLayer, y2, z);
                        array.put(y2, new MinimalVoxel(v.getMaterial(), v.getUranium(), v.getThorium(), v.getPotassium(), y2 * this.Voxy.getVoxelDimensionY(), z * this.Voxy.getVoxelDimensionZ(), this.Voxy.getVoxelDimensionY(), this.Voxy.getVoxelDimensionZ()));
                    }
                    this.grid.put(z, array);
                }
                break;
            }
        }
        this.repaint();
    }
    
    /**
     * Update the grid when a voxel is changed.
     */
    public void updateMaterial() {
        final int nbVoxelZ = this.Voxy.getNbVoxelZ();
        final int nbVoxelY = this.Voxy.getNbVoxelY();
        final int nbVoxelX = this.Voxy.getNbVoxelX();
        switch (this.mode) {
            case XY: {
                if (nbVoxelX != this.grid.get(0).size() || nbVoxelY != this.grid.size()) {
                    this.updateMode(Grid2D.Layer.XY, this.lastXY);
                }
                for (int y = 0; y < nbVoxelY; ++y) {
                    for (int x = 0; x < nbVoxelX; ++x) {
                        this.grid.get(y).get(x).m = this.Voxy.getVoxelToIndices(x, y, this.activeLayer).getMaterial();
                        this.grid.get(y).get(x).u = this.Voxy.getVoxelToIndices(x, y, this.activeLayer).getUranium();
                        this.grid.get(y).get(x).th = this.Voxy.getVoxelToIndices(x, y, this.activeLayer).getThorium();
                        this.grid.get(y).get(x).k = this.Voxy.getVoxelToIndices(x, y, this.activeLayer).getPotassium();
                    }
                }
                break;
            }
            case XZ: {
                if (nbVoxelX != this.grid.get(0).size() || nbVoxelZ != this.grid.size()) {
                    this.updateMode(Grid2D.Layer.XZ, this.lastXZ);
                }
                for (int z = 0; z < nbVoxelZ; ++z) {
                    for (int x = 0; x < nbVoxelX; ++x) {
                        this.grid.get(z).get(x).m = this.Voxy.getVoxelToIndices(x, this.activeLayer, z).getMaterial();
                        this.grid.get(z).get(x).u = this.Voxy.getVoxelToIndices(x, this.activeLayer, z).getUranium();
                        this.grid.get(z).get(x).th = this.Voxy.getVoxelToIndices(x, this.activeLayer, z).getThorium();
                        this.grid.get(z).get(x).k = this.Voxy.getVoxelToIndices(x, this.activeLayer, z).getPotassium();
                    }
                }
                break;
            }
            case YZ: {
                if (nbVoxelZ != this.grid.size() || nbVoxelY != this.grid.get(0).size()) {
                    this.updateMode(Grid2D.Layer.YZ, this.lastYZ);
                }
                for (int z = nbVoxelZ - 1; z >= 0; --z) {
                    for (int y2 = 0; y2 < nbVoxelY; ++y2) {
                        this.grid.get(z).get(y2).m = this.Voxy.getVoxelToIndices(this.activeLayer, y2, z).getMaterial();
                        this.grid.get(y2).get(y2).u = this.Voxy.getVoxelToIndices(this.activeLayer, y2, z).getUranium();
                        this.grid.get(y2).get(y2).th = this.Voxy.getVoxelToIndices(this.activeLayer, y2, z).getThorium();
                        this.grid.get(y2).get(y2).k = this.Voxy.getVoxelToIndices(this.activeLayer, y2, z).getPotassium();
                    }
                }
                break;
            }
        }
        this.repaint();
    }
    
    /**
     * @return the current Grid2D.Layer view mode.
     */
    public Grid2D.Layer getLayerViewMode() {
        return this.mode;
    }
    
    @Override
    public void stateChanged(final ChangeEvent e) {
        this.activeLayer = ((JSlider)e.getSource()).getValue();
        switch (this.mode) {
            case XY: {
                this.lastXY = this.activeLayer;
                break;
            }
            case XZ: {
                this.lastXZ = this.activeLayer;
                break;
            }
            case YZ: {
                this.lastYZ = this.activeLayer;
                break;
            }
        }
        this.updateMaterial();
        ((EditorPane)this.getParent().getParent().getParent()).updateYourself();
    }
    
    /**
     * Set the Grid2D.RadMode
     * @param mat the new radiation mode.
     */
    public void setShowMode(final Grid2D.RadMode mat) {
        this.showMode = mat;
    }
    
    /**
     * @return the last XY.
     */
    public int getLastXY() {
        return this.lastXY;
    }
    
    /**
     * @param the last XY.
     */
    public void setLastXY(final int lastXY) {
        this.lastXY = lastXY;
    }
    
    /**
     * @return the last XZ.
     */
    public int getLastXZ() {
        return this.lastXZ;
    }
    
    /**
     * @param the last XZ.
     */
    public void setLastXZ(final int lastXZ) {
        this.lastXZ = lastXZ;
    }
    
    /**
     * @return the last YZ.
     */
    public int getLastYZ() {
        return this.lastYZ;
    }
    /**
     * 
     * @param the last YZ.
     */
    public void setLastYZ(final int lastYZ) {
        this.lastYZ = lastYZ;
    }
    
    /**
     * @return the active layer.
     */
    public int getActiveLayer() {
        return this.activeLayer;
    }
    
    /**
     * Do linking with the {@link Grid2D}.
     * @param grid2d the viewer editable 2D.
     */
    public void addLayerListener(final Grid2D grid2d) {
        this.layerlistener = grid2d;
    }
    
    /**
     * Change the probe color.
     * @param color the new color to set.
     */
    public void setProbeColor(final Color color) {
        this.probeColor = color;
    }
    /**
     * Represents a voxel by its necessary information, just to
     * be handle by a non-editable viewer.
     * @author alan
     *
     */
    private class MinimalVoxel extends Rectangle2D.Float
    {
        private static final long serialVersionUID = -66354520292976888L;
        public Material m;
        public float u;
        public float th;
        public float k;
        
        public MinimalVoxel(final Material m, final float u, final float th, final float k, final float x, final float y, final float width, final float height) {
            this.setRect(x, y, width, height);
            this.m = m;
            this.u = u;
            this.th = th;
            this.k = k;
        }
        
        public boolean isVisible(final Rectangle bounds, final AffineTransform coordTransform) {
            final double tX = coordTransform.getTranslateX() + this.getBounds().getLocation().x * coordTransform.getScaleX();
            final double tY = coordTransform.getTranslateY() + this.getBounds().getLocation().y * coordTransform.getScaleY();
            return tX + this.getWidth() * coordTransform.getScaleX() > bounds.x - this.width && tY + this.getHeight() * coordTransform.getScaleY() > bounds.y - this.height && tX < bounds.width + this.width && tY < bounds.height + this.height;
        }
        
        @Override
        public void setRect(float x,float y,float w, float h) {
            
            if(!VoxelObject.KEEP_RATIO_ON_GRID)
            {
            	x = x / w;
            	y = y / h;
            	w = h = 1;
            }
            this.x = x * VoxelObject.FACTOR_DISPLAY_GRID;
            this.y = y * VoxelObject.FACTOR_DISPLAY_GRID;
            this.width = w * VoxelObject.FACTOR_DISPLAY_GRID;
            this.height = h * VoxelObject.FACTOR_DISPLAY_GRID;
        }
    }
}
