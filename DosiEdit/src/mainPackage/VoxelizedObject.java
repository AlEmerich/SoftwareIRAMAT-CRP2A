package mainPackage;

import interfaceObject.Grid2D;

import java.awt.geom.Rectangle2D;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import javax.swing.JProgressBar;

import util.Couple;

/**
 * Abstract class supporting a skeleton of a voxelized object. It provides method to support matrices, 
 * basic field as the number of voxel and some method to handle vertices if needed of 3D visualisation. 
 * This class has to be extended.
 * @author alan
 *
 */
public abstract class VoxelizedObject implements Serializable
{
    private static final long serialVersionUID = -2024788144012275564L;
    protected int nbVoxelX;
    protected int nbVoxelY;
    protected int nbVoxelZ;
    protected float VoxelDimensionX;
    protected float VoxelDimensionY;
    protected float VoxelDimensionZ;
    protected String DimensionUnit;
    protected HashMap<Integer, HashMap<Integer, HashMap<Integer, VoxelObject>>> VoxelDepthContainer;
    public boolean isUpdateForScene;
    
    /**
     * @return the voxelDepthContainer.
     */
    public HashMap<Integer, HashMap<Integer, HashMap<Integer, VoxelObject>>> getVoxelDepthContainer() {
        return this.VoxelDepthContainer;
    }
    
    /**
     * Constructor. Create a new empty instance of a voxelized object.
     */
    public VoxelizedObject() {
        this.nbVoxelX = 20;
        this.nbVoxelY = 20;
        this.nbVoxelZ = 20;
        this.VoxelDimensionX = 1.0f;
        this.VoxelDimensionY = 1.0f;
        this.VoxelDimensionZ = 1.0f;
        this.loadHashMap();
    }
    
    /**
     * Constructor. Create the internal voxelized world loaded 
     * and associated by Loader3DScan and AssociationFrame reading the files a second times.
     * @param loader the loader in the right state (so the file is read and the associations are made).
     * @param bar the progress bar to update dynamically.
     * @throws NumberFormatException
     * @throws IOException
     */
    public void ScanToVoxel(final Loader3DScan loader, final JProgressBar bar,String nameGroup) throws NumberFormatException, IOException {
        this.VoxelDepthContainer = null;
        this.nbVoxelX = loader.getFirstSizeX();
        this.nbVoxelY = loader.getFirstSizeY();
        this.nbVoxelZ = loader.getFirstSizeZ();
        final File directory = loader.getDirectory();
        final ArrayList<Couple<Material, Integer>> assocList = loader.getAssocList();
        this.VoxelDepthContainer = new HashMap<Integer, HashMap<Integer, HashMap<Integer, VoxelObject>>>(this.nbVoxelZ);
        int i = 0;
        final List<File> listFiles = new ArrayList<File>(Arrays.asList(directory.listFiles()));
        Collections.sort(listFiles);
        for (final File fileEntry : listFiles) {
        	if(fileEntry.getName().contains(nameGroup))
        	{
        		final HashMap<Integer, HashMap<Integer, VoxelObject>> VoxelHeightContainer = new HashMap<Integer, HashMap<Integer, VoxelObject>>(this.nbVoxelY);
        		final BufferedReader buf = new BufferedReader(new InputStreamReader(new FileInputStream(fileEntry)));
        		String str = null;
        		
        		int j = 0;
        		while ((str = buf.readLine()) != null) {
        			final HashMap<Integer, VoxelObject> VoxelArray = new HashMap<Integer, VoxelObject>(this.nbVoxelX);
        			final String[] matrix = str.replaceAll("\\s+", " ").split(" ");
        			for (int k = 0; k < matrix.length; ++k) {
        				final int shades = Integer.parseInt(matrix[k]);
        				for (final Couple<Material, Integer> c : assocList) {
        					if (c.getValeur2() == shades) {
        						VoxelArray.put(k, new VoxelObject(k * this.VoxelDimensionX, j * this.VoxelDimensionY, this.VoxelDimensionX, this.VoxelDimensionY, c.getValeur1()));
        						break;
        					}
        				}
        			}
        			VoxelHeightContainer.put(j++, VoxelArray);
        		}
        		this.VoxelDepthContainer.put(i++, VoxelHeightContainer);
        		bar.setValue(i);
        		buf.close();
        	}
        }
    }
    
    /**
     * Method call when the reader of the file is ready to read matrices, and fill the voxel with material and radiation values. 
     * Method call by loadFile in TopLevelInterface.
     */
    public void loadHashMap() {
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
     * Method set to be called by a 2D representation, to check if the MouseX and MouseY parameters is in the voxel.
     * @param MouseX the X coordinate of the point to check.
     * @param MouseY the Y coordinate of the point to check.
     * @param mode the layer mode.
     * @param activeLayer the current layer in which seek the collision.
     * @param matToFillWith if the mouse is in a voxel, the voxel will be filled by this material.
     * @return the list of voxel which has been changed
     */
    public ArrayList<Integer> checkAndFill(final double MouseX, final double MouseY, final Grid2D.Layer mode, final int activeLayer, final Material matToFillWith) {
        if (matToFillWith == null) {
            return null;
        }
        final ArrayList<Integer> list = new ArrayList<Integer>();
        switch (mode) {
            case XY: {
                for (int y = 0; y < this.nbVoxelY; ++y) {
                    for (int x = 0; x < this.nbVoxelX; ++x) {
                        final VoxelObject vox = this.VoxelDepthContainer.get(activeLayer).get(y).get(x);
                        if (vox.contains(MouseX, MouseY)) {
                            if (vox.getMaterial() != null && vox.getMaterial().isActive()) {
                                list.add(x);
                                list.add(y);
                                list.add(activeLayer);
                            }
                            vox.setMaterialIndex(matToFillWith);
                        }
                    }
                }
                return list;
            }
            case XZ: {
                for (int z = 0; z < this.nbVoxelZ; ++z) {
                    for (int x = 0; x < this.nbVoxelX; ++x) {
                        final VoxelObject vox = this.VoxelDepthContainer.get(z).get(activeLayer).get(x);
                        if (vox.contains(MouseX, MouseY)) {
                            if (vox.getMaterial() != null && vox.getMaterial().isActive()) {
                                list.add(x);
                                list.add(activeLayer);
                                list.add(z);
                            }
                            vox.setMaterialIndex(matToFillWith);
                        }
                    }
                }
                return list;
            }
            case YZ: {
                for (int z = 0; z < this.nbVoxelZ; ++z) {
                    for (int y2 = 0; y2 < this.nbVoxelY; ++y2) {
                        final VoxelObject vox = this.VoxelDepthContainer.get(z).get(y2).get(activeLayer);
                        if (vox.contains(MouseX, MouseY)) {
                            if (vox.getMaterial() != null && vox.getMaterial().isActive()) {
                                list.add(activeLayer);
                                list.add(y2);
                                list.add(z);
                            }
                            vox.setMaterialIndex(matToFillWith);
                        }
                    }
                }
                return list;
            }
            default: {
                return null;
            }
        }
    }
    
    /**
     * Method set to be called by a 2D representation, to check if the rectangle parameters is in the voxel.
     * @param selectRect the rect to check with.
     * @param mode the layer mode.
     * @param activeLayer the current layer in which seek the colision.
     * @param matToFillWith if the mouse is in a voxel, the voxel will be filled by this material.
     * @return the list of voxel which has been changed
     */
    public ArrayList<Integer> checkAndFill(final Rectangle2D.Float selectRect, final Grid2D.Layer mode, final int activeLayer, final Material matToFillWith) {
        if (matToFillWith == null) {
            return null;
        }
        final ArrayList<Integer> list = new ArrayList<Integer>();
        switch (mode) {
            case XY: {
                for (int y = 0; y < this.nbVoxelY; ++y) {
                    for (int x = 0; x < this.nbVoxelX; ++x) {
                        final VoxelObject vox = this.VoxelDepthContainer.get(activeLayer).get(y).get(x);
                        if (selectRect.intersects(vox)) {
                            if (vox.getMaterial() != null && vox.getMaterial().isActive()) {
                                list.add(x);
                                list.add(y);
                                list.add(activeLayer);
                            }
                            vox.setMaterialIndex(matToFillWith);
                        }
                    }
                }
                return list;
            }
            case XZ: {
                for (int z = 0; z < this.nbVoxelZ; ++z) {
                    for (int x = 0; x < this.nbVoxelX; ++x) {
                        final VoxelObject vox = this.VoxelDepthContainer.get(z).get(activeLayer).get(x);
                        if (selectRect.intersects(vox)) {
                            if (vox.getMaterial() != null && vox.getMaterial().isActive()) {
                                list.add(x);
                                list.add(activeLayer);
                                list.add(z);
                            }
                            vox.setMaterialIndex(matToFillWith);
                        }
                    }
                }
                return list;
            }
            case YZ: {
                for (int z = 0; z < this.nbVoxelZ; ++z) {
                    for (int y2 = 0; y2 < this.nbVoxelY; ++y2) {
                        final VoxelObject vox = this.VoxelDepthContainer.get(z).get(y2).get(activeLayer);
                        if (selectRect.intersects(vox)) {
                            if (vox.getMaterial() != null && vox.getMaterial().isActive()) {
                                list.add(activeLayer);
                                list.add(y2);
                                list.add(z);
                            }
                            vox.setMaterialIndex(matToFillWith);
                        }
                    }
                }
                return list;
            }
            default: {
                return null;
            }
        }
    }
    
    /**
     * Reload default radiation value.
     * @param m the material to reload radiation value.
     */
    public void reloadReferenceRadiationValue(final Material m) {
        for (int z = 0; z < this.nbVoxelZ; ++z) {
            for (int y = 0; y < this.nbVoxelY; ++y) {
                for (int x = 0; x < this.nbVoxelX; ++x) {
                    final VoxelObject v = this.VoxelDepthContainer.get(z).get(y).get(x);
                    if (!v.isEmpty() && v.getMaterial().equals(m)) {
                        v.setUranium(v.getMaterial().getRefUraniumValue());
                        v.setThorium(v.getMaterial().getRefThoriumValue());
                        v.setPotassium(v.getMaterial().getRefPotassiumValue());
                        v.setDefinedRadMolecule(v.getMaterial().getRefUserDefinedValue());
                    }
                }
            }
        }
    }
    
    /**
     * @param x coords
     * @param y coords
     * @param z coords
     * @return the voxel at those coordinates.
     */
    public VoxelObject getVoxelToIndices(final int x, final int y, final int z) {
        return this.VoxelDepthContainer.get(z).get(y).get(x);
    }
    
    /**
     * Set the number of voxel in the X dimension.
     * @param nbVoxelX
     */
    public void setNbVoxelX(final int nbVoxelX) {
        this.nbVoxelX = nbVoxelX;
    }
    
    /**
     * @return the number of voxel in the X dimension.
     */
    public int getNbVoxelX() {
        return this.nbVoxelX;
    }
    
    /**
     * Set the number of voxel in the Z dimension.
     * @param nbVoxelZ
     */
    public void setNbVoxelZ(final int nbVoxelZ) {
        this.nbVoxelZ = nbVoxelZ;
    }
    
    /**
     * @return the number of voxel in the Z dimension.
     */
    public int getNbVoxelZ() {
        return this.nbVoxelZ;
    }
    
    /**
     * Set the number of voxel in the Y dimension.
     * @param nbVoxelY
     */
    public void setNbVoxelY(final int nbVoxelY) {
        this.nbVoxelY = nbVoxelY;
    }
    
    /**
     * @return the number of voxel in the Y dimension.
     */
    public int getNbVoxelY() {
        return this.nbVoxelY;
    }
    
    /**
     * @return the voxelDimensionX.
     */
    public float getVoxelDimensionX() {
        return this.VoxelDimensionX;
    }
    
    /**
     * @param voxelDimensionX the voxelDimensionX to set
     */
    public void setVoxelDimensionX(final float voxelDimensionX) {
        this.VoxelDimensionX = voxelDimensionX;
        this.isUpdateForScene = false;
    }
    
    /**
     * @return the voxelDimensionY
     */
    public float getVoxelDimensionY() {
        return this.VoxelDimensionY;
    }
    
    /**
     * @param voxelDimensionY the voxelDimensionY to set
     */
    public void setVoxelDimensionY(final float voxelDimensionY) {
        this.VoxelDimensionY = voxelDimensionY;
        this.isUpdateForScene = false;
    }
    
    /**
     * @return the voxelDimensionZ.
     */
    public float getVoxelDimensionZ() {
        return this.VoxelDimensionZ;
    }
    
    /**
     * @param voxelDimensionZ the voxelDimensionZ to set
     */
    public void setVoxelDimensionZ(final float voxelDimensionZ) {
        this.VoxelDimensionZ = voxelDimensionZ;
        this.isUpdateForScene = false;
    }
    
    /**
     * @return the dimensionUnit.
     */
    public String getDimensionUnit() {
        return this.DimensionUnit;
    }
    
    /**
     * @param dimensionUnit the dimensionUnit to set.
     */
    public void setDimensionUnit(final String dimensionUnit) {
        this.DimensionUnit = dimensionUnit;
    }
    
    /**
     * Resize the number of voxel in the Z-axis.
     * @param newNbX the new number of voxel in the Z-axis.
     */
    public void resizeDimensionX(final int newNbX) {
        for (int z = 0; z < this.nbVoxelZ; ++z) {
            for (int y = 0; y < this.nbVoxelY; ++y) {
                if (this.nbVoxelX < newNbX) {
                    for (int x = this.nbVoxelX; x < newNbX; ++x) {
                        this.VoxelDepthContainer.get(z).get(y).put(x, new VoxelObject(x * this.VoxelDimensionX, y * this.VoxelDimensionY, this.VoxelDimensionX, this.VoxelDimensionY));
                    }
                }
                else {
                    for (int x = newNbX; x < this.nbVoxelX; ++x) {
                        this.VoxelDepthContainer.get(z).get(y).remove(x);
                    }
                }
            }
        }
        this.setNbVoxelX(newNbX);
        this.isUpdateForScene = false;
    }
    
    /**
     * Resize the voxel dimension in the Y-axis.
     * @param newNbY the new dimension in the Y-axis.
     */
    public void resizeDimensionY(final int newNbY) {
        for (int z = 0; z < this.nbVoxelZ; ++z) {
            if (this.nbVoxelY < newNbY) {
                for (int y = this.nbVoxelY; y < newNbY; ++y) {
                    final HashMap<Integer, VoxelObject> voxelArray = new HashMap<Integer, VoxelObject>(this.nbVoxelX);
                    for (int x = 0; x < this.nbVoxelX; ++x) {
                        voxelArray.put(x, new VoxelObject(x * this.VoxelDimensionX, y * this.VoxelDimensionY, this.VoxelDimensionX, this.VoxelDimensionY));
                    }
                    this.VoxelDepthContainer.get(z).put(y, voxelArray);
                }
            }
            else {
                for (int y = newNbY; y < this.nbVoxelY; ++y) {
                    this.VoxelDepthContainer.get(z).remove(y);
                }
            }
        }
        this.setNbVoxelY(newNbY);
        this.isUpdateForScene = false;
    }
    
    /**
     * Resize the voxel dimension in the Z-axis.
     * @param newNbZ the new dimension in the Z-axis.
     */
    public void resizeDimensionZ(final int newNbZ) {
        if (this.nbVoxelZ < newNbZ) {
            for (int z = this.nbVoxelZ; z < newNbZ; ++z) {
                final HashMap<Integer, HashMap<Integer, VoxelObject>> VoxelHeightContainer = new HashMap<Integer, HashMap<Integer, VoxelObject>>(this.nbVoxelY);
                for (int y = 0; y < this.nbVoxelY; ++y) {
                    final HashMap<Integer, VoxelObject> voxelArray = new HashMap<Integer, VoxelObject>(this.nbVoxelX);
                    for (int x = 0; x < this.nbVoxelX; ++x) {
                        voxelArray.put(x, new VoxelObject(x * this.VoxelDimensionX, y * this.VoxelDimensionY, this.VoxelDimensionX, this.VoxelDimensionY));
                    }
                    VoxelHeightContainer.put(y, voxelArray);
                }
                this.VoxelDepthContainer.put(z, VoxelHeightContainer);
            }
        }
        else {
            for (int z = newNbZ; z < this.nbVoxelZ; ++z) {
                this.VoxelDepthContainer.remove(z);
            }
        }
        this.setNbVoxelZ(newNbZ);
        this.isUpdateForScene = false;
    }
    
    public void resizeVoxelDimensionX(final float value) {
        this.VoxelDimensionX = value;
        this.isUpdateForScene = false;
    }
    
    public void resizeVoxelDimensionY(final float value) {
        this.VoxelDimensionY = value;
        this.isUpdateForScene = false;
    }
    
    public void resizeVoxelDimensionZ(final float value) {
        this.VoxelDimensionZ = value;
        this.isUpdateForScene = false;
    }
    
    @Override
    public String toString() {
        String str = "";
        for (int i = 0; i < this.nbVoxelZ; ++i) {
            for (int j = 0; j < this.nbVoxelY; ++j) {
                for (int k = 0; k < this.nbVoxelX; ++k) {
                    str = String.valueOf(str) + this.VoxelDepthContainer.get(i).get(j).get(k);
                }
                str = String.valueOf(str) + "\n";
            }
            str = String.valueOf(str) + "\n\n";
        }
        return str;
    }
    
    /**
     * Update the x, y, width and height values in voxels, to pass to the {@link Grid2D}.
     * @param mode the new mode to set with.
     * @param activeLayer the current layer.
     */
    public void updateAccordingView(final Grid2D.Layer mode, final int activeLayer) {
        switch (mode) {
            case XY: {
                for (int y = 0; y < this.nbVoxelY; ++y) {
                    for (int x = 0; x < this.nbVoxelX; ++x) {
                        this.VoxelDepthContainer.get(activeLayer).get(y).get(x).setRect(x * this.VoxelDimensionX, y * this.VoxelDimensionY, this.VoxelDimensionX, this.VoxelDimensionY);
                    }
                }
                break;
            }
            case XZ: {
                for (int z = 0; z < this.nbVoxelZ; ++z) {
                    for (int x = 0; x < this.nbVoxelX; ++x) {
                        this.VoxelDepthContainer.get(z).get(activeLayer).get(x).setRect(x * this.VoxelDimensionX, z * this.VoxelDimensionZ, this.VoxelDimensionX, this.VoxelDimensionZ);
                    }
                }
                break;
            }
            case YZ: {
                for (int z = this.nbVoxelZ - 1; z >= 0; --z) {
                    for (int y2 = 0; y2 < this.nbVoxelY; ++y2) {
                        this.VoxelDepthContainer.get(z).get(y2).get(activeLayer).setRect(y2 * this.VoxelDimensionY, z * this.VoxelDimensionZ, this.VoxelDimensionY, this.VoxelDimensionZ);
                    }
                }
                break;
            }
        }
    }
    
    /**
     * Duplicate the active layer to the layers kept in list.
     * @param mode the current mode.
     * @param activeLayer the layer to copy.
     * @param list the list of layer to modify.
     */
    public void duplicate(final Grid2D.Layer mode, final int activeLayer, final List<Integer> list) {
        switch (mode) {
            case XY: {
                for (final int layerTarget : list) {
                    final HashMap<Integer, HashMap<Integer, VoxelObject>> layerVox = this.cloneOf2(this.VoxelDepthContainer.get(activeLayer));
                    for (int y = 0; y < this.nbVoxelY; ++y) {
                        for (int x = 0; x < this.nbVoxelX; ++x) {
                            this.VoxelDepthContainer.put(layerTarget, layerVox);
                        }
                    }
                }
                break;
            }
            case XZ: {
                for (final int layerTarget : list) {
                    for (int z = 0; z < this.nbVoxelZ; ++z) {
                        final HashMap<Integer, VoxelObject> array = this.cloneOf(this.VoxelDepthContainer.get(z).get(activeLayer));
                        for (int x = 0; x < this.nbVoxelX; ++x) {
                            this.VoxelDepthContainer.get(z).put(layerTarget, array);
                        }
                    }
                }
                break;
            }
            case YZ: {
                for (final int layerTarget : list) {
                    for (int z = 0; z < this.nbVoxelZ; ++z) {
                        for (int y = 0; y < this.nbVoxelY; ++y) {
                            this.VoxelDepthContainer.get(z).get(y).put(layerTarget, this.VoxelDepthContainer.get(z).get(y).get(activeLayer).clone());
                        }
                    }
                }
                break;
            }
        }
    }
    
    private HashMap<Integer, HashMap<Integer, VoxelObject>> cloneOf2(final HashMap<Integer, HashMap<Integer, VoxelObject>> hashMap) {
        final HashMap<Integer, HashMap<Integer, VoxelObject>> clone = new HashMap<Integer, HashMap<Integer, VoxelObject>>(hashMap);
        for (int i = 0; i < hashMap.size(); ++i) {
            if (hashMap.get(i) != null) {
                clone.put(i, this.cloneOf(hashMap.get(i)));
            }
        }
        return clone;
    }
    
    private HashMap<Integer, VoxelObject> cloneOf(final HashMap<Integer, VoxelObject> hashMap) {
        final HashMap<Integer, VoxelObject> clone = new HashMap<Integer, VoxelObject>(hashMap);
        for (int i = 0; i < hashMap.size(); ++i) {
            clone.put(i, hashMap.get(i).clone());
        }
        return clone;
    }
    
    /**
     * @return true if all voxels are filled, false if not.
     */
    public boolean isComplete() {
        for (int z = 0; z < this.nbVoxelZ; ++z) {
            for (int y = 0; y < this.nbVoxelY; ++y) {
                for (int x = 0; x < this.nbVoxelX; ++x) {
                    if (this.VoxelDepthContainer.get(z).get(y).get(x).isEmpty()) {
                        return false;
                    }
                }
            }
        }
        return true;
    }
    
    /**
     * Check material in voxels to see if that materials is not deleted by the user.
     * @param listOfMaterial the list of all materials.
     */
    public void checkExistenceMaterial(final List<Material> listOfMaterial) {
        for (int z = 0; z < this.nbVoxelZ; ++z) {
            for (int y = 0; y < this.nbVoxelY; ++y) {
                for (int x = 0; x < this.nbVoxelX; ++x) {
                    if (!listOfMaterial.contains(this.VoxelDepthContainer.get(z).get(y).get(x).getMaterial())) {
                        this.VoxelDepthContainer.get(z).get(y).get(x).setMaterialIndex(null);
                    }
                }
            }
        }
    }
}
