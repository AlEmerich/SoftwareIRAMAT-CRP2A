package iramat.dosiedit2d.controler;

import java.awt.event.FocusEvent;
import java.awt.event.KeyEvent;

import iramat.dosiedit2d.model.Dosi2DModel;
import iramat.dosiedit2d.view.EditorGridPane;
import iramat.dosiseed.controler.AbstractFieldControler;

/**
 * Controller of the clock value in EditorGridPane and model.
 * @author alan
 * @see EditorGridPane
 */
public class ClockValueControler extends AbstractFieldControler
{
	/**
	 * Constructor.
	 * @param model the model with a clock value field.
	 */
	public ClockValueControler(Dosi2DModel model)
	{
		super(model);
	}
	
	@Override
	public void keyPressed(KeyEvent e)
	{
		if(e.getKeyCode() == KeyEvent.VK_ENTER)
		{
			float nb = ((Dosi2DModel)model).getClockValue();
			try
			{
				if(this.getFloatOfSource(e) != -1)
					nb = this.getFloatOfSource(e);
				
			}catch(Exception e1)
			{
			}
			((Dosi2DModel)model).setClockValue(nb);
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

	@Override
	public void focusGained(FocusEvent arg0)
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void focusLost(FocusEvent e)
	{
		float nb = ((Dosi2DModel)model).getClockValue();
		try
		{
			if(this.getFloatOfSource(e) != -1)
				nb = this.getFloatOfSource(e);
			
		}catch(Exception e1)
		{
		}
		((Dosi2DModel)model).setClockValue(nb);

		model.notifyObservers();
	}
}
