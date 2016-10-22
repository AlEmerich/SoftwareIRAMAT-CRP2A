package mainPackage;

import interfaceObject.AssociationFrame;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.swing.JFrame;

import util.Couple;
/**
 * The Loader3DScan reads a serie of text files, one text file containing a matrice representing a 2D layer. 
 * The first reading of that class is just to create a list of shades of grey. 
 * Then the loader is passed to {@link AssociationFrame} to do the associations between the shades and material, 
 * then the loader is passed to the constructor of {@link InternalVoxelizedWorld}.
 * @author alan
 * @see InternalVoxelizedWorld
 */
public class Loader3DScan
{
    private boolean valid;
    private SortedSet<Integer> ShadesOfGray;
    private ArrayList<Couple<Material, Integer>> assocList;
    private int firstSizeX;
    private int firstSizeY;
    private int firstSizeZ;
    private AssociationFrame associationWindow;
    private File directory;
    
    /**
     * Constructor. Creates and initializes the Set of shades of gray.
     */
    public Loader3DScan() {
        this.valid = false;
        this.assocList = null;
        this.firstSizeX = -1;
        this.firstSizeY = -1;
        this.firstSizeZ = -1;
        this.associationWindow = null;
        this.ShadesOfGray = new TreeSet<Integer>();
    }
    
    /**
     * Read the directory passes file by file.
     * @param directory containing all text file.
     * @param nameGroup 
     * @throws Exception
     */
    public void loadScan(final File directory, String nameGroup) throws FileNotFoundException, IOException,
    			ScanException, NumberFormatException 
    {
        int i = 0;
        this.setDirectory(directory);
        File[] listFiles;
        this.firstSizeZ = 0;
        for (int length = (listFiles = directory.listFiles()).length, j = 0; j < length; ++j) {
            final File fileEntry = listFiles[j];
            String fileName = fileEntry.getName();
            if(fileName.contains(nameGroup))
            {
            	this.readImageFile(fileEntry, i++);
            	this.firstSizeZ++;
            }
        }
    }
    
    /**
     * Read the grey scale image.
     * @param file the file to read.
     * @param index the index of the layer to read.
     * @throws FileNotFoundException
     * @throws IOException
     * @throws Exception
     */
    private void readImageFile(final File file, final int index) throws FileNotFoundException, IOException,
    			ScanException, NumberFormatException 
    {
        final BufferedReader buf = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
        String str = null;
        int count = 0;
        while ((str = buf.readLine()) != null) {
            ++count;
            final String[] matrix = str.replaceAll("\\s+", " ").split(" ");
            if (this.firstSizeX == -1) {
                this.firstSizeX = matrix.length;
            }
            else if (this.firstSizeX != matrix.length) {
            	buf.close();
                throw new ScanException("Error inside the image file in the x size: " + file.getName() + " " + " line " + index);
            }
            for (int i = 0; i < matrix.length; ++i) {
                final int shades = Integer.parseInt(matrix[i]);
                if (shades < 0 && shades > 255) {
                	buf.close();
                    throw new ScanException("A shade value is not between 0 and 255: " + file.getName() + " " + " line " + index);
                }
                this.ShadesOfGray.add(shades);
            }
        }
        if (this.firstSizeY == -1) {
            this.firstSizeY = count;
        }
        else if (this.firstSizeY != count) {
        	buf.close();
            throw new ScanException("Error inside the image file in the y size: " + file.getName());
        }
        buf.close();
    }
    
    /**
     * Show the Swing component.
     * @param parent the JFrame parent.
     * @param listM the list of material.
     */
    public void showJDialog(final JFrame parent, final List<Material> listM) {
        (this.associationWindow = new AssociationFrame(parent, "Association Window", true, this, listM)).setLocationRelativeTo(parent);
        this.associationWindow.setVisible(true);
    }
    
    /**
     * @return a set of shades of gray.
     */
    public SortedSet<Integer> getShadesOfGray() {
        return this.ShadesOfGray;
    }
    
    /**
     * @return the firstSizeX.
     */
    public int getFirstSizeX() {
        return this.firstSizeX;
    }
    
    /**
     * @return the firstSizeY.
     */
    public int getFirstSizeY() {
        return this.firstSizeY;
    }
    
    /**
     * @return the firstSizeZ.
     */
    public int getFirstSizeZ() {
        return this.firstSizeZ;
    }
    
    /**
     * Show in the standard output the content of the loader.
     */
    public void showContentLoader() {
        System.out.println("size X Y Z: " + this.firstSizeX + " " + this.firstSizeY + " " + this.firstSizeZ);
        String str = "";
        final Iterator<Integer> it = this.ShadesOfGray.iterator();
        while (it.hasNext()) {
            str = String.valueOf(str) + it.next() + " ";
        }
        System.out.println(str);
    }
    
    /**
     * Set the loader valid state.
     * @param valid the boolean to set in valid.
     */
    public void setValid(final boolean valid) {
        this.assocList = this.associationWindow.getListAssociations();
        this.valid = valid;
    }
    
    /**
     * @return true if the loader is valid, false if not.
     */
    public boolean isValid() {
        return this.valid;
    }
    
    public ArrayList<Couple<Material, Integer>> getAssocList() {
        return this.assocList;
    }
    
    public void setDirectory(final File directory) {
        this.directory = directory;
    }
    
    public File getDirectory() {
        return this.directory;
    }
}
