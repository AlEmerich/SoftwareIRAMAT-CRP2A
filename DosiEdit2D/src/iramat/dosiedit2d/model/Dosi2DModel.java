package iramat.dosiedit2d.model;

import iramat.dosiseed.model.AbstractModel;
import iramat.dosiseed.model.ColoredMaterial;
import iramat.dosiseed.model.GrainFraction;
import iramat.dosiseed.model.Incoherence;
import iramat.dosiseed.model.Incoherence.Inc;
import iramat.dosiseed.model.Material;

import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import util.Couple;

import mainPackage.Component;
import mainPackage.PrimaryParticles;

public class Dosi2DModel extends AbstractModel
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 8756499375286815932L;

	private HashMap<Integer,HashMap<Integer,VoxelObject>> grid;

	private float voxelDimensionX,voxelDimensionY;
	
	private float realDimX,realDimY;

	private float clockValue;

	private int nbThread;

	private String emissionProcess;

	private Material materialForDoseMapping;

	private boolean excludeEdge;

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
	 * Use for average range in that class.
	 */
	private float maxRange;

	/**
	 * Production cut, in millimeter (should be less than of the size of the smallest object).
	 */
	private float productionCut;

	/**
	 * Constructor.
	 */
	public Dosi2DModel()
	{
		this.grid = new HashMap<>();
		this.voxelDimensionX = 1.0f;
		this.voxelDimensionY = 1.0f;
		this.setRealDimX(1.0f);
		this.setRealDimY(1.0f);
		this.clockValue = 1;
		this.emissionProcess = "2D std";
		this.materialForDoseMapping = null;
		this.nbParticleEmitted = 1;
		this.primaryParticle = PrimaryParticles.Bêta;
		this.radioElementSim = new boolean[4];
		for(int i=0;i<4;i++)
			radioElementSim[i] = false;
		this.maxRange = 3;
		this.productionCut = 0.001f;
		this.nbThread = 1;
		for(int y=0;y<20;y++)
		{
			HashMap<Integer,VoxelObject> row = new HashMap<>();
			for(int x=0;x<20;x++)
				row.put(x,new VoxelObject(x * this.voxelDimensionX, y * this.voxelDimensionY, 
						this.voxelDimensionX, this.voxelDimensionY));
			grid.put(y, row);
		}
		this.setExcludeEdge(true);
		this.setChanged();
	}

	/**
	 * @return the grid
	 */
	public HashMap<Integer, HashMap<Integer, VoxelObject>> getGrid()
	{
		return grid;
	}

	/**
	 * @return the clockValue
	 */
	public float getClockValue()
	{
		return clockValue;
	}

	/**
	 * @param clockValue the clockValue to set
	 */
	public void setClockValue(float clockValue)
	{
		this.clockValue = clockValue;
		this.setChanged();
	}

	/**
	 * @return the nbThread
	 */
	public int getNbThread()
	{
		return nbThread;
	}

	/**
	 * @param nbThread the nbThread to set
	 */
	public void setNbThread(int nbThread)
	{
		this.nbThread = nbThread;
		this.setChanged();
	}

	/**
	 * @return the emissionProcess
	 */
	public String getEmissionProcess()
	{
		return emissionProcess;
	}

	/**
	 * @param emissionProcess the emissionProcess to set
	 */
	public void setEmissionProcess(String emissionProcess)
	{
		this.emissionProcess = emissionProcess;
		this.setChanged();
	}

	/**
	 * @return the materialForDoseMapping
	 */
	public Material getMaterialForDoseMapping()
	{
		return materialForDoseMapping;
	}

	/**
	 * @param materialForDoseMapping the materialForDoseMapping to set
	 */
	public void setMaterialForDoseMapping(Material materialForDoseMapping)
	{
		this.materialForDoseMapping = materialForDoseMapping;
		this.setChanged();
	}

	/**
	 * @return the voxelDimensionX
	 */
	public float getVoxelDimensionX()
	{
		return voxelDimensionX;
	}

	/**
	 * @param voxelDimensionX the voxelDimensionX to set
	 */
	public void setVoxelDimensionX(float voxelDimensionX)
	{
		this.voxelDimensionX = voxelDimensionX;
		this.setChanged();
	}

	/**
	 * @return the voxelDimensionY
	 */
	public float getVoxelDimensionY()
	{
		return voxelDimensionY;
	}

	/**
	 * @param voxelDimensionY the voxelDimensionY to set
	 */
	public void setVoxelDimensionY(float voxelDimensionY)
	{
		this.voxelDimensionY = voxelDimensionY;
		this.setChanged();
	}

	/**
	 * @return the realDimX
	 */
	public float getRealDimX()
	{
		return realDimX;
	}

	/**
	 * @param realDimX the realDimX to set
	 */
	public void setRealDimX(float realDimX)
	{
		this.realDimX = realDimX;
		this.setChanged();
	}

	/**
	 * @return the realDimY
	 */
	public float getRealDimY()
	{
		return realDimY;
	}

	/**
	 * @param realDimY the realDimY to set
	 */
	public void setRealDimY(float realDimY)
	{
		this.realDimY = realDimY;
		this.setChanged();
	}

	/**
	 * @return the excludeEdge
	 */
	public boolean isExcludeEdge()
	{
		return excludeEdge;
	}

	/**
	 * @param excludeEdge the excludeEdge to set
	 */
	public void setExcludeEdge(boolean excludeEdge)
	{
		this.excludeEdge = excludeEdge;
		if(this.excludeEdge)
			this.performExclusion();
		this.setChanged();
	}

	private void performExclusion()
	{
		float widthSample = this.realDimX * this.grid.get(0).size();
		float heightSample = this.realDimY * this.grid.size();
		float PRange = this.maxRange;
		for(int y=0;y<this.grid.size();y++)
			for(int x=0;x<this.grid.get(y).size();x++)
			{
				VoxelObject vox = this.grid.get(y).get(x);
				vox.setExcluded(false);
				if(((1.*x)*this.realDimX<=PRange)
						||(y*this.realDimY<=PRange)
						||(widthSample-(1.*x+1)*this.realDimX<=PRange)
						||(heightSample-(1.*y+1)*this.realDimY<=PRange))
					vox.setExcluded(true);
			}
	}

	/**
	 * Method set to be called by a 2D representation, to check if the MouseX and MouseY parameters is in the voxel.
	 * @param MouseX the X coordinate of the point to check.
	 * @param MouseY the Y coordinate of the point to check.
	 * @param matToFillWith if the mouse is in a voxel, the voxel will be filled by this material.
	 * @return the list of voxel which has been changed
	 */
	public ArrayList<Integer> checkAndFill(final double MouseX, final double MouseY,final ColoredMaterial matToFillWith) {
		if (matToFillWith == null) {
			return null;
		}
		final ArrayList<Integer> list = new ArrayList<Integer>();

		for (int y = 0; y < this.grid.size(); ++y) {
			for (int x = 0; x < this.grid.get(y).size(); ++x) {
				final VoxelObject vox = this.grid.get(y).get(x);
				if (vox.contains(MouseX, MouseY)) {
					if (vox.getMaterial() != null) {
						list.add(x);
						list.add(y);
					}
					vox.setMaterialIndex(matToFillWith);
				}
			}
		}
		this.setChanged();
		return list;
	}

	/**
	 * Method set to be called by a 2D representation, to check if the rectangle parameters is in the voxel.
	 * @param selectRect the rect to check with.
	 * @param matToFillWith if the mouse is in a voxel, the voxel will be filled by this material.
	 * @return the list of voxel which has been changed
	 */
	public ArrayList<Integer> checkAndFill(final Rectangle2D.Float selectRect, final ColoredMaterial matToFillWith) {
		if (matToFillWith == null) {
			return null;
		}
		final ArrayList<Integer> list = new ArrayList<Integer>();

		for (int y = 0; y < this.grid.size(); ++y) {
			for (int x = 0; x < this.grid.get(y).size(); ++x) {
				final VoxelObject vox = this.grid.get(y).get(x);
				if (selectRect.intersects(vox)) {
					if (vox.getMaterial() != null) {
						list.add(x);
						list.add(y);
					}
					vox.setMaterialIndex(matToFillWith);
				}
			}
		}
		this.setChanged();
		return list;
	}

	/**
	 * Reload default radiation value.
	 * @param m the material to reload radiation value.
	 */
	public void reloadReferenceRadiationValue(final Material m) {
		for (int y = 0; y < this.grid.size(); ++y) {
			for (int x = 0; x < this.grid.get(y).size(); ++x) {
				final VoxelObject v = this.grid.get(y).get(x);
				if (!v.isEmpty() && v.getMaterial().equals(m)) {
					v.setUranium(v.getMaterial().getRefUraniumValue());
					v.setThorium(v.getMaterial().getRefThoriumValue());
					v.setPotassium(v.getMaterial().getRefPotassiumValue());
					v.setDefinedRadMolecule(v.getMaterial().getRefUserDefinedValue());
				}
			}
		}
		this.setChanged();
	}

	/**
	 * Resize the grid i terms of number of voxel. After resizing, the calculation of which voxel
	 * is excluded according to average range is performed. 
	 * @param newWidth the new width to set.
	 * @param newHeight the new height to set.
	 * @see Dosi2DModel#performExclusion()
	 */
	public void resizeWorld(int newWidth, int newHeight)
	{
		int nbX = this.grid.get(0).size();
		int nbY = this.grid.size();

		if(nbY != newHeight)
			this.resizeHeight(nbY,newHeight);

		if(nbX != newWidth)
			this.resizeWidth(nbX,newWidth);

		this.performExclusion();
		this.setChanged();
	}

	/**
	 * Method used by {@link #resizeWorld(int, int)} to resize height.
	 * @param old the old height.
	 * @param n the new height.
	 */
	private void resizeHeight(int old,int n)
	{
		if(old < n)
		{
			for(int y=old;y<n;y++)
			{
				HashMap<Integer, VoxelObject> row = new HashMap<>();
				for(int x=0;x<this.grid.get(0).size();x++)
					row.put(x, new VoxelObject(x * this.voxelDimensionX, y * this.voxelDimensionY, 
							this.voxelDimensionX, this.voxelDimensionY));
				this.grid.put(y, row);
			}
		}
		else
		{
			for(int y=n;y<old;y++)
			{
				this.grid.remove(y);
			}
		}
	}

	/**
	 * Method used by {@link #resizeWorld(int, int)} to resize width.
	 * @param old the old width.
	 * @param n the new width.
	 */
	private void resizeWidth(int old,int n)
	{
		if(old < n)
		{
			for(int y=0;y<this.grid.size();y++)
			{
				for(int x=old;x<n;x++)
					this.grid.get(y).put(x, new VoxelObject(x * this.voxelDimensionX, y * this.voxelDimensionY, 
							this.voxelDimensionX, this.voxelDimensionY));
			}
		}
		else
		{
			for(int y=0;y<this.grid.size();y++)
			{
				for(int x=n;x<old;x++)
					this.grid.get(y).remove(x);
			}
		}
	}

	/**
	 * Resize voxel dimensions. After resizing, the calculation of which voxel
	 * is excluded according to average range is performed. 
	 * @param width the new width to set.
	 * @param height the height to set.
	 */
	public void resizeVoxels(float width,float height)
	{
		int nbX = this.grid.get(0).size();
		int nbY = this.grid.size();
		for(int y=0;y<nbY;y++)
			for(int x=0;x<nbX;x++)
			{
				VoxelObject vox = this.grid.get(y).get(x);
				vox.x = ((float) x) * width * VoxelObject.FACTOR_DISPLAY_GRID;;
				vox.y = ((float) y) * height * VoxelObject.FACTOR_DISPLAY_GRID;;
				vox.width = width * VoxelObject.FACTOR_DISPLAY_GRID;
				vox.height = height * VoxelObject.FACTOR_DISPLAY_GRID;
			}
		this.voxelDimensionX = width;
		this.voxelDimensionY = height;
		
		this.performExclusion();
		this.setChanged();
	}

	@Override
	public void removeMaterial(Material mat)
	{
		ListOfMaterials.remove(mat);
		for (int y = 0; y < this.grid.size(); ++y) {
			for (int x = 0; x < this.grid.get(y).size(); ++x) {
				final VoxelObject vox = this.grid.get(y).get(x);

				if(vox.getMaterial() == ((ColoredMaterial) mat) )
					vox.setMaterialIndex(null);
			}
		}
		this.setChanged();
	}

	@Override
	public float getNbParticlesEmitted()
	{
		return this.nbParticleEmitted;
	}

	@Override
	public void setNbParticlesEmitted(float nb)
	{
		this.nbParticleEmitted = nb;
		this.setChanged();
	}

	@Override
	public PrimaryParticles getPrimaryParticle()
	{
		return this.primaryParticle;
	}

	@Override
	public void setPrimaryParticle(PrimaryParticles pp)
	{
		this.primaryParticle = pp;
		this.setChanged();
	}

	@Override
	public boolean[] getWhichRadioelementIsSimulated()
	{
		return this.radioElementSim;
	}

	@Override
	public void setWhichRadioelementIsSimulated(boolean k, boolean u,
			boolean th, boolean ud)
	{
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
	public boolean whichMedium()
	{
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void setMedium(boolean medium)
	{
		// TODO Auto-generated method stub

	}

	/**
	 * Use for average range in that class.
	 */
	@Override
	public float getMaxRange()
	{
		return this.maxRange;
	}

	/**
	 * Use for average range in that class.
	 */
	@Override
	public void setMaxRange(float r)
	{
		this.maxRange = r;
		this.performExclusion();
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
	public float getWfInMatrix()
	{
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void setWfInMatrix(float wfInMatrix)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public GrainFraction getCoarseFraction()
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public GrainFraction getFineFraction()
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Material> getCompositionOfGrains()
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void reset()
	{
		// TODO Auto-generated method stub

	}

	@Override
	public String toString()
	{
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * Check if the model is valid for the generation of PTF file. The method is also triggered when the 
	 * tab changing, to prevent eventual null pointer or other runtime exception.
	 * @throws Incoherence the exception when the model is inconsistent.
	 */
	public void isValid() throws Incoherence
	{
		for(int y=0;y<this.grid.size();y++)
			for(int x=0;x<this.grid.get(y).size();x++)
				if(this.grid.get(y).get(x).isEmpty())
					throw new Incoherence(Inc.WORLD, "One or several voxels is empty. First found at x:"+x+" y:"+y );

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

	/**
	 * Load a gray shade image text file into the object. Each pixel become a voxel.
	 * @param image the image to load.
	 * @param associations a list of couple which handles associations between
	 * 		gray shade value in the image and material.
	 */
	public void loadImage(BufferedImage image,ArrayList<Couple<Material, Integer>> associations)
	{
		HashMap<Integer,HashMap<Integer,VoxelObject>> loaded = new HashMap<Integer,HashMap<Integer,VoxelObject>>();
		for(int y=0;y<image.getHeight();y++)
		{
			HashMap<Integer,VoxelObject> lines = new HashMap<Integer,VoxelObject>();
			for(int x=0;x<image.getWidth();x++)
			{
				Material m = null;
				WritableRaster raster = image.getRaster();
				int[] iArray = new int[1];
				raster.getPixel(x, y, iArray);
				for(Couple<Material,Integer> c : associations)
					if(iArray[0] == c.getValeur2())
						m = c.getValeur1();

				lines.put(x,new VoxelObject(x * this.voxelDimensionX, y * this.voxelDimensionY, 
						this.voxelDimensionX, this.voxelDimensionY,(ColoredMaterial) m));
			}
			loaded.put(y, lines);
		}
		this.grid = loaded;
		
		this.performExclusion();
		this.setChanged();
	}
}
