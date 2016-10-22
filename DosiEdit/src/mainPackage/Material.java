package mainPackage;

import java.awt.Color;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.TexturePaint;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

import util.Couple;

/**
 * A material is a class made of component.
 * @author alan
 * @see Component
 */
public class Material implements Serializable
{
    private static final long serialVersionUID = 5589948919236210170L;
    private int index;
    private String name;
    private boolean useCalculatedDensity = true;
    private float calculatedDryDensity;
    private float dryDensity;
    private float waterContent;
    private int nbComponents;
    private ArrayList<Couple<Component, Float>> ComponentsList;
    private float EmptyPercent;
    private float RefUraniumValue;
    private float RefThoriumValue;
    private float RefPotassiumValue;
    private float RefUserDefinedValue;
    private float currentUraniumValue;
    private float currentThoriumValue;
    private float currentPotassiumValue;
    private float currentUserDefinedValue;
    private boolean used;
    private int grain_component_index;
    private float grain_density;
    private float grain_compacity;
    private int grain_nbGranulometricFractions;
    private ArrayList<Couple<Float, Float>> GrainsList;
    private Color color;
    private ImageIcon textureIcon;
    private TexturePaint texture;
    private String nameTexture;
    private static int WIDTH_MINIATURE;
    private static int HEIGHT_MINIATURE;
    private boolean isActive;
    
    static {
        Material.WIDTH_MINIATURE = 20;
        Material.HEIGHT_MINIATURE = 20;
    }
    
    /**
     * Default constructor initializing an empty ComponentList and GrainList.
     * @param i the index of the material.
     */
    public Material(final int i) {
        this.name = "Unknown";
        this.dryDensity = 0.0f;
        this.waterContent = 0.0f;
        this.nbComponents = 0;
        this.ComponentsList = null;
        this.EmptyPercent = 100.0f;
        this.RefUraniumValue = 0.0f;
        this.RefThoriumValue = 0.0f;
        this.RefPotassiumValue = 0.0f;
        this.RefUserDefinedValue = 0.0f;
        this.currentUraniumValue = 0.0f;
        this.currentThoriumValue = 0.0f;
        this.currentPotassiumValue = 0.0f;
        this.currentUserDefinedValue = 0.0f;
        this.used = false;
        this.grain_component_index = 0;
        this.grain_density = 0.0f;
        this.grain_compacity = 0.0f;
        this.grain_nbGranulometricFractions = 0;
        this.color = null;
        this.textureIcon = null;
        this.texture = null;
        this.nameTexture = "";
        this.isActive = true;
        this.name = String.valueOf(this.name) + i;
        this.ComponentsList = new ArrayList<Couple<Component, Float>>(2);
        this.GrainsList = new ArrayList<Couple<Float, Float>>(1);
        this.index = i;
    }
    
    /**
     * Constructor initializing the name and the empty lists, Component and Grain.
     * @param name the name of the material.
     * @param i the index of the material.
     */
    public Material(final String name, final int i) {
        this.name = "Unknown";
        this.dryDensity = 0.0f;
        this.waterContent = 0.0f;
        this.nbComponents = 0;
        this.ComponentsList = null;
        this.EmptyPercent = 100.0f;
        this.RefUraniumValue = 0.0f;
        this.RefThoriumValue = 0.0f;
        this.RefPotassiumValue = 0.0f;
        this.RefUserDefinedValue = 0.0f;
        this.currentUraniumValue = 0.0f;
        this.currentThoriumValue = 0.0f;
        this.currentPotassiumValue = 0.0f;
        this.currentUserDefinedValue = 0.0f;
        this.used = false;
        this.grain_component_index = 0;
        this.grain_density = 0.0f;
        this.grain_compacity = 0.0f;
        this.grain_nbGranulometricFractions = 0;
        this.color = null;
        this.textureIcon = null;
        this.texture = null;
        this.nameTexture = "";
        this.isActive = true;
        this.name = name;
        this.ComponentsList = new ArrayList<Couple<Component, Float>>(2);
        this.GrainsList = new ArrayList<Couple<Float, Float>>(1);
        this.index = i;
    }
    
    public void loadTexture(final File file) throws IOException {
        final Image img = ImageIO.read(file);
        this.texture = new TexturePaint((BufferedImage)img, new Rectangle(0, 0, 60, 60));
        this.textureIcon = new ImageIcon(new ImageIcon(ImageIO.read(file)).getImage().getScaledInstance(Material.WIDTH_MINIATURE, Material.HEIGHT_MINIATURE, 1));
        this.nameTexture = file.getName();
    }
    
    public void setActive(final boolean isActive) {
        this.isActive = isActive;
    }
    
    public boolean isActive() {
        return this.isActive;
    }
    
    public TexturePaint getTexture() {
        return this.texture;
    }
    
    public ImageIcon getIcon() {
        return this.textureIcon;
    }
    
    public String getNameTexture() {
        return this.nameTexture;
    }
    
    public int getIndexMaterial() {
        return this.index;
    }
    
    public String getName() {
        return this.name;
    }
    
    public void setName(final String name) {
        this.name = name;
    }
    
    /**
	 * @return the calculatedDryDensity
	 */
	public float getCalculatedDryDensity()
	{
		return calculatedDryDensity;
	}

	/**
	 * @param calculatedDryDensity the calculatedDryDensity to set
	 */
	public void setCalculatedDryDensity(float calculatedDryDensity)
	{
		this.calculatedDryDensity = calculatedDryDensity;
	}

	public float getDryDensity() {
        return this.dryDensity;
    }
    
    public void setDryDensity(final float dryDensity) {
        this.dryDensity = dryDensity;
    }
    
    public float getWaterContent() {
        return this.waterContent;
    }
    
    public void setWaterContent(final float waterContent) {
        this.waterContent = waterContent;
    }
    
    public int getNbComponent() {
        return this.nbComponents;
    }
    
    public void addComponent(final Component c) {
        this.ComponentsList.add(new Couple<Component, Float>(c, this.EmptyPercent));
        this.EmptyPercent = 0.0f;
        ++this.nbComponents;
    }
    
    public void addComponent(final Component c, float percent) {
        if (percent < 0.0f) {
            percent = 0.0f;
        }
        this.ComponentsList.add(new Couple<Component, Float>(c, percent));
        ++this.nbComponents;
    }
    
    public void deleteComponent(final Component c) {
        final Iterator<Couple<Component, Float>> it = this.ComponentsList.iterator();
        while (it.hasNext()) {
            final Couple<Component, Float> couple = it.next();
            if (couple.getValeur1() == c) {
                this.EmptyPercent += couple.getValeur2();
                it.remove();
                --this.nbComponents;
            }
        }
    }
    
    public void setComponentPercent(final int indexOfComponent, float percent) {
        if (percent < 0.0f) {
            percent = 0.0f;
        }
        this.ComponentsList.get(indexOfComponent).setValeur2(percent);
    }
    
    public ArrayList<Couple<Component, Float>> getListComponents() {
        return this.ComponentsList;
    }
    
    public void setRefUraniumValue(final float uraniumValue) {
        this.RefUraniumValue = uraniumValue;
    }
    
    public float getRefUraniumValue() {
        return this.RefUraniumValue;
    }
    
    public void setRefThoriumValue(final float thoriumValue) {
        this.RefThoriumValue = thoriumValue;
    }
    
    public float getRefThoriumValue() {
        return this.RefThoriumValue;
    }
    
    public void setRefPotassiumValue(final float potassiumValue) {
        this.RefPotassiumValue = potassiumValue;
    }
    
    public float getRefPotassiumValue() {
        return this.RefPotassiumValue;
    }
    
    public void setRefUserDefinedValue(final float refUserDefinedValue) {
        this.RefUserDefinedValue = refUserDefinedValue;
    }
    
    public float getRefUserDefinedValue() {
        return this.RefUserDefinedValue;
    }
    
    public void setUsed(final boolean used) {
        this.used = used;
    }
    
    public boolean isUsed() {
        return this.used;
    }
    
    public int getGrainComponentIndex() {
        return this.grain_component_index;
    }
    
    public void setGrainComponentIndex(final int index) {
        this.grain_component_index = index;
    }
    
    public float getGrainDensity() {
        return this.grain_density;
    }
    
    public void setGrainDensity(final float density) {
        this.grain_density = density;
    }
    
    public float getGrainCompacity() {
        return this.grain_compacity;
    }
    
    public void setGrainCompacity(final float compacity) {
        this.grain_compacity = compacity;
    }
    
    public int getGrainNbGranulometricFractions() {
        return this.grain_nbGranulometricFractions;
    }
    
    @Deprecated
    public void setGrainNbGranulometricFractions(final int nb) {
        this.grain_nbGranulometricFractions = nb;
    }
    
    public ArrayList<Couple<Float, Float>> getGrainList() {
        return this.GrainsList;
    }
    
    public void addGrains(final float diameter, final float percent) {
        final Couple<Float, Float> c = new Couple<Float, Float>(diameter, percent);
        this.GrainsList.add(c);
        ++this.grain_nbGranulometricFractions;
    }
    
    public void clearGrainList() {
        this.GrainsList.clear();
        this.grain_nbGranulometricFractions = 0;
    }
    
    @Override
    public String toString() {
        return String.valueOf(this.name) + " " + this.RefUraniumValue + " " + this.RefThoriumValue + " " + this.RefPotassiumValue;
    }
    
    public void setColor(final Color color) {
        this.color = color;
    }
    
    public Color getColor() {
        return this.color;
    }
    
    public void setCurrentUraniumValue(final float currentUraniumValue) {
        this.currentUraniumValue = currentUraniumValue;
    }
    
    public float getCurrentUraniumValue() {
        return this.currentUraniumValue;
    }
    
    public void setCurrentThoriumValue(final float currentThoriumValue) {
        this.currentThoriumValue = currentThoriumValue;
    }
    
    public float getCurrentThoriumValue() {
        return this.currentThoriumValue;
    }
    
    public void setCurrentPotassiumValue(final float currentPotassiumValue) {
        this.currentPotassiumValue = currentPotassiumValue;
    }
    
    public float getCurrentPotassiumValue() {
        return this.currentPotassiumValue;
    }
    
    public void setCurrentUserDefinedValue(final float currentUserDefinedValue) {
        this.currentUserDefinedValue = currentUserDefinedValue;
    }
    
    public float getCurrentUserDefinedValue() {
        return this.currentUserDefinedValue;
    }
    
    @Override
    public int hashCode() {
        int result = 1;
        result = 31 * result + ((this.ComponentsList == null) ? 0 : this.ComponentsList.hashCode());
        result = 31 * result + ((this.GrainsList == null) ? 0 : this.GrainsList.hashCode());
        result = 31 * result + Float.floatToIntBits(this.RefPotassiumValue);
        result = 31 * result + Float.floatToIntBits(this.RefThoriumValue);
        result = 31 * result + Float.floatToIntBits(this.RefUraniumValue);
        result = 31 * result + Float.floatToIntBits(this.RefUserDefinedValue);
        result = 31 * result + ((this.color == null) ? 0 : this.color.hashCode());
        result = 31 * result + Float.floatToIntBits(this.currentPotassiumValue);
        result = 31 * result + Float.floatToIntBits(this.currentThoriumValue);
        result = 31 * result + Float.floatToIntBits(this.currentUraniumValue);
        result = 31 * result + Float.floatToIntBits(this.currentUserDefinedValue);
        result = 31 * result + Float.floatToIntBits(this.dryDensity);
        result = 31 * result + Float.floatToIntBits(this.grain_compacity);
        result = 31 * result + this.grain_component_index;
        result = 31 * result + Float.floatToIntBits(this.grain_density);
        result = 31 * result + this.grain_nbGranulometricFractions;
        result = 31 * result + this.index;
        result = 31 * result + ((this.name == null) ? 0 : this.name.hashCode());
        result = 31 * result + this.nbComponents;
        result = 31 * result + Float.floatToIntBits(this.waterContent);
        return result;
    }
    
    @Override
    public boolean equals(final Object obj) {
        if (obj == null) {
            return false;
        }
        if (this.getClass() != obj.getClass()) {
            return false;
        }
        final Material other = (Material)obj;
        if (this.ComponentsList == null) {
            if (other.ComponentsList != null) {
                return false;
            }
        }
        else if (!this.ComponentsList.equals(other.ComponentsList)) {
            return false;
        }
        if (this.GrainsList == null) {
            if (other.GrainsList != null) {
                return false;
            }
        }
        else if (!this.GrainsList.equals(other.GrainsList)) {
            return false;
        }
        if (Float.floatToIntBits(this.RefPotassiumValue) != Float.floatToIntBits(other.RefPotassiumValue)) {
            return false;
        }
        if (Float.floatToIntBits(this.RefThoriumValue) != Float.floatToIntBits(other.RefThoriumValue)) {
            return false;
        }
        if (Float.floatToIntBits(this.RefUraniumValue) != Float.floatToIntBits(other.RefUraniumValue)) {
            return false;
        }
        if (Float.floatToIntBits(this.RefUserDefinedValue) != Float.floatToIntBits(other.RefUserDefinedValue)) {
            return false;
        }
        if (this.color == null) {
            if (other.color != null) {
                return false;
            }
        }
        else if (!this.color.equals(other.color)) {
            return false;
        }
        if (Float.floatToIntBits(this.currentPotassiumValue) != Float.floatToIntBits(other.currentPotassiumValue)) {
            return false;
        }
        if (Float.floatToIntBits(this.currentThoriumValue) != Float.floatToIntBits(other.currentThoriumValue)) {
            return false;
        }
        if (Float.floatToIntBits(this.currentUraniumValue) != Float.floatToIntBits(other.currentUraniumValue)) {
            return false;
        }
        if (Float.floatToIntBits(this.currentUserDefinedValue) != Float.floatToIntBits(other.currentUserDefinedValue)) {
            return false;
        }
        if (Float.floatToIntBits(this.dryDensity) != Float.floatToIntBits(other.dryDensity)) {
            return false;
        }
        if (Float.floatToIntBits(this.grain_compacity) != Float.floatToIntBits(other.grain_compacity)) {
            return false;
        }
        if (this.grain_component_index != other.grain_component_index) {
            return false;
        }
        if (Float.floatToIntBits(this.grain_density) != Float.floatToIntBits(other.grain_density)) {
            return false;
        }
        if (this.grain_nbGranulometricFractions != other.grain_nbGranulometricFractions) {
            return false;
        }
        if (this.index != other.index) {
            return false;
        }
        if (this.name == null) {
            if (other.name != null) {
                return false;
            }
        }
        else if (!this.name.equals(other.name)) {
            return false;
        }
        return this.nbComponents == other.nbComponents && Float.floatToIntBits(this.waterContent) == Float.floatToIntBits(other.waterContent);
    }

	/**
	 * @return the useCalculatedDensity
	 */
	public boolean isUseCalculatedDensity()
	{
		return useCalculatedDensity;
	}

	/**
	 * @param useCalculatedDensity the useCalculatedDensity to set
	 */
	public void setUseCalculatedDensity(boolean useCalculatedDensity)
	{
		this.useCalculatedDensity = useCalculatedDensity;
	}
}
