package iramat.dosiedit2d.controler;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import iramat.dosiedit2d.model.Dosi2DModel;
import iramat.dosiedit2d.view.DoubleFormattedField;
import iramat.dosiedit2d.view.EditorGridPane;
import iramat.dosiseed.view.TripleFormattedField.XYZ;

/**
 * Controller of the numbers of voxel field in view and model.
 * @author alan
 * @see EditorGridPane
 */
public class NbVoxel2DControler implements KeyListener
{
	/**
	 * The model to work with.
	 */
	private Dosi2DModel model;

	/**
	 * The view to update.
	 */
	private DoubleFormattedField fields;

	/**
	 * Constructor.
	 * @param model the model to work with.
	 */
	public NbVoxel2DControler(Dosi2DModel model)
	{
		this.model = model;
	}

	/**
	 * Set the fileds view to update.
	 * @param fields the fields view to update.
	 */
	public void setView(DoubleFormattedField fields)
	{
		this.fields = fields;
	}

	@Override
	public void keyPressed(KeyEvent e)
	{
		if(e.getKeyCode() == KeyEvent.VK_ENTER)
		{
			validation();
		}
		model.notifyObservers();
	}

	@Override
	public void keyReleased(KeyEvent e)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void keyTyped(KeyEvent e)
	{
		if(e.getKeyCode() == KeyEvent.VK_ENTER)
		{
			validation();
		}
		model.notifyObservers();
	}

	public void validation()
	{
		int x=(int) model.getGrid().get(0).size(),
				y=(int) model.getGrid().size();
		try
		{
			x = (int) fields.getValue(XYZ.X);
		} catch (Exception e1)
		{
		}
		try
		{
			y = (int) fields.getValue(XYZ.Y);
		} catch (Exception e1)
		{
		}

		if(x>0 && y>0)
			model.resizeWorld(x,y);
	}

}
