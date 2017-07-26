package interfaceObject;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListCellRenderer;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import mainPackage.Component;
import mainPackage.Material;
import mainPackage.MaterialComponentProviderInterface;
import util.Couple;

/**
 * JPanel containing all the material factory, itself contained by {@link ForgeWindow}.
 * @author alan
 * @see ListSelectionListener
 * @see ComponentAddedListener
 * @see KeyListener
 * @see ItemListener
 */
public class NorthMaterialPanel extends JPanel implements ListSelectionListener, ActionListener, ComponentAddedListener, KeyListener, ItemListener
{
    private static final long serialVersionUID = 8407607594843160047L;
    
    public static class ComponentListRenderer extends JLabel implements ListCellRenderer<Couple<Component, Float>>
    {
    	private DefaultListCellRenderer defaultRenderer = new DefaultListCellRenderer();
    	
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		@Override
		public java.awt.Component getListCellRendererComponent(JList<? extends Couple<Component, Float>> list,
				Couple<Component, Float> value, int index, boolean isSelected, boolean cellHasFocus) {
			
			JLabel label = (JLabel) defaultRenderer.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
			label.setText(value.getValeur1().getName()+ "               " + value.getValeur1().getDensity() + "                         " + value.getValeur2());
			
			return label;
		}
    }
    /**
     * Listeners.
     */
    private MaterialAddedListener materialListener;
    
    /**
     * UI fields.
     */
    private JList<mainPackage.Component> AllComponentList;
    private DefaultListModel<mainPackage.Component> AllCompModelList;
    private JScrollPane CompScroll;
    private JList<Couple<mainPackage.Component, Float>> JlistCompInMat;
    private DefaultListModel<Couple<mainPackage.Component, Float>> listModelComponentInMaterial;
    private JScrollPane FactoryScroll;
    private JTextField nameField;
    private JFormattedTextField waterField;
    private JFormattedTextField massPercentField;
    private JLabel massLabel;
    private JLabel densityLabel;
    private JFormattedTextField setDensityField;
    private JComboBox<mainPackage.Component> comboGrain;
    private JFormattedTextField compacityField;
    private JFormattedTextField densityField;
    private JTextArea GrainArea;
    private JScrollPane MaterialScroll;
    private JList<Material> JlistMaterial;
    private DefaultListModel<Material> listModelMaterial;
    private Dimension Resolution;
    private JCheckBox useCalculatedDensity;
    
    /**
     * Brute data.
     */
    private static boolean listeningTolistComponentIsEnable;
    private MaterialComponentProviderInterface Father;
    private Material currentMaterial;
    private mainPackage.Component currentComponent;
    
    static {
        NorthMaterialPanel.listeningTolistComponentIsEnable = true;
    }
    
    /**
     * Constructor. Initialize Swing components.
     * @param Father to have access to the list of material and component.
     * @throws ParseException from the initialization of the mask.
     */
    public NorthMaterialPanel(final MaterialComponentProviderInterface Father) throws ParseException {
        this.Father = Father;
        this.setLayout(new FlowLayout());
        this.setBorder(BorderFactory.createTitledBorder(BorderFactory.createBevelBorder(1), "MATERIAL FACTORY", 2, 0));
        this.Resolution = Toolkit.getDefaultToolkit().getScreenSize();
        this.initWestSideofNorthFactory();
        this.add(this.createVerticalHorizontal());
        this.initCenterOfNorthFactory();
        this.add(this.createVerticalHorizontal());
        this.initEastSideofNorthFactory();
        this.updateFrameComponent();
    }
    
    /**
     * Create vertical separator.
     * @return the newly created component.
     */
    private Container createVerticalHorizontal() {
        final JSeparator j = new JSeparator(1);
        j.setPreferredSize(new Dimension(8, 510));
        return j;
    }
    
    /**
     * Initialization of the west side of the panel.
     */
    private void initWestSideofNorthFactory() {
        this.AllCompModelList = new DefaultListModel<mainPackage.Component>();
        for (int i = 0; i < this.Father.getListOfComponent().size(); ++i) {
            this.AllCompModelList.insertElementAt((Component) this.Father.getListOfComponent().get(i), i);
        }
        (this.AllComponentList = new JList<mainPackage.Component>(this.AllCompModelList)).setSelectedIndex(0);
        this.AllComponentList.addListSelectionListener(this);
        this.setCurrentComponent(this.AllComponentList.getSelectedValue());
        (this.CompScroll = new JScrollPane(this.AllComponentList, 20, 30)).setBorder(BorderFactory.createTitledBorder("Components list"));
        this.CompScroll.setPreferredSize(new Dimension(310, 500));
        final JButton buttonAddComp = new JButton("Add Component to Material \u25b6");
        buttonAddComp.setActionCommand("add component");
        buttonAddComp.setMinimumSize(new Dimension(120, 30));
        buttonAddComp.setSize(new Dimension(this.Resolution.width / 4, 30));
        buttonAddComp.addActionListener(this);
        final JPanel WestSide = new JPanel(new BorderLayout());
        WestSide.add(buttonAddComp, "North");
        WestSide.add(this.CompScroll, "Center");
        this.add(WestSide, "West");
    }
    
    /**
     * Initialization of the center of the panel.
     * @throws ParseException
     */
    private void initCenterOfNorthFactory() throws ParseException {
        final JPanel MatPanelInfo = new JPanel();
        final BoxLayout bl = new BoxLayout(MatPanelInfo, 3);
        MatPanelInfo.setLayout(bl);
        MatPanelInfo.setPreferredSize(new Dimension(300, 100));
        final JPanel namePan = new JPanel();
        final JLabel nameLabel = new JLabel("Name:");
        nameLabel.setPreferredSize(new Dimension(50, 25));
        (this.nameField = new JTextField()).setPreferredSize(new Dimension(300, 25));
        this.nameField.addKeyListener(this);
        namePan.add(this.nameField);
        final JLabel Description = new JLabel("Component     Density (g/cm3)    (%)");
        this.listModelComponentInMaterial = new DefaultListModel<Couple<mainPackage.Component, Float>>();
        (this.JlistCompInMat = new JList<Couple<mainPackage.Component, Float>>(this.listModelComponentInMaterial)).addListSelectionListener(this);
        this.JlistCompInMat.setCellRenderer(new ComponentListRenderer());
        (this.FactoryScroll = new JScrollPane(this.JlistCompInMat)).setMaximumSize(new Dimension(300, 100));
        final JPanel tmpPan = new JPanel();
        final JPanel listPan = new JPanel(new BorderLayout());
        listPan.add(Description, "North");
        listPan.add(this.FactoryScroll, "Center");
        final JButton removeComponent = new JButton("Remove selected Component");
        removeComponent.setActionCommand("remove component");
        removeComponent.addActionListener(this);
        listPan.add(removeComponent, "South");
        tmpPan.add(listPan, "North");
        tmpPan.setMinimumSize(new Dimension(300, 50));
        final JPanel waterPan = new JPanel();
        waterPan.setPreferredSize(new Dimension(400, 45));
        final JLabel waterLabel = new JLabel("Water content (%):");
        waterLabel.setPreferredSize(new Dimension(150, 25));
        (this.waterField = new JFormattedTextField(TripleFormattedPanel.createmask(false))).setPreferredSize(new Dimension(70, 25));
        this.waterField.setFocusLostBehavior(3);
        this.waterField.addKeyListener(this);
        waterPan.add(Box.createRigidArea(new Dimension(83,10)));
        waterPan.add(waterLabel);
        waterPan.add(this.waterField);
        final JPanel massPanel = new JPanel();
        final JLabel massPercentLabel = new JLabel("<html><br>Change mass percentage <br>" +
        		"of the selected component:</b></html>");
       // massPercentLabel.setPreferredSize(new Dimension(350, 25));
        (this.massPercentField = new JFormattedTextField(TripleFormattedPanel.createmask(false))).setPreferredSize(new Dimension(70, 25));
        this.massPercentField.setFocusLostBehavior(3);
        this.massPercentField.addKeyListener(this);
        massPanel.add(Box.createRigidArea(new Dimension(57,10)));
        massPanel.add(massPercentLabel);
        massPanel.add(this.massPercentField);
        
        final JPanel DryValue = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        DryValue.setBorder(BorderFactory.createTitledBorder("Dry Values"));
        final JLabel labelM = new JLabel("Total mass % :");
        labelM.setPreferredSize(new Dimension(150, 20));
        
        (this.massLabel = new JLabel("0")).setPreferredSize(new Dimension(70, 25));
        final JLabel labelD = new JLabel("Calculated density value (g/cm3):");
        labelD.setPreferredSize(new Dimension(220, 20));
        (this.densityLabel = new JLabel("0")).setPreferredSize(new Dimension(70, 25));
        final JLabel setDensLabel = new JLabel("Set density manually (g/cm3):");
        setDensLabel.setPreferredSize(new Dimension(200, 20));
        (this.setDensityField = new JFormattedTextField(TripleFormattedPanel.createmask(false))).setFocusLostBehavior(3);
        this.setDensityField.setPreferredSize(new Dimension(70, 25));
        this.setDensityField.addKeyListener(this);
        DryValue.add(labelM);
        DryValue.add(this.massLabel);
        this.useCalculatedDensity = new JCheckBox("Use calculated Density");
        this.useCalculatedDensity.addActionListener(this);
        this.useCalculatedDensity.setActionCommand("use default density");
        this.useCalculatedDensity.setSelected(true);
        this.setDensityField.setEnabled(false);
        JPanel centerUsePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        centerUsePanel.add(useCalculatedDensity);
        DryValue.add(centerUsePanel);
        
        DryValue.add(labelD);
        DryValue.add(this.densityLabel);
        DryValue.add(setDensLabel);
        DryValue.add(this.setDensityField);
        DryValue.setPreferredSize(new Dimension(300, 160));
        
        final JPanel newPanel = new JPanel();
        newPanel.setLayout(new BoxLayout(newPanel, BoxLayout.PAGE_AXIS));
        newPanel.add(DryValue);
        JPanel box = new JPanel();
        box.add(massPanel);
        newPanel.add(box);
        MatPanelInfo.add(namePan);
        tmpPan.setLayout(new BoxLayout(tmpPan, BoxLayout.PAGE_AXIS));
        tmpPan.add(newPanel);
        JPanel box2 = new JPanel();
        box2.add(waterPan);
        tmpPan.add(box2);
        MatPanelInfo.add(tmpPan);

        
        final JPanel GrainPan = new JPanel();
        GrainPan.setBorder(BorderFactory.createTitledBorder("Sediment information"));
        GrainPan.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = GridBagConstraints.REMAINDER; gbc.gridheight = 1;
        gbc.anchor = GridBagConstraints.LINE_END;
        final JLabel GrainCompLabel = new JLabel("Grain composition: ");
        GrainCompLabel.setAlignmentX(0.5f);
        (this.comboGrain = new JComboBox<mainPackage.Component>()).setPreferredSize(new Dimension(80, 20));
        this.comboGrain.addItemListener(this);
        final JPanel comPan = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        final JLabel compacityLabel = new JLabel("Compacity (%): ");
        (this.compacityField = new JFormattedTextField(TripleFormattedPanel.createmask(false))).setPreferredSize(new Dimension(50, 25));
        this.compacityField.setFocusLostBehavior(3);
        this.compacityField.addKeyListener(this);
        comPan.add(compacityLabel);
        comPan.add(this.compacityField);
        final JPanel densPan = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        final JLabel densityLabel = new JLabel("Density (g/cm3): ");
        (this.densityField = new JFormattedTextField(TripleFormattedPanel.createmask(false))).setPreferredSize(new Dimension(50, 25));
        this.densityField.setFocusLostBehavior(3);
        this.densityField.addKeyListener(this);
        densPan.add(densityLabel);
        densPan.add(this.densityField);
        final JPanel grainDistribPan = new JPanel(new BorderLayout());
        final JLabel grainDistribLabel = new JLabel("Granulometric distributions:");
        final JLabel describeGrainDistrib = new JLabel("diam. (mm)  vol.fraction (%)");
        (this.GrainArea = new JTextArea()).addKeyListener(this);

        grainDistribPan.add(grainDistribLabel, "North");
        grainDistribPan.add(describeGrainDistrib, "Center");
        JPanel pan = new JPanel(new BorderLayout());
        pan.add(describeGrainDistrib, BorderLayout.NORTH);
        JScrollPane scroll = new JScrollPane(this.GrainArea);
        scroll.setPreferredSize(new Dimension(150,320));
        scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        pan.add(scroll);
        		
        grainDistribPan.add(pan);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        GrainPan.add(GrainCompLabel,gbc);
        gbc.gridy = 1;
        GrainPan.add(this.comboGrain,gbc);
        gbc.gridy = 2;
        GrainPan.add(comPan,gbc);
        gbc.gridy = 3;
        GrainPan.add(densPan,gbc);
        gbc.gridy = 4;
        gbc.gridheight = 4;
        gbc.fill = GridBagConstraints.BOTH;
        GrainPan.add(grainDistribPan,gbc);
        GrainPan.setMaximumSize(new Dimension(200, 450));
      
        final JPanel GlobalCenter = new JPanel(new BorderLayout());
        GlobalCenter.add(MatPanelInfo, "Center");
        GlobalCenter.add(GrainPan, "East");
        GlobalCenter.setPreferredSize(new Dimension(550, 510));
        this.add(GlobalCenter, "Center");
    }
    
    /**
     * Initialization of the east side of the panel.
     */
    private void initEastSideofNorthFactory() {
        final JPanel eastPanel = new JPanel(new BorderLayout());
        @SuppressWarnings("unchecked")
		final List<Material> listMaterial = (List<Material>) this.Father.getListOfMaterial();
        this.listModelMaterial = new DefaultListModel<Material>();
        for (int i = 0; i < listMaterial.size(); ++i) {
            this.listModelMaterial.insertElementAt(listMaterial.get(i), i);
        }
        this.JlistMaterial = new JList<Material>(this.listModelMaterial);
        this.currentMaterial = this.JlistMaterial.getSelectedValue();
        (this.MaterialScroll = new JScrollPane(this.JlistMaterial, 20, 30)).setBorder(BorderFactory.createTitledBorder("Material List"));
        eastPanel.setPreferredSize(new Dimension(250, 500));
       
        this.JlistMaterial.addListSelectionListener(this);
        final JButton addMaterial = new JButton("Create new material");
        addMaterial.setActionCommand("add material");
        addMaterial.addActionListener(this);
        final JButton deleteMaterial = new JButton("Remove selected material");
        deleteMaterial.setActionCommand("delete material");
        deleteMaterial.addActionListener(this);
        eastPanel.add(addMaterial, "North");
        eastPanel.add(this.MaterialScroll, "Center");
        eastPanel.add(deleteMaterial, "South");
        this.add(eastPanel, "East");
    }
    
    /**
     * Update Swing components.
     */
    public void updateFrameComponent() {
        NorthMaterialPanel.listeningTolistComponentIsEnable = false;
        final int saveIndexComp = this.JlistCompInMat.getSelectedIndex();
        final int saveIndexGrain = this.comboGrain.getSelectedIndex();
        this.listModelComponentInMaterial.clear();
        this.comboGrain.removeAllItems();
        for (int i = 0; i < this.Father.getListOfMaterial().size(); ++i) {
            if (!this.listModelMaterial.contains(this.Father.getListOfMaterial().get(i))) {
                this.listModelMaterial.add(i, (Material) this.Father.getListOfMaterial().get(i));
            }
        }
        if (!this.JlistMaterial.isSelectionEmpty()) {
            final ArrayList<Couple<mainPackage.Component, Float>> tmpMat = this.JlistMaterial.getSelectedValue().getListComponents();
            for (int j = 0; j < tmpMat.size(); ++j) {
                this.listModelComponentInMaterial.addElement(tmpMat.get(j));
                this.comboGrain.addItem(tmpMat.get(j).getValeur1());
            }
        }
        NorthMaterialPanel.listeningTolistComponentIsEnable = true;
        this.JlistCompInMat.setSelectedIndex(saveIndexComp);
        this.comboGrain.setSelectedItem(saveIndexGrain);
        if (this.currentMaterial != null) {
            this.nameField.setText(this.currentMaterial.getName());
            this.nameField.setBorder(BorderFactory.createLineBorder(Color.green, 2));
            this.waterField.setText(new StringBuilder(String.valueOf(this.currentMaterial.getWaterContent())).toString());
            this.waterField.setBorder(BorderFactory.createLineBorder(Color.green, 2));
            float calculatedMass = 0.0f;
            float calculatedDensity = 0.0f;
            for (int k = 0; k < this.currentMaterial.getNbComponent(); ++k) {
                final float mass = this.currentMaterial.getListComponents().get(k).getValeur2();
                calculatedDensity += this.currentMaterial.getListComponents().get(k).getValeur1().getDensity() * mass / 100.0f;
                calculatedMass += mass;
            }
            this.massLabel.setText(new StringBuilder(String.valueOf(calculatedMass)).toString());
            
            this.massLabel.setBorder(BorderFactory.createLineBorder(Color.green, 2));
            if (calculatedMass > 100.0f) {
                this.massLabel.setForeground(Color.RED);
            }
            else {
                this.massLabel.setForeground(null);
            }
            
            this.densityLabel.setText(calculatedDensity+"");
            this.currentMaterial.setCalculatedDryDensity(calculatedDensity);
            this.densityLabel.setBorder(BorderFactory.createLineBorder(Color.green, 2));
            try
            {
            	if(Float.parseFloat(this.setDensityField.getText().trim()) == 0)
            		this.currentMaterial.setDryDensity(calculatedDensity);
            }catch(NumberFormatException e)
            {
            	//Ignore
            }
            
            
            this.useCalculatedDensity.setSelected(this.currentMaterial.isUseCalculatedDensity());
            
            this.setDensityField.setText(this.currentMaterial.getDryDensity()+"");
            if(this.useCalculatedDensity.isSelected())
            {
            	this.setDensityField.setEnabled(false);
            }
        	else
            {
            	this.setDensityField.setEnabled(true);
            }
            
            this.setDensityField.setBorder(BorderFactory.createLineBorder(Color.green, 2));
            
            this.comboGrain.setSelectedItem(this.Father.getListOfComponent().get(this.currentMaterial.getGrainComponentIndex()));
            this.compacityField.setText(new StringBuilder(String.valueOf(this.currentMaterial.getGrainCompacity())).toString());
            this.compacityField.setBorder(BorderFactory.createLineBorder(Color.green, 2));
            this.densityField.setText(this.currentMaterial.getGrainDensity()+"");
            this.densityField.setBorder(BorderFactory.createLineBorder(Color.green, 2));
            String text = "";
            for (int l = 0; l < this.currentMaterial.getGrainNbGranulometricFractions(); ++l) {
                text = String.valueOf(text) + this.currentMaterial.getGrainList().get(l) + "\n";
            }
            this.GrainArea.setText(text);
            this.GrainArea.setBorder(BorderFactory.createLineBorder(Color.green, 2));
        }
    }
    
    /**
     * Parse the list of grains to add into the right material.
     * @throws ArrayIndexOutOfBoundsException
     * @throws NumberFormatException
     */
    private void parseGrainAreaToMaterial() throws ArrayIndexOutOfBoundsException, NumberFormatException {
        this.currentMaterial.clearGrainList();
        final String[] grainAreaFormattedText = this.GrainArea.getText().split("\\s+");
        if (grainAreaFormattedText[0].isEmpty()) {
            return;
        }
        for (int i = 0; i < grainAreaFormattedText.length; i += 2) {
            this.currentMaterial.addGrains(Float.parseFloat(grainAreaFormattedText[i].replaceAll("\\s", "")), Float.parseFloat(grainAreaFormattedText[i + 1].replaceAll("\\s", "")));
        }
    }
    
    /**
     * A material is valid if it is filled by at least one material and if it is 100% filled.
     * @return true if all material is valid, false if not.
     */
    public boolean checkIntegrityOfAllMaterial() {
        @SuppressWarnings("unchecked")
		final List<Material> listM = (List<Material>) this.Father.getListOfMaterial();
        for (final Material m : listM) {
            if (m.getNbComponent() <= 0) {
                JOptionPane.showMessageDialog(null, "The material " + m.getName() + " is fill with nothing. Delete it or fill it.", "Invalid material.", 0);
                return false;
            }
            float totalC = 0;
            for (final Couple<mainPackage.Component, Float> c : m.getListComponents()) {
                totalC += c.getValeur2();
            }
            if (totalC != 100) {
                JOptionPane.showMessageDialog(null, "The material " + m.getName() + " is not 100% filled. Please fill the rest of matter.", "Invalid material.", 0);
                return false;
            }
            if (m.getGrainList().isEmpty()) {
                continue;
            }
            float totalG = 0;
            for (final Couple<Float, Float> g : m.getGrainList()) {
                totalG += g.getValeur2();
            }
            if (totalG != 100) {
                JOptionPane.showMessageDialog(null, "The sum of the grain fractions of the material " + m.getName() + " is not equal to 100.", "Invalid material.", 0);
                return false;
            }
            final mainPackage.Component c2 = (Component) this.Father.getListOfComponent().get(m.getGrainComponentIndex());
            Couple<mainPackage.Component, Float> comp = null;
            for (final Couple<mainPackage.Component, Float> couple : m.getListComponents()) {
                if (couple.getValeur1().equals(c2)) {
                    comp = couple;
                }
            }
            float dryDens = (m.isUseCalculatedDensity() ? m.getCalculatedDryDensity() : m.getDryDensity());
            if(comp != null)
            	if (m.getGrainDensity() * (m.getGrainCompacity() / 100.0f) > dryDens * (comp.getValeur2() / 100.0f)) {
            		JOptionPane.showMessageDialog(null, "The mass of the grains exceed the " + comp.getValeur1().getName() + " mass defined for the material " + m.getName() + ".", "Invalid material.", 0);
            		return false;
            	}
        }
        return true;
    }
    
    @Override
    public void valueChanged(final ListSelectionEvent e) {
        if (e.getSource() == this.JlistMaterial) {
            this.currentMaterial = this.JlistMaterial.getSelectedValue();
            this.updateFrameComponent();
        }
        else if (e.getSource() == this.JlistCompInMat) {
            if (NorthMaterialPanel.listeningTolistComponentIsEnable) {
                this.massPercentField.setText(new StringBuilder().append(this.JlistCompInMat.getSelectedValue().getValeur2()).toString());
            }
        }
        else if (NorthMaterialPanel.listeningTolistComponentIsEnable && e.getSource() == this.AllComponentList) {
            this.setCurrentComponent(this.AllComponentList.getSelectedValue());
            this.updateFrameComponent();
        }
    }
    
    @SuppressWarnings("unchecked")
	@Override
    public void actionPerformed(final ActionEvent e) {
        if (e.getActionCommand().equals("add component")) {
            if (this.currentMaterial != null) {
                this.currentMaterial.addComponent(this.AllComponentList.getSelectedValue(), 100f - Float.parseFloat(this.massLabel.getText().replaceAll("\\s", "")));
            }
        }
        else if (e.getActionCommand().equals("remove component")) {
            if (this.currentMaterial != null) {
                this.currentMaterial.deleteComponent(this.JlistCompInMat.getSelectedValue().getValeur1());
            }
        }
        else if (e.getActionCommand().equals("add material")) {
            final Material m = new Material(this.Father.getListOfMaterial().size());
            System.err.println("new material "+m);
            this.currentMaterial = m;
            ((List<Material>)this.Father.getListOfMaterial()).add(m);
            System.err.println("list size "+this.Father.getListOfMaterial().size());
            this.listModelMaterial.addElement(m);
            this.JlistMaterial.setSelectedValue(m, true);
            this.materialListener.materialAdded();
        }
        else if (e.getActionCommand().equals("delete material")) {
            final int index = this.JlistMaterial.getSelectedIndex();
            if (index != -1) {
                this.Father.getListOfMaterial().remove(index);
                this.listModelMaterial.removeElementAt(index);
            }
        } else if(e.getActionCommand().equals("use default density"))
        {
        	if(this.useCalculatedDensity.isSelected())
            {
            	this.setDensityField.setEnabled(false);
            	this.currentMaterial.setUseCalculatedDensity(true);
            }
        	else
            {
            	this.setDensityField.setEnabled(true);
            	this.currentMaterial.setUseCalculatedDensity(false);
            }
        }
        this.updateFrameComponent();
    }
    
    @Override
    public void componentAdded(final mainPackage.Component c) {
        this.AllCompModelList.addElement(c);
    }
    
    @Override
    public void keyPressed(final KeyEvent e) {
        if (e.getKeyCode() == 10) {
            if (this.currentMaterial == null) {
                return;
            }
            if (e.getSource() == this.massPercentField || e.getSource() == this.waterField 
            		|| e.getSource() == this.setDensityField || e.getSource() == this.nameField 
            		|| e.getSource() == this.compacityField || e.getSource() == this.densityField 
            		|| e.getSource() == this.GrainArea) {
            	try
            	{
            		if (!this.massPercentField.getText().replaceAll("\\s", "").isEmpty() && !this.JlistCompInMat.isSelectionEmpty()) {
                        final Material tmpMat = this.JlistMaterial.getSelectedValue();
                        tmpMat.setComponentPercent(this.JlistCompInMat.getSelectedIndex(), Float.parseFloat(this.massPercentField.getText().replaceAll("\\s", "")));
                    }
                    if (!this.waterField.getText().replaceAll("\\s", "").isEmpty()) {
                        this.currentMaterial.setWaterContent(Float.parseFloat(this.waterField.getText().replaceAll("\\s", "")));
                    }
                    if (!this.setDensityField.getText().replaceAll("\\s", "").isEmpty()) {
                        this.currentMaterial.setDryDensity(Float.parseFloat(this.setDensityField.getText().replaceAll("\\s", "")));
                    }
                    if (!this.nameField.getText().replaceAll("\\s", "").isEmpty()) {
                        this.currentMaterial.setName(this.nameField.getText().trim());
                    }
                    if (!this.compacityField.getText().replaceAll("\\s", "").isEmpty()) {
                        this.currentMaterial.setGrainCompacity(Float.parseFloat(this.compacityField.getText().replaceAll("\\s", "")));
                    }
                    if (!this.densityField.getText().replaceAll("\\s", "").isEmpty()) {
                        this.currentMaterial.setGrainDensity(Float.parseFloat(this.densityField.getText().replaceAll("\\s", "")));
                    }
            	}catch(NumberFormatException e2)
            	{
            		//Ignore
            	}
                
                try {
                    this.parseGrainAreaToMaterial();
                }
                catch (ArrayIndexOutOfBoundsException ae) {
                    JOptionPane.showMessageDialog(null, "A value in the grain distribution area is missing.", "Missing value", 0);
                }
                catch (NumberFormatException ne) {
                    JOptionPane.showMessageDialog(null, "A value in the grain distribution area is not a number.", "Forbidden value", 0);
                }
            }
            this.updateFrameComponent();
        }
        else if (e.getSource() == this.massPercentField) {
            this.massPercentField.setBorder(BorderFactory.createLineBorder(Color.red, 2));
        }
        else if (e.getSource() == this.waterField) {
            this.waterField.setBorder(BorderFactory.createLineBorder(Color.red, 2));
        }
        else if (e.getSource() == this.setDensityField) {
            this.setDensityField.setBorder(BorderFactory.createLineBorder(Color.red, 2));
        }
        else if (e.getSource() == this.nameField) {
            this.nameField.setBorder(BorderFactory.createLineBorder(Color.red, 2));
        }
        else if (e.getSource() == this.compacityField) {
            this.compacityField.setBorder(BorderFactory.createLineBorder(Color.red, 2));
        }
        else if (e.getSource() == this.densityField) {
            this.densityField.setBorder(BorderFactory.createLineBorder(Color.red, 2));
        }
        else if (e.getSource() == this.GrainArea) {
            this.GrainArea.setBorder(BorderFactory.createLineBorder(Color.red, 2));
        }
    }
    
    @Override
    public void keyReleased(final KeyEvent arg0) {
    }
    
    @Override
    public void keyTyped(final KeyEvent arg0) {
    }
    
    @Override
    public void itemStateChanged(final ItemEvent e) {
        if (NorthMaterialPanel.listeningTolistComponentIsEnable && e.getSource() == this.comboGrain) {
            final mainPackage.Component c = (mainPackage.Component)this.comboGrain.getSelectedItem();
            if (c != null) {
                this.currentMaterial.setGrainComponentIndex(this.Father.getListOfComponent().indexOf(c));
            }
        }
    }
    
    /**
     * Listener will be notified if a material is added.
     * @param main the listener.
     */
    public void addmaterialAddedListener(final MaterialAddedListener main) {
        this.materialListener = main;
    }
    
    /**
     * Change the current component to work with.
     * @param currentComponent the new current component.
     */
    public void setCurrentComponent(final mainPackage.Component currentComponent) {
        this.currentComponent = currentComponent;
    }
    
    /**
     * @return the current selected component.
     */
    public mainPackage.Component getCurrentComponent() {
        return this.currentComponent;
    }
    
    /**
     * Change the current material to work with and update the Swing components.
     * @param currentComponent the new current material.
     */
    public void setCurrentMaterial(final Material m) {
        this.JlistMaterial.setSelectedValue(m, true);
        this.updateFrameComponent();
    }
}
