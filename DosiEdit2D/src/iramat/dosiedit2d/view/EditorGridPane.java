package iramat.dosiedit2d.view;

import interfaceObject.Grid2D.RadMode;
import interfaceObject.ZoomAndPanListener;
import iramat.dosiedit2d.controler.*;
import iramat.dosiedit2d.model.Dosi2DModel;
import iramat.dosiedit2d.view.TextureCellRenderer.CheckListItem;
import iramat.dosiseed.controler.AbstractFieldControler;
import iramat.dosiseed.controler.MaxRangeControler;
import iramat.dosiseed.controler.NbParticleControler;
import iramat.dosiseed.controler.ProductionCutControler;
import iramat.dosiseed.model.AbstractModel;
import iramat.dosiseed.model.ColoredMaterial;
import iramat.dosiseed.model.Material;
import iramat.dosiseed.view.FormattedLabelField;
import iramat.dosiseed.view.TripleFormattedField.XYZ;
import mainPackage.PrimaryParticles;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.*;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

/**
 * Class which handles the view of the editor tab of the project. It handles the grid, the fields to
 * change value in the model, the current material to work with.
 * @author alan
 *
 */
public class EditorGridPane extends JPanel implements ListSelectionListener, Observer, MouseMotionListener, MouseListener, ActionListener, ChangeListener
{
	/**
	 * Inner class extending {@link DefaultListCellRenderer} to show only the name of the 
	 * material in item renderer.
	 * @author alan
	 *
	 */
	public class ComboMaterialRender extends DefaultListCellRenderer
	{

		/**
		 * 
		 */
		private static final long serialVersionUID = -7087268775527109818L;

		@Override
		public Component getListCellRendererComponent(
				JList<?> list, Object value,
				int index, boolean isSelected, boolean cellHasFocus)
		{
			JLabel renderer = (JLabel) super.getListCellRendererComponent(list, value, index,
			        isSelected, cellHasFocus);
			if(value != null)
				renderer.setText( ((ColoredMaterial) value).getName());
			else
				renderer.setText("All material");
			return renderer;
		}
		
	}
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -7723317114887242631L;
	
	/**
	 * The global listener for fields.
	 */
	private GlobalControler controler;
	
	/**
	 * The grid panel.
	 */
	private Grid2D gridPane;

	/**
	 * The view of number in which voxel the mouse cursor is.
	 */
	private JLabel VoxelInfo;

	/**
	 * The view of the current material.
	 */
	private JLabel currentMatLabel;
	
	/**
	 * The view of the number of particle emitted.
	 */
	private FormattedLabelField nbParticleEmittedField;
	
	/**
	 * The view of the clock value.
	 */
	private FormattedLabelField clockValueField;
	
	/**
	 * The view of the number of the thread.
	 */
	private FormattedLabelField nbThreadField;
	
	/**
	 * The view to choose the particle to emit.
	 */
	private JComboBox<PrimaryParticles> particleCombo;
	
	/**
	 * The view to choose the emitted process.
	 */
	private JComboBox<String> emissionProcessCombo;
	
	/**
	 * Views of radio-element to simulate.
	 */
	private JCheckBox[] radioElementsCheck;
	
	/**
	 * The view of average range value.
	 */
	private FormattedLabelField averageRangeField;
	
	/**
	 * The view of the cutting range value.
	 */
	private FormattedLabelField cutRangeValueField;
	
	/**
	 * the view for the material chosen for dose mapping.
	 */
	private JComboBox<ColoredMaterial> doseMappingCombo;
	
	/**
	 * The view of number of voxel.
	 */
	private DoubleFormattedField nbVoxelField;
	
	/**
	 * The view of size of voxel.
	 */
	private DoubleFormattedField sizeVoxelField;
	
	/**
	 * The view for the exclusion edge boolean.
	 */
	private JCheckBox excludeEdgeCheck;
	
	/**
	 * The view for the list of material in model.
	 */
	private JList<CheckListItem> listMaterial;
	
	/**
	 * The model of the {@link #listMaterial}
	 */
	private DefaultListModel<CheckListItem> listModelMaterial;
	
	/**
	 * The contextual menu to let the user change color, texture or fields with right click.
	 */
	private MaterialPopupMenu ColorTexturePopup;

	/**
	 * The tab manager.
	 */
	private TabManager tabManager;

	/**
	 * The view of the name of current material.
	 */
	private JLabel nameLabel;

	/**
	 * The view of the current radiation value used by the current material.
	 */
	private ExtensibleLabelizedPanel CurrentRadValues;

	/**
	 * The view to change the current radiation value used by the current material.
	 */
	private ExtensibleFormattedPanel RadField;

	/**
	 * The view of the reference radiation value of the current material.
	 */
	private ExtensibleLabelizedPanel ReferenceRadValues;

	/**
	 * The view to change the reference radiation value of the current material.
	 */
	private ExtensibleFormattedPanel ReferenceRadField;
	
	/**
	 * The view for displaying mode.
	 */
	private JRadioButton MatMode;
	
	/**
	 * The view for displaying mode.
	 */
	private JRadioButton UMode;
	
	/**
	 * The view for displaying mode.
	 */
	private JRadioButton ThMode;
	
	/**
	 * The view for displaying mode.
	 */
	private JRadioButton KMode;
	
	/**
	 * The view for displaying mode.
	 */
	private JRadioButton UdMode;
	
	/**
	 * Triggered Loading image with:
	 * - FileChooser : choose the path of the directory containing image(s).
	 * - ScanningWindow : Choose image.
	 * - Association2dFrame : Associate grey levels and material. 
	 */
	private JButton loadImageButton;
	
	/**
	 * Constructor.
	 * @param tab the tab manager.
	 * @param model the model to work with.
	 * @param globalControl the listener for fields.
	 */
	public EditorGridPane(final TabManager tab, AbstractModel model,GlobalControler globalControl)
	{
		this.setLayout(new BorderLayout());
		(gridPane = new Grid2D(globalControl)).addMouseMotionListener(this);
		this.VoxelInfo = new JLabel();
		this.VoxelInfo.setPreferredSize(new Dimension(70,30));
		this.currentMatLabel = new JLabel();
		this.currentMatLabel.setPreferredSize(new Dimension(140,30));
		
		JPanel WestPane = new JPanel(new BorderLayout());
		final JToolBar toolbar = new JToolBar();
		final JToggleButton penButtonTool = new JToggleButton(View.createImageIcon("/resources/pen.png"));
		penButtonTool.setActionCommand("pen mode selection");
		penButtonTool.setToolTipText("Modifying voxel per voxel");
		penButtonTool.addActionListener(this.gridPane);
		final JToggleButton recButtonTool = new JToggleButton(View.createImageIcon("/resources/rectangle.png"));
		recButtonTool.setActionCommand("rectangle mode selection");
		recButtonTool.setToolTipText("Modifying voxels intersects with the selection rectangle");
		recButtonTool.addActionListener(this.gridPane);
		final JButton fillLayerButtonTool = new JButton(View.createImageIcon("/resources/fillLayer.png"));
		fillLayerButtonTool.setActionCommand("fill the selected layer");
		fillLayerButtonTool.setToolTipText("Filling all voxels in the selected layer with the selected material");
		fillLayerButtonTool.addActionListener(this.gridPane);
		final JButton fillEmptyLayerButtonTool = new JButton(View.createImageIcon("/resources/fillEmptyLayer.png"));
		fillEmptyLayerButtonTool.setActionCommand("fill empty voxel in the selected layer");
		fillEmptyLayerButtonTool.setToolTipText("Filling all empty voxels in the selected layer with the selected material");
		fillEmptyLayerButtonTool.addActionListener(this.gridPane);
		
		final ButtonGroup bg2 = new ButtonGroup();
		bg2.add(penButtonTool);
		bg2.add(recButtonTool);
		penButtonTool.setSelected(true);
		toolbar.add(penButtonTool);
		toolbar.add(recButtonTool);
		toolbar.addSeparator();
		toolbar.add(fillEmptyLayerButtonTool);
		toolbar.add(fillLayerButtonTool);
		toolbar.addSeparator();
		toolbar.add(this.currentMatLabel);
		toolbar.addSeparator();
		toolbar.add(this.VoxelInfo);
		toolbar.addSeparator();
		final JPanel showRadioPanel = new JPanel();
		(this.MatMode = new JRadioButton("Mat")).addChangeListener(this);
		this.MatMode.setToolTipText("Material");
		showRadioPanel.add(this.MatMode);
		(this.UMode = new JRadioButton("U")).addChangeListener(this);
		this.UMode.setToolTipText("Uranium");
		showRadioPanel.add(this.UMode);
		(this.ThMode = new JRadioButton("Th")).addChangeListener(this);
		this.ThMode.setToolTipText("Thorium");
		showRadioPanel.add(this.ThMode);
		(this.KMode = new JRadioButton("K")).addChangeListener(this);
		this.KMode.setToolTipText("Potassium");
		showRadioPanel.add(this.KMode);
		(this.UdMode = new JRadioButton("User-def")).addChangeListener(this);
		this.UdMode.setToolTipText("User-defined");
		showRadioPanel.add(this.UdMode);
		final ButtonGroup bg1 = new ButtonGroup();
		bg1.add(this.MatMode);
		bg1.add(this.UMode);
		bg1.add(this.ThMode);
		bg1.add(this.KMode);
		bg1.add(this.UdMode);
		this.MatMode.setSelected(true);
		toolbar.add(showRadioPanel);
		WestPane.add(toolbar,BorderLayout.NORTH);
		WestPane.add(this.gridPane);
		this.add(WestPane);
		
		JPanel EastNorthPane = new JPanel(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		gbc.gridheight = 1;
		gbc.anchor = GridBagConstraints.LINE_END;
		this.nbParticleEmittedField = new FormattedLabelField(this, new JLabel("<html><b>Number of particle emitted (x1000): </b></html>"));
		EastNorthPane.add(this.nbParticleEmittedField,gbc);
		gbc.gridy = 1;
		this.clockValueField = new FormattedLabelField(this, new JLabel("<html><b>Clock value (%): </b></html>"));
		EastNorthPane.add(this.clockValueField,gbc);
		gbc.gridy = 2;
		this.nbThreadField = new FormattedLabelField(this, new JLabel("<html><b>Number of CPU core used for simulation: </b></html>"));
		EastNorthPane.add(this.nbThreadField,gbc);
		gbc.gridy = 3;
		JPanel particlePan = new JPanel();
		JLabel particleLab = new JLabel("<html><b>Primary particle emitted: </b></html>");
		this.particleCombo = new JComboBox<>();
		this.particleCombo.addItem(PrimaryParticles.Alpha);
		this.particleCombo.addItem(PrimaryParticles.Beta);
		this.particleCombo.addItem(PrimaryParticles.Gamma);
		particlePan.add(particleLab);
		particlePan.add(this.particleCombo);
		EastNorthPane.add(particlePan,gbc);
		gbc.gridy = 4;
		JLabel emissionLab = new JLabel("<html><b>Emission process: </b></html>");
		this.emissionProcessCombo = new JComboBox<>();
		this.emissionProcessCombo.addItem("2D Partial");
		this.emissionProcessCombo.addItem("3D");
		this.emissionProcessCombo.addItem("2D real");
		JPanel emissionPan = new JPanel();
		emissionPan.add(emissionLab);
		emissionPan.add(emissionProcessCombo);
		EastNorthPane.add(emissionPan,gbc);
		gbc.gridy = 5;
		JPanel radioElementPanel = new JPanel();
		radioElementPanel.add(new JLabel("<html><b>Radio-active elements to simulate: </b></html>"));
		this.radioElementsCheck = new JCheckBox[4];
		JPanel panelcheck = new JPanel();
		radioElementsCheck[0] = new JCheckBox("K");
		panelcheck.add(radioElementsCheck[0]);
		radioElementsCheck[1] = new JCheckBox("U");
		panelcheck.add(radioElementsCheck[1]);
		radioElementsCheck[2] = new JCheckBox("Th");
		panelcheck.add(radioElementsCheck[2]);
		radioElementsCheck[3] = new JCheckBox("Ud");
		panelcheck.add(radioElementsCheck[3]);
		radioElementPanel.add(panelcheck);
		EastNorthPane.add(radioElementPanel,gbc);
		this.averageRangeField = new FormattedLabelField(this,new JLabel("<html><b>Particle average range (in mm): </b></html>"));
		gbc.gridy = 6;
		EastNorthPane.add(this.averageRangeField,gbc);
		this.cutRangeValueField = new FormattedLabelField(this, new JLabel("<html><b>Cut in range value (in mm): </b></html>"));
		gbc.gridy = 7;
		EastNorthPane.add(this.cutRangeValueField,gbc);
		JPanel doseMappingPan = new JPanel();
		this.doseMappingCombo = new JComboBox<>();
		this.doseMappingCombo.setRenderer(new ComboMaterialRender());
		this.doseMappingCombo.setPreferredSize(new Dimension(200,25));
		doseMappingPan.add(new JLabel("<html><b>Material for dose mapping</b></html>"));
		doseMappingPan.add(this.doseMappingCombo);
		gbc.gridy = 8;
		EastNorthPane.add(doseMappingPan,gbc);
		this.excludeEdgeCheck = new JCheckBox("<html><b>Exclude edge effect zones for dose results (recommanded)</b></html>");
		this.excludeEdgeCheck.setHorizontalTextPosition(SwingConstants.LEFT);
		gbc.gridy = 9;
		EastNorthPane.add(this.excludeEdgeCheck,gbc);
		
		this.loadImageButton = new JButton("Load a grey scale image into the grid");
		this.loadImageButton.setActionCommand("load image");
		this.loadImageButton.addActionListener(globalControl);
		gbc.gridy = 10;
		gbc.gridx = 0;
		EastNorthPane.add(this.loadImageButton,gbc);
		
		JPanel descGridPanel = new JPanel();
		descGridPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createRaisedSoftBevelBorder(),
				"Voxel description",
				TitledBorder.CENTER,TitledBorder.CENTER));
		this.nbVoxelField = new DoubleFormattedField(new JLabel("Number:"), false);
		NbVoxel2DControler nbControl = new NbVoxel2DControler((Dosi2DModel) model);
		nbControl.setView(nbVoxelField);
		this.nbVoxelField.addKeyListener(nbControl);
		descGridPanel.add(this.nbVoxelField);
		
		this.sizeVoxelField = new DoubleFormattedField(new JLabel("Size:"), false);
		SizeVoxelControler sizeControl = new SizeVoxelControler((Dosi2DModel) model);
		sizeControl.setView(sizeVoxelField);
		this.sizeVoxelField.addKeyListener(sizeControl);
		descGridPanel.add(this.sizeVoxelField);
		gbc.gridy = 11;
		gbc.fill = GridBagConstraints.BOTH;
		EastNorthPane.add(descGridPanel,gbc);
		JButton validateButton = new JButton("Validate values");
		validateButton.setActionCommand("Validate");
		validateButton.setToolTipText("Validate all outdated fields");
		JButton reloadButton = new JButton("Refresh values");
		reloadButton.setToolTipText("Update all outdated fields with previous saved values");
		reloadButton.setActionCommand("Reload");
		Dimension dim = new Dimension(220,30);
		validateButton.setPreferredSize(dim);
		validateButton.addActionListener(this);
		reloadButton.setPreferredSize(dim);
		reloadButton.addActionListener(this);
		gbc.gridy = 12;
		gbc.gridwidth = 1;
		EastNorthPane.add(validateButton,gbc);
		gbc.gridx = 1;
		//gbc.gridwidth = GridBagConstraints.REMAINDER;
		EastNorthPane.add(reloadButton,gbc);
		
		JSplitPane EastPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
		JPanel fillAllNorthPanel= new JPanel(new BorderLayout());
		fillAllNorthPanel.add(EastNorthPane,BorderLayout.NORTH);
		EastPane.setTopComponent(new JScrollPane(fillAllNorthPanel));
		
		JPanel EastSouthPane = new JPanel(new GridBagLayout());
		GridBagConstraints gbcS = new GridBagConstraints();
		gbcS.gridx = 0;
		gbcS.gridy = 0;
		gbcS.gridwidth = GridBagConstraints.REMAINDER;
		gbcS.gridheight = 1;
		gbcS.fill = GridBagConstraints.BOTH;
		gbcS.anchor = GridBagConstraints.FIRST_LINE_START;
		JPanel info = initRaditionPan(globalControl);
		info.setPreferredSize(new Dimension(450,270));
		EastSouthPane.add(info,gbcS);
		this.listModelMaterial = new DefaultListModel<>();
		(this.listMaterial = new JList<>(this.listModelMaterial))
				.setCellRenderer(new TextureCellRenderer<Object>());
		this.listMaterial.addListSelectionListener(this);
		this.listMaterial.addMouseListener(this);
		this.listMaterial.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		gbcS.gridy = 1;
		JScrollPane jsp = new JScrollPane(this.listMaterial);
		jsp.setBorder(BorderFactory.createTitledBorder(BorderFactory.createRaisedBevelBorder(),"List of material"));
		jsp.setPreferredSize(new Dimension(200,200));
		EastSouthPane.add(jsp,gbcS);
		EastNorthPane.setBorder(BorderFactory.createTitledBorder(BorderFactory.createRaisedBevelBorder(), "PROJECT INFO"));
		
		JPanel fillAllSouthPanel = new JPanel(new BorderLayout());
		fillAllSouthPanel.add(EastSouthPane,BorderLayout.NORTH);
		EastPane.setBottomComponent(new JScrollPane(fillAllSouthPanel));
		EastPane.setPreferredSize(new Dimension(475,100));
		this.add(EastPane,BorderLayout.EAST);
		
		EastPane.setDividerLocation(510);
		setControler(tab,model,globalControl);
		
		model.addObserver(gridPane);
		model.addObserver(this);
		model.setChanged();
		model.notifyObservers();
	}

	/**
	 * Initializes material information panel.
	 * @param control listener to link with fields created.
	 * @return the material information.
	 */
	private JPanel initRaditionPan(GlobalControler control)
	{
		final JPanel InfoPan = new JPanel(new BorderLayout());
		InfoPan.setBorder(BorderFactory.createTitledBorder(BorderFactory.createRaisedBevelBorder(),"Material and radioactivity"));
		final JPanel namePan = new JPanel(new BorderLayout());
		(this.nameLabel = new JLabel("Name:    ")).setAlignmentX(0.5f);
		this.CurrentRadValues = new ExtensibleLabelizedPanel(new JLabel("<html>Radiaoactive<br> Element Contents:</html> "), "U", "Th", "K", null);
		this.CurrentRadValues.addLabel("Ud");
		namePan.add(this.nameLabel, "North");
		namePan.add(this.CurrentRadValues, "East");
		final JPanel RadPan = new JPanel(new BorderLayout());
		(this.RadField = new ExtensibleFormattedPanel(new JLabel("<html>Changing values<br> above:</html>"), "U", "Th", "K", false)).addKeyListener(
				new KeyListener()
				{

					@Override
					public void keyTyped(KeyEvent e)
					{
					}

					@Override
					public void keyReleased(KeyEvent e)
					{

					}

					@Override
					public void keyPressed(KeyEvent e)
					{
						if (e.getKeyCode() == 10 && EditorGridPane.this.gridPane.getCurrentMaterial() != null) {
							
							float x=0,y=0,z=0;
							float ud=0;
							try {
										x=EditorGridPane.this.RadField.getValue(XYZ.X);
							}
							catch (Exception ex) {}
							try {
										y=EditorGridPane.this.RadField.getValue(XYZ.Y);
							}
							catch (Exception ex2) {}
							try {
										z=EditorGridPane.this.RadField.getValue(XYZ.Z);
							}
							catch (Exception ex3) {}
							
							ud = Float.parseFloat(EditorGridPane.this.RadField.getFieldFromList(0).getText().replaceAll("\\s+", ""));
							EditorGridPane.this.controler.setCurrentRadValue(EditorGridPane.this.gridPane.getCurrentMaterial(),x,y,z,ud);
							EditorGridPane.this.RadField.updateFrameComponent(x, y, z,ud);
							EditorGridPane.this.updateFieldFromCurrentMaterial();
						}
					}
				});
		this.RadField.addFields("Ud", false);
		RadPan.add(this.RadField, "East");
		RadPan.add(this.createVerticalHorizontal(), "South");
		final JPanel RefPan = new JPanel(new BorderLayout());
		final JButton applyRefValue = new JButton("\u21ba Reload the reference values \u21bb");
		applyRefValue.setActionCommand("apply ref value");
		applyRefValue.addActionListener(this);
		this.ReferenceRadValues = new ExtensibleLabelizedPanel(new JLabel("Reference Values : "), "U", "Th", "K", null);
		this.ReferenceRadValues.addLabel("Ud");
		RefPan.add(applyRefValue, "North");
		RefPan.add(this.ReferenceRadValues, "Center");

		(this.ReferenceRadField = new ExtensibleFormattedPanel(new JLabel("Change reference values"),"U","Th","K", false)).addKeyListener(
				new KeyListener()
				{

					@Override
					public void keyTyped(KeyEvent e)
					{


					}

					@Override
					public void keyReleased(KeyEvent arg0)
					{

					}

					@Override
					public void keyPressed(KeyEvent e)
					{
						if (e.getKeyCode() == 10 && EditorGridPane.this.gridPane.getCurrentMaterial() != null) {
							
							float x=0,y=0,z=0;
							float ud=0;
							try {
										x=EditorGridPane.this.ReferenceRadField.getValue(XYZ.X);
							}
							catch (Exception ex) {}
							try {
										y=EditorGridPane.this.ReferenceRadField.getValue(XYZ.Y);
							}
							catch (Exception ex2) {}
							try {
										z=EditorGridPane.this.ReferenceRadField.getValue(XYZ.Z);
							}
							catch (Exception ex3) {}
							ud = Float.parseFloat(EditorGridPane.this.ReferenceRadField.getFieldFromList(0).getText().replaceAll("\\s+", ""));
							EditorGridPane.this.controler.setRefRadValue(EditorGridPane.this.gridPane.getCurrentMaterial(),x,y,z,ud);
							EditorGridPane.this.ReferenceRadField.updateFrameComponent(x, y, z,ud);
							EditorGridPane.this.updateFieldFromCurrentMaterial();
						}

					}
				});
		this.ReferenceRadField.addFields("Ud", false);
		RefPan.add(this.ReferenceRadField, BorderLayout.SOUTH);

		InfoPan.add(namePan, "North");
		InfoPan.add(RadPan, "Center");
		InfoPan.add(RefPan, "South");
		return InfoPan;
	}
	
	/**
	 * Instantiates controller for AbstractFieldControler in members.
	 * @param tab the tab manager.
	 * @param model the model to work with.
	 * @param control the listener to set to members.
	 */
	private void setControler(TabManager tab,AbstractModel model, GlobalControler control)
	{
		AbstractFieldControler l = new NbParticleControler(model);
		this.nbParticleEmittedField.getField().addKeyListener(l);
		this.nbParticleEmittedField.getField().addFocusListener(l);
		AbstractFieldControler cl = new ClockValueControler((Dosi2DModel) model);
		this.clockValueField.getField().addKeyListener(cl);
		this.clockValueField.getField().addFocusListener(cl);
		AbstractFieldControler ntl = (new NbThreadControler(model));
		this.nbThreadField.getField().addKeyListener(ntl);
		this.nbThreadField.getField().addFocusListener(ntl);
		this.radioElementsCheck[0].addActionListener(control);
		this.radioElementsCheck[0].setActionCommand("K boolean");
		this.radioElementsCheck[1].addActionListener(control);
		this.radioElementsCheck[1].setActionCommand("U boolean");
		this.radioElementsCheck[2].addActionListener(control);
		this.radioElementsCheck[2].setActionCommand("Th boolean");
		this.radioElementsCheck[3].addActionListener(control);
		this.radioElementsCheck[3].setActionCommand("Ud boolean");

		particleCombo.addActionListener(control);
		particleCombo.setActionCommand("type particle");
		this.emissionProcessCombo.addActionListener(control);
		this.emissionProcessCombo.setActionCommand("emission process");
		AbstractFieldControler arc = new MaxRangeControler(model);
		this.averageRangeField.getField().addKeyListener(arc);
		this.averageRangeField.getField().addFocusListener(arc);
		AbstractFieldControler crc = new ProductionCutControler(model);
		this.cutRangeValueField.getField().addKeyListener(crc);
		this.cutRangeValueField.getField().addFocusListener(crc);
		this.doseMappingCombo.addActionListener(control);
		this.doseMappingCombo.setActionCommand("dose mapping");
		this.excludeEdgeCheck.addActionListener(control);
		this.excludeEdgeCheck.setActionCommand("exclude edge");
		
		this.tabManager = tab;
		this.controler = control;
	}
	
	public void setLoadTitle(String title)
	{
		this.loadImageButton.setText("<html><b>"+title +"</b> loaded</html>");
	}
	/**
	 * Update the Swing components.
	 * @return the created separator.
	 */
	private Container createVerticalHorizontal() {
		final JSeparator j = new JSeparator(0);
		j.setPreferredSize(new Dimension(100, 5));
		return j;
	}
	
	/**
	 * Update views of radiation values with values in current material.
	 */
	public void updateFieldFromCurrentMaterial()
	{
		if (this.gridPane.getCurrentMaterial() != null) {

			ColoredMaterial current = this.gridPane.getCurrentMaterial();
			this.nameLabel.setText("<html><b>Name :   " + current.getName() + "</b></html>");
			this.CurrentRadValues.setXLabel(new StringBuilder(String.valueOf(current.getCurrentUraniumValue())).toString());
			this.CurrentRadValues.setYLabel(new StringBuilder(String.valueOf(current.getCurrentThoriumValue())).toString());
			this.CurrentRadValues.setZLabel(new StringBuilder(String.valueOf(current.getCurrentPotassiumValue())).toString());
			this.CurrentRadValues.getLabelFromList(0).setText(current.getCurrentUserDefinedValue()+"");
			
			this.ReferenceRadValues.setXLabel(new StringBuilder(String.valueOf(current.getRefUraniumValue())).toString());
			this.ReferenceRadValues.setYLabel(new StringBuilder(String.valueOf(current.getRefThoriumValue())).toString());
			this.ReferenceRadValues.setZLabel(new StringBuilder(String.valueOf(current.getRefPotassiumValue())).toString());
			this.ReferenceRadValues.getLabelFromList(0).setText(current.getRefUserDefinedValue()+"");
		}
	}
	
	/**
	 * Triggered when a value in the material list is changed.
	 */
	@Override
	public void valueChanged(final ListSelectionEvent e) {
		if (!this.listMaterial.isSelectionEmpty()) {
			ColoredMaterial material = this.listMaterial.getSelectedValue().getM();
			this.gridPane.setCurrentMaterial(material);
			this.currentMatLabel.setText("<html><b>" + material.getName() + "<b></html>");
			this.updateFieldFromCurrentMaterial();
		}
	}

	@Override
	public void update(Observable obs, Object arg1)
	{
		Dosi2DModel model = (Dosi2DModel) obs;
		this.nbParticleEmittedField.getField().setText(model.getNbParticlesEmitted()+"");
		this.clockValueField.getField().setText(model.getClockValue()+"");
		this.nbThreadField.getField().setText(model.getNbThread()+"");
		this.particleCombo.setSelectedItem(model.getPrimaryParticle());
		this.emissionProcessCombo.setSelectedItem(model.getEmissionProcess());
		boolean[] radio = model.getWhichRadioelementIsSimulated();
		for(int i=0;i<radio.length;i++)
			radioElementsCheck[i].setSelected(radio[i]);
		this.averageRangeField.getField().setText(model.getMaxRange()+"");
		this.cutRangeValueField.getField().setText(model.getProductionCut()+"");
		
		this.doseMappingCombo.removeAllItems();
		this.doseMappingCombo.addItem(null);
		for(Material m : model.getListOfMaterial())
			this.doseMappingCombo.addItem((ColoredMaterial) m);
		this.doseMappingCombo.setSelectedItem(model.getMaterialForDoseMapping());
		this.excludeEdgeCheck.setSelected(model.isExcludeEdge());
		
		listModelMaterial.removeAllElements();
		final List<Material> listM = model.getListOfMaterial();
		int i = 0;
		for (final Material m : listM) {
			if (this.listModelMaterial.size() <= i) {
				this.listModelMaterial.insertElementAt(new TextureCellRenderer.CheckListItem((ColoredMaterial) m), i);
			}
			else if (!m.toString().equals(this.listModelMaterial.get(i))) {
				this.listModelMaterial.get(i).setLabel(m.getName());
			}
			++i;
		}
		if (this.gridPane.getCurrentMaterial() != null) {
			this.listMaterial.setSelectedValue(this.gridPane.getCurrentMaterial(), true);
		}

		this.nbVoxelField.updateFrameComponent(model.getGrid().get(0).size(), model.getGrid().size());
		//this.sizeVoxelField.updateFrameComponent(model.getVoxelDimensionX(), model.getVoxelDimensionY());
		this.sizeVoxelField.updateFrameComponent(model.getRealDimX(), model.getRealDimY());
		this.updateFieldFromCurrentMaterial();
	}

	@Override
	public void mouseClicked(MouseEvent event)
	{
		if (!event.getSource().getClass().equals(JList.class)) {
			return;
		}
		final JList<?> list = (JList<?>)event.getSource();
		final int index = list.locationToIndex(event.getPoint());
		if (index == -1) {
			return;
		}
		//final TextureCellRenderer.CheckListItem item = (TextureCellRenderer.CheckListItem)list.getModel().getElementAt(index);
		final Point ptI = MouseInfo.getPointerInfo().getLocation();
		SwingUtilities.convertPointFromScreen(ptI, list);

		//item.getM().setActive(item.isSelected());
		list.repaint(list.getCellBounds(index, index));
		this.repaint();
	}

	@Override
	public void mouseEntered(MouseEvent arg0)
	{
	}

	@Override
	public void mouseExited(MouseEvent arg0)
	{
	}

	@Override
	public void mousePressed(MouseEvent e)
	{
		if (e.getSource() == this.listMaterial && e.isPopupTrigger()) {
			this.mouseMultiOsPopupTrigger(e);
		}
	}

	@Override
	public void mouseReleased(MouseEvent e)
	{
		if (e.getSource() == this.listMaterial && e.isPopupTrigger()) {
			this.mouseMultiOsPopupTrigger(e);
		}
	}
	
	/**
	 * That function is set for contextual menu, opened with right-click on Windows or Linux,
	 * but MacOS has a special mouse when right-click is nonexistent. 
	 * @param e the Event handling info.
	 */
	private void mouseMultiOsPopupTrigger(final MouseEvent e) {
		this.listMaterial.setSelectedIndex(this.listMaterial.locationToIndex(e.getPoint()));
		this.gridPane.setCurrentMaterial(this.listMaterial.getSelectedValue().getM());
		try {
			this.ColorTexturePopup = new MaterialPopupMenu(this.listMaterial,this.tabManager, this.gridPane,
					this.listMaterial.getSelectedValue().getM());
		}
		catch (NullPointerException exception) {
			System.err.println("Material Popup menu get a null pointer exception.");
		}
		this.ColorTexturePopup.show(this, this.getMousePosition().x, this.getMousePosition().y);
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		if (e.getActionCommand().equals("apply ref value")) {
			if (this.gridPane.getCurrentMaterial() == null) {
				return;
			}
			int retour = JOptionPane.showConfirmDialog(this, "You are about to change all radiation values" +
					"in every voxels.", "WARNING", JOptionPane.YES_NO_CANCEL_OPTION,JOptionPane.WARNING_MESSAGE);
			if(retour == JOptionPane.OK_OPTION)
			{
				final Material m = this.gridPane.getCurrentMaterial();
				m.setCurrentUraniumValue(m.getRefUraniumValue());
				m.setCurrentThoriumValue(m.getRefThoriumValue());
				m.setCurrentPotassiumValue(m.getRefPotassiumValue());
				m.setCurrentUserDefinedValue(m.getRefUserDefinedValue());
				this.controler.reloadReferenceRadiationValue(m);
				this.gridPane.repaint();
			}
		}
		else if(e.getActionCommand().equals("Validate"))
		{
			//Nb particle
			KeyEvent nbParticleEvent = new KeyEvent(this.nbParticleEmittedField.getField(), 1, 20, 
					0, KeyEvent.VK_ENTER, 'a');
			this.nbParticleEmittedField.getField().getKeyListeners()[0].keyPressed(nbParticleEvent);
			
			//Clock value
			KeyEvent clockValueEvent = new KeyEvent(this.clockValueField.getField(), 2, 20, 0,
					KeyEvent.VK_ENTER, 'a');
			this.clockValueField.getField().getKeyListeners()[0].keyPressed(clockValueEvent);
			
			//nb thread
			KeyEvent nbThreadEvent = new KeyEvent(this.nbThreadField.getField(), 3, 20, 0,
					KeyEvent.VK_ENTER, 'a');
			this.nbThreadField.getField().getKeyListeners()[0].keyPressed(nbThreadEvent);
			
			//partcle range
			KeyEvent averageRangeEvent = new KeyEvent(this.averageRangeField.getField(), 4, 20,0,
					KeyEvent.VK_ENTER, 'a');
			this.averageRangeField.getField().getKeyListeners()[0].keyPressed(averageRangeEvent);
			
			//cut range
			KeyEvent cutRangeEvent = new KeyEvent(this.cutRangeValueField.getField(), 5, 20, 0,
					KeyEvent.VK_ENTER, 'a');
			this.cutRangeValueField.getField().getKeyListeners()[0].keyPressed(cutRangeEvent);
			
			controler.validateNumberAndSize(this.nbVoxelField,
					this.sizeVoxelField);	
		}
		else if(e.getActionCommand().equals("Reload"))
		{
			controler.askForUpdate();
		}
		
	}

	@Override
	public void stateChanged(ChangeEvent e)
	{
		if(MatMode.isSelected())
		{
			gridPane.changeMode(RadMode.Mat);
		}else if(UMode.isSelected())
		{
			gridPane.changeMode(RadMode.U);
		}else if(KMode.isSelected())
		{
			gridPane.changeMode(RadMode.K);
		}else if(ThMode.isSelected())
		{
			gridPane.changeMode(RadMode.Th);
		}
		else
			gridPane.changeMode(RadMode.Ud);
	}

	@Override
	public void mouseDragged(MouseEvent arg0)
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseMoved(MouseEvent e)
	{
		final ZoomAndPanListener zoomManager = this.gridPane.getZoomManager();
		int x = 0;
		int y = 0;
		final double widthVoxelOnScreen = controler.getWidthScreen(zoomManager.getCoordTransform().getScaleX());
		final double heightVoxelOnScreen = controler.getHeightScreen(zoomManager.getCoordTransform().getScaleY());
		
		x = (int)((e.getX() - zoomManager.getCoordTransform().getTranslateX()) / widthVoxelOnScreen);
		y = (int)((e.getY() - zoomManager.getCoordTransform().getTranslateY()) / heightVoxelOnScreen);

		if (x >= controler.getNbVoxelX() || x < 0 || y >= controler.getNbVoxelY() || y < 0) {
			x = 0;
			y = 0;
		}
		
		this.VoxelInfo.setText("x:" + x + " y:" + y );
		this.VoxelInfo.repaint();
	}
}
