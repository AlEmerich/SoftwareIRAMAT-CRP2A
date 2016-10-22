package iramat.dosiseed.view;

import iramat.dosiseed.controler.GlobalControler;
import iramat.dosiseed.controler.GrainFractionControler;
import iramat.dosiseed.controler.MaxRangeControler;
import iramat.dosiseed.controler.NbParticleControler;
import iramat.dosiseed.controler.NbVoxelControler;
import iramat.dosiseed.controler.ProductionCutControler;
import iramat.dosiseed.controler.VolumicFractionFieldControler;
import iramat.dosiseed.controler.VoxelSizeFieldControler;
import iramat.dosiseed.controler.WfControler;
import iramat.dosiseed.model.AbstractModel;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JPanel;



class HighContainerGlobal extends JPanel
{

	/**
	 * 
	 */
	private static final long	serialVersionUID	= 6996032761029090266L;
	
	/**
	 * UI swing components.
	 */
	private GrainFractionPanel coarseFractionPanel;
	
	private GrainFractionPanel fineFractionPanel;
	
	private GlobalInfoPanel globalPanel;

	HighContainerGlobal(final GlobalControler control, final AbstractModel model)
	{	
		this.setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		globalPanel = new GlobalInfoPanel();
		globalPanel.setGlobalInfoControler(control);
		globalPanel.setNbParticleControler(new NbParticleControler(model));
		globalPanel.setMaxRangeControler(new MaxRangeControler(model));
		globalPanel.setProductionCutControler(new ProductionCutControler(model));
		globalPanel.setWaterFractionControler(new WfControler(model));
		globalPanel.setNbVoxelControler(new NbVoxelControler(model));
		
		model.addObserver(globalPanel);
		
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.gridheight = 1;
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		this.add(globalPanel,gbc);
		
		coarseFractionPanel = new GrainFractionPanel(true);
		coarseFractionPanel.setControler(new VolumicFractionFieldControler(model,true),
				new VoxelSizeFieldControler(model,true),
				new GrainFractionControler(coarseFractionPanel,model,true));
		fineFractionPanel = new GrainFractionPanel(false);
		fineFractionPanel.setControler(new VolumicFractionFieldControler(model,false),
				new VoxelSizeFieldControler(model,false),
				new GrainFractionControler(fineFractionPanel,model, false));
		gbc.gridx = 0;
		gbc.gridy = 1;
		gbc.gridwidth = 1;
		gbc.gridheight = 1;
		gbc.anchor = GridBagConstraints.LINE_END;
		this.add(coarseFractionPanel,gbc);
		
		gbc.gridx = 2;
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		gbc.anchor = GridBagConstraints.LINE_START;
		this.add(fineFractionPanel,gbc);
		model.getCoarseFraction().addObserver(coarseFractionPanel);
		model.getFineFraction().addObserver(fineFractionPanel);
	}
	
	public GlobalInfoPanel getGlobalPanel()
	{
		return globalPanel;
	}
	
	public GrainFractionPanel getCoarseFractionPanel()
	{
		return coarseFractionPanel;
	}
	
	public GrainFractionPanel getFineFractionPanel()
	{
		return fineFractionPanel;
	}
}
