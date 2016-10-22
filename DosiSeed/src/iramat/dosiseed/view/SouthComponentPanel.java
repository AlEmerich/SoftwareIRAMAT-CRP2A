package iramat.dosiseed.view;

import interfaceObject.PeriodicTablePanel;
import iramat.dosiseed.controler.ForgeControler;
import iramat.dosiseed.model.AbstractModel;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Insets;
import java.io.IOException;
import java.text.ParseException;
import java.util.Observable;
import java.util.Observer;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.text.MaskFormatter;

import mainPackage.Component;

public class SouthComponentPanel extends JPanel implements Observer, ListSelectionListener
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 7425688424617623263L;
	
	 /**
     * UI fields.
     */
    private JPanel EastComponentPan;
    
    private JTextField nameComponentField;
    
    private JLabel FormulaChangingLabel;
    
    private JTextField densityCompField;
    
    private JFormattedTextField setNbAtomLastElement;
    
    private JList<mainPackage.Component> AllComponentList;
    
    private DefaultListModel<mainPackage.Component> AllCompModelList;
    
    private JScrollPane CompScroll;
	
	public SouthComponentPanel(ForgeControler forgeControler)
	{
        this.setLayout(new BorderLayout());
        this.setBorder(BorderFactory.createTitledBorder(BorderFactory.createBevelBorder(1), "COMPONENT FACTORY", 2, 0));
        this.setPreferredSize(new Dimension(800, 300));
        PeriodicTablePanel perPan = null;
		try
		{
			perPan = new PeriodicTablePanel(forgeControler, new Dimension(500, 250));
			this.add(perPan, "West");
			this.initEastSideOfSouthFactory(forgeControler);
			this.AllComponentList.setSelectedIndex(0);
		} catch (IOException e)
		{
			e.printStackTrace();
		} catch (ParseException e)
		{
			e.printStackTrace();
		}     
	}

	/**
     * Initialization of the east side of the panel.
	 * @param forgeControler 
     * @throws ParseException
     */
    private void initEastSideOfSouthFactory(ForgeControler forgeControler) throws ParseException {
        final JPanel ComponentPan = new JPanel();
        ComponentPan.setLayout(new BoxLayout(ComponentPan, 3));
        ComponentPan.setPreferredSize(new Dimension(200, 350));
        ComponentPan.add(Box.createHorizontalGlue());
        ComponentPan.add(Box.createVerticalGlue());
        final JPanel ButtonComponentPan = new JPanel();
        final JButton CreateNewComponent = new JButton("Create new component");
        CreateNewComponent.setActionCommand("create new component");
        CreateNewComponent.setPreferredSize(new Dimension(200, 30));
        CreateNewComponent.addActionListener(forgeControler);
        ButtonComponentPan.add(CreateNewComponent);
        ComponentPan.add(ButtonComponentPan);
        final JPanel namePan = new JPanel();
        namePan.setPreferredSize(new Dimension(200, 70));
        final JLabel nameLabel = new JLabel("Name of the component:");
        nameLabel.setPreferredSize(new Dimension(185, 30));
        (this.nameComponentField = new JTextField()).setPreferredSize(new Dimension(200, 30));
        this.nameComponentField.addActionListener(forgeControler);
        this.nameComponentField.setActionCommand("name");
        namePan.add(nameLabel);
        namePan.add(this.nameComponentField);
        ComponentPan.add(namePan);
        final JPanel densityPan = new JPanel();
        densityPan.setPreferredSize(new Dimension(200, 30));
        final JLabel densityCompLabel = new JLabel("Density (g/cm3):");
        densityCompLabel.setPreferredSize(new Dimension(120, 30));
        densityPan.add(densityCompLabel);
        (this.densityCompField = new JTextField()).setPreferredSize(new Dimension(50, 25));
        this.densityCompField.addActionListener(forgeControler);
        this.densityCompField.setActionCommand("density");
        densityPan.add(this.densityCompField);
        ComponentPan.add(densityPan);
        final JPanel nbAtomPan = new JPanel();
        final JLabel setAtomNbLastElementLabel = new JLabel("Set the number of atoms for the last selected element:");
        nbAtomPan.add(setAtomNbLastElementLabel);
        nbAtomPan.setPreferredSize(new Dimension(200, 65));
        (this.setNbAtomLastElement = new JFormattedTextField(new MaskFormatter("###"))).setPreferredSize(new Dimension(30, 25));
        this.setNbAtomLastElement.setFocusLostBehavior(3);
        nbAtomPan.add(this.setNbAtomLastElement);
        final JButton OkButtonNbAtom = new JButton("OK");
        OkButtonNbAtom.setActionCommand("Ok button nb atom");
        OkButtonNbAtom.setPreferredSize(new Dimension(50, 30));
        OkButtonNbAtom.setMargin(new Insets(0, 0, 0, 0));
        OkButtonNbAtom.addActionListener(forgeControler);
        nbAtomPan.add(OkButtonNbAtom);
        ComponentPan.add(nbAtomPan);
        final JLabel formulaLabel = new JLabel("Formula");
        formulaLabel.setPreferredSize(new Dimension(300, 30));
        ComponentPan.add(formulaLabel);
        (this.FormulaChangingLabel = new JLabel("")).setFont(new Font("Serif", 1, 12));
        this.FormulaChangingLabel.setPreferredSize(new Dimension(150, 30));
        this.FormulaChangingLabel.setBackground(Color.GRAY);
        this.FormulaChangingLabel.setAlignmentX(CENTER_ALIGNMENT);
        ComponentPan.add(this.FormulaChangingLabel);
        final JButton ClearFormulaButton = new JButton("Clear formula");
        ClearFormulaButton.setActionCommand("Clear formula");
        ClearFormulaButton.setPreferredSize(new Dimension(60, 30));
        ClearFormulaButton.addActionListener(forgeControler);
        ComponentPan.add(ClearFormulaButton);
        this.AllCompModelList = new DefaultListModel<mainPackage.Component>();
        
        (this.AllComponentList = new JList<mainPackage.Component>(this.AllCompModelList)).setSelectedIndex(0);
        this.AllComponentList.addListSelectionListener(this);
        
        (this.CompScroll = new JScrollPane(this.AllComponentList, 20, 30)).setBorder(BorderFactory.createTitledBorder("Components list"));
        this.CompScroll.setPreferredSize(new Dimension(100, 360));
        (this.EastComponentPan = new JPanel()).setLayout(new BoxLayout(this.EastComponentPan, 2));
        this.EastComponentPan.add(ComponentPan);
        this.EastComponentPan.add(this.CompScroll);
        this.EastComponentPan.setMaximumSize(new Dimension(700, 400));
        this.add(this.EastComponentPan, "Center");
    }

    public void addSetCurrentComponent(Component c)
    {
    	this.AllCompModelList.addElement(c);
    	this.AllComponentList.setSelectedValue(c, true);
    }
    
    public Component getCurrentComponent()
    {
    	return AllComponentList.getSelectedValue();
    }
    
    public int getNbAtomLastElement()
    {
    	if (this.setNbAtomLastElement.getText().trim() != "") {
            return Integer.parseInt(this.setNbAtomLastElement.getText().trim());
        }
    	return -1;
    }
    
	@Override
	public void update(Observable arg0, Object arg1)
	{
		AbstractModel model = (AbstractModel) arg0;
		
		for (int i = 0; i < model.getListOfComponent().size(); ++i) {
			Component c = model.getListOfComponent().get(i);
			if(!this.AllCompModelList.contains(c))
				this.AllCompModelList.insertElementAt(c, i);
        }
		
		this.updateCurrentValue();
	}

	@Override
	public void valueChanged(ListSelectionEvent e)
	{
		this.updateCurrentValue();
	}
	
	private void updateCurrentValue()
	{
		Component current = this.getCurrentComponent();
		if(current == null)
			return;
		this.nameComponentField.setText(current.getName());
		this.densityCompField.setText(current.getDensity()+"");
		this.FormulaChangingLabel.setText(current.getFormulaAsString());
	}
	
	/***************************************************************************
	 *                STATIC METHODS
	 ***************************************************************************/
	
	public static void errorWindowAlteringBasic()
	{
		JOptionPane.showMessageDialog(null, 
				"Not allowed to alterate basic component. You may want to create a new one.", 
				"Information", 1);
	}
	
	public static void numberOnly()
	{
		JOptionPane.showMessageDialog(null,
				"Number format exception in density field. Please input numbers only.");
	}
}
