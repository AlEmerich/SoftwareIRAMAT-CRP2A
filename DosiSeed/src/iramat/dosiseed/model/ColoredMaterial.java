package iramat.dosiseed.model;

import iramat.dosiseed.model.Material;

import java.awt.Color;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.TexturePaint;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

public class ColoredMaterial extends Material
{

	/**
	 * 
	 */
	private static final long serialVersionUID = -2068521055905849835L;
	
	private Color color;
	
	private ImageIcon textureIcon;
	
    private TexturePaint texture;
    
    private String nameTexture;

	private int index;
	
	private boolean useTexture;
	
	private float RefUraniumValue;
	private float RefThoriumValue;
    private float RefPotassiumValue;
    private float RefUserDefinedValue;
    private float currentUraniumValue;
    private float currentThoriumValue;
    private float currentPotassiumValue;
    private float currentUserDefinedValue;
	
	private static int WIDTH_MINIATURE = 20;
	
    private static int HEIGHT_MINIATURE = 20;
	
	public ColoredMaterial(int index)
	{
		super();
		this.index = index;
		this.color = null;
		this.textureIcon = null;
        this.texture = null;
        this.nameTexture = "";
        this.useTexture = false;
	}
	
	public ColoredMaterial(String name,float dens,int index)
	{
		super(name,dens);
		this.index = index;
		this.color = null;
		this.textureIcon = null;
        this.texture = null;
        this.nameTexture = "";
	}
	
	public void setColor(final Color color) {
        this.color = color;
        this.useTexture = false;
    }
    
    public Color getColor() {
        return this.color;
    }

	public void setIndexMaterial(int index)
	{
		this.index = index;
	}
	
	public int getIndexMaterial()
	{
		return this.index;
	}
	
	public void loadTexture(final File file) throws IOException {
        final Image img = ImageIO.read(file);
        this.texture = new TexturePaint((BufferedImage)img, new Rectangle(0, 0, 60, 60));
        this.textureIcon = new ImageIcon(new ImageIcon(ImageIO.read(file)).getImage().getScaledInstance(WIDTH_MINIATURE, HEIGHT_MINIATURE, 1));
        this.nameTexture = file.getName();
        this.setUseTexture(true);
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

	/**
	 * @return the useTexture
	 */
	public boolean isUseTexture()
	{
		return useTexture;
	}

	/**
	 * @param useTexture the useTexture to set
	 */
	public void setUseTexture(boolean useTexture)
	{
		this.useTexture = useTexture;
	}
	
	/**
	 * @return the refUraniumValue
	 */
	public float getRefUraniumValue()
	{
		return RefUraniumValue;
	}

	/**
	 * @param refUraniumValue the refUraniumValue to set
	 */
	public void setRefUraniumValue(float refUraniumValue)
	{
		RefUraniumValue = refUraniumValue;
	}

	/**
	 * @return the refThoriumValue
	 */
	public float getRefThoriumValue()
	{
		return RefThoriumValue;
	}

	/**
	 * @param refThoriumValue the refThoriumValue to set
	 */
	public void setRefThoriumValue(float refThoriumValue)
	{
		RefThoriumValue = refThoriumValue;
	}

	/**
	 * @return the refPotassiumValue
	 */
	public float getRefPotassiumValue()
	{
		return RefPotassiumValue;
	}

	/**
	 * @param refPotassiumValue the refPotassiumValue to set
	 */
	public void setRefPotassiumValue(float refPotassiumValue)
	{
		RefPotassiumValue = refPotassiumValue;
	}

	/**
	 * @return the refUserDefinedValue
	 */
	public float getRefUserDefinedValue()
	{
		return RefUserDefinedValue;
	}

	/**
	 * @param refUserDefinedValue the refUserDefinedValue to set
	 */
	public void setRefUserDefinedValue(float refUserDefinedValue)
	{
		RefUserDefinedValue = refUserDefinedValue;
	}

	/**
	 * @return the currentUraniumValue
	 */
	public float getCurrentUraniumValue()
	{
		return currentUraniumValue;
	}

	/**
	 * @param currentUraniumValue the currentUraniumValue to set
	 */
	public void setCurrentUraniumValue(float currentUraniumValue)
	{
		this.currentUraniumValue = currentUraniumValue;
	}

	/**
	 * @return the currentThoriumValue
	 */
	public float getCurrentThoriumValue()
	{
		return currentThoriumValue;
	}

	/**
	 * @param currentThoriumValue the currentThoriumValue to set
	 */
	public void setCurrentThoriumValue(float currentThoriumValue)
	{
		this.currentThoriumValue = currentThoriumValue;
	}

	/**
	 * @return the currentPotassiumValue
	 */
	public float getCurrentPotassiumValue()
	{
		return currentPotassiumValue;
	}

	/**
	 * @param currentPotassiumValue the currentPotassiumValue to set
	 */
	public void setCurrentPotassiumValue(float currentPotassiumValue)
	{
		this.currentPotassiumValue = currentPotassiumValue;
	}

	/**
	 * @return the currentUserDefinedValue
	 */
	public float getCurrentUserDefinedValue()
	{
		return currentUserDefinedValue;
	}

	/**
	 * @param currentUserDefinedValue the currentUserDefinedValue to set
	 */
	public void setCurrentUserDefinedValue(float currentUserDefinedValue)
	{
		this.currentUserDefinedValue = currentUserDefinedValue;
	}
}
