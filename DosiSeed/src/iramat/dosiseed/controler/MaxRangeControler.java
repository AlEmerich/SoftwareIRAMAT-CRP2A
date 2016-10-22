package iramat.dosiseed.controler;

import java.awt.event.FocusEvent;
import java.awt.event.KeyEvent;

import iramat.dosiseed.model.AbstractModel;

public class MaxRangeControler extends AbstractFieldControler
{
	public MaxRangeControler(AbstractModel model)
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
		float nb = model.getMaxRange();
		try
		{
			if(this.getFloatOfSource(arg0) != -1)
				nb = this.getFloatOfSource(arg0);
		}
		catch(Exception e1)
		{
			
		}
		model.setMaxRange(nb);
		model.notifyObservers();
	}

	@Override
	public void keyPressed(KeyEvent arg0)
	{
		if(arg0.getKeyCode() == KeyEvent.VK_ENTER)
		{	
			float nb = model.getMaxRange();
			try
			{
				if(this.getFloatOfSource(arg0) != -1)
					nb = this.getFloatOfSource(arg0);
			}
			catch(Exception e1)
			{
				
			}
			model.setMaxRange(nb);
		}		
		
		model.notifyObservers();
	}

	@Override
	public void keyReleased(KeyEvent arg0)
	{
	}

	@Override
	public void keyTyped(KeyEvent arg0)
	{
	}
}
