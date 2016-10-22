package iramat.dosiseed.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

import mainPackage.Component;
import mainPackage.PeriodicTable;
import mainPackage.PrimaryParticles;

public abstract class AbstractModel extends Observable implements Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -108499193937900806L;

	/**
	 * Path to the pilot text file.
	 */
	protected String CURRENT_PATH;

	/**
	 * List of all components available in the project.
	 */
	protected List<Component> ListOfComponents;
	
	/**
	 * List of all materials available in the project.
	 */
	protected List<Material> ListOfMaterials;
	
	private ConfigRunModel config;
	
	/**
	 * Constructor.
	 */
	public AbstractModel()
	{
		this.init();
	}
	
	protected void init()
	{
		this.CURRENT_PATH = "";
		this.ListOfComponents = this.initializeListOfComponent();
		this.ListOfMaterials = new ArrayList<Material>();
		config = new ConfigRunModel();
		super.setChanged();
	}
	/******************************************************************************
	 * 			                     GETTER / SETTER
	 ******************************************************************************/
	
	/**
	 * @return the cURRENT_PATH
	 */
	public String getCURRENT_PATH() {
		return CURRENT_PATH; 
	}

	/**
	 * @param cURRENT_PATH the cURRENT_PATH to set
	 */
	public void setCURRENT_PATH(String cURRENT_PATH) {
		CURRENT_PATH = cURRENT_PATH;
		super.setChanged();
	}

	/**
	 * @return the listOfComponents
	 */
	public final List<Component> getListOfComponent() {
		return ListOfComponents;
	}

	/**
	 * @param comp the Component to add
	 */
	public void addComponent(Component comp) {
		ListOfComponents.add(comp);
		super.setChanged();
	}
	
	/**
	 * @return the listOfComponents
	 */
	public List<Material> getListOfMaterial() {
		return ListOfMaterials;
	}

	/**
	 * Add the material.
	 * @param mat the material to add.
	 */
	public void addMaterial(Material mat) {
		
		ListOfMaterials.add(mat);
		super.setChanged();
	}
	
	/**
	 * Remove the material.
	 * @param mat the material to remove.
	 */
	public void removeMaterial(Material mat)
	{
		ListOfMaterials.remove(mat);
		super.setChanged();
	}
	
	@Override
	public void setChanged()
	{
		super.setChanged();
	}
	
	/******************************************************************************
	 * 			                     ABSTRACT METHODS
	 ******************************************************************************/
	
	/**
	 * @return the number of particles emitted.
	 */
	public abstract float getNbParticlesEmitted();
	
	/**
	 * Set the number of particle emitted.
	 * @param nb the number to set.
	 */
	public abstract void setNbParticlesEmitted(float nb);
	
	/**
	 * @return the primary particle emitted.
	 */
	public abstract PrimaryParticles getPrimaryParticle();
	
	/**
	 * Set the primary particle which has to be emitted.
	 * @param pp the primary particle.
	 */
	public abstract void setPrimaryParticle(PrimaryParticles pp);
	
	/**
	 * @return a boolean array, booleans(K,U,Th,user_def).
	 */
	public abstract boolean[] getWhichRadioelementIsSimulated();
	
	/**
	 * Set which radio-active elements will be simulated.
	 * @param k true if potassium will be simulated, false if not.
	 * @param u true if uranium will be simulated, false if not.
	 * @param th true if thorium will be simulated, false if not.
	 * @param ud true if user-defined element will be simulated, false if not.
	 */
	public abstract void setWhichRadioelementIsSimulated(boolean k,boolean u,boolean th,boolean ud);
	
	/**
	 * Set Potassium simulated or not.
	 * @param k true if potassium will be simulated, false if not.
	 */
	public abstract void setPotassiumSimulated(boolean k);
	
	/**
	 * Set Uranium simulated or not.
	 * @param u true if uranium will be simulated, false if not.
	 */
	public abstract void setUraniumSimulated(boolean u);
	
	/**
	 * Set Thorium simulated or not.
	 * @param th true if thorium will be simulated, false if not.
	 */
	public abstract void setThoriumSimulated(boolean th);
	
	/**
	 * Set User-defined molecule simulated or not.
	 * @param ud true if user-defined molecule will be simulated, false if not.
	 */
	public abstract void setUserDefSimulated(boolean ud);
	
	/**
	 * @return the medium. 0=false=fixed medium, 1=true=infinite medium.
	 */
	public abstract boolean whichMedium();
	
	/**
	 * @param medium the medium to set.
	 */
	public abstract void setMedium(boolean medium); 
	
	/**
	 * @return particles max range in millimeter (for the calculation of the infinite medium).
	 */
	public abstract float getMaxRange();
	
	/**
	 * Set the the particle max range, in millimeters (for the calculation of the infinite medium).
	 * @param r the new max range to set.
	 */
	public abstract void setMaxRange(float r);
	
	/**
	 * @return production cut, in millimeter (should be less than the size of the smallest object.
	 */
	public abstract float getProductionCut();
	
	/**
	 * Set the production  cut, in millimeter (should be less than the size of the smallest object.
	 * @param cut the new cut to set.
	 */
	public abstract void setProductionCut(float cut);
	
	/**
	 * @return the wfInMatrix
	 */
	public abstract float getWfInMatrix();
	
	/**
	 * @param wfInMatrix the wfInMatrix to set
	 */
	public abstract void setWfInMatrix(float wfInMatrix);
	
	public abstract GrainFraction getCoarseFraction();
	
	public abstract GrainFraction getFineFraction();
	
	public abstract List<Material> getCompositionOfGrains();
	
	public abstract void reset();
	
	public abstract String toString();
	
	/******************************************************************************
	 * 			                     OTHER METHOD
	 ******************************************************************************/
	
	/**
     * Initialization of built-in and read-only components.
     * @return the list of built-in component.
     */
    private List<mainPackage.Component> initializeListOfComponent() {
        final List<mainPackage.Component> list = new ArrayList<mainPackage.Component>();
        mainPackage.Component c = new mainPackage.Component("Water", 1.0f, true);
        c.addAtomToFormula(PeriodicTable.H, 2);
        c.addAtomToFormula(PeriodicTable.O);
        list.add(c);
        c = new mainPackage.Component("Silicon dioxide", 2.65f, true);
        c.addAtomToFormula(PeriodicTable.Si);
        c.addAtomToFormula(PeriodicTable.O, 2);
        list.add(c);
        c = new mainPackage.Component("Alumina", 3.96f, true);
        c.addAtomToFormula(PeriodicTable.Al, 2);
        c.addAtomToFormula(PeriodicTable.O, 3);
        list.add(c);
        c = new mainPackage.Component("Calcium carbonate", 2.711f, true);
        c.addAtomToFormula(PeriodicTable.Ca);
        c.addAtomToFormula(PeriodicTable.C);
        c.addAtomToFormula(PeriodicTable.O, 3);
        list.add(c);
        c = new mainPackage.Component("Magnesium carbonate", 3.0f, true);
        c.addAtomToFormula(PeriodicTable.Mg);
        c.addAtomToFormula(PeriodicTable.C);
        c.addAtomToFormula(PeriodicTable.O, 3);
        list.add(c);
        c = new mainPackage.Component("Limestone", 2.7f, true);
        list.add(c);
        c = new mainPackage.Component("Zircon", 4.0f, true);
        c.addAtomToFormula(PeriodicTable.Zr);
        c.addAtomToFormula(PeriodicTable.Si);
        c.addAtomToFormula(PeriodicTable.O, 4);
        list.add(c);
        c = new mainPackage.Component("K-Feldspar", 2.6f, true);
        c.addAtomToFormula(PeriodicTable.K);
        c.addAtomToFormula(PeriodicTable.F);
        list.add(c);
        c = new mainPackage.Component("H_apatite", 3.8f, true);
        list.add(c);
        c = new mainPackage.Component("Iron(III) oxide", 5.242f, true);
        c.addAtomToFormula(PeriodicTable.Fe, 2);
        c.addAtomToFormula(PeriodicTable.O, 3);
        list.add(c);
        c = new mainPackage.Component("Iron(II) oxide", 5.745f, true);
        c.addAtomToFormula(PeriodicTable.Fe);
        c.addAtomToFormula(PeriodicTable.O);
        list.add(c);
        c = new mainPackage.Component("Magnesium oxide", 3.58f, true);
        c.addAtomToFormula(PeriodicTable.Mg);
        c.addAtomToFormula(PeriodicTable.O);
        list.add(c);
        c = new mainPackage.Component("Calcium oxide", 3.35f, true);
        c.addAtomToFormula(PeriodicTable.Ca);
        c.addAtomToFormula(PeriodicTable.O);
        list.add(c);
        c = new mainPackage.Component("Sodium oxide", 2.27f, true);
        c.addAtomToFormula(PeriodicTable.Na, 2);
        c.addAtomToFormula(PeriodicTable.O);
        list.add(c);
        c = new mainPackage.Component("Potassium oxide", 2.35f, true);
        c.addAtomToFormula(PeriodicTable.K, 2);
        c.addAtomToFormula(PeriodicTable.O);
        list.add(c);
        c = new mainPackage.Component("air", 0.00129f, true);
        list.add(c);
        c = new mainPackage.Component("vaccum", 0.0f, true);
        list.add(c);
        c = new mainPackage.Component("lead", 11.35f, true);
        list.add(c);
        return list;
    }

	/**
	 * @return the config
	 */
	public ConfigRunModel getConfig()
	{
		return config;
	}
}
