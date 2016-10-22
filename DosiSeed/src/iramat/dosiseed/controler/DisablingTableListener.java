package iramat.dosiseed.controler;

import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;

public abstract class DisablingTableListener implements TableModelListener
{
	protected static boolean active = true;
	
	public static void setActive(boolean active)
	{
		DisablingTableListener.active = active;
	}
	
	@Override
	public abstract void tableChanged(TableModelEvent e);
	
}
