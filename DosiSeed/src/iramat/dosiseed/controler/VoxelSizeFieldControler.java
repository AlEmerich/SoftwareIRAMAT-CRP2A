package iramat.dosiseed.controler;

import iramat.dosiseed.model.AbstractModel;

import java.awt.event.FocusEvent;
import java.awt.event.KeyEvent;


public class VoxelSizeFieldControler extends AbstractFieldControler
{
	private boolean isCoarse;
	
	public VoxelSizeFieldControler(AbstractModel model, boolean coarse)
	{
		super(model);
		this.isCoarse = coarse;
	}

	@Override
	public void focusGained(FocusEvent arg0)
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void focusLost(FocusEvent e)
	{
		if(isCoarse)
		{
			float nb = model.getCoarseFraction().getVoxelSize();
			try
			{
				if(this.getFloatOfSource(e) != -1)
					nb = this.getFloatOfSource(e);
			}catch(Exception e1)
			{
				
			}
			model.getCoarseFraction().setVoxelSize(nb);
			model.getCoarseFraction().notifyObservers();
		}
		else
		{
			float nb = model.getFineFraction().getVoxelSize();
			try
			{
				if(this.getFloatOfSource(e) != -1)
					nb = this.getFloatOfSource(e);
			}catch(Exception e1)
			{
				
			}
			model.getFineFraction().setVoxelSize(nb);
			model.getFineFraction().notifyObservers();
		}
		model.notifyObservers();
	}

	@Override
	public void keyPressed(KeyEvent e)
	{
		if(e.getKeyCode() == KeyEvent.VK_ENTER)
		{
			if(isCoarse)
			{
				float nb = model.getCoarseFraction().getVoxelSize();
				try
				{
					if(this.getFloatOfSource(e) != -1)
						nb = this.getFloatOfSource(e);
				}catch(Exception e1)
				{
					
				}
				model.getCoarseFraction().setVoxelSize(nb);
				model.getCoarseFraction().notifyObservers();
			}
			else
			{
				float nb = model.getFineFraction().getVoxelSize();
				try
				{
					if(this.getFloatOfSource(e) != -1)
						nb = this.getFloatOfSource(e);
				}catch(Exception e1)
				{
					
				}
				model.getFineFraction().setVoxelSize(nb);
				model.getFineFraction().notifyObservers();
			}
		}
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
			if(isCoarse)
			{
				model.getCoarseFraction().setVoxelSize(this.getFloatOfSource(e));
				model.getCoarseFraction().notifyObservers();
			}
			else
			{
				model.getFineFraction().setVoxelSize(this.getFloatOfSource(e));
				model.getFineFraction().notifyObservers();
			}
		}
	}
}
