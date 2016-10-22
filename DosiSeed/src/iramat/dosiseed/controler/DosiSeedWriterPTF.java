package iramat.dosiseed.controler;

import iramat.dosiseed.model.GrainFraction;
import iramat.dosiseed.model.Material;
import iramat.dosiseed.model.Model;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import mainPackage.Component;

public class DosiSeedWriterPTF
{
	public static File generate(Model model,final String path)
	{
		try
		{
			File file = new File(path);
			FileWriter writer = new FileWriter(file);
			
			writer.write(file.getName()+" # results file name\n\n");
			writer.write("			#********** particle emission and properties **********#\n\n");
			writer.write(model.getNbParticlesEmitted()+" # number of thousand particles emmited\n");
			writer.write(model.getPrimaryParticle().ordinal()+" # 0=alpha, 1=beta, 2=gamma\n");
			boolean[] radioBoolElement = model.getWhichRadioelementIsSimulated();
			int k = (radioBoolElement[0] ? 1 : 0);
			int u = (radioBoolElement[1] ? 1 : 0);
			int th = (radioBoolElement[2] ? 1 : 0);
			int ud = (radioBoolElement[3] ? 1 : 0);
			writer.write(k+" "+u+" "+th+" "+ud+" # radioactive elements simulated: booleans(K,U,Th,user_def)\n");
			writer.write(model.getMaxRange()+" # particles max range in mm (for the calculation of the infinite medium size)\n");
			writer.write(model.getProductionCut()+" # production cut, in millimeter (should be less than size of the smallest object)\n\n");
			
			writer.write("			      #********** material description **********#\n\n");
			writer.write("# new components\n");
			List<Component> listComponent = model.getListOfComponent();
			writer.write(listComponent.size()-18+" # number of new component\n");
			writer.write("# ID,name,density,number of atom type, list of atom symbol/number of atom of this symbol\n");
			for(int i=18;i<listComponent.size();i++)
				writer.write(i+" "+listComponent.get(i).getName()+" "+listComponent.get(i).getFormulaAsList().size()
						+" "+listComponent.get(i).getFormulaAsString()+"\n");
			
			writer.write("\n# material\n");
			writer.write((model.getNbOfGrainTypes()-1)+" # number of grain types (coarse and fine fraction)\n");
			writer.write(model.getWfInMatrix()+" # WF in the matrix, in percent\n");
			writer.write("# ID,name,density,number of component, list of component ID/mass fraction\n");
			int idType=0;
			for(Material mat : model.getCompositionOfGrains())
			{
				writer.write((idType)+" "+mat.toString(model.getListOfComponent())
						+(idType==0 ? " # matrix\n" : " # type"+(idType)+" grain material\n"));
				idType++;
			}
								
			writer.write("\n			  #********** voxelised geometry parameters **********#\n\n");
			writer.write(model.getCoarseFraction().getVoxelSize()+" # coarse fraction voxel size, in mm\n");
			writer.write(model.getFineFraction().getVoxelSize()+" # fine fraction voxel size, in mm (this size will be adapted to fit integraly in the coarse fraction voxel)\n");
			writer.write(((int)model.getNbVoxelsInDetector().getX())+" "+
					((int)model.getNbVoxelsInDetector().getY())+" "+
					((int)model.getNbVoxelsInDetector().getZ())+
					" # number of coarse fraction voxels in the detector by X,Y and Z axis\n");
			writer.write((model.whichMedium() ? 0 : 1)+" # 0=fixed medium, 1=infinite medium\n");
			writer.write( (model.getCoarseFraction().distributionMaterial() ? 1 : 0) +" "+
					(model.getFineFraction().distributionMaterial() ? 1 : 0)+
					" # Coarse fraction and Fine fraction distribution of material in the grain "+
					"(0=random grain composition in each voxel, 1=recurent grain composition in every voxels)\n");
			
			writer.write("\n			#********** coarse grain fraction description **********#\n\n");
			GrainFraction coarse = model.getCoarseFraction();
			writer.write(coarse.getVolumicFraction()+" # volumic fraction of the coarse grains (on the total volume of sample)\n");
			writer.write(coarse.getDescOfEachSize().size()+" # number of grain sizes\n\n");
			writer.write("# D = spherical grain diameter in mm\n");
			writer.write("# f = volumic fraction of this diameter\n");
			writer.write("# Tn = volumic fraction of grain type n in this diameter\n\n");
			writer.write("D\tf\t");
			for(int i=0;i<model.getNbOfGrainTypes()-1;i++)
				writer.write("T"+(i+1)+"\t");
			writer.write("\n");
			for(List<Float> size : coarse.getDescOfEachSize())
			{
				for(float value : size)
					writer.write(value+"\t");
				writer.write("\n");
			}
					
			writer.write("\n");
			for(int i=0;i<model.getNbOfGrainTypes()-1;i++)
				writer.write("T"+(i+1)+"\t");
			writer.write("\n");
			for(int i=0;i<4;i++)
			{
				for(List<Float> type : coarse.getDescOfEachType())
					writer.write(type.get(i)+"\t");
				if(i==0)
					writer.write("# % of K in Mtx=matrix and Tn=Type n grain\n");
				else if(i==1)
					writer.write("# ppm of U series in Mtx=matrix and Tn=Type n grain\n");
				else if(i==2)
					writer.write("# ppm of Th series in Mtx=matrix and Tn=Type n grain\n");
				else if(i==3)
					writer.write("# ppm of user defined radio-element in Mtx=matrix and Tn=Type n grain\n");
			}
			
			
			writer.write("\n			 #********** fine grain fraction description **********#\n\n");
			GrainFraction fine = model.getFineFraction();
			writer.write(fine.getVolumicFraction()+" # volumic fraction of the fine grains (without the matrix volumic fraction and on the total volume of sample)\n");
			writer.write(fine.getDescOfEachSize().size()+" # number of grain sizes\n\n");
			writer.write("# D = spherical grain diameter in mm\n");
			writer.write("# f = volumic fraction of this diameter\n");
			writer.write("# Tn = percent of grain type in this diameter\n\n");
			writer.write("D\tf\t");
			for(int i=0;i<model.getNbOfGrainTypes()-1;i++)
				writer.write("T"+(i+1)+"\t");
			writer.write("\n");
			for(List<Float> size : fine.getDescOfEachSize())
			{
				for(float value : size)
					writer.write(value+"\t");
				writer.write("\n");
			}
			
			writer.write("\nMtx\t");
			for(int i=0;i<model.getNbOfGrainTypes()-1;i++)
				writer.write("T"+(i+1)+"\t");
			writer.write("\n");
			for(int i=0;i<4;i++)
			{
				for(List<Float> type : fine.getDescOfEachType())
					writer.write(type.get(i)+"\t");
				if(i==0)
					writer.write("# % of K in Mtx=matrix and Tn=Type n grain\n");
				else if(i==1)
					writer.write("# ppm of U series in Mtx=matrix and Tn=Type n grain\n");
				else if(i==2)
					writer.write("# ppm of Th series in Mtx=matrix and Tn=Type n grain\n");
				else if(i==3)
					writer.write("# ppm of user defined radio-element in Mtx=matrix and Tn=Type n grain\n");
			}
			
			writer.close();
			
			return file;
			
		} catch (IOException e)
		{
			return null;
		}
	}
}
