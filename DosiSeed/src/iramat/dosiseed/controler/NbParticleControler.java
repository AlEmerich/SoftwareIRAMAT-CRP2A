package iramat.dosiseed.controler;

import java.awt.event.FocusEvent;
import java.awt.event.KeyEvent;

import iramat.dosiseed.model.AbstractModel;


public class NbParticleControler extends AbstractFieldControler
{
	public NbParticleControler(AbstractModel model)
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
		float nb = model.getNbParticlesEmitted();
		try
		{
			if(this.getFloatOfSource(arg0) != -1)
				nb = this.getFloatOfSource(arg0);
		}
		catch(Exception e)
		{
		}
		
		model.setNbParticlesEmitted(nb);
		model.notifyObservers();
	}

	@Override
	public void keyPressed(KeyEvent arg0)
	{
		if(arg0.getKeyCode() == KeyEvent.VK_ENTER)
		{		
			float nb = model.getNbParticlesEmitted();
			System.err.println(nb);
			try
			{
				if(this.getFloatOfSource(arg0) != -1)
					nb = this.getFloatOfSource(arg0);
			}
			catch(Exception e)
			{
			}
			
			model.setNbParticlesEmitted(nb);
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
