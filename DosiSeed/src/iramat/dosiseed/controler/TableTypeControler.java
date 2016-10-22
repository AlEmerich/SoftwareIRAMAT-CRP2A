package iramat.dosiseed.controler;

import iramat.dosiseed.model.AbstractModel;
import iramat.dosiseed.model.GrainFraction;

import javax.swing.event.TableModelEvent;
import javax.swing.table.DefaultTableModel;


public class TableTypeControler extends DisablingTableListener
{
	private AbstractModel model;

	private boolean isCoarse;

	public TableTypeControler(AbstractModel model,boolean coarse)
	{
		this.model = model;
		this.isCoarse = coarse;
	}

	@Override
	public void tableChanged(TableModelEvent e)
	{
		if(active)
		{
			DefaultTableModel table = (DefaultTableModel) e.getSource();
			GrainFraction fraction = isCoarse ? model.getCoarseFraction() : model.getFineFraction();

			if(e.getType() == TableModelEvent.UPDATE)
			{
				int row,col;
				if((row = e.getFirstRow()) != -1 && (col = e.getColumn()) != -1)
				{
					String cell = ((String) table.getValueAt(row, col)).replaceAll("\\s+", "");
					String[] toJoin = cell.replaceAll("[^0-9.]","").split("\\.");

					if(toJoin.length > 0)
					{
						cell = toJoin[0]+".";
						for(int i=1;i<toJoin.length;i++)
							cell += toJoin[i];
						
						float nb = fraction.getValueOfType(row, col);
						try
						{
							nb = Float.parseFloat(cell);
						}catch(Exception e1)
						{
						}
						
						fraction.setValueOfType(row, col-1, nb);
					}
					else
						fraction.setValueOfType(row, col-1, 0);
				}
			}

			fraction.notifyObservers();
		}
		
	}
}
