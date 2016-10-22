package iramat.dosiedit2d.controler;

import iramat.dosiedit2d.model.Dosi2DModel;
import iramat.dosiedit2d.model.VoxelObject;
import iramat.dosiseed.model.ColoredMaterial;
import iramat.dosiseed.model.Material;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import mainPackage.Component;
import util.Couple;

/**
 * Class which generates the pilot text file interpreted by DosiVox2D software.
 * @author alan
 *
 */
public class Dosi2dPtfWriter
{
	/**
	 * Generates the pilot text file corresponding to the model in parameters.
	 * @param model the model to translate into a pilot text file.
	 * @param path the path where to save the file in disk.
	 * @return the file generated.
	 */
	public static File generate(Dosi2DModel model,final String path)
	{
		if(path.equals(""))
			return null;
		try
		{
			String pathWithoutTxt = path.substring(0,path.lastIndexOf("."));
			File file = new File(path);
			FileWriter writer = new FileWriter(file);
			
			writer.write(pathWithoutTxt+" # results file name\n\n");
			writer.write(model.getNbParticlesEmitted()+" "+model.getClockValue()+" "+model.getNbThread());
			writer.write(" # ( x 1000 ) emitted particles, clock value (%), number of CPU core used (multithreading)\n");
			String particle = "";
			switch(model.getPrimaryParticle())
			{
				case Alpha:
					particle = "1";
					break;
				case Bêta:
					particle = "2";
					break;
				case Gamma:
					particle = "3";
					break;
					default:
						break;
			}
			String emission = "";
			if(model.getEmissionProcess().equals("2D std"))
				emission = "0";
			else if(model.getEmissionProcess().equals("3D"))
				emission = "1";
			else
				emission = "2";
			writer.write(particle+" "+emission);
			writer.write("  #particle emitted: 1 for alpha, 2 for beta, 3 for gamma ; emission process (0 = 2D std; 1 = 3D; 2 = 2D real)\n");
			boolean[] radioBoolElement = model.getWhichRadioelementIsSimulated();
			int k = (radioBoolElement[0] ? 1 : 0);
			int u = (radioBoolElement[1] ? 1 : 0);
			int th = (radioBoolElement[2] ? 1 : 0);
			int ud = (radioBoolElement[3] ? 1 : 0);
			writer.write(k+" "+u+" "+th+" "+ud);
			writer.write(" #element emitter (1 for uranium series, 2 for thorium series, 3 for potassium, 0 for User Defined)\n");
			writer.write(model.getMaxRange()+" "+model.getProductionCut());
			writer.write("   # particle average range in mm; cut in range value in mm (= resolution for the creation of secondary particles\n");
			Material m = model.getMaterialForDoseMapping();
			int index = -1;
			if(m != null)
				index = model.getListOfMaterial().indexOf(m);
			writer.write(index+" "+(model.isExcludeEdge() ? "1" : "0"));
			writer.write(" # material for dose mapping (-1 = dose mapping in all material; " +
					"n = dose mapping only for material of index n); exclude edge effect zones " +
					"for dose results (recommanded)\n\n");
			
			writer.write((model.getListOfComponent().size() - 18)+"   #number of new components defined\n\n");
			for(int i = 18;i<model.getListOfComponent().size();i++)
				writer.write(model.getListOfComponent().get(i)+" \n");
			
			List<ColoredMaterial> usedMaterial = new ArrayList<ColoredMaterial>();
			for(int y=0;y<model.getGrid().size();y++)
				for(int x=0;x<model.getGrid().get(y).size();x++)
				{
					ColoredMaterial mat = model.getGrid().get(y).get(x).getMaterial();
					if(!usedMaterial.contains(mat))
						usedMaterial.add(mat);
				}
			writer.write(usedMaterial.size()+"  #number of materials used\n");
			int indexOfMaterial=0;
			for(ColoredMaterial mat : usedMaterial)
			{			
				writer.write("\n"+(indexOfMaterial++)+"   # material index\n");
				writer.write(mat.getName()+" # material name\n");
				writer.write(mat.getUsedDensity()+" # dry density (g/cm3)\n");
				writer.write(mat.getWaterFraction()+" # water content, % of dry mass\n");
				writer.write(mat.getListComponent().size()+" # number of components\n");
				for(Couple<Component,Float> couple: mat.getListComponent())
				{
					int indexComponent = model.getListOfComponent().indexOf(couple.getValeur1());
					
					writer.write(indexComponent+" "+couple.getValeur2()+" ");
				}
				writer.write(" # component index, % of dry mass\n");
			}
			
			writer.write("\n"+model.getGrid().get(0).size()+"    "+model.getGrid().size()
					+"     # voxels number along X and Y axes " +
					"(= number of pixel of the image in the X and Y directions)\n");
			writer.write(model.getRealDimX()+"    "+model.getRealDimY() +
					"      #voxels size along X and Y axes " +
					"(mm) (= resolution of the image in the X and Y directions)\n\n");
			
			writer.write("### MEDIUM COMPOSITION - mapping of the materials with their index\n\n");
			
			String potassium="",uranium="",thorium="",userdef="";
			
			for(int y=0;y<model.getGrid().size();y++)
			{	
				for(int x=0;x<model.getGrid().get(y).size();x++)
				{
					VoxelObject vox = model.getGrid().get(y).get(x);
					int indexMaterial = usedMaterial.indexOf(vox.getMaterial());
					writer.write(indexMaterial+" ");
					potassium += vox.getPotassium()+" ";
					uranium += vox.getUranium()+" ";
					thorium += vox.getThorium()+" ";
					userdef += vox.getDefinedRadMolecule()+" ";
				}
				writer.write("\n");
				potassium += "\n";
				uranium += "\n";
				thorium += "\n";
				userdef += "\n";
			}
			
			writer.write("\n### mapping of the K content in each voxels: \n\n");
			writer.write(potassium);
			writer.write("\n### mapping of the U content in each voxels: \n\n");
			writer.write(uranium);
			writer.write("\n### mapping of the Th content in each voxels: \n\n");
			writer.write(thorium);
			writer.write("\n### mapping of the User-defined element content in each voxels\n\n");
			writer.write(userdef);
			
			writer.close();
			
			return file;
			
		} catch (IOException e)
		{
			return null;
		}
	}
}
