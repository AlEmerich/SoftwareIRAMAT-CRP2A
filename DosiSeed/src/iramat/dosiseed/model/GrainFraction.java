package iramat.dosiseed.model;

import iramat.dosiseed.view.GrainFractionPanel;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Observable;


public class GrainFraction extends Observable implements Serializable
{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -6668167775406254268L;

	/**
	 * Voxel size, in millimeter.
	 */
	protected float voxelSize;
	
	/**
	 * Distribution of material in the grain.
	 * (0=false=random grain composition in each voxel, 1=true=recurrent grain composition in every voxels).
	 */
	protected boolean distributionMaterial;
	
	/**
	 * Volumic fraction of the grains.
	 * For coarse fraction, on the total volume of sample.
	 * For fine fraction, without the matrix volumic fraction and on the total volume sample.
	 */
	protected float volumicFraction;
	
	/**
	 * 2 dimensions array for description of each size of grains and type.
	 * 
	 * 	D	f  	T1   T2	T3	T4
	 *	2.	100	0	100	0	0
	 */
	protected List<List<Float>> descOfEachSize;
	
	/**
	 * 2 dimensions array for description of each radio-element and type of grains.
	 * 
	 * 	T1	T2	T3	T4 
	 *	0	0	0	0	# % of K in Mtx=matrix and Tn=Type n grain
	 *	1	0	0	0	# ppm of U series in Mtx=matrix and Tn=Type n grain 
	 *	0	0 	0	0	# ppm of Th series in Mtx=matrix and Tn=Type n grain
	 *	0	0	0	0	# ppm of user defined radio-element in Mtx=matrix and Tn=Type n grain
	 */
	protected List<List<Float>> descOfEachType;

	/**
	 * The global controler set it when he add grain to the model. By this way, this information will be
	 * known in the {@link GrainFractionPanel#update(java.util.Observable, Object)}
	 */
	private int nbTypeGrain;
	
	private boolean isCoarse;
	
	/**
	 * Default constructor.
	 */
	public GrainFraction(boolean coarse)
	{
		if(coarse)
			voxelSize = 10;
		else
			voxelSize = 1;
		nbTypeGrain = 0;
		
		distributionMaterial = false;
		volumicFraction = 0;
		descOfEachSize = new ArrayList<List<Float>>();
		descOfEachType= new ArrayList<List<Float>>();
		isCoarse = coarse;
		
		super.setChanged();
	}
	
	/**************************************************************************
	 * 							GETTER / SETTER
	 **************************************************************************/
	
	/**
	 * @return the voxelSize
	 */
	public float getVoxelSize() {
		return voxelSize;
	}

	/**
	 * @param voxelSize the voxelSize to set
	 */
	public void setVoxelSize(float voxelSize) {
		this.voxelSize = voxelSize;
		
		super.setChanged();
	}

	/**
	 * @return the distributionMaterial
	 */
	public boolean distributionMaterial() {
		return distributionMaterial;
	}

	/**
	 * @param distributionMaterial the distributionMaterial to set
	 */
	public void setDistributionMaterial(boolean distributionMaterial) {
		this.distributionMaterial = distributionMaterial;
		
		super.setChanged();
	}

	/**
	 * @return the volumicFraction
	 */
	public float getVolumicFraction() {
		return volumicFraction;
	}

	/**
	 * @param volumicFraction the volumicFraction to set
	 */
	public void setVolumicFraction(float volumicFraction) {
		this.volumicFraction = volumicFraction;
		
		super.setChanged();
	}

	/**
	 * @return the nbTypeGrain
	 */
	public int getNbTypeGrain()
	{
		return nbTypeGrain;
	}

	public void increaseTypeOfGrainNb()
	{
		for(List<Float> size : this.descOfEachSize)
			size.add(0f);
		
		List<Float> list = new ArrayList<Float>(4);
			for(int i=0; i<4;i++)
				list.add(0f);
			this.descOfEachType.add(list);
		
		
		this.nbTypeGrain++;
		super.setChanged();
	}
	
	public void decreaseTypeOfGrainNb(int index)
	{
		if(this.nbTypeGrain != 0)
		{
			for(List<Float> size : this.descOfEachSize)
				size.remove(index+1);
			this.nbTypeGrain--;		
		}
		
		if(isCoarse)
		{
			if(index == 0 && !this.descOfEachType.isEmpty())
				this.descOfEachType.remove(index);
			else
				this.descOfEachType.remove(index - 1);
		}
		else
			this.descOfEachType.remove(index);
		super.setChanged();
	}

	/**
	 * DESCRIPTION LIST METHOD
	 */
	
	/**
	 * @return the descriptionGrain
	 */
	public List<List<Float>> getDescOfEachSize() {
		return Collections.unmodifiableList(descOfEachSize);
	}

	public void addSizeOfGrain()
	{
		List<Float> list = createListWithZeroFloatValue(
				this.nbTypeGrain+2-(isCoarse ? 0 : 1)); // 2 for diameter and fraction + type N - matrix
		this.descOfEachSize.add(list);
		super.setChanged();
	}

	public void removeSizeOfGrain(int index)
	{
		if(index >= 0 && index < this.descOfEachSize.size())
			this.descOfEachSize.remove(index);
		
		super.setChanged();
	}
	
	public float getValueOfSize(int numRow,int numCol)
	{
		if(this.descOfEachSize.get(numRow) != null)
			if(this.descOfEachSize.get(numRow).get(numCol) != null)
				return this.descOfEachSize.get(numRow).get(numCol);
		return -1;
	}
	
	/**
	 * @param numRow the number of the row.
	 * @param numCol the number of the column.
	 * @param value the value to set.
	 */
	public void setValueOfSize(int numRow,int numCol,float value) {
		if(this.descOfEachSize.get(numRow) != null)
			if(this.descOfEachSize.get(numRow).get(numCol) != null)
				this.descOfEachSize.get(numRow).set(numCol,value);
		super.setChanged();
	}

	/**
	 * @return the descriptionFraction
	 */
	public List<List<Float>> getDescOfEachType() {
		return Collections.unmodifiableList(descOfEachType);
	}

	public float getValueOfType(int numRow,int numCol)
	{
		if(this.descOfEachType.get(numCol) != null)
			if(this.descOfEachType.get(numCol).get(numRow) != null)
				return this.descOfEachType.get(numCol).get(numRow);
		return -1;
	}
	
	/**
	 * @param numRow the number of the row.
	 * @param numCol the number of the column.
	 * @param value the value to set.
	 */
	public void setValueOfType(int numRow,int numCol,float value) {
		if(this.descOfEachType.get(numCol) != null)
			if(this.descOfEachType.get(numCol).get(numRow) != null)
				this.descOfEachType.get(numCol).set(numRow,value);
		super.setChanged();
	}
	
	public static List<Float> createListWithZeroFloatValue(int size)
	{
		List<Float> list = new ArrayList<Float>(size);
		for(int i = 0;i<size;i++)
			list.add(0f);
		return list;
	}
	
	@Override
	public void setChanged()
	{
		super.setChanged();
	}
	
	public String toString()
	{
		String str = "\tVoxel size: "+voxelSize+"\n";
		str += "\tDistribution material (false=random,true=recurent): "+distributionMaterial+"\n";
		str += "\tVolumic fraction: "+volumicFraction+"\n";
		str += "\tDescription of each size of grain: "+descOfEachSize+"\n";
		str += "\tDescription of each type: "+descOfEachType+"\n";
		return str;
	}
}
