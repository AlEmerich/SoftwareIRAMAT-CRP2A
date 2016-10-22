package mainPackage;

import java.io.IOException;
import java.util.HashMap;

import javax.swing.JProgressBar;

/**
 * This object represents the internal voxelized world. 
 * It may has been created manually with specified number and dimensions of voxels, or with a 3D-scan.
 * @author alan
 * @see Loader3DScan
 */
public class InternalVoxelizedWorld extends VoxelizedObject
{
    private static final long serialVersionUID = -6407261061228232430L;
    
    /**
     * Create a new empty internal voxelized world with the specified attribute.
     * @param nbX the number of voxel in the X-axis.
     * @param nbY the number of voxel in the Y-axis.
     * @param nbZ the number of voxel in the Z-axis.
     * @param dimX the dimension of voxel in the X-axis.
     * @param dimY the dimension of voxel in the Y-axis.
     * @param dimZ the dimension of voxel in the Z-axis.
     */
    public InternalVoxelizedWorld(final int nbX, final int nbY, final int nbZ, final float dimX, final float dimY, final float dimZ) {
        this.nbVoxelX = nbX;
        this.nbVoxelY = nbY;
        this.nbVoxelZ = nbZ;
        this.VoxelDimensionX = dimX;
        this.VoxelDimensionY = dimY;
        this.VoxelDimensionZ = dimZ;
        this.VoxelDepthContainer = new HashMap<Integer, HashMap<Integer, HashMap<Integer, VoxelObject>>>(this.nbVoxelZ);
        for (int i = 0; i < this.nbVoxelZ; ++i) {
            final HashMap<Integer, HashMap<Integer, VoxelObject>> VoxelHeightContainer = new HashMap<Integer, HashMap<Integer, VoxelObject>>(this.nbVoxelY);
            for (int j = 0; j < this.nbVoxelY; ++j) {
                final HashMap<Integer, VoxelObject> VoxelArray = new HashMap<Integer, VoxelObject>(this.nbVoxelX);
                for (int k = 0; k < this.nbVoxelX; ++k) {
                    VoxelArray.put(k, new VoxelObject(k * this.VoxelDimensionX, j * this.VoxelDimensionY, this.VoxelDimensionX, this.VoxelDimensionY));
                }
                VoxelHeightContainer.put(j, VoxelArray);
            }
            this.VoxelDepthContainer.put(i, VoxelHeightContainer);
        }
    }
    
    /**
     * Create the internal voxelized world from a 3D-scan.
     * @param loader the loader when it is in the second state, i.e. after the first read.
     * @param bar the progress to update.
     * @throws NumberFormatException when the file is not well written.
     * @throws IOException when the file is not well written.
     */
    public InternalVoxelizedWorld(final Loader3DScan loader, final JProgressBar bar,String nameGroup) throws NumberFormatException, IOException {
        super.ScanToVoxel(loader, bar,nameGroup);
    }
    
    @Override
    public String toString() {
        String str = "";
        for (int i = 0; i < this.nbVoxelZ; ++i) {
            for (int j = 0; j < this.nbVoxelY; ++j) {
                for (int k = 0; k < this.nbVoxelX; ++k) {
                    System.err.println(this.VoxelDepthContainer.get(i).get(j).get(k));
                }
                str = String.valueOf(str) + "\n";
            }
            str = String.valueOf(str) + "\n\n";
        }
        return str;
    }
}
