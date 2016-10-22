package mainPackage;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import javax.swing.JLabel;

import util.Couple;

/**
 * Class which handles the generation of the Pilot Text File.
 * @author alan
 */
public class WriterPTF
{
    private ExternalVoxelizedWorld vox;
    private List<Component> listC;
    private List<Material> listM;
    private int particle;
    private String UserDefinedExt;
    private String UraniumExt;
    private String ThoriumExt;
    private String PotassiumExt;
    FileWriter writerUserDefined;
    FileWriter writerUranium;
    FileWriter writerThorium;
    FileWriter writerPotassium;
    
    
    /**
     * Constructor. Create the writer.
     * @param father the top level father.
     */
    public WriterPTF(final TopLevelInterface father) {
        this.vox = null;
        this.listC = null;
        this.listM = null;
        this.particle = -1;
        this.UserDefinedExt = "_ud_";
        this.UraniumExt = "_U_";
        this.ThoriumExt = "_Th_";
        this.PotassiumExt = "_K_";
        this.writerUserDefined = null;
        this.writerUranium = null;
        this.writerThorium = null;
        this.writerPotassium = null;
        this.vox = father.getVoxSample();
        this.listC = father.getListOfComponent();
        this.listM = father.getListOfMaterial();
        switch (this.vox.getIndexPrimaryParticle()) {
            case 0: {
                this.particle = 1;
                this.UserDefinedExt = String.valueOf(this.UserDefinedExt) + "a";
                this.UraniumExt = String.valueOf(this.UraniumExt) + "a";
                this.ThoriumExt = String.valueOf(this.ThoriumExt) + "a";
                this.PotassiumExt = String.valueOf(this.PotassiumExt) + "a";
                break;
            }
            case 1: {
                this.particle = 2;
                this.UserDefinedExt = String.valueOf(this.UserDefinedExt) + "b";
                this.UraniumExt = String.valueOf(this.UraniumExt) + "b";
                this.ThoriumExt = String.valueOf(this.ThoriumExt) + "b";
                this.PotassiumExt = String.valueOf(this.PotassiumExt) + "b";
                break;
            }
            case 2: {
                this.particle = 3;
                this.UserDefinedExt = String.valueOf(this.UserDefinedExt) + "g";
                this.UraniumExt = String.valueOf(this.UraniumExt) + "g";
                this.ThoriumExt = String.valueOf(this.ThoriumExt) + "g";
                this.PotassiumExt = String.valueOf(this.PotassiumExt) + "g";
                break;
            }
        }
    }
    
    /**
     * Writes the Pilot Text File.
     * @param modalWaiting 
     * @param file the file to write in.
     * @param udFile 
     * @param thFile 
     * @param uFile 
     * @param kFile 
     */
    public void generatePTF(JLabel modalMessage, final File file) {
        String path = "";
        Label_0041: {
            if (file.getName().contains(".")) {
                path = file.getAbsolutePath().substring(0, file.getAbsolutePath().lastIndexOf("."));
                break Label_0041;
            }
            path = file.getAbsolutePath();
            try {
                this.writerUserDefined = new FileWriter(new File(String.valueOf(path) + this.UserDefinedExt));
                this.writerUranium = new FileWriter(new File(String.valueOf(path) + this.UraniumExt));
                this.writerThorium = new FileWriter(new File(String.valueOf(path) + this.ThoriumExt));
                if (this.particle != 1) {
                    this.writerPotassium = new FileWriter(new File(String.valueOf(path) + this.PotassiumExt));
                }
                String name = "";
                if (file.getName().contains(".")) {
                    name = file.getName().substring(0, file.getName().lastIndexOf("."));
                }
                else {
                    name = file.getName();
                }
                this.writerUserDefined.write(String.valueOf(name) + this.UserDefinedExt + "          #result file name\n\n");
                this.writerUranium.write(String.valueOf(name) + this.UraniumExt + "          #result file name\n\n");
                this.writerThorium.write(String.valueOf(name) + this.ThoriumExt + "          #result file name\n\n");
                if (this.particle != 1) {
                    this.writerPotassium.write(String.valueOf(name) + this.PotassiumExt + "          #result file name\n\n");
                }
                this.writeInFile(String.valueOf(this.vox.getNbPrimaryUnit()) + " ");
                this.writeInFile(String.valueOf(this.vox.getClockValue()) + "  # ( x 1000 ) emitted particles, clock value (%)\n");
                this.writeInFile(String.valueOf(this.particle) + " ");
                this.writeInFile(String.valueOf(this.vox.getMomentumX()) + " ");
                this.writeInFile(String.valueOf(this.vox.getMomentumY()) + " ");
                this.writeInFile(new StringBuilder(String.valueOf(this.vox.getMomentumZ())).toString());
                this.writeInFile("  #particle emitted: 1 for alpha, 2 for beta, 3 for gamma ; momentum of emission in X,Y,Z directions (0 0 0 for random direction)\n");
                this.writerUserDefined.write("0 " + this.vox.getCuttingValue());
                this.writerUranium.write("1 " + this.vox.getCuttingValue());
                this.writerThorium.write("2 " + this.vox.getCuttingValue());
                if (this.particle != 1) {
                    this.writerPotassium.write("3 " + this.vox.getCuttingValue());
                }
                this.writeInFile("  #element emitter (1 for uranium series, 2 for thorium series, 3 for potassium, 0 for User Defined), cut in range value (mm)\n");
                this.writeInFile(String.valueOf(this.vox.getIndexDetectorDefinition()) + " ");
                final int voxel_detector_numb = this.vox.getProbeLocationX() + this.vox.getNbVoxelX() * this.vox.getProbeLocationY() + this.vox.getNbVoxelX() * this.vox.getNbVoxelY() * this.vox.getProbeLocationZ();
                this.writeInFile(String.valueOf(voxel_detector_numb) + " ");
                this.writeInFile(String.valueOf(this.vox.getMaterialForMap()) + " ");
                if (this.vox.isEmissionFromGrain()) {
                    this.writeInFile("1");
                }
                else {
                    this.writeInFile("0");
                }
                this.writeInFile("  #index for detector defintion, detector voxel, material for mapping, emission from grains\n");
                this.writeInFile(String.valueOf(this.vox.getNumberCells()) + " ");
                this.writeInFile(String.valueOf(this.vox.getProbeDiameter()) + " ");
                this.writeInFile(String.valueOf(this.vox.getProbeOffsetX()) + " ");
                this.writeInFile(String.valueOf(this.vox.getProbeOffsetY()) + " ");
                this.writeInFile("  #number of probe cells, probe diam (mm), probe offset in X and Y (in % of voxel X,Y sizes)\n");
                final int nbNewC = this.listC.size() - 18;
                this.writeInFile(String.valueOf(nbNewC) + " #number of new components defined\n");
                final int nbNewM = this.listM.size();
                this.writeInFile(String.valueOf(nbNewM) + " #number of material used\n\n");
                
                modalMessage.setText("Writing component informations...");
                for (int i = 18; i < this.listC.size(); ++i) {
                    this.writeInFile(String.valueOf(i) + " ");
                    this.writeInFile(this.listC.get(i).getName() + " ");
                    this.writeInFile(String.valueOf(this.listC.get(i).getDensity()) + " ");
                    this.writeInFile(String.valueOf(this.listC.get(i).getNbAtomInTheFormula()) + " ");
                    for (final Couple<PeriodicTable, Integer> c : this.listC.get(i).getFormulaAsList()) {
                        this.writeInFile(c.getValeur1() + " " + c.getValeur2() + " ");
                    }
                    this.writeInFile("\n\n");
                }
                
                modalMessage.setText("Writing material informations...");
                for (int i = 0; i < this.listM.size(); ++i) {
                    final Material m = this.listM.get(i);
                    this.writeInFile(String.valueOf(i) + "  # material index\n");
                    this.writeInFile(String.valueOf(m.getName()) + "   # material name\n");
                    if(m.isUseCalculatedDensity())
                    	this.writeInFile(String.valueOf(m.getCalculatedDryDensity()) + "   # dry density (g/cm3)\n");
                    else
                    	this.writeInFile(String.valueOf(m.getDryDensity()) + "   # dry density (g/cm3)\n");
                    this.writeInFile(String.valueOf(m.getWaterContent()) + "   # water content, % of dry mass\n");
                    this.writeInFile(String.valueOf(m.getNbComponent()) + "   # number of components\n");
                    for (final Couple<Component, Float> compCouple : m.getListComponents()) {
                        final int indexC = this.listC.indexOf(compCouple.getValeur1());
                        this.writeInFile(String.valueOf(indexC) + " " + compCouple.getValeur2() + " ");
                    }
                    this.writeInFile("    # component index, % of dry mass\n### granulometry ###\n");
                    this.writeInFile(String.valueOf(m.getGrainComponentIndex()) + " " + m.getGrainDensity() + " " + m.getGrainCompacity() + " " + m.getGrainNbGranulometricFractions());
                    this.writeInFile("    #grain component index, density, compacity (%), number of granulometric fractions\n### diameter in mm, volumetric fraction (%) of the total grains mass (sum must be equal to 100) ###\n");
                    for (final Couple<Float, Float> grainC : m.getGrainList()) {
                        this.writeInFile(grainC.getValeur1() + " " + grainC.getValeur2() + "\n");
                    }
                    this.writeInFile("\n");
                }
                this.writeInFile(String.valueOf(this.vox.getNbVoxelX()) + "      " + this.vox.getNbVoxelY() + "      " + this.vox.getNbVoxelZ() + "  # voxels number along X,Y and Z axes\n");
                float multiplicator = 1.0f;
                if (this.vox.getDimensionUnit().equals("1 \u03bcm")) {
                    multiplicator = 1000.0f;
                }
                else if (this.vox.getDimensionUnit().equals("10 \u03bcm")) {
                    multiplicator = 100.0f;
                }
                else if (this.vox.getDimensionUnit().equals("100 \u03bcm")) {
                    multiplicator = 10.0f;
                }
                else if (this.vox.getDimensionUnit().equals("1 cm")) {
                    multiplicator = 0.1f;
                }
                this.writeInFile(String.valueOf(this.vox.getVoxelDimensionX() / multiplicator) + "      " + this.vox.getVoxelDimensionY() / multiplicator + "      " + this.vox.getVoxelDimensionZ() / multiplicator + "  # voxels size along X,Y and Z axes (mm)\n\n");
                
                modalMessage.setText("Writing cartography of materials...");
                this.writeInFile("### MEDIUM COMPOSITION - cartography of materials with their index\n\n");
                for (int z = 0; z < this.vox.getNbVoxelZ(); ++z) {
                    for (int y = 0; y < this.vox.getNbVoxelY(); ++y) {
                        for (int x = 0; x < this.vox.getNbVoxelX(); ++x) {
                            final int index = this.listM.indexOf(this.vox.getVoxelToIndices(x, y, z).getMaterial());
                            this.writeInFile(String.valueOf(index) + " ");
                        }
                        this.writeInFile("\n");
                    }
                    this.writeInFile("\n");
                }
                
                modalMessage.setText("Writing cartography of radiation values...");
                this.writeInFile("### Cartography of U, Th or K for the external medium :\n\n");
                for (int z = 0; z < this.vox.getNbVoxelZ(); ++z) {
                    for (int y = 0; y < this.vox.getNbVoxelY(); ++y) {
                        for (int x = 0; x < this.vox.getNbVoxelX(); ++x) {
                            final float u = this.vox.getVoxelToIndices(x, y, z).getUranium();
                            this.writerUranium.write(String.valueOf(u) + " ");
                            final float th = this.vox.getVoxelToIndices(x, y, z).getThorium();
                            this.writerThorium.write(String.valueOf(th) + " ");
                            if (this.particle != 1) {
                                final float k = this.vox.getVoxelToIndices(x, y, z).getPotassium();
                                this.writerPotassium.write(String.valueOf(k) + " ");
                            }
                            final float ud = this.vox.getVoxelToIndices(x, y, z).getUranium();
                            this.writerUserDefined.write(String.valueOf(ud) + " ");
                        }
                        this.writeInFile("\n");
                    }
                    this.writeInFile("\n");
                }
                if (this.vox.getIndexDetectorDefinition() != 0) {
                    this.writeInFile("### Define detector size ###\n\n");
                }
                if (this.vox.getIndexDetectorDefinition() == 1) {
                    if (this.vox.getInternalVoxelizedObject() == null) {
                        return;
                    }
                    this.writeInFile(String.valueOf(this.vox.getInternalVoxelizedObject().getNbVoxelX()) + " " + this.vox.getInternalVoxelizedObject().getNbVoxelY() + " " + this.vox.getInternalVoxelizedObject().getNbVoxelZ() + " # voxels number along X,Y and Z axes\n\n");
                }
                else if (this.vox.getIndexDetectorDefinition() == 2 || this.vox.getIndexDetectorDefinition() == 3) {
                    this.writeInFile(String.valueOf(this.vox.getSizeOfPackingBoxX()) + " " + this.vox.getSizeOfPackingBoxY() + " " + this.vox.getSizeOfPackingBoxZ() + " #box size inside the internal voxel, for X,Y,Z (in % of X,Y,Z voxel sizes)\n");
                    this.writeInFile(String.valueOf(this.vox.getOffsetX()) + " " + this.vox.getOffsetY() + " " + this.vox.getOffsetZ() + " #offset of box location inside the internal voxel, for X,Y,Z in % of voxel dimensions\n");
                }
                if (this.vox.getInternalVoxelizedObject() != null) {
                	modalMessage.setText("Writing internal cartography of materials...");
                    this.writeInFile("### INTERNAL VOXEL COMPOSITION - cartography of materials with their index\n\n");
                    for (int z = 0; z < this.vox.getInternalVoxelizedObject().getNbVoxelZ(); ++z) {
                        for (int y = 0; y < this.vox.getInternalVoxelizedObject().getNbVoxelY(); ++y) {
                            for (int x = 0; x < this.vox.getInternalVoxelizedObject().getNbVoxelX(); ++x) {
                                final int index = this.listM.indexOf(this.vox.getInternalVoxelizedObject().getVoxelToIndices(x, y, z).getMaterial());
                                this.writeInFile(String.valueOf(index) + " ");
                            }
                            this.writeInFile("\n");
                        }
                        this.writeInFile("\n");
                    }
                    
                    modalMessage.setText("Writing internal cartography of radiation values...");
                    this.writeInFile("### Cartography of U, Th or K for the internal voxel :\n\n");
                    for (int z = 0; z < this.vox.getInternalVoxelizedObject().getNbVoxelZ(); ++z) {
                        for (int y = 0; y < this.vox.getInternalVoxelizedObject().getNbVoxelY(); ++y) {
                            for (int x = 0; x < this.vox.getInternalVoxelizedObject().getNbVoxelX(); ++x) {
                                final float u = this.vox.getInternalVoxelizedObject().getVoxelToIndices(x, y, z).getUranium();
                                this.writerUranium.write(String.valueOf(u) + " ");
                                final float th = this.vox.getInternalVoxelizedObject().getVoxelToIndices(x, y, z).getThorium();
                                this.writerThorium.write(String.valueOf(th) + " ");
                                if (this.particle != 1) {
                                    final float k = this.vox.getInternalVoxelizedObject().getVoxelToIndices(x, y, z).getPotassium();
                                    this.writerPotassium.write(String.valueOf(k) + " ");
                                }
                                final float ud = this.vox.getInternalVoxelizedObject().getVoxelToIndices(x, y, z).getUranium();
                                this.writerUserDefined.write(String.valueOf(ud) + " ");
                            }
                            this.writeInFile("\n");
                        }
                        this.writeInFile("\n");
                    }
                }
            }
            catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            catch (IOException e2) {
                e2.printStackTrace();
            }
            finally {
                try {
                    this.closeStream();
                }
                catch (IOException e3) {
                    e3.printStackTrace();
                }
            }
        }
        try {
            this.closeStream();
        }
        catch (IOException e3) {
            e3.printStackTrace();
        }
    }
    
    private void writeInFile(final String s) throws IOException {
        this.writerUserDefined.write(s);
        this.writerUranium.write(s);
        this.writerThorium.write(s);
        if (this.particle != 1) {
            this.writerPotassium.write(s);
        }
    }
    
    private void closeStream() throws IOException {
        this.writerUserDefined.close();
        this.writerUranium.close();
        this.writerThorium.close();
        if (this.particle != 1) {
            this.writerPotassium.close();
        }
    }
}
