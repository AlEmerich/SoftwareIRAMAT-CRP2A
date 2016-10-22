package iramat.dosiseed.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import mainPackage.Component;
import util.Couple;

public class Material implements Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -1779820612452325917L;

	/**
	 * Name of the material.
	 */
	private String name;
	
	/**
	 * Which density to use.
	 */
	private boolean useCalculated = true;
	/**
	 * Density of the material.
	 */
	private float density;
	
	/**
	 * Calculated density of the material.
	 */
	private float calculatedDensity;
	
	/**
	 * Composition of the material. 
	 * It is a list of couple made by the index of the component and its mass fraction.
	 */
	private List<Couple<Component,Float>> listComponentFraction;

	private float totalMass;
	
	private float RefUraniumValue;
	
    private float RefThoriumValue;
    
    private float RefPotassiumValue;
    
    private float RefUserDefinedValue;
    
    private float currentUraniumValue;
    
    private float currentThoriumValue;
    
    private float currentPotassiumValue;
    
    private float currentUserDefinedValue;
    
    private boolean used;
    
    private float waterFraction;
    
	/**
	 * Default constructor.
	 */
	public Material()
	{
		name = "";
		setDensity(0f);
		totalMass = 0;
		this.RefUraniumValue = 0.0f;
        this.RefThoriumValue = 0.0f;
        this.RefPotassiumValue = 0.0f;
        this.RefUserDefinedValue = 0.0f;
        this.currentUraniumValue = 0.0f;
        this.currentThoriumValue = 0.0f;
        this.currentPotassiumValue = 0.0f;
        this.currentUserDefinedValue = 0.0f;
        this.setWaterFraction(0.0f);
        this.used = false;
		listComponentFraction = new ArrayList<Couple<Component, Float>>();
	}
	
	/**
	 * Constructor.
	 * @param name the name to set.
	 * @param dens the dens to set.
	 */
	public Material(String name,float dens)
	{
		this.name = name;
		this.setDensity(dens);
		this.RefUraniumValue = 0.0f;
        this.RefThoriumValue = 0.0f;
        this.RefPotassiumValue = 0.0f;
        this.RefUserDefinedValue = 0.0f;
        this.currentUraniumValue = 0.0f;
        this.currentThoriumValue = 0.0f;
        this.currentPotassiumValue = 0.0f;
        this.currentUserDefinedValue = 0.0f;
        this.setWaterFraction(0.0f);
        this.used = false;
		listComponentFraction = new ArrayList<Couple<Component, Float>>();
	}

	/**************************************************************************
	 * 							GETTER / SETTER
	 **************************************************************************/
	
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the useCalculated
	 */
	public boolean isUseCalculated()
	{
		return useCalculated;
	}

	/**
	 * @param useCalculated the useCalculated to set
	 */
	public void setUseCalculated(boolean useCalculated)
	{
		this.useCalculated = useCalculated;
	}

	public float getUsedDensity()
	{
		if(this.useCalculated)
			return this.calculatedDensity;
		return this.density;
	}
	/**
	 * @return the density
	 */
	public float getDensity() {
		return density;
	}

	/**
	 * @param density the density to set
	 */
	public void setDensity(float density) {
		this.density = density;
	}
	
	public void setCalculatedDensity(float calDens)
	{
		this.calculatedDensity = calDens;
	}
	
	public float getCalculatedDensity()
	{
		return calculatedDensity;
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
    
    public void setUsed(final boolean used) {
        this.used = used;
    }
    
    public boolean isUsed() {
        return this.used;
    }
    
	
	/*********************************************************************************
	 *							OTHER METHODS 
	 *********************************************************************************/
	
	/**
	 * Add a component to the composition of the material.
	 * @param comp the component to add.
	 * @param dens the density of the component within the material.
	 */
	public void addComponent(Component comp,float dens)
	{
		listComponentFraction.add(new Couple<Component, Float>(comp,dens));
	}
	
	/**
	 * Remove a component from the material.
	 * @param c the component to remove..
	 */
	public void removeComponent(Component c)
	{
		for(int i=0;i<this.listComponentFraction.size();i++)
		{
			Couple<Component,Float> couple = this.listComponentFraction.get(i);
			if(couple.getValeur1() == c)
				this.listComponentFraction.remove(couple);
		}	
	}
	
	/**
	 * Change the density of the component pointed by the index.
	 * @param index the index of the component to change the density.
	 * @param dens the new density to set.
	 */
	public void setDensityToComponent(int index, float dens)
	{
		this.listComponentFraction.get(index).setValeur2(dens);
	}
	
	public List<Couple<Component,Float>> getListComponent()
	{
		return this.listComponentFraction;
	}
	
	public float getTotalMass()
	{
		return this.totalMass;
	}
	
	public void setTotalMass(float calculatedMass)
	{
		this.totalMass = calculatedMass;
	}
	
	/**
	 * @return the waterFraction
	 */
	public float getWaterFraction()
	{
		return waterFraction;
	}

	/**
	 * @param waterFraction the waterFraction to set
	 */
	public void setWaterFraction(float waterFraction)
	{
		this.waterFraction = waterFraction;
	}

	public String toString(List<Component> list)
	{
		String str = this.name+" "+(this.useCalculated ? this.calculatedDensity : this.density)+" "
				+this.listComponentFraction.size()+" ";
		for(Couple<Component,Float> couple : this.listComponentFraction)
			str += list.indexOf(couple.getValeur1())+" "+couple.getValeur2()+" ";
		return str;
	}
	
	public String toString()
	{
		String str = this.name+" "+(this.useCalculated ? this.calculatedDensity : this.density)+" "
				+this.listComponentFraction.size()+" ";
		for(Couple<Component,Float> couple : this.listComponentFraction)
			str += couple.getValeur1()+" "+couple.getValeur2()+" ";
		return str;
	}

	
}
