package iramat.dosiseed.controler;

import iramat.dosiseed.model.AbstractModel;

import java.awt.event.FocusEvent;
import java.awt.event.KeyEvent;


public class WfControler extends AbstractFieldControler
{

	public WfControler(AbstractModel model)
	{
		super(model);
	}

	@Override
	public void focusGained(FocusEvent arg0)
	{
	}

	@Override
	public void focusLost(FocusEvent arg0)
	{
		float nb = model.getWfInMatrix();
		try
		{
			if(this.getFloatOfSource(arg0) != -1)
				nb = this.getFloatOfSource(arg0);
		}
		catch(Exception e1)
		{
			
		}
		model.setWfInMatrix(nb);
		model.notifyObservers();
	}

	@Override
	public void keyPressed(KeyEvent arg0)
	{
		if(arg0.getKeyCode() == KeyEvent.VK_ENTER)
		{		
			float nb = model.getWfInMatrix();
			try
			{
				if(this.getFloatOfSource(arg0) != -1)
					nb = this.getFloatOfSource(arg0);
			}
			catch(Exception e1)
			{
				
			}
			model.setWfInMatrix(nb);
			model.notifyObservers();
		}		
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
