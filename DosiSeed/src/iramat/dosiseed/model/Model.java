package iramat.dosiseed.model;

import iramat.dosiseed.model.Incoherence.Inc;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import util.Couple;
import util.Vector3d;
import mainPackage.Component;
import mainPackage.PrimaryParticles;

public class Model extends AbstractModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3318665159094456990L;

	/**
	 * WF in the matrix, in percent.
	 */
	private float wfInMatrix;

	/**
	 * Coarse grain fraction description.
	 */
	private GrainFraction coarseFraction;

	/**
	 * Fine grain fraction description.
	 */
	private GrainFraction fineFraction;

	/**
	 * 0=false=fixed medium, 1=true=infinite medium.
	 */
	private boolean medium;

	/**
	 * Number of coarse fraction voxels in the detector by X,Y and Z axis.
	 */
	private Vector3d nbVoxelsInDetector;

	/********************************************************
	 * 				FIELDS FROM SUPERCLASS
	 ********************************************************/

	/**
	 * Number of thousand particles emitted.
	 */
	private float nbParticleEmitted;

	/**
	 * Type of primary particle emitted.
	 */
	private PrimaryParticles primaryParticle;

	/**
	 * radio-active elements simulated: booleans(K,U,Th,user_def).
	 */
	private boolean[] radioElementSim;

	/**
	 * Particles max range in millimeter (for the calculation of the infinite medium size).
	 */
	private float maxRange;

	/**
	 * Production cut, in millimeter (should be less than of the size of the smallest object).
	 */
	private float productionCut;

	/**
	 * Default constructor.
	 */
	public Model() {
		this.init();
	}

	protected void init()
	{
		super.init();
		wfInMatrix = 0;
		coarseFraction = new GrainFraction(true);
		fineFraction = new GrainFraction(false);
		medium = true;
		nbParticleEmitted = 1;
		primaryParticle = PrimaryParticles.Bêta;
		radioElementSim = new boolean[4];
		for(int i=0;i<4;i++)
			radioElementSim[i] = false;
		maxRange = 3;
		productionCut = 0.001f;
		nbVoxelsInDetector = new Vector3d(1,1,1);
		this.addTypeOfGrain(new Material("Matrix",0f));
		this.setChanged();
	}

	/**************************************************************************
	 * 						GETTER / SETTER
	 **************************************************************************/

	/**
	 * @return the nbOfGrainTypes
	 */
	public int getNbOfGrainTypes() {
		return this.ListOfMaterials.size();
	}

	/**
	 * @return the wfInMatrix
	 */
	public float getWfInMatrix() {
		return wfInMatrix;
	}

	/**
	 * @param wfInMatrix the wfInMatrix to set
	 */
	public void setWfInMatrix(float wfInMatrix) {
		this.wfInMatrix = wfInMatrix;
		this.setChanged();
	}

	/**
	 * @return the compositionOfGrains
	 */
	public List<Material> getCompositionOfGrains() {
		return Collections.unmodifiableList(ListOfMaterials);
	}

	/**
	 * @param material the material to add a grain type.
	 */
	public void addTypeOfGrain(Material material) {
		this.ListOfMaterials.add(material);
		if(this.ListOfMaterials.size()-1 == 0)
		{
			this.fineFraction.increaseTypeOfGrainNb();

			this.coarseFraction.setDistributionMaterial(this.coarseFraction.distributionMaterial()); //dumb call to trigger change
		}
		else
		{
			this.coarseFraction.increaseTypeOfGrainNb();
			this.fineFraction.increaseTypeOfGrainNb();
		}
		this.setChanged();
	}

	public void removeTypeOfGrain(int index)
	{
		this.ListOfMaterials.remove(index);
		this.coarseFraction.decreaseTypeOfGrainNb(index);
		this.fineFraction.decreaseTypeOfGrainNb(index);
		this.setChanged();
	}

	/**
	 * @return the medium
	 */
	public boolean whichMedium() {
		return medium;
	}

	/**
	 * @param medium the medium to set
	 */
	public void setMedium(boolean medium) {
		this.medium = medium;
		this.setChanged();
	}

	/**
	 * @return the coarseFraction
	 */
	public GrainFraction getCoarseFraction() {
		return coarseFraction;
	}

	/**
	 * @return the fineFraction
	 */
	public GrainFraction getFineFraction() {
		return fineFraction;
	}

	/**
	 * @return the nbVoxelsInDetector
	 */
	public Vector3d getNbVoxelsInDetector() {
		return nbVoxelsInDetector;
	}

	/**
	 * Set the number of voxels in detector.
	 * @param x the x coordinates.
	 * @param y the y coordinates.
	 * @param z the z coordinates.
	 */
	public void setNbVoxelsInDetector(double x,double y, double z) {
		this.nbVoxelsInDetector.setX(x);
		this.nbVoxelsInDetector.setY(y);
		this.nbVoxelsInDetector.setZ(z);

		this.setChanged();
	}

	/**
	 * Check if the model has valid value in it.
	 */
	public void isValid() throws Incoherence
	{
		//WF IN MATRIX
		if(this.wfInMatrix > 100)
			throw new Incoherence(Inc.WF_UP, "higher than 100.");

		//VOLUMIC FRACTION
		if(this.coarseFraction.getVolumicFraction() + this.fineFraction.getVolumicFraction() > 100)
			throw new Incoherence(Inc.VOLUMIC_FRACTION_UP, "sum of the coarse and fine volumic fraction is higher than 100.");

		//COARSE SIZE TAB
		float fraction = 0;
		List<Float> diameterCoarseList = new ArrayList<Float>();
		for(List<Float> size : this.coarseFraction.getDescOfEachSize())
		{
			float typeFraction = 0;
			
			fraction += size.get(1);
			for(int i=2;i<size.size();i++)
				typeFraction += size.get(i);
			if(diameterCoarseList.contains(size.get(0)))
				throw new Incoherence(Inc.COARSE_FRACTION_SIZE_TAB, "the size "+size.get(0)+" is set twice.");
			else
				diameterCoarseList.add(size.get(0));
			if(size.size() > 2 && typeFraction != 100)
				throw new Incoherence(Inc.COARSE_FRACTION_SIZE_TAB, "sum of types in size "+size.get(0)+" is not equal to 100 ("+typeFraction+")");
		}
		if(this.coarseFraction.getDescOfEachSize().size() > 0 && fraction != 100)
			throw new Incoherence(Inc.COARSE_FRACTION_SIZE_TAB, "sizes fraction is not equal to 100 ("+fraction+")");
					
		
		//FINE SIZE TAB
		fraction = 0;
		List<Float> diameterFineList = new ArrayList<Float>();
		for(List<Float> size : this.fineFraction.getDescOfEachSize())
		{
			float typeFraction = 0;
			fraction += size.get(1);
			for(int i=2;i<size.size();i++)
				typeFraction += size.get(i);
			if(diameterFineList.contains(size.get(0)))
				throw new Incoherence(Inc.FINE_FRACTION_SIZE_TAB, "the size "+size.get(0)+" is set twice.");
			else
				diameterFineList.add(size.get(0));
			if(size.size() > 2 && typeFraction != 100)
				throw new Incoherence(Inc.FINE_FRACTION_SIZE_TAB, "sum of types in size "+size.get(0)+" is not equal to 100 ("+typeFraction+")");
		}
		if(this.fineFraction.getDescOfEachSize().size() > 0 && fraction != 100)
			throw new Incoherence(Inc.FINE_FRACTION_SIZE_TAB, "sizes fraction is not equal to 100 ("+fraction+")");

		if(!diameterCoarseList.isEmpty() && !diameterFineList.isEmpty())
		{
			float maxF = Collections.max(diameterFineList);
			float minC = Collections.min(diameterCoarseList);
			if(minC <= maxF)
				throw new Incoherence(Inc.FINE_COARSE_SIZE, "the greatest of the fine fraction is higher than the smallest of the coarse fraction.");
		}
		
		//MATERIAL
		for(Material mat : this.getListOfMaterial())
		{
			if(mat.getListComponent().size() <= 0)
				throw new Incoherence(Inc.MATERIAL, mat.getName()+": fill with nothing. Delete it or fill it.");
			
			float totalC = 0;
			for(Couple<Component,Float> c : mat.getListComponent())
				totalC += c.getValeur2();
			if(totalC != 100)
				throw new Incoherence(Inc.MATERIAL, mat.getName()+" not exactly 100% filled.");
		}
	}

	/****************************************************************************
	 * 							OVERRIDDEN SUPERCLASS METHODS
	 ****************************************************************************/

	@Override
	public float getNbParticlesEmitted() {
		return nbParticleEmitted;
	}

	@Override
	public void setNbParticlesEmitted(float nb) {
		this.nbParticleEmitted = nb;
		this.setChanged();
	}

	@Override
	public PrimaryParticles getPrimaryParticle() {
		return this.primaryParticle;
	}

	@Override
	public void setPrimaryParticle(PrimaryParticles pp) {
		this.primaryParticle = pp; 
		this.setChanged();
	}

	@Override
	public boolean[] getWhichRadioelementIsSimulated() {
		return radioElementSim;
	}

	@Override
	public void setWhichRadioelementIsSimulated(boolean k, boolean u,
			boolean th, boolean ud) {
		this.radioElementSim[0] = k;
		this.radioElementSim[1] = u;
		this.radioElementSim[2] = th;
		this.radioElementSim[3] = ud;
		this.setChanged();
	}

	@Override
	public void setPotassiumSimulated(boolean k)
	{
		this.radioElementSim[0] = k;
		this.setChanged();
	}

	@Override
	public void setUraniumSimulated(boolean u)
	{
		this.radioElementSim[1] = u;
		this.setChanged();
	}

	@Override
	public void setThoriumSimulated(boolean th)
	{
		this.radioElementSim[2] = th;
		this.setChanged();
	}

	@Override
	public void setUserDefSimulated(boolean ud)
	{
		this.radioElementSim[3] = ud;
		this.setChanged();
	}

	@Override
	public float getMaxRange() {
		return this.maxRange;
	}

	@Override
	public void setMaxRange(float r) 
	{
		this.maxRange = r;
		this.setChanged();
	}

	@Override
	public float getProductionCut() 
	{
		return this.productionCut;
	}

	@Override
	public void setProductionCut(float cut)
	{
		this.productionCut = cut;
		this.setChanged();
	}

	@Override
	public void notifyObservers()
	{
		super.notifyObservers();
		this.coarseFraction.notifyObservers();
		this.fineFraction.notifyObservers();
	}

	@Override
	public void setChanged()
	{
		super.setChanged();
		this.coarseFraction.setChanged();
		this.fineFraction.setChanged();
	}


	@Override
	public void reset()
	{
		this.init();
	}

	@Override
	public String toString()
	{

		String str = "***************** MODEL ******************\n" +
				"Number of grains type : "+this.ListOfMaterials.size()+"\n"+
				"WF in matrix : "+wfInMatrix+"\n"+
				"grain composition : \n";
		for(int i=0;i<this.ListOfMaterials.size();i++)
		{
			if(i==0)
				str+="Matrix ";
			else
				str+="Type "+i+" ";
			str += this.ListOfMaterials.get(i)+"\n";
		}
		str += "Number of coarse fraction voxel: "+nbVoxelsInDetector+"\n";
		str += "Coarse fraction description:\n"+coarseFraction+"\nFine fraction description :\n "+fineFraction+"\n";
		str += "Medium : "+this.medium+"\n";
		str += "Number of thousand particles emitted : "+this.nbParticleEmitted+"\n";
		str += "Primary particle emitted : "+this.primaryParticle+"\n";
		str += "Radio elements simulated : "+this.radioElementSim[0]+" "+this.radioElementSim[1]+" "+
				this.radioElementSim[2]+" "+this.radioElementSim[3]+"\n";
		str += "Max range : "+this.maxRange+"\n";
		str += "Production cut : "+this.productionCut+"\n";
		return str+"******************************************\n";
	}

}
