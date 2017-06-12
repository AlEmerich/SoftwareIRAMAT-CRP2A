package iramat.dosiseed.view;

import interfaceObject.TripleFormattedPanel;
import iramat.dosiseed.controler.ForgeControler;
import iramat.dosiseed.controler.MassControler;
import iramat.dosiseed.controler.SetDensityControler;
import iramat.dosiseed.controler.WaterFractionControler;
import iramat.dosiseed.model.AbstractModel;
import iramat.dosiseed.model.ColoredMaterial;
import iramat.dosiseed.model.Material;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.text.ParseException;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.ListCellRenderer;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import mainPackage.Component;
import interfaceObject.NorthMaterialPanel.ComponentListRenderer;
import util.Couple;

public class NorthMaterialPanel extends JPanel implements Observer, ListSelectionListener, KeyListener
{	
	private class MaterialCellRenderer implements ListCellRenderer<Material>
	{
		private DefaultListCellRenderer defaultRenderer = new DefaultListCellRenderer();
		
		@Override
		public java.awt.Component getListCellRendererComponent(
				JList<? extends Material> list, Material value, int index,
				boolean isSelected, boolean cellHasFocus)
		{
			JLabel label = (JLabel) defaultRenderer.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
			label.setText(value.getName());
			return label;
		}
		
	}
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1312028131338617834L;

	private JList<Component> AllComponentList;

	private DefaultListModel<Component> AllCompModelList;

	private JScrollPane CompScroll;

	private JList<Couple<Component, Float>> JlistCompInMat;

	private DefaultListModel<Couple<Component, Float>> listModelComponentInMaterial;

	private JScrollPane FactoryScroll;

	private JTextField nameField;

	private JLabel massLabel;

	private JLabel densityLabel;

	private JFormattedTextField setDensityField;

	private JFormattedTextField massPercentField;
	
	private JFormattedTextField waterField;

	private JScrollPane MaterialScroll;

	private JList<Material> JlistMaterial;

	private DefaultListModel<Material> listModelMaterial;

	private Dimension Resolution;

	private JCheckBox useCalculatedDensity;
	
	private boolean useWaterField;
	
	public NorthMaterialPanel(ForgeControler forgeControler,boolean waterFraction) throws ParseException
	{
		this.setLayout(new FlowLayout());
		this.setBorder(BorderFactory.createTitledBorder(BorderFactory.createBevelBorder(1), "MATERIAL FACTORY", 2, 0));
		this.Resolution = Toolkit.getDefaultToolkit().getScreenSize();
		this.initWestSideofNorthFactory(forgeControler);
		this.add(this.createVerticalHorizontal());
		this.initCenterOfNorthFactory(forgeControler,waterFraction);
		this.initEastSideofNorthFactory(forgeControler);
		
		this.useWaterField = waterFraction;
	}

	/**
	 * Create vertical separator.
	 * @return the newly created component.
	 */
	private Container createVerticalHorizontal() {
		final JSeparator j = new JSeparator(1);
		j.setPreferredSize(new Dimension(8, 400));
		return j;
	}

	/**
	 * Initialization of the west side of the panel.
	 */
	private void initWestSideofNorthFactory(ForgeControler forgeControler) {
		this.AllCompModelList = new DefaultListModel<mainPackage.Component>();

		(this.AllComponentList = new JList<mainPackage.Component>(this.AllCompModelList)).setSelectedIndex(0);
		this.AllComponentList.addListSelectionListener(this);

		(this.CompScroll = new JScrollPane(this.AllComponentList, 20, 30)).setBorder(BorderFactory.createTitledBorder("Components list"));
		this.CompScroll.setPreferredSize(new Dimension(300, 400));
		final JButton buttonAddComp = new JButton("Add Component to Material \u25b6");
		buttonAddComp.setActionCommand("add component");
		buttonAddComp.setMinimumSize(new Dimension(120, 30));
		buttonAddComp.setSize(new Dimension(this.Resolution.width / 4, 30));
		buttonAddComp.addActionListener(forgeControler);
		final JPanel WestSide = new JPanel(new BorderLayout());
		WestSide.add(buttonAddComp, "North");
		WestSide.add(this.CompScroll, "Center");
		this.add(WestSide, "West");
	}

	/**
	 * Initialization of the center of the panel.
	 * @throws ParseException
	 */
	private void initCenterOfNorthFactory(ForgeControler forgeControler,boolean water) throws ParseException {
		final JPanel MatPanelInfo = new JPanel();
		final BoxLayout bl = new BoxLayout(MatPanelInfo, 3);
		MatPanelInfo.setLayout(bl);
		MatPanelInfo.setPreferredSize(new Dimension(300, 100));
		final JPanel namePan = new JPanel();
		final JLabel nameLabel = new JLabel("Name:");
		nameLabel.setPreferredSize(new Dimension(50, 25));
		(this.nameField = new JTextField()).setPreferredSize(new Dimension(300, 25));
		this.nameField.addActionListener(forgeControler);
		this.nameField.addKeyListener(this);
		this.nameField.setActionCommand("name material");
		namePan.add(this.nameField);
		final JLabel Description = new JLabel("Component     Density (g/cm3)    (%)");
		this.listModelComponentInMaterial = new DefaultListModel<Couple<Component, Float>>();
		(this.JlistCompInMat = new JList<Couple<Component, Float>>(this.listModelComponentInMaterial)).addListSelectionListener(this);
		this.JlistCompInMat.setCellRenderer(new ComponentListRenderer());
		(this.FactoryScroll = new JScrollPane(this.JlistCompInMat)).setMaximumSize(new Dimension(300, 100));
		final JPanel tmpPan = new JPanel(new BorderLayout());
		final JPanel listPan = new JPanel(new BorderLayout());
		listPan.add(Description, "North");
		listPan.add(this.FactoryScroll, "Center");
		final JButton removeComponent = new JButton("Remove selected Component");
		removeComponent.setActionCommand("remove component");
		removeComponent.addActionListener(forgeControler);
		listPan.add(removeComponent, "South");
		tmpPan.add(listPan, "North");
		tmpPan.setMinimumSize(new Dimension(300, 50));

		final JPanel DryValue = new JPanel();
		DryValue.setBorder(BorderFactory.createTitledBorder("Density"));
		final JLabel labelM = new JLabel("Total mass % :   ");
		labelM.setPreferredSize(new Dimension(150, 20));
		(this.massLabel = new JLabel("0")).setPreferredSize(new Dimension(70, 20));
		final JLabel labelD = new JLabel("Calculated density value (g/cm3):");
		labelD.setPreferredSize(new Dimension(220, 20));
		(this.densityLabel = new JLabel("0")).setPreferredSize(new Dimension(70, 20));
		final JLabel setDensLabel = new JLabel("Set density manually (g/cm3):");
		setDensLabel.setPreferredSize(new Dimension(200, 20));
		(this.setDensityField = new JFormattedTextField(TripleFormattedPanel.createmask(false))).setFocusLostBehavior(3);
		this.setDensityField.setPreferredSize(new Dimension(50, 20));
		this.setDensityField.addKeyListener(this);
		
		DryValue.add(labelM);
		DryValue.add(this.massLabel);
		this.useCalculatedDensity = new JCheckBox("Use calculated Density");
		this.useCalculatedDensity.addActionListener(forgeControler);
		this.useCalculatedDensity.setActionCommand("use default density");
		this.useCalculatedDensity.setSelected(true);
		this.setDensityField.setEnabled(false);
		DryValue.add(useCalculatedDensity);
		DryValue.add(labelD);
		DryValue.add(this.densityLabel);
		DryValue.add(Box.createRigidArea(new Dimension(50,20)));
		DryValue.add(setDensLabel);
		DryValue.add(this.setDensityField);
		DryValue.setPreferredSize(new Dimension(200, 150));

		final JPanel centerPanel = new JPanel(new GridLayout(0,1));
		final JPanel massPanel = new JPanel();
		final JLabel massPercentLabel = new JLabel("Change mass percentage of the selected component:");
		massPercentLabel.setPreferredSize(new Dimension(350, 25));
		(this.massPercentField = new JFormattedTextField(TripleFormattedPanel.createmask(false))).setPreferredSize(new Dimension(50, 30));
		this.massPercentField.setFocusLostBehavior(3);
		this.massPercentField.addKeyListener(this);
		
		this.massPercentField.setActionCommand("mass percent field");
		massPanel.add(massPercentLabel);
		massPanel.add(this.massPercentField);
		final JPanel newPanel = new JPanel(new BorderLayout());
		newPanel.add(DryValue, "North");
		centerPanel.add(massPanel);
		newPanel.add(centerPanel);
		
		if(water)
		{
			JPanel waterPan = new JPanel();
			waterPan.add(new JLabel("Water fraction in current material: "));
			(this.waterField = new JFormattedTextField(TripleFormattedPanel.createmask(false))).setPreferredSize(new Dimension(50, 30));
			this.waterField.setFocusLostBehavior(3);
			this.waterField.addKeyListener(this);
			waterPan.add(this.waterField);
			centerPanel.add(waterPan,BorderLayout.SOUTH);
		}
		MatPanelInfo.add(namePan);
		tmpPan.add(newPanel, "Center");
		MatPanelInfo.add(tmpPan);

		final JPanel GlobalCenter = new JPanel(new BorderLayout());
		GlobalCenter.add(MatPanelInfo, "Center");
		GlobalCenter.setPreferredSize(new Dimension(550, 500));
		this.add(GlobalCenter, "Center");
	}

	/**
	 * Initialization of the east side of the panel.
	 */
	private void initEastSideofNorthFactory(ForgeControler forgeControler) {
		final JPanel eastPanel = new JPanel(new BorderLayout());

		this.listModelMaterial = new DefaultListModel<Material>();

		this.JlistMaterial = new JList<Material>(this.listModelMaterial);

		(this.MaterialScroll = new JScrollPane(this.JlistMaterial, 20, 30)).setBorder(BorderFactory.createTitledBorder("Material List"));
		this.MaterialScroll.setPreferredSize(new Dimension(250, 360));
		this.MaterialScroll.setMinimumSize(new Dimension(100, 300));
		this.JlistMaterial.addListSelectionListener(this);
		this.JlistMaterial.setCellRenderer(new MaterialCellRenderer());
		final JButton addMaterial = new JButton("Create new material");
		addMaterial.setActionCommand("add material");
		addMaterial.addActionListener(forgeControler);
		final JButton deleteMaterial = new JButton("Remove selected material");
		deleteMaterial.setActionCommand("delete material");
		deleteMaterial.addActionListener(forgeControler);
		eastPanel.add(addMaterial, "North");
		eastPanel.add(this.MaterialScroll, "Center");
		eastPanel.add(deleteMaterial, "South");
		this.add(eastPanel, "East");
	}

	public void addSetCurrentMaterial(Material m)
	{
		this.listModelMaterial.addElement(m);
		this.JlistMaterial.setSelectedValue(m, true);
		this.flushListCompInMat();
	}

	public Material getCurrentMaterial()
	{
		return this.JlistMaterial.getSelectedValue();
	}
	
	public void setCurrentMaterial(ColoredMaterial material)
	{
		this.JlistMaterial.setSelectedValue(material, true);
	}

	public Couple<Component,Float> getSelectedCompInMat()
	{
		return this.JlistCompInMat.getSelectedValue();
	}

	public Component getSelectedComponent()
	{
		return this.AllComponentList.getSelectedValue();
	}
	
	public void useCalculatedDensity(boolean use)
	{
		if(use)
        {
        	this.setDensityField.setEnabled(false);
        }
    	else
        {
        	this.setDensityField.setEnabled(true);
        }
	}

	public void flushListCompInMat()
	{
		this.listModelComponentInMaterial.removeAllElements();
	}
	
	public void flushListMaterial()
	{
		this.listModelMaterial.removeAllElements();
	}
	
	private void updateCurrentValue()
	{
		Material current = this.getCurrentMaterial();
		if(current == null)
		{
			try{
				this.JlistMaterial.setSelectedIndex(0);
				current = this.listModelMaterial.get(0);
			}catch(ArrayIndexOutOfBoundsException e)
			{
				return;
			}
		}

		if(this.getSelectedComponent() == null)
		{
			try{
				this.AllComponentList.setSelectedIndex(0);
			}catch(ArrayIndexOutOfBoundsException e)
			{
				//Ignore and do nothing.
			}
		}

		final List<Couple<Component,Float>> listComp = current.getListComponent();
		for(int i=0;i<listComp.size();i++)
		{
			Couple<Component,Float> couple = listComp.get(i);
			if(!this.listModelComponentInMaterial.contains(couple))
				this.listModelComponentInMaterial.insertElementAt(couple, i);  	
		}

		this.nameField.setText(current.getName());
		this.nameField.setBorder(BorderFactory.createLineBorder(Color.green, 2));
		
		this.densityLabel.setText(current.getCalculatedDensity()+"");
		this.setDensityField.setText(current.getDensity()+"");
		this.setDensityField.setBorder(BorderFactory.createLineBorder(Color.green, 2));
		
		float mass = current.getTotalMass();
		this.massLabel.setText(mass+"");
		if(mass > 100.f)
		{
			this.massLabel.setForeground(Color.RED);
        }
        else {
            this.massLabel.setForeground(null);
        }
		
		if(this.useWaterField)
		{
			this.waterField.setText(current.getWaterFraction()+"");
			this.waterField.setBorder(BorderFactory.createLineBorder(Color.green, 2));
		}
		
		Couple<Component,Float> compDensityCouple = this.getSelectedCompInMat();

		if(compDensityCouple != null)
		{
			this.massPercentField.setText(compDensityCouple.getValeur2()+"");
			this.massPercentField.setBorder(BorderFactory.createLineBorder(Color.green, 2));
		}
		
		this.useCalculatedDensity.setSelected(current.isUseCalculated());
		this.useCalculatedDensity(this.useCalculatedDensity.isSelected());
	}

	@Override
	public void valueChanged(ListSelectionEvent e)
	{
		if(e.getSource() != this.JlistCompInMat)
			this.flushListCompInMat();
		this.updateCurrentValue();
	}

	@Override
	public void update(Observable arg0, Object arg1)
	{
		AbstractModel model = (AbstractModel) arg0;
		
		final List<Material> listMaterial = (List<Material>) model.getListOfMaterial();
		for (int i = 0; i < listMaterial.size(); ++i) {
			Material m = listMaterial.get(i);
			if(!this.listModelMaterial.contains(m))
				this.listModelMaterial.insertElementAt(m, i);
		}
		this.JlistMaterial.repaint();

		final List<Component> listComponent = (List<Component>) model.getListOfComponent();
		for (int i = 0; i < listComponent.size(); ++i) {
			Component c = listComponent.get(i);
			if(!this.AllCompModelList.contains(c))
				this.AllCompModelList.insertElementAt(c, i);  		
		}
		this.AllComponentList.repaint();

		Material currentMat = this.getCurrentMaterial();
		if(currentMat != null)
		{
			final List<Couple<Component,Float>> listCompInMat = currentMat.getListComponent();
			for(int i=0;i<listCompInMat.size();i++)
			{
				Couple<Component,Float> couple = listCompInMat.get(i);
				if(!this.listModelComponentInMaterial.contains(couple))
					this.listModelComponentInMaterial.insertElementAt(couple, i);
			}
			this.JlistCompInMat.repaint();
		}

		this.updateCurrentValue();
	}

	@Override
	public void keyPressed(KeyEvent e)
	{
		((JTextField)e.getSource()).setBorder(BorderFactory.createLineBorder(Color.red, 2));
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

	public void setFieldControler(SetDensityControler setDensityControler,
			MassControler massControler,WaterFractionControler waterFractionControler)
	{
		this.massPercentField.addKeyListener(massControler);
		this.massPercentField.addFocusListener(massControler);
		
		this.setDensityField.addKeyListener(setDensityControler);
		this.setDensityField.addFocusListener(setDensityControler);
		
		if(useWaterField)
		{
			this.waterField.addKeyListener(waterFractionControler);
			this.waterField.addFocusListener(waterFractionControler);
		}
		
	}
}
