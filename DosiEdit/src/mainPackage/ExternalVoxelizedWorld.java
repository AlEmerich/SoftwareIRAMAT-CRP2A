package mainPackage;

import java.io.IOException;

import javax.swing.JOptionPane;
import javax.swing.JProgressBar;

/**
 * This object represents the external object of the object. It is a {@link VoxelizedObject} with all values DosiVox need to performs. 
 * That object, if sub-voxelized detector is setted, has the {@link InternalVoxelizedWorld}.
 * @author alan
 * @see VoxelizedObject
 * @see InternalVoxelizedWorld
 */
public class ExternalVoxelizedWorld extends VoxelizedObject
{
    private static final long serialVersionUID = -5632026019745873403L;
    private VoxelizedObject InternalVoxelizedObject;
    private int indexDetectorDefinition;
    private int nbPrimaryUnit;
    private float clockValue;
    private int materialForMap;
    private boolean emissionFromGrain;
    private PrimaryParticles primaryParticle;
    private float momentumX;
    private float momentumY;
    private float momentumZ;
    private float cuttingValue;
    private int probeLocationX;
    private int probeLocationY;
    private int probeLocationZ;
    private int numberCells;
    private float probeDiameter;
    private float probeOffsetX;
    private float probeOffsetY;
    private float sizeOfPackingBoxX;
    private float sizeOfPackingBoxY;
    private float sizeOfPackingBoxZ;
    private float OffsetX;
    private float OffsetY;
    private float OffsetZ;
    private boolean probeIsUpdate;
    
    /**
     * The default constructor which initializes an empty voxelized object.
     */
    public ExternalVoxelizedWorld() {
        this.InternalVoxelizedObject = null;
        this.sizeOfPackingBoxX = 100.0f;
        this.sizeOfPackingBoxY = 100.0f;
        this.sizeOfPackingBoxZ = 100.0f;
        this.OffsetX = 50.0f;
        this.OffsetY = 50.0f;
        this.OffsetZ = 50.0f;
        this.probeIsUpdate = false;
        this.indexDetectorDefinition = 0;
        this.nbPrimaryUnit = 1;
        this.clockValue = 1;
        this.materialForMap = -1;
        this.emissionFromGrain = false;
        this.primaryParticle = PrimaryParticles.B\u00eata;
        this.momentumX = 0.0f;
        this.momentumY = 0.0f;
        this.momentumZ = 0.0f;
        this.cuttingValue = 0.001f;
        this.VoxelDimensionX = 1.0f;
        this.VoxelDimensionY = 1.0f;
        this.VoxelDimensionZ = 1.0f;
        this.DimensionUnit = "1mm";
        this.probeLocationX = 10;
        this.probeLocationY = 10;
        this.probeLocationZ = 10;
        this.numberCells = 100;
        this.probeDiameter = 5.0f;
        this.probeOffsetX = 50.0f;
        this.probeOffsetY = 50.0f;
        this.loadHashMap();
    }
    
    /**
     * Create an internal voxelized world with the specified number of voxel and dimensions.
     * @param nbX Number of voxel in the X axis.
     * @param nbY Number of voxel in the Y axis.
     * @param nbZ Number of voxel in the Z axis.
     * @param dimX Dimension of voxels in the X axis.
     * @param dimY Dimension of voxels in the Y axis.
     * @param dimZ Dimension of voxels in the Z axis.
     */
    public void createInternalWorldIntheSpecifiedVoxel(final int nbX, final int nbY, final int nbZ, final float dimX, final float dimY, final float dimZ) {
        this.InternalVoxelizedObject = new InternalVoxelizedWorld(nbX, nbY, nbZ, dimX, dimY, dimZ);
    }
    
    /**
     * Create an internal voxelized world from a 3D-scan loaded by {@link Loader3DScan}.
     * @param loader the loader whcih has read the file.
     * @param bar the window showing the progress of the creation of the world.
     */
    public void createInternalWorldIntheSpecifiedVoxel(final Loader3DScan loader, final JProgressBar bar,String nameGroup) {
        if (loader != null) {
            try {
                this.InternalVoxelizedObject = new InternalVoxelizedWorld(loader, bar,nameGroup);
            }
            catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "The file is not well written:\n" + e.getMessage(), "NumberFormatException", 0);
                e.printStackTrace();
            }
            catch (IOException e2) {
                JOptionPane.showMessageDialog(null, "Error during the reading of the file:\n" + e2.getMessage(), "IOExecptions", 0);
                e2.printStackTrace();
            }
        }
    }
    
    /**
     * @return the indexDetectorDefinition.
     */
    public int getIndexDetectorDefinition() {
        return this.indexDetectorDefinition;
    }
    
    /**
     * @param indexDetectorDefinition the indexDetectorDefinition to set.
     */
    public void setIndexDetectorDefinition(final int indexDetectorDefinition) {
        this.indexDetectorDefinition = indexDetectorDefinition;
    }
    
    /**
     * @return the nbPrimaryUnit.
     */
    public int getNbPrimaryUnit() {
        return this.nbPrimaryUnit;
    }
    
    /**
     * @param nbPrimaryUnit the nbPrimaryUnit to set.
     */
    public void setNbPrimaryUnit(final int nbPrimaryUnit) {
        this.nbPrimaryUnit = nbPrimaryUnit;
    }
    
    /**
     * @return the clockValue.
     */
    public float getClockValue() {
        return this.clockValue;
    }
    
    /**
     * @param f the clockValue to set.
     */
    public void setClockValue(final float f) {
        this.clockValue = f;
    }
    
    /**
     * @return the materialForMap.
     */
    public int getMaterialForMap() {
        return this.materialForMap;
    }
    
    /**
     * @param materialForMap the materialForMap to set.
     */
    public void setMaterialForMap(final int materialForMap) {
        this.materialForMap = materialForMap;
    }
    
    /**
     * @return the emissionFromGrain.
     */
    public boolean isEmissionFromGrain() {
        return this.emissionFromGrain;
    }
    
    public void setEmissionFromGrain(final boolean emissionFromGrain) {
        this.emissionFromGrain = emissionFromGrain;
    }
    
    public PrimaryParticles getPrimaryParticle() {
        return this.primaryParticle;
    }
    
    public void setPrimaryParticle(final PrimaryParticles primaryParticle) {
        this.primaryParticle = primaryParticle;
    }
    
    public float getMomentumX() {
        return this.momentumX;
    }
    
    public void setMomentumX(final float momentumX) {
        this.momentumX = momentumX;
    }
    
    public float getMomentumY() {
        return this.momentumY;
    }
    
    public void setMomentumY(final float momentumY) {
        this.momentumY = momentumY;
    }
    
    public float getMomentumZ() {
        return this.momentumZ;
    }
    
    public void setMomentumZ(final float momentumZ) {
        this.momentumZ = momentumZ;
    }
    
    public float getCuttingValue() {
        return this.cuttingValue;
    }
    
    public void setCuttingValue(final float cuttingValue) {
        this.cuttingValue = cuttingValue;
    }
    
    public int getProbeLocationX() {
        return this.probeLocationX;
    }
    
    public void setProbeLocationX(final int probeLocationX) {
        this.probeLocationX = probeLocationX;
        this.setProbeIsUpdate(false);
    }
    
    public int getProbeLocationY() {
        return this.probeLocationY;
    }
    
    public void setProbeLocationY(final int probeLocationY) {
        this.probeLocationY = probeLocationY;
        this.setProbeIsUpdate(false);
    }
    
    public int getProbeLocationZ() {
        return this.probeLocationZ;
    }
    
    public void setProbeLocationZ(final int probeLocationZ) {
        this.probeLocationZ = probeLocationZ;
        this.setProbeIsUpdate(false);
    }
    
    public int getNumberCells() {
        return this.numberCells;
    }
    
    public void setNumberCells(final int numberCells) {
        this.numberCells = numberCells;
        this.setProbeIsUpdate(false);
    }
    
    public float getProbeDiameter() {
        return this.probeDiameter;
    }
    
    public void setProbeDiameter(final float probeDiameter) {
        this.probeDiameter = probeDiameter;
        this.setProbeIsUpdate(false);
    }
    
    public float getProbeOffsetX() {
        return this.probeOffsetX;
    }
    
    public void setProbeOffsetX(final float probeOffsetX) {
        this.probeOffsetX = probeOffsetX;
        this.setProbeIsUpdate(false);
    }
    
    public float getProbeOffsetY() {
        return this.probeOffsetY;
    }
    
    public void setProbeOffsetY(final float f) {
        this.probeOffsetY = f;
        this.setProbeIsUpdate(false);
    }
    
    public float getSizeOfPackingBoxX() {
        return this.sizeOfPackingBoxX;
    }
    
    public void setSizeOfPackingBoxX(final float sizeOfPackingBoxX) {
        this.sizeOfPackingBoxX = sizeOfPackingBoxX;
    }
    
    public float getSizeOfPackingBoxY() {
        return this.sizeOfPackingBoxY;
    }
    
    public void setSizeOfPackingBoxY(final float sizeOfPackingBoxY) {
        this.sizeOfPackingBoxY = sizeOfPackingBoxY;
    }
    
    public float getSizeOfPackingBoxZ() {
        return this.sizeOfPackingBoxZ;
    }
    
    public void setSizeOfPackingBoxZ(final float sizeOfPackingBoxZ) {
        this.sizeOfPackingBoxZ = sizeOfPackingBoxZ;
    }
    
    public float getOffsetX() {
        return this.OffsetX;
    }
    
    public void setOffsetX(final float offsetX) {
        this.OffsetX = offsetX;
    }
    
    public float getOffsetY() {
        return this.OffsetY;
    }
    
    public void setOffsetY(final float offsetY) {
        this.OffsetY = offsetY;
    }
    
    public float getOffsetZ() {
        return this.OffsetZ;
    }
    
    public void setOffsetZ(final float offsetZ) {
        this.OffsetZ = offsetZ;
    }
    
    public int getIndexPrimaryParticle() {
        switch (this.primaryParticle) {
            case Alpha: {
                return 0;
            }
            case B\u00eata: {
                return 1;
            }
            case Gamma: {
                return 2;
            }
            default: {
                return -1;
            }
        }
    }
    
    @Override
    public String toString() {
        String str = "";
        str = String.valueOf(str) + "indexDetectorDefinition " + this.indexDetectorDefinition + "\n";
        str = String.valueOf(str) + "nbPrimaryUnit " + this.nbPrimaryUnit + "\n";
        str = String.valueOf(str) + "clockValue " + this.clockValue + "\n";
        str = String.valueOf(str) + "materialForMap " + this.materialForMap + "\n";
        str = String.valueOf(str) + "emissionFromGrain " + this.emissionFromGrain + "\n";
        str = String.valueOf(str) + "primaryParticle " + this.primaryParticle + "\n";
        str = String.valueOf(str) + "momentum " + this.momentumX + " " + this.momentumY + " " + this.momentumZ + "\n";
        str = String.valueOf(str) + "cutiingValue " + this.cuttingValue + "\n";
        str = String.valueOf(str) + "VoxelDimension " + this.VoxelDimensionX + " " + this.VoxelDimensionY + " " + this.VoxelDimensionZ + "\n";
        str = String.valueOf(str) + "DimensionUnit " + this.DimensionUnit + "\n";
        str = String.valueOf(str) + "probeLocation " + this.probeLocationX + " " + this.probeLocationY + " " + this.probeLocationZ + "\n";
        str = String.valueOf(str) + "numberCells " + this.numberCells + "\n";
        str = String.valueOf(str) + "probeDiameter " + this.probeDiameter + "\n";
        str = String.valueOf(str) + "probeOffset " + this.probeOffsetX + " " + this.probeOffsetY + "\n";
        str = String.valueOf(str) + "Object's size " + this.nbVoxelX + " " + this.nbVoxelY + " " + this.nbVoxelZ + "\n\n";
        for (int i = 0; i < this.nbVoxelZ; ++i) {
            for (int j = 0; j < this.nbVoxelY; ++j) {
                for (int k = 0; k < this.nbVoxelX; ++k) {
                    str = String.valueOf(str) + this.VoxelDepthContainer.get(i).get(j).get(k);
                }
                str = String.valueOf(str) + "\n";
            }
            str = String.valueOf(str) + "\n\n";
        }
        str = String.valueOf(str) + "Size of the packing box (%) " + this.sizeOfPackingBoxX + " " + this.sizeOfPackingBoxY + " " + this.sizeOfPackingBoxZ + "\n";
        str = String.valueOf(str) + "Offset of the voxel size (%) " + this.OffsetX + " " + this.OffsetY + " " + this.OffsetZ + "\n";
        return str;
    }
    
    public VoxelizedObject getInternalVoxelizedObject() {
        return this.InternalVoxelizedObject;
    }
    
    /**
     * Remove the internal voxelized world.
     */
    public void removeInternalObject() {
        this.InternalVoxelizedObject = null;
    }
    
    public void setProbeIsUpdate(final boolean probeIsUpdate) {
        this.probeIsUpdate = probeIsUpdate;
    }
    
    public boolean ProbeIsUpdate() {
        return this.probeIsUpdate;
    }
}
