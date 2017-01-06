package iramat.dosiedit2d.controler;

import iramat.dosiedit2d.main.Main;
import iramat.dosiedit2d.model.Dosi2DModel;
import iramat.dosiedit2d.model.VoxelObject;
import iramat.dosiedit2d.view.Association2dFrame;
import iramat.dosiedit2d.view.DoubleFormattedField;
import iramat.dosiedit2d.view.ScanningWindow;
import iramat.dosiedit2d.view.View;
import iramat.dosiseed.model.AbstractModel;
import iramat.dosiseed.model.ColoredMaterial;
import iramat.dosiseed.model.Incoherence;
import iramat.dosiseed.model.Material;
import mainPackage.PrimaryParticles;
import mainPackage.SaveBox;
import util.Couple;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * The global controller of the project. It handles project action, like saving,
 * loading, quitting, generating pilot text file... And global informations
 * in the model.
 * @author alan
 * @see ActionListener
 */
public class GlobalControler implements ActionListener
{
	/**
	 * The view to work with.
	 */
	private View view;
	
	/**
	 * The model which handles project information.
	 */
	private AbstractModel model;
	
	/**
	 * This boolean is used to prevent the application for quitting when the user
	 * stop the quitting procedure.
	 */
	private boolean preventToQuit = false;
	
	/**
	 * Constructor. Set the model.
	 * @param model
	 */
	public GlobalControler(AbstractModel model)
	{
		this.model = model;
	}

	/**
	 * Ask to the user if he want to save the current project.
	 * @return true if the user has not canceled the action.
	 */
    private boolean requestSaveProject()
    {
    	final int result = View.yesnocancel(view,"Do you want to save the current project ?");
        if (result == JOptionPane.OK_OPTION)
        	this.actionPerformed(new ActionEvent(this, 0, "Save"));
        else if(result == JOptionPane.CANCEL_OPTION
        		|| result == JOptionPane.CLOSED_OPTION)
        	return false;
        return true;
    }
    
    /**
     * Function which load a serializable object indicates by the filename path. 
     * This function is not generic, it loads a {@link SaveBox} object.
     * @param filePath the path to the file to load.
     */
	public void load(final String filePath)
	{
		final JDialog modalWaiting = new JDialog(view, "Please wait", true);
        final JLabel saveLab = new JLabel("Loading file...");
        modalWaiting.setSize(200, 80);
        modalWaiting.setLayout(new BorderLayout());
        modalWaiting.setLocationRelativeTo(view);
        modalWaiting.setResizable(false);
        modalWaiting.setDefaultCloseOperation(0);
        modalWaiting.add(saveLab);
		final Thread t = new Thread() {
            @Override
            public void run() {
                Main.main(new String[]{filePath});
                modalWaiting.setVisible(false);
            }
        };
        t.start();
        modalWaiting.setVisible(true);
        
        view.dispose();
	}
	
	/**
	 * Save the current project to the path.
	 * @param path the path where to save the file.
	 */
	public void save(final String path) {
		
		final Thread t = new Thread(() -> {
            ObjectOutputStream oos = null;
            try {

                final File file = new File(path);
                oos = new ObjectOutputStream(new FileOutputStream(file));

                oos.writeObject(GlobalControler.this.model);
                GlobalControler.this.model.setCURRENT_PATH(file.getAbsolutePath());
                GlobalControler.this.model.notifyObservers();
            }
            catch (FileNotFoundException e)
            {
                e.printStackTrace();
            }
            catch (IOException e2)
            {
                e2.printStackTrace();
                JOptionPane.showMessageDialog(null, e2.getMessage(), "Error during the reading of the file", 0);
            }
            finally
            {
                if (oos != null) {
                    try {
                        oos.close();
                    }
                    catch (IOException e4) {
                        e4.printStackTrace();
                        JOptionPane.showMessageDialog(null, e4.getMessage(), "Error during the closing of the file reader", 0);
                    }
                }
            }
            if (oos != null) {
                try {
                    oos.close();
                }
                catch (IOException e4) {
                    e4.printStackTrace();
                    JOptionPane.showMessageDialog(null, e4.getMessage(), "Error during the closing of the file reader", 0);
                }
            }

            JOptionPane.showMessageDialog(view, "Project saved !");
        });
        t.start();
	}
	
	/**
	 * Quit the application if {@link #preventToQuit} is false.
	 */
	public void Quit()
    {
		if(!preventToQuit)
		{
			view.dispose();
			System.exit(0);
		}
		else
			preventToQuit = false;
    }
	
	@Override
	public void actionPerformed(ActionEvent e)
	{
		switch(e.getActionCommand())
		{
			/**
			 * MENU COMMAND
			 */
			case "New project":
				if(!requestSaveProject())
					return;
				model.reset();
				break;
			case "Load file":
				if(!requestSaveProject())
					return;
				String path = view.showLoadingWindow();
				if(!path.equals(""))
					this.load(path);
				break;
			case "Save":
				String saving = "";
				
				if(model.getCURRENT_PATH().isEmpty())
				{
					saving = view.showSavingWindow();
					if(saving.equals("CLOSED"))
					{
						preventToQuit = true;
						return;
					}
					this.save(saving);
				}
				else
					this.save(model.getCURRENT_PATH());
				break;
			case "Save as":
				String savingAs = view.showSavingWindow();
				if(savingAs.equals("CLOSED"))
				{
					preventToQuit = true;
					return;
				}
				this.save(savingAs);
				break;
			case "Generate":
				try
				{
					((Dosi2DModel) model).isValid();
				} catch (Incoherence e1)
				{
					View.showIncoherenceWindow(view, e1.whoIsGuilty(), e1.getMessage());
					return;
				}
				// check if no radio-element is selected
				boolean[] radio = model.getWhichRadioelementIsSimulated();
				if(radio[0] == false && radio[1] == false && radio[2] == false && radio[3] == false)
				{
					final int result = View.yesnocancel(view, "No radio-element is selected. Continue ?");
					if(result != JOptionPane.YES_OPTION)
						return;
					
				}
				File file = Dosi2dPtfWriter.generate((Dosi2DModel) model, view.showGenerateWindow());
				if(file == null)
					return;
				int result = View.yesnocancel(view,"Open the PTF file to check it ? (Read only)");
				if(result == JOptionPane.OK_OPTION)
					view.openPTFFile(file);
				break;
			case "load image":
				ScanningWindow scan = View.showScanningWindow(view);
				
				if(scan != null && scan.isScanValid())
				{
					Association2dFrame assocWin = new Association2dFrame(view, "Associate color value with material",
						true, scan, model.getListOfMaterial());
					scan.getSelectedPanel().setSelected(false);
					assocWin.setVisible(true);
					
					if(scan.getSelectedPanel() != null && assocWin.getListAssociations() != null
							&& assocWin.isAssocValid())
					{
						this.loadImage(scan.getSelectedPanel().getImage(),assocWin.getListAssociations());
						view.setLoadTitle(scan.getSelectedPanel().getNameFile());
					}
					
				}
					
				break;
			case "Quit":
				if(!requestSaveProject())
					return;
				this.Quit();
				break;
			case "Help":
				view.Help();
				break;
				
				/**
				 * MAIN INFO COMMAND
				 */
				
			case "type particle":
				JComboBox<?> source;
				if( e.getSource() instanceof JComboBox<?>)
					source = (JComboBox<?>) e.getSource();
				else
					return;
				model.setPrimaryParticle((PrimaryParticles) source.getSelectedItem());
				break;
			case "emission process":
				JComboBox<?> source2;
				if( e.getSource() instanceof JComboBox<?>)
					source2 = (JComboBox<?>) e.getSource();
				else
					return;
				((Dosi2DModel) model).setEmissionProcess((String)source2.getSelectedItem());
				break;
			case "dose mapping":
				JComboBox<?> source3;
				if( e.getSource() instanceof JComboBox<?>)
					source3 = (JComboBox<?>) e.getSource();
				else
					return;
				((Dosi2DModel) model).setMaterialForDoseMapping((Material) source3.getSelectedItem());
				break;
			case "exclude edge":
				boolean exclude = ((JCheckBox)e.getSource()).isSelected();
				((Dosi2DModel) model).setExcludeEdge(exclude);
				break;
			case "K boolean":
				boolean k = ((JCheckBox)e.getSource()).isSelected();
				model.setPotassiumSimulated(k);
				break;
				
			case "U boolean":
				boolean u = ((JCheckBox)e.getSource()).isSelected();
				model.setUraniumSimulated(u);
				break;
				
			case "Th boolean":
				boolean th = ((JCheckBox)e.getSource()).isSelected();
				model.setThoriumSimulated(th);
				break;
				
			case "Ud boolean":
				boolean ud = ((JCheckBox)e.getSource()).isSelected();
				model.setUserDefSimulated(ud);
				break;
				default:
					break;
		}
		
		model.notifyObservers();
	}
	
	/**
	 * Set the view of the application.
	 * @param v
	 */
	public void setView(View v)
	{
		this.view = v;
		model.addObserver(v);
	}
	
	/***********************************************
	 *               GRID LISTENER
	 ***********************************************/
	
	/**
	 * Fill layer with the current material.
	 * @param justEmpty if true, only empty voxels will be filled. If false, all voxels will be.
	 */
	public void fillLayer(ColoredMaterial currentMaterial, final boolean justEmpty) {

		HashMap<Integer, HashMap<Integer, VoxelObject>> grid = ((Dosi2DModel) model).getGrid();
		for (int y = 0; y < grid.size(); ++y) {
			for (int x = 0; x < grid.get(y).size(); ++x) {
				final VoxelObject v = grid.get(y).get(x);
				if (justEmpty) {
					if (v.isEmpty()) {
						v.setMaterialIndex(currentMaterial);
					}
				}
				else {
					v.setMaterialIndex(currentMaterial);
				}
			}
		}
		model.setChanged();
		model.notifyObservers();
	}

	/**
	 * Call the namesake method in the model.
	 * @param x the mouse x coordinate.
	 * @param y the mouse y coordinate.
	 * @param currentMaterial the material to fill with.
	 * @see Dosi2DModel#checkAndFill(double, double, ColoredMaterial)
	 */
	public void checkAndFill(double x, double y, ColoredMaterial currentMaterial)
	{
		((Dosi2DModel) model).checkAndFill(x, y, currentMaterial);
		model.notifyObservers();
	}

	/**
	 * Call the namesake method in the model.
	 * @param cursorRectSelection the rectangle to check with.
	 * @param currentMaterial the material to fill with.
	 * @see Dosi2DModel#checkAndFill(java.awt.geom.Rectangle2D.Float, ColoredMaterial)
	 */
	public void checkAndFill(Rectangle2D.Float cursorRectSelection,ColoredMaterial currentMaterial)
	{
		((Dosi2DModel) model).checkAndFill(cursorRectSelection, currentMaterial);
		model.notifyObservers();
	}

	/**
	 * Call the namesake method in the model.
	 * @param m the material to reload reference.
	 * @see Dosi2DModel#reloadReferenceRadiationValue(Material)
	 */
	public void reloadReferenceRadiationValue(Material m)
	{
		((Dosi2DModel) model).reloadReferenceRadiationValue(m);
	}

	/**
	 * Set the current radiation value to the current material.
	 * @param currentMaterial the material to update.
	 * @param u the new uranium value.
	 * @param th the new thorium value.
	 * @param k the new potassium value.
	 * @param ud the new user-defined radiation value.
	 */
	public void setCurrentRadValue(ColoredMaterial currentMaterial, float u,
			float th, float k, float ud)
	{
		currentMaterial.setCurrentUraniumValue(u);
		currentMaterial.setCurrentThoriumValue(th);
		currentMaterial.setCurrentPotassiumValue(k);
		currentMaterial.setCurrentUserDefinedValue(ud);
		model.setChanged();
		model.notifyObservers();
	}

	/**
	 * Set the reference radiation value to the current material.
	 * @param currentMaterial the material to update.
	 * @param u the new reference value for uranium.
	 * @param th the new reference value for thorium.
	 * @param k the new reference value for potassium.
	 * @param ud the new reference value for user-defined value.
	 */
	public void setRefRadValue(ColoredMaterial currentMaterial, float u,
			float th, float k,float ud)
	{
		currentMaterial.setRefUraniumValue(u);
		currentMaterial.setRefThoriumValue(th);
		currentMaterial.setRefPotassiumValue(k);
		currentMaterial.setRefUserDefinedValue(ud);
		model.setChanged();
		model.notifyObservers();
	}

	/**
	 * Get the width of the voxelized grid on the screen.
	 * @param scaleX the width scale transformation.
	 * @return the size in pixel.
	 */
	public double getWidthScreen(double scaleX)
	{
		//Dosi2DModel model = (Dosi2DModel) this.model;
		return scaleX * /* model.getVoxelDimensionX() */ VoxelObject.FACTOR_DISPLAY_GRID;
	}
		
	/**
	 * Get the height of the voxelized grid on the screen.
	 * @param scaleY the height scale transformation.
	 * @return the height size in pixel.
	 */
	public double getHeightScreen(double scaleY)
	{
		//Dosi2DModel model = (Dosi2DModel) this.model;
		return scaleY  */* model.getVoxelDimensionY() */ VoxelObject.FACTOR_DISPLAY_GRID;
	}

	/**
	 * Get the number of voxel in the X-axis.
	 * @return the number of voxel in the X-axis.
	 */
	public int getNbVoxelX()
	{
		return ((Dosi2DModel) model).getGrid().get(0).size();
	}

	/**
	 * Get the number of voxel in the Y-axis.
	 * @return the number of voxel in the Y-axis.
	 */
	public int getNbVoxelY()
	{
		return ((Dosi2DModel) model).getGrid().size();
	}
	
	/**
	 * Load a gray shade image text file into the object. Each pixel become a voxel.
	 * @param image the image to load.
	 * @param associations a list of couple which handles associations between
	 * 		gray shade value in the image and material.
	 * @see Dosi2DModel#loadImage(BufferedImage, ArrayList)
	 */
	public void loadImage(BufferedImage image,ArrayList<Couple<Material, Integer>> associations)
	{
		((Dosi2DModel) model).loadImage(image,associations);
	}

	public void validateNumberAndSize(DoubleFormattedField nbVoxelField,
			DoubleFormattedField sizeVoxelField)
	{
		((NbVoxel2DControler) nbVoxelField.getXField().getKeyListeners()[0]).validation();
		((SizeVoxelControler) sizeVoxelField.getXField().getKeyListeners()[0]).validation();
		model.notifyObservers();
	}

	public void askForUpdate()
	{
		model.setChanged();
		model.notifyObservers();
	}
}
