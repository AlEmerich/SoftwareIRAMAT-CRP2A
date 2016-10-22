package mainPackage;

import interfaceObject.Grid2D;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.io.Serializable;

/**
 * Intern class which represents a single voxel. 
 * The object which provides in a single object the index and radiation value of the material which fills the voxel.
 * @author alan
 */
public class VoxelObject extends Rectangle2D.Float implements Serializable
{
    private static final long serialVersionUID = 84769043914526859L;
    
    public static boolean KEEP_RATIO_ON_GRID = false;
    
    public static float FACTOR_DISPLAY_GRID;
    private Material mat;
    private float Uranium;
    private float Thorium;
    private float Potassium;
    private float DefinedRadMolecule;
    private boolean isUpdateForScene;
    
    static {
        VoxelObject.FACTOR_DISPLAY_GRID = 40.0f;
    }
    
    /**
     * The default constructor. Do nothing.
     * @param x the x position of the drawable rectangle.
     * @param y the y position of the drawable rectangle.
     * @param width the width of the drawable rectangle.
     * @param height the height of the drawable rectangle.
     */
    public VoxelObject(final float x, final float y, final float width, final float height) {
        this.mat = null;
        this.Uranium = 0.0f;
        this.Thorium = 0.0f;
        this.Potassium = 0.0f;
        this.DefinedRadMolecule = 0.0f;
        this.isUpdateForScene = false;
        this.setRect(x, y, width, height);
    }
    
    /**
     * The constructor called demanding to fill with the material.
     * @param x the x position of the drawable rectangle.
     * @param y the y position of the drawable rectangle.
     * @param width the width of the drawable rectangle.
     * @param height the height of the drawable rectangle.
     * @param m the material to fill with.
     */
    public VoxelObject(final float x, final float y, final float width, final float height, final Material m) {
        this.mat = null;
        this.Uranium = 0.0f;
        this.Thorium = 0.0f;
        this.Potassium = 0.0f;
        this.DefinedRadMolecule = 0.0f;
        this.isUpdateForScene = false;
        this.mat = m;
        this.Uranium = m.getCurrentUraniumValue();
        this.Thorium = m.getCurrentThoriumValue();
        this.Potassium = m.getCurrentPotassiumValue();
        this.DefinedRadMolecule = m.getCurrentUserDefinedValue();
        this.setRect(x, y, width, height);
    }
    
    @Override
    public void setRect(float x, float y, float w, float h) {
        
        if(!KEEP_RATIO_ON_GRID)
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
    
    /**
     * change the material filling the voxel.
     * @param m the new material.
     */
    public void setMaterialIndex(final Material m) {
        this.mat = m;
        if (this.mat != null) {
            this.setUranium(m.getCurrentUraniumValue());
            this.setThorium(m.getCurrentThoriumValue());
            this.setPotassium(m.getCurrentPotassiumValue());
        }
        else {
            this.setUranium(0.0f);
            this.setThorium(0.0f);
            this.setPotassium(0.0f);
        }
        this.isUpdateForScene = false;
    }
    
    /**
     * Set a new uranium radiation value.
     * @param uranium
     */
    public void setUranium(final float uranium) {
        this.Uranium = uranium;
    }
    
    /**
     * @return the uranium value.
     */
    public float getUranium() {
        return this.Uranium;
    }
    
    /**
     * Set a new thorium value.
     * @param thorium
     */
    public void setThorium(final float thorium) {
        this.Thorium = thorium;
    }
    
    /**
     * @return the thorium value.
     */
    public float getThorium() {
        return this.Thorium;
    }
    
    /**
     * Set a new potassium value.
     * @param potassium
     */
    public void setPotassium(final float potassium) {
        this.Potassium = potassium;
    }
    
    /**
     * @return the potassium value.
     */
    public float getPotassium() {
        return this.Potassium;
    }
    
    /**
     * Set a new user-defined molecule radiation value.
     * @param definedRadMolecule
     */
    public void setDefinedRadMolecule(final float definedRadMolecule) {
        this.DefinedRadMolecule = definedRadMolecule;
    }
    
    /**
     * @return the user-defined molecule radiation value.
     */
    public float getDefinedRadMolecule() {
        return this.DefinedRadMolecule;
    }
    
    @Override
    public String toString() {
        final String str = this.mat + "," + this.Uranium + "," + this.Thorium + "," + this.Potassium + "," + this.DefinedRadMolecule + " | ";
        return str;
    }
    
    /**
     * Drax the voxel.
     * @param g the Graphics context.
     * @param showMode the show mode, Material, U, Th, K.
     */
    public void draw(final Graphics2D g, final Grid2D.RadMode showMode) {
        switch (showMode) {
            case Mat: {
                if (this.mat != null) {
                    if (this.mat.getTexture() == null) {
                        g.setColor(this.mat.getColor());
                    }
                    else {
                        g.setPaint(this.mat.getTexture());
                    }
                }
                else {
                    g.setColor(Color.white);
                }
                g.fill(this);
                break;
            }
            case U: {
                g.setColor(Color.white);
                g.fill(this);
                g.setColor(Color.black);
                if (this.mat != null) {
                    g.drawString(new StringBuilder(String.valueOf(this.Uranium)).toString(), this.x + this.width / 5.0f, this.y + this.height * 2.0f / 3.0f);
                    break;
                }
                break;
            }
            case Th: {
                g.setColor(Color.white);
                g.fill(this);
                g.setColor(Color.black);
                if (this.mat != null) {
                    g.drawString(new StringBuilder(String.valueOf(this.Thorium)).toString(), this.x + this.width / 5.0f, this.y + this.height * 2.0f / 3.0f);
                    break;
                }
                break;
            }
            case K: {
                g.setColor(Color.white);
                g.fill(this);
                g.setColor(Color.black);
                if (this.mat != null) {
                    g.drawString(new StringBuilder(String.valueOf(this.Potassium)).toString(), this.x + this.width / 5.0f, this.y + this.height * 2.0f / 3.0f);
                    break;
                }
                break;
            }
			default:
				break;
        }
    }
    
    @Override
    public boolean isEmpty() {
        return this.mat == null;
    }
    
    /**
     * @return true if the voxel is up to date for scene.
     */
    public boolean isUpdateForScene() {
        return this.isUpdateForScene;
    }
    
    /**
     * Set the update state of the voxel.
     */
    public void setUpdate() {
        this.isUpdateForScene = true;
    }
    
    /**
     * @return the material filling the voxel.
     */
    public Material getMaterial() {
        return this.mat;
    }
    
    /**
     * Check if voxel is visible in the Grid2D.
     * @param bounds the current bounds.
     * @param coordTransform the current affine transform.
     * @return true if voxel is visible, false if not.
     */
    public boolean isVisible(final Rectangle bounds, final AffineTransform coordTransform) {
        final double tX = coordTransform.getTranslateX() + this.getBounds().getLocation().x * coordTransform.getScaleX();
        final double tY = coordTransform.getTranslateY() + this.getBounds().getLocation().y * coordTransform.getScaleY();
        return tX + this.getWidth() * coordTransform.getScaleX() > bounds.x - this.width && tY + this.getHeight() * coordTransform.getScaleY() > bounds.y - this.height && tX < bounds.width + this.width && tY < bounds.height + this.height;
    }
    
    /**
     * Clone the current voxel.
     */
    @Override
    public VoxelObject clone() {
        if (this.mat == null) {
            return new VoxelObject(this.x, this.y, this.width, this.height);
        }
        return new VoxelObject(this.x, this.y, this.width, this.height, this.mat);
    }
}
