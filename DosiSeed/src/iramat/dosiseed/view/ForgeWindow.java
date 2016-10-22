package iramat.dosiseed.view;

import iramat.dosiseed.controler.ForgeControler;
import iramat.dosiseed.controler.MassControler;
import iramat.dosiseed.controler.SetDensityControler;
import iramat.dosiseed.controler.WaterFractionControler;
import iramat.dosiseed.model.AbstractModel;
import iramat.dosiseed.model.ColoredMaterial;
import iramat.dosiseed.model.Material;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;

import mainPackage.Component;

/**
 * One of the three panel the OngletManagement show in its tabs. 
 * It contains the material factory and the component factory.
 * @author alan
 * @see Material
 * @see Component
 */
public class ForgeWindow extends JPanel implements ActionListener, ComponentListener
{
    private static final long serialVersionUID = 2101004009366159933L;
    
    /**
     * North and South components of the forge.
     */
    private NorthMaterialPanel FactoryMaterialPan;
    private SouthComponentPanel FactoryComponentPan;
    
    /**
     * Icon arrows.
     */
    ImageIcon downArrow;
    ImageIcon upArrow;
    
    /**
     * Button to hide the south component.
     */
    private JButton ShowHideSouthButton;
    
    /**
     * Constructor. Construct and initialize the component and the material factory.
     * @param forgeControler the controler in MVC pattern.
     * @param model the model in MVC pattern.
     * @param water true if a field water content is needed, false if not.
     */
    public ForgeWindow(ForgeControler forgeControler, AbstractModel model,boolean water) {
        this.setLayout(new BorderLayout());
        try {
            this.FactoryMaterialPan = new NorthMaterialPanel(forgeControler,water);
            this.FactoryComponentPan = new SouthComponentPanel(forgeControler);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        
        forgeControler.setComponentFactory(this.FactoryComponentPan);
        forgeControler.setMaterialFactory(this.FactoryMaterialPan);
        FactoryMaterialPan.setFieldControler(new SetDensityControler(model, FactoryMaterialPan),
        		new MassControler(model, FactoryMaterialPan),
        		new WaterFractionControler(model, FactoryMaterialPan));
        
        this.add(this.FactoryMaterialPan, "North");
        this.downArrow = View.createImageIcon("/resources/DownArrow.png");
        this.upArrow = View.createImageIcon("/resources/UpArrow.png");
        (this.ShowHideSouthButton = new JButton("Show the component factory", this.upArrow)).addActionListener(this);
        final JPanel GlobalSouth = new JPanel(new BorderLayout());
        GlobalSouth.add(this.ShowHideSouthButton, "North");
        GlobalSouth.add(this.FactoryComponentPan, "South");
        this.FactoryComponentPan.setVisible(false);
        this.add(GlobalSouth, "South");
        
        model.addObserver(FactoryComponentPan);
        model.addObserver(FactoryMaterialPan);
        
        this.addComponentListener(this);
    }
    
    private void openComponentFactory()
    {
    	this.ShowHideSouthButton.setIcon(this.downArrow);
            this.ShowHideSouthButton.setText("Hide the component factory");
            this.FactoryComponentPan.setVisible(true);
            final JScrollBar scroll = ((JScrollPane)this.getParent().getParent()).getVerticalScrollBar();
            ((JScrollPane)this.getParent().getParent()).validate();
            scroll.setValue(scroll.getMaximum());
    }
    
    private void closeComponentFactory()
    {
    	this.ShowHideSouthButton.setIcon(this.upArrow);
            this.ShowHideSouthButton.setText("Show the component factory");
            this.FactoryComponentPan.setVisible(false);
    }
    
    /**
     * Triggered when an action is performed, like clicking on a button.
     */
    @Override
    public void actionPerformed(final ActionEvent e) {
        if (this.FactoryComponentPan.isVisible())
            closeComponentFactory();
        else
            openComponentFactory();
    }

	@Override
	public void componentHidden(ComponentEvent e)
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void componentMoved(ComponentEvent e)
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void componentResized(ComponentEvent e)
	{
		int sizeForge = this.getSize().height;
		int sizeMaterial = FactoryMaterialPan.getSize().height;
		int sizeComp = FactoryComponentPan.getPreferredSize().height;
		
		if(sizeMaterial + sizeComp <= sizeForge)
			if(!FactoryComponentPan.isVisible())
				openComponentFactory();
	}

	@Override
	public void componentShown(ComponentEvent e)
	{
		// TODO Auto-generated method stub
		
	}

	public void setCurrentMaterial(ColoredMaterial material)
	{
		// TODO Auto-generated method stub
		this.FactoryMaterialPan.setCurrentMaterial(material);
	}
}
