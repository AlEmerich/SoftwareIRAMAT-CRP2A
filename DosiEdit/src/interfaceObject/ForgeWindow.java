package interfaceObject;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;

import mainPackage.Component;
import mainPackage.Material;
import mainPackage.MaterialComponentProviderInterface;
import mainPackage.TopLevelInterface;

/**
 * One of the three panel the OngletManagement show in its tabs. 
 * It contains the material factory and the component factory.
 * @author alan
 * @see Material
 * @see Component
 */
public class ForgeWindow extends JPanel implements ActionListener
{
    private static final long serialVersionUID = 2101004009366159933L;
    
    /**
     * North and South components of the forge.
     */
    private NorthMaterialPanel FactoryMaterialPan;
    private SouthComponentPanel FactoryComponentPan;
    private JPanel GlobalSouth;
    
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
     * @param parent to get informations of the project.
     */
    public ForgeWindow(final MaterialComponentProviderInterface parent) {
        this.setLayout(new BorderLayout());
        try {
            this.FactoryMaterialPan = new NorthMaterialPanel(parent);
            (this.FactoryComponentPan = new SouthComponentPanel(parent)).addComponentAddedListener(this.FactoryMaterialPan);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        this.add(this.FactoryMaterialPan, BorderLayout.CENTER);
        this.downArrow = TopLevelInterface.createImageIcon("/resources/DownArrow.png");
        this.upArrow = TopLevelInterface.createImageIcon("/resources/UpArrow.png");
        (this.ShowHideSouthButton = new JButton("Show the component factory", this.upArrow)).addActionListener(this);
        this.GlobalSouth = new JPanel(new BorderLayout());
        GlobalSouth.add(this.ShowHideSouthButton, "North");
        GlobalSouth.add(this.FactoryComponentPan, "South");
        this.FactoryComponentPan.setVisible(false);
        this.GlobalSouth.add(ForgeWindow.this.ShowHideSouthButton, "North");
		this.GlobalSouth.add(ForgeWindow.this.FactoryComponentPan, "South");
		
        
        this.add(GlobalSouth, "South");
    }
    
    /**
     * @return true if all materials are valid, false if not.
     */
    public boolean IsValidTab() {
        return this.FactoryMaterialPan.checkIntegrityOfAllMaterial();
    }
    
    /**
     * Triggered when an action is performed, like clicking on a button.
     */
    @Override
    public void actionPerformed(final ActionEvent e) {
        if (this.FactoryComponentPan.isVisible()) {
            this.ShowHideSouthButton.setIcon(this.upArrow);
            this.ShowHideSouthButton.setText("Show the component factory");
            this.FactoryComponentPan.setVisible(false);
        }
        else {
            this.ShowHideSouthButton.setIcon(this.downArrow);
            this.ShowHideSouthButton.setText("Hide the component factory");
            this.FactoryComponentPan.setVisible(true);
            final JScrollBar scroll = ((JScrollPane)this.getParent().getParent()).getVerticalScrollBar();
            ((JScrollPane)this.getParent().getParent()).validate();
            scroll.setValue(scroll.getMaximum());
        }
    }
    
    /**
     * The listener will be triggered when a material is added in the forge.
     * @param main The object to link with.
     */
    public void addmaterialAddedListener(final MaterialAddedListener main) {
        this.FactoryMaterialPan.addmaterialAddedListener(main);
    }
    
    /**
     * Update the Component and the Material Factory.
     */
    public void update() {
        this.FactoryMaterialPan.updateFrameComponent();
        this.FactoryComponentPan.updateFrameComponent();
    }
    
    /**
     * Change the curret material to work with.
     * @param m the new material to set to current.
     */
    public void setCurrentMaterial(final Material m) {
        this.FactoryMaterialPan.setCurrentMaterial(m);
    }
}
