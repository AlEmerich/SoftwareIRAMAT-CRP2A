package iramat.dosiseed.controler;

import iramat.dosiseed.model.AbstractModel;
import iramat.dosiseed.model.Material;
import iramat.dosiseed.view.NorthMaterialPanel;

import java.awt.event.FocusEvent;
import java.awt.event.KeyEvent;


public class SetDensityControler extends AbstractFieldControler
{
	private NorthMaterialPanel matFactory;
	
	public SetDensityControler(AbstractModel model, NorthMaterialPanel view)
	{
		super(model);
		this.matFactory = view;
	}
	
	@Override
	public void focusGained(FocusEvent arg0)
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void focusLost(FocusEvent e)
	{
		Material currentMaterial = matFactory.getCurrentMaterial();
		if(currentMaterial != null)
		{
			float nb = currentMaterial.getDensity();
			try
			{
				if(this.getFloatOfSource(e) != -1)
					nb = this.getFloatOfSource(e);
			}catch(Exception e1)
			{
				
			}
			currentMaterial.setDensity(nb);
			ForgeControler.reloadCalculatedMassAndDensity(currentMaterial);
			model.setChanged();
		}
		
		model.notifyObservers();
	}

	@Override
	public void keyPressed(KeyEvent e)
	{
		Material currentMaterial = matFactory.getCurrentMaterial();
		if(e.getKeyCode() == KeyEvent.VK_ENTER && currentMaterial != null)
		{		
			float nb = currentMaterial.getDensity();
			try
			{
				if(this.getFloatOfSource(e) != -1)
					nb = this.getFloatOfSource(e);
			}catch(Exception e1)
			{
				
			}
			currentMaterial.setDensity(nb);
			ForgeControler.reloadCalculatedMassAndDensity(currentMaterial);
			model.setChanged();
		}		
		
		model.notifyObservers();
		
	}

	@Override
	public void keyReleased(KeyEvent arg0)
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyTyped(KeyEvent arg0)
	{
		// TODO Auto-generated method stub
		
	}
}
