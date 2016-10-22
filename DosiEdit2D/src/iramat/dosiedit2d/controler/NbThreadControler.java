package iramat.dosiedit2d.controler;

import java.awt.event.FocusEvent;
import java.awt.event.KeyEvent;

import iramat.dosiedit2d.model.Dosi2DModel;
import iramat.dosiseed.controler.AbstractFieldControler;
import iramat.dosiseed.model.AbstractModel;

/**
 * Controller of the number of thread value in view and model.
 * @author alan
 *
 */
public class NbThreadControler extends AbstractFieldControler
{
	/**
	 * Controller.
	 * @param model the model with a number of thread value.
	 */
	public NbThreadControler(AbstractModel model)
	{
		super(model);
	}

	@Override
	public void focusGained(FocusEvent arg0)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void focusLost(FocusEvent e)
	{
		int nb = ((Dosi2DModel)model).getNbThread();
		try
		{
			if(this.getFloatOfSource(e) != -1)
				nb = (int) this.getFloatOfSource(e);
		}
		catch(Exception e1)
		{
		}
		((Dosi2DModel)model).setNbThread(nb);

		model.notifyObservers();
	}

	@Override
	public void keyPressed(KeyEvent e)
	{
		if(e.getKeyCode() == KeyEvent.VK_ENTER)
		{
			int nb = ((Dosi2DModel)model).getNbThread();
			
			try
			{
				if(this.getFloatOfSource(e) != -1)
					nb = (int) this.getFloatOfSource(e);
			}
			catch(Exception e1)
			{
			}
			((Dosi2DModel)model).setNbThread(nb);
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
