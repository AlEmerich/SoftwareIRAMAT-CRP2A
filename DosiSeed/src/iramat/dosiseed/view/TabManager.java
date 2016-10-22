package iramat.dosiseed.view;

import iramat.dosiseed.controler.ForgeControler;
import iramat.dosiseed.controler.GlobalControler;
import iramat.dosiseed.model.AbstractModel;
import iramat.dosiseed.model.Incoherence;
import iramat.dosiseed.model.Model;
import iramat.dosiseed.model.Incoherence.Inc;

import javax.swing.ImageIcon;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;


public class TabManager extends JTabbedPane
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 6187870258276765132L;

	private HighContainerGlobal highContainerglobal;
	
	private ForgeWindow forge;
	
	private Model model;
	
	private static int UNIT_INCREMENT_SCROLL = 10;
	
	public TabManager(ForgeControler forgeControler, final GlobalControler controler, final AbstractModel model, ImageIcon homeIcon, ImageIcon forgeIcon)
	{
		JScrollPane scroll1 = new JScrollPane(highContainerglobal = new HighContainerGlobal(controler,model));
		scroll1.getVerticalScrollBar().setUnitIncrement(UNIT_INCREMENT_SCROLL);
		this.addTab("Global Information", homeIcon, scroll1);
		
		JScrollPane scroll2 = new JScrollPane(forge = new ForgeWindow(forgeControler,model,false));
		scroll2.getVerticalScrollBar().setUnitIncrement(UNIT_INCREMENT_SCROLL);
		this.addTab("Forge", forgeIcon, scroll2);
		
		this.model = (Model) model;
	}

	public HighContainerGlobal getHighGlobal()
	{
		return highContainerglobal;
	}
	
	public ForgeWindow getForgeWindow()
	{
		return forge;
	}

	public int getSelectedType()
	{
		return this.highContainerglobal.getGlobalPanel().getSelectedType();
	}
	
	@Override
    public void setSelectedIndex(final int index) 
	{	
		if(model != null)
		{
			try
			{
				this.model.isValid();
				
			} catch (Incoherence e)
			{
				if(e.whoIsGuilty() == Inc.MATERIAL && index != 1)
					View.showIncoherenceWindow(this, e.whoIsGuilty(), e.getMessage());
				
				if(e.whoIsGuilty() == Inc.MATERIAL)
					super.setSelectedIndex(1);
				else
				{
					View.showIncoherenceWindow(this, e.whoIsGuilty(), e.getMessage());
					super.setSelectedIndex(0);
				}
				return;
			}
		}
		
		super.setSelectedIndex(index);
	}
}
