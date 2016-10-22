package mainPackage;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Class used to gather serializable class to save when the user want to. 
 * In that way, the result of the file is just one file.
 * @author alan
 * @see ExternalVoxelizedWorld
 * @see Material
 * @see Component
 */
public class SaveBox implements Serializable
{
    private static final long serialVersionUID = 8240806791415862885L;
    private ExternalVoxelizedWorld vox;
    private ArrayList<Material> ListOfMaterials;
    private ArrayList<Component> ListOfComponent;
    private String path;
    
    /**
     * Constructor.
     */
    public SaveBox() {
        this.vox = null;
        this.ListOfMaterials = null;
        this.ListOfComponent = null;
        this.path = "";
    }
    
    /**
     * @return the voxelized object.
     */
    public ExternalVoxelizedWorld getVox() {
        return this.vox;
    }
    
    /**
     * Set the voxelized object to save.
     * @param vox the voxelized object to save.
     */
    public void setVox(final ExternalVoxelizedWorld vox) {
        this.vox = vox;
    }
    
    /**
     * @return the list of material.
     */
    public ArrayList<Material> getListMaterial() {
        return this.ListOfMaterials;
    }
    
    /**
     * Set the list of material to save.
     * @param list the list to save.
     */
    public void setListMaterial(final ArrayList<Material> list) {
        this.ListOfMaterials = list;
    }
    
    /**
     * @return the list of component.
     */
    public ArrayList<Component> getListComponent() {
        return this.ListOfComponent;
    }
    
    /**
     * Set the list of component to save.
     * @param list the list to save.
     */
    public void setListComponent(final ArrayList<Component> list) {
        this.ListOfComponent = list;
    }
    
    /**
     * Set the path of the project to save.
     * @param path
     */
    public void setPath(final String path) {
        this.path = path;
    }
    
    /**
     * @return the path of the project.
     */
    public String getPath() {
        return this.path;
    }
}
