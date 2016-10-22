package iramat.dosiseed.controler;

import iramat.dosiseed.model.AbstractModel;
import iramat.dosiseed.view.NorthMaterialPanel;

import java.awt.event.FocusEvent;
import java.awt.event.KeyEvent;

import mainPackage.Component;
import util.Couple;

public class MassControler extends AbstractFieldControler
{
	private NorthMaterialPanel matFactory;
	
	public MassControler(AbstractModel model,NorthMaterialPanel view)
	{
		super(model);
		this.matFactory = view;
	}

	@Override
	public void focusGained(FocusEvent e)
	{
		
	}

	@Override
	public void focusLost(FocusEvent e)
	{
		Couple<Component,Float> currentComponentInMat = matFactory.getSelectedCompInMat();
		if(currentComponentInMat != null)
		{
			float nb = currentComponentInMat.getValeur2();
			try
			{
				if(this.getFloatOfSource(e) != -1)
					nb = this.getFloatOfSource(e);
			}catch(Exception e1)
			{
				
			}
			float newMass = nb;
			currentComponentInMat.setValeur2(newMass);
			ForgeControler.reloadCalculatedMassAndDensity(matFactory.getCurrentMaterial());
			model.setChanged();
		}
		
		model.notifyObservers();
	}

	@Override
	public void keyPressed(KeyEvent e)
	{
		Couple<Component,Float> currentComponentInMat = matFactory.getSelectedCompInMat();
		if(e.getKeyCode() == KeyEvent.VK_ENTER && currentComponentInMat != null)
		{
			float nb = currentComponentInMat.getValeur2();
			try
			{
				if(this.getFloatOfSource(e) != -1)
					nb = this.getFloatOfSource(e);
			}catch(Exception e1)
			{
				
			}
			float newMass = nb;
			currentComponentInMat.setValeur2(newMass);
			ForgeControler.reloadCalculatedMassAndDensity(matFactory.getCurrentMaterial());
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
