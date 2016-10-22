package iramat.dosiseed.controler;

import iramat.dosiseed.model.AbstractModel;
import iramat.dosiseed.model.GrainFraction;
import iramat.dosiseed.view.GrainFractionPanel;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;



public class GrainFractionControler implements ActionListener
{
	private AbstractModel model;
	
	private TableSizeControler tableSizeControl;
	
	private TableTypeControler tableTypeControl;
	
	private GrainFractionPanel grainFractionView;
	
	private boolean isCoarse;
	
	public GrainFractionControler(GrainFractionPanel view,AbstractModel model,boolean isCoarse)
	{
		tableSizeControl = new TableSizeControler(model,isCoarse);
		tableTypeControl = new TableTypeControler(model,isCoarse);
		this.grainFractionView = view;
		this.isCoarse = isCoarse;
		this.model = model;
	}

	/**
	 * @return the tableSizeControl
	 */
	public TableSizeControler getTableSizeControl()
	{
		return tableSizeControl;
	}

	/**
	 * @return the tableTypeControl
	 */
	public TableTypeControler getTableTypeControl()
	{
		return tableTypeControl;
	}
	

	@Override
	public void actionPerformed(ActionEvent e)
	{
		GrainFraction fractionModel =
				(isCoarse ? model.getCoarseFraction() : model.getFineFraction());
		if(e.getActionCommand().equals("random"))
		{
			fractionModel.setDistributionMaterial(false);
		}
		else if(e.getActionCommand().equals("recurent"))
		{
			fractionModel.setDistributionMaterial(true);
		}
		else if(e.getActionCommand().equals("add size of grain"))
		{
			if(fractionModel.getNbTypeGrain() > 0 || 
					(isCoarse && model.getFineFraction().getNbTypeGrain() == 1))
				fractionModel.addSizeOfGrain();
		}
		else if(e.getActionCommand().equals("remove size of grain"))
		{
			if(fractionModel.getNbTypeGrain() > 0 ||
					(isCoarse && model.getFineFraction().getNbTypeGrain() == 1))
				fractionModel.removeSizeOfGrain(this.grainFractionView.getSelectedSizeOfGrain());	
		}
		model.notifyObservers();
	}
}
