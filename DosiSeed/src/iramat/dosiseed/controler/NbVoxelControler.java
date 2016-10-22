package iramat.dosiseed.controler;

import iramat.dosiseed.model.AbstractModel;
import iramat.dosiseed.model.Model;
import iramat.dosiseed.view.TripleFormattedField;
import iramat.dosiseed.view.TripleFormattedField.XYZ;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;



public class NbVoxelControler implements KeyListener
{
	private AbstractModel model;
	private TripleFormattedField fields;
	
	public NbVoxelControler(AbstractModel model )
	{
		this.model = model;
	}
	
	public void setFieldsView(TripleFormattedField fields)
	{
		this.fields = fields;
	}

	@Override
	public void keyPressed(KeyEvent e)
	{
		if(e.getKeyCode() == KeyEvent.VK_ENTER)
		{
			int x=(int) ((Model)model).getNbVoxelsInDetector().getX(),
					y=(int) ((Model)model).getNbVoxelsInDetector().getY(),
					z=(int) ((Model)model).getNbVoxelsInDetector().getZ();
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
			try
			{
				z = (int) fields.getValue(XYZ.Z);
			} catch (Exception e1)
			{
			}
			((Model)model).setNbVoxelsInDetector(x,y,z);
		}
		model.notifyObservers();
	}

	@Override
	public void keyReleased(KeyEvent arg0)
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyTyped(KeyEvent e)
	{
		if(e.getKeyCode() == KeyEvent.VK_ENTER)
		{
			try
			{
				((Model)model).setNbVoxelsInDetector(fields.getValue(XYZ.X), 
						fields.getValue(XYZ.Y),
						fields.getValue(XYZ.Z));
			} catch (Exception e1)
			{
			}
		}
		model.notifyObservers();
	}

}
