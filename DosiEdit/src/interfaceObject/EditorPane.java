package interfaceObject;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JColorChooser;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JSlider;
import javax.swing.JSplitPane;
import javax.swing.JToggleButton;
import javax.swing.JToolBar;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import mainPackage.Material;
import mainPackage.Scene;
import mainPackage.TopLevelInterface;
import mainPackage.VoxelObject;
import mainPackage.VoxelizedObject;

/**
 * One of the three panel the {@link OngletManagement} show in its tabs. Its contains all classes and functions to render the voxelized object. 
 * Its a JSplitPane and it has four parts: <ul>
 * <li>WEST-NORTH: the {@link Grid2D}, editable, to edit the voxelized world with a toolbar.</li>
 * <li>WEST-SOUTH: the {@link ViewGrid2D}, not editable, just to see the object and offers a different point of view.</li>
 * <li>EAST-NORTH: the 3D render, not editable, using JOGL to have the best view on the world.</li>
 * <li>EAST-SOUTH: the list of material you can select to fill the voxel in the Grid2D and some fields to change its radiations.</li>
 * </ul>
 * @author alan
 *
 */
public class EditorPane extends JSplitPane implements ListSelectionListener, FocusListener, 
						MouseListener, ActionListener, KeyListener, ChangeListener, ItemListener, MouseMotionListener
{
	private static final long serialVersionUID = -1090348722433171148L;

	private static final int DRAG_SPEED = 1;
	/**
	 * UI Object.
	 */
	private JRadioButton LayerNorthModeXY;
	private JRadioButton LayerNorthModeXZ;
	private JRadioButton LayerNorthModeYZ;
	private JRadioButton LayerSouthModeXY;
	private JRadioButton LayerSouthModeXZ;
	private JRadioButton LayerSouthModeYZ;
	private JRadioButton MatMode;
	private JRadioButton UMode;
	private JRadioButton ThMode;
	private JRadioButton KMode;
	private JLabel nameLabel;
	private TripleLabelizedPanel CurrentRadValues;
	private TripleFormattedPanel RadField;
	private TripleLabelizedPanel ReferenceRadValues;
	private TripleFormattedPanel ReferenceRadField;
	private JList<TextureCellRenderer.CheckListItem> JListMaterial;
	private DefaultListModel<TextureCellRenderer.CheckListItem> listModelMaterial;
	private JSlider activeLayerSlideNorth;
	private JSlider activeLayerSlideSouth;
	private JSplitPane EastPan;
	private JLabel VoxelInfo;
	private JLabel activeLayerLabel;
	private JLabel currentMatLabel;
	private JLabel stepDrawVoxel;
	private JComboBox<Integer> activeLayerNorthCombo;
	private JLabel activeModeLayer;
	private JComboBox<Integer> activeLayerSouthCombo;
	private JButton dragButton;
	private JCheckBox keepRadioCheck;

	/**
	 * Viewer 2D and 3D.
	 */
	private Grid2D grid;
	private ViewGrid2D vg;
	private Scene VoxelizedScene;

	/**
	 * Listener and other objects.
	 */
	private Material currentMaterial;
	private ManuallyChangeListener listener;
	private MaterialPopupMenu ColorTexturePopup;
	private TopLevelInterface father;

	/**
	 * Constructor. Call the super constructor of JSplitPane with an horizontal split, keep the param in fields and init.
	 * @param father TopLevelInterface which have all informations of the project.
	 * @param sceneObject The 3DScene to add.
	 */
	public EditorPane(final TopLevelInterface father, final Scene sceneObject) {
		super(1);
		this.listener = null;
		this.ColorTexturePopup = null;
		this.currentMaterial = null;
		this.VoxelInfo = null;
		this.activeLayerLabel = null;
		this.currentMatLabel = null;
		this.stepDrawVoxel = null;
		this.setOneTouchExpandable(true);
		this.father = father;
		this.VoxelizedScene = sceneObject;
		this.init();
	}

	/**
	 * Initialization of all components.
	 */
	private void init() {
		this.initWestSide();
		this.initEastSide();
		this.setDividerLocation(780);
		this.setResizeWeight(0.5);
		this.JListMaterial.setSelectedIndex(0);
		this.LayerNorthModeXZ.setSelected(true);
		this.LayerSouthModeXY.setSelected(true);
		this.MatMode.setSelected(true);
		this.grid.addObjectChangedListener(this.VoxelizedScene);
		this.validate();
	}

	/**
	 * Initialization of the west side of the window.
	 */
	private void initWestSide() {
		final JPanel WestNorthPan = new JPanel(new BorderLayout());
		(this.grid = new Grid2D(this.VoxelizedScene.getVoxelObject())).addMouseMotionListener(this);
		(this.activeLayerSlideNorth = new JSlider()).setMaximum(this.VoxelizedScene.getVoxelObject().getNbVoxelZ() - 1);
		this.activeLayerSlideNorth.setMinimum(0);
		this.activeLayerSlideNorth.setValue(0);
		this.activeLayerSlideNorth.setPaintLabels(true);
		this.activeLayerSlideNorth.setPaintTicks(true);
		this.activeLayerSlideNorth.setMinorTickSpacing(1);
		this.activeLayerSlideNorth.setMajorTickSpacing(10);
		this.activeLayerSlideNorth.addChangeListener(this.grid);
		final JPanel LayerRadioPanel = new JPanel();
		(this.LayerNorthModeXZ = new JRadioButton("XZ view")).addChangeListener(this);
		LayerRadioPanel.add(this.LayerNorthModeXZ);
		(this.LayerNorthModeYZ = new JRadioButton("YZ view")).addChangeListener(this);
		LayerRadioPanel.add(this.LayerNorthModeYZ);
		(this.LayerNorthModeXY = new JRadioButton("XY view")).addChangeListener(this);
		LayerRadioPanel.add(this.LayerNorthModeXY);
		final ButtonGroup bg = new ButtonGroup();
		bg.add(this.LayerNorthModeXY);
		bg.add(this.LayerNorthModeXZ);
		bg.add(this.LayerNorthModeYZ);
		final JToolBar toolbar = new JToolBar();
		final JToggleButton penButtonTool = new JToggleButton(TopLevelInterface.createImageIcon("/resources/pen.png"));
		penButtonTool.setActionCommand("pen mode selection");
		penButtonTool.setToolTipText("Modyfing voxel per voxel");
		penButtonTool.addActionListener(this.grid);
		final JToggleButton recButtonTool = new JToggleButton(TopLevelInterface.createImageIcon("/resources/rectangle.png"));
		recButtonTool.setActionCommand("rectangle mode selection");
		recButtonTool.setToolTipText("Modyfing voxels intersects with the selection rectangle");
		recButtonTool.addActionListener(this.grid);
		final JButton fillLayerButtonTool = new JButton(TopLevelInterface.createImageIcon("/resources/fillLayer.png"));
		fillLayerButtonTool.setActionCommand("fill the selected layer");
		fillLayerButtonTool.setToolTipText("Filling all voxels in the selected layer with the selected material");
		fillLayerButtonTool.addActionListener(this.grid);
		final JButton allLayerButtonTool = new JButton(TopLevelInterface.createImageIcon("/resources/fillObject.png"));
		allLayerButtonTool.setActionCommand("filling all");
		allLayerButtonTool.setToolTipText("filling all voxels in the object with the selected material");
		allLayerButtonTool.addActionListener(this.grid);
		final JButton fillEmptyLayerButtonTool = new JButton(TopLevelInterface.createImageIcon("/resources/fillEmptyLayer.png"));
		fillEmptyLayerButtonTool.setActionCommand("fill empty voxel in the selected layer");
		fillEmptyLayerButtonTool.setToolTipText("Filling all empty voxels in the selected layer with the selected material");
		fillEmptyLayerButtonTool.addActionListener(this.grid);
		final JButton fillEmptyVoxelButtonTool = new JButton(TopLevelInterface.createImageIcon("/resources/fillEmptyObject.png"));
		fillEmptyVoxelButtonTool.setActionCommand("fill empty voxel in the object");
		fillEmptyVoxelButtonTool.setToolTipText("Filling all empty voxels in the object with the selected material");
		fillEmptyVoxelButtonTool.addActionListener(this.grid);
		final JButton duplicateButton = new JButton("Duplicate");
		duplicateButton.setActionCommand("duplicate");
		duplicateButton.setToolTipText("Duplicate the current layer into others");
		duplicateButton.addActionListener(this.grid);
		final ButtonGroup bg2 = new ButtonGroup();
		bg2.add(penButtonTool);
		bg2.add(recButtonTool);
		this.activeLayerLabel = new JLabel("Active Layer : ");
		(this.activeLayerNorthCombo = new JComboBox<Integer>()).addItemListener(this);
		this.activeModeLayer = new JLabel();
		this.currentMatLabel = new JLabel();
		this.currentMatLabel.setPreferredSize(new Dimension(140,30));
		this.VoxelInfo = new JLabel();
		this.VoxelInfo.setPreferredSize(new Dimension(130,30));
		penButtonTool.setSelected(true);
		toolbar.add(penButtonTool);
		toolbar.add(recButtonTool);
		toolbar.addSeparator();
		toolbar.add(fillEmptyLayerButtonTool);
		toolbar.add(fillEmptyVoxelButtonTool);
		toolbar.add(fillLayerButtonTool);
		toolbar.add(allLayerButtonTool);
		toolbar.addSeparator();
		toolbar.add(duplicateButton);
		toolbar.addSeparator();
		toolbar.add(this.activeLayerLabel);
		toolbar.add(this.activeLayerNorthCombo);
		toolbar.add(this.activeModeLayer);
		toolbar.addSeparator();
		toolbar.add(this.currentMatLabel);
		toolbar.addSeparator();
		toolbar.add(this.VoxelInfo);
		WestNorthPan.add(toolbar, "North");
		WestNorthPan.add(this.grid, "Center");
		final JPanel SouthPanelLayerAndMode = new JPanel(new BorderLayout());
		SouthPanelLayerAndMode.add(this.activeLayerSlideNorth);
		SouthPanelLayerAndMode.add(LayerRadioPanel, "East");
		WestNorthPan.add(SouthPanelLayerAndMode, "South");
		final JSplitPane WestSplitPane = new JSplitPane(0);
		WestSplitPane.setDividerLocation(900);
		WestSplitPane.setOneTouchExpandable(true);
		WestSplitPane.setTopComponent(WestNorthPan);
		final JPanel WestSouthPane = new JPanel(new BorderLayout());
		this.vg = new ViewGrid2D(this.VoxelizedScene.getVoxelObject());
		(this.activeLayerSlideSouth = new JSlider()).setMaximum(this.VoxelizedScene.getVoxelObject().getNbVoxelZ() - 1);
		this.activeLayerSlideSouth.setMinimum(0);
		this.activeLayerSlideSouth.setValue(0);
		this.activeLayerSlideSouth.setPaintLabels(true);
		this.activeLayerSlideSouth.setPaintTicks(true);
		this.activeLayerSlideSouth.setMinorTickSpacing(1);
		this.activeLayerSlideSouth.setMajorTickSpacing(10);
		this.activeLayerSlideSouth.addChangeListener(this.vg);
		final JPanel LayerSouthRadioPanel = new JPanel();
		(this.activeLayerSouthCombo = new JComboBox<Integer>()).addItemListener(this);
		LayerSouthRadioPanel.add(this.activeLayerSouthCombo);
		(this.LayerSouthModeXY = new JRadioButton("<html>XY view<br>(top view)</html>")).setForeground(Color.red);
		this.LayerSouthModeXY.addChangeListener(this);
		LayerSouthRadioPanel.add(this.LayerSouthModeXY);
		(this.LayerSouthModeXZ = new JRadioButton("XZ view")).addChangeListener(this);
		LayerSouthRadioPanel.add(this.LayerSouthModeXZ);
		(this.LayerSouthModeYZ = new JRadioButton("YZ view")).addChangeListener(this);
		LayerSouthRadioPanel.add(this.LayerSouthModeYZ);
		final ButtonGroup bg3 = new ButtonGroup();
		bg3.add(this.LayerSouthModeXY);
		bg3.add(this.LayerSouthModeXZ);
		bg3.add(this.LayerSouthModeYZ);
		final JPanel SouthPanel = new JPanel(new BorderLayout());
		SouthPanel.add(this.activeLayerSlideSouth);
		SouthPanel.add(LayerSouthRadioPanel, "East");
		WestSouthPane.add(this.vg, "Center");
		WestSouthPane.add(SouthPanel, "North");
		this.grid.addGridListener(this.vg);
		WestSplitPane.setBottomComponent(WestSouthPane);
		this.setLeftComponent(WestSplitPane);
	}

	/**
	 * Initialization of the east side of the window.
	 */
	private void initEastSide() {
		(this.EastPan = new JSplitPane(0)).setOneTouchExpandable(true);
		
		final JPanel pan3D = new JPanel(new BorderLayout());
		pan3D.setSize(new Dimension(200, 200));
		pan3D.add((Component)this.VoxelizedScene);
		final JPanel legendPan = new JPanel();
		final JButton colorProbeButton = new JButton("P");
		colorProbeButton.setActionCommand("color probe");
		colorProbeButton.addActionListener(this);
		colorProbeButton.setForeground(Color.white);
		colorProbeButton.setMargin(new Insets(0, 0, 0, 0));
		colorProbeButton.setBackground(Color.black);
		final JButton modeButton = new JButton("mode");
		modeButton.setActionCommand("change view mode");
		modeButton.addActionListener(this);
		modeButton.setPreferredSize(new Dimension(70, 20));
		modeButton.setForeground(Color.white);
		modeButton.setMargin(new Insets(0, 0, 0, 0));
		modeButton.setBackground(Color.black);
		legendPan.setBackground(Color.black);
		final JLabel labX = new JLabel("X-axis");
		labX.setForeground(Color.green);
		final JLabel labY = new JLabel("Y-axis");
		labY.setForeground(Color.blue);
		final JLabel labZ = new JLabel("Z-axis");
		labZ.setForeground(Color.red);
		(this.stepDrawVoxel = new JLabel("1/1 Voxel drawn")).setForeground(Color.white);
		final JButton resetButton = new JButton("reset");
		resetButton.setActionCommand("reset camera");
		resetButton.addActionListener(this);
		resetButton.setPreferredSize(new Dimension(70, 20));
		resetButton.setForeground(Color.white);
		resetButton.setMargin(new Insets(0, 0, 0, 0));
		resetButton.setBackground(Color.black);
		
		dragButton = new JButton(TopLevelInterface.createImageIcon("/resources/drag.png"));
		dragButton.setPreferredSize(new Dimension(30,30));
		dragButton.setBackground(null);
		dragButton.setBorder(null);
		dragButton.addMouseMotionListener(this);
		dragButton.addMouseListener(this);
		dragButton.setMargin(new Insets(0, 0, 0, 0));
		
		keepRadioCheck = new JCheckBox("Ratio",true);
		keepRadioCheck.setForeground(Color.WHITE);
		keepRadioCheck.addActionListener(this);
		keepRadioCheck.setActionCommand("keep ratio");
		legendPan.add(keepRadioCheck);
		legendPan.add(colorProbeButton);
		legendPan.add(modeButton);
		legendPan.add(resetButton);
		legendPan.add(labX);
		legendPan.add(labY);
		legendPan.add(labZ);
		legendPan.add(this.stepDrawVoxel);  
		
		JPanel legendWithDrag = new JPanel(new BorderLayout());
		
		JPanel buttonPan = new JPanel();
		buttonPan.setBackground(Color.BLACK);
		buttonPan.add(dragButton);
		legendWithDrag.add(buttonPan,BorderLayout.WEST);
		legendWithDrag.add(legendPan);
		pan3D.add(legendWithDrag, "South");
		this.EastPan.setTopComponent(pan3D);
		final JPanel GlobalPane = new JPanel();
		GlobalPane.setLayout(new BoxLayout(GlobalPane, BoxLayout.PAGE_AXIS));
		final JPanel NorthPanel = new JPanel(new BorderLayout());
		final JPanel showRadioPanel = new JPanel();
		(this.MatMode = new JRadioButton("Material")).addChangeListener(this);
		showRadioPanel.add(this.MatMode);
		(this.UMode = new JRadioButton("Uranium")).addChangeListener(this);
		showRadioPanel.add(this.UMode);
		(this.ThMode = new JRadioButton("Thorium")).addChangeListener(this);
		showRadioPanel.add(this.ThMode);
		(this.KMode = new JRadioButton("Potassium")).addChangeListener(this);
		showRadioPanel.add(this.KMode);
		NorthPanel.add(showRadioPanel, "South");
		final ButtonGroup bg2 = new ButtonGroup();
		bg2.add(this.MatMode);
		bg2.add(this.UMode);
		bg2.add(this.ThMode);
		bg2.add(this.KMode);
		GlobalPane.add(NorthPanel, "North");
		final JPanel InfoPan = new JPanel(new BorderLayout());
		InfoPan.setBorder(BorderFactory.createTitledBorder("Material and radioactivity"));
		final JPanel namePan = new JPanel(new BorderLayout());
		(this.nameLabel = new JLabel("Name:    ")).setAlignmentX(0.5f);
		this.CurrentRadValues = new TripleLabelizedPanel(new JLabel("Radiation: "), "U", "Th", "K", null);
		namePan.add(this.nameLabel, "North");
		namePan.add(this.CurrentRadValues, "East");
		final JPanel RadPan = new JPanel(new BorderLayout());
		(this.RadField = new TripleFormattedPanel(null, new JLabel("<html>Changing values<br> above:</html>"), "U", "Th", "K", false)).addKeyListener(
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
						if (e.getKeyCode() == 10 && EditorPane.this.currentMaterial != null) {
							float x=0,y=0,z=0;
							try {
								EditorPane.this.currentMaterial.setCurrentUraniumValue(
										x=EditorPane.this.RadField.getValue(TripleFormattedPanel.XYZ.X));
							}
							catch (Exception ex) {}
							try {
								EditorPane.this.currentMaterial.setCurrentThoriumValue(
										y=EditorPane.this.RadField.getValue(TripleFormattedPanel.XYZ.Y));
							}
							catch (Exception ex2) {}
							try {
								EditorPane.this.currentMaterial.setCurrentPotassiumValue(
										z=EditorPane.this.RadField.getValue(TripleFormattedPanel.XYZ.Z));
							}
							catch (Exception ex3) {}
							EditorPane.this.RadField.updateFrameComponent(x, y, z);
							EditorPane.this.updateFrameComponent();
						}
					}
				});
		RadPan.add(this.RadField, "East");
		RadPan.add(this.createVerticalHorizontal(), "South");
		final JPanel RefPan = new JPanel(new BorderLayout());
		final JButton applyRefValue = new JButton("\u21ba Reload the reference values \u21bb");
		applyRefValue.setActionCommand("apply ref value");
		applyRefValue.addActionListener(this);
		this.ReferenceRadValues = new TripleLabelizedPanel(new JLabel("Reference Values : "), "U", "Th", "K", null);
		RefPan.add(applyRefValue, "North");
		RefPan.add(this.ReferenceRadValues, "Center");

		(this.ReferenceRadField = new TripleFormattedPanel(null, new JLabel("Change reference values"),"U","Th","K", false)).addKeyListener(
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
						if (e.getKeyCode() == 10 && EditorPane.this.currentMaterial != null) {
							float x=0,y=0,z=0;
							try {
								EditorPane.this.currentMaterial.setRefUraniumValue(
										x=EditorPane.this.ReferenceRadField.getValue(TripleFormattedPanel.XYZ.X));
							}
							catch (Exception ex) {}
							try {
								EditorPane.this.currentMaterial.setRefThoriumValue(
										y=EditorPane.this.ReferenceRadField.getValue(TripleFormattedPanel.XYZ.Y));
							}
							catch (Exception ex2) {}
							try {
								EditorPane.this.currentMaterial.setRefPotassiumValue(
										z=EditorPane.this.ReferenceRadField.getValue(TripleFormattedPanel.XYZ.Z));
							}
							catch (Exception ex3) {}
							EditorPane.this.ReferenceRadField.updateFrameComponent(x, y, z);
							EditorPane.this.updateFrameComponent();
						}

					}
				});
		RefPan.add(this.ReferenceRadField, BorderLayout.SOUTH);

		InfoPan.add(namePan, "North");
		InfoPan.add(RadPan, "Center");
		InfoPan.add(RefPan, "South");
		final JPanel bottomPane = new JPanel(new BorderLayout());
		this.listModelMaterial = new DefaultListModel<TextureCellRenderer.CheckListItem>();
		(this.JListMaterial = new JList<TextureCellRenderer.CheckListItem>(this.listModelMaterial)).setCellRenderer(new TextureCellRenderer<Object>());
		this.JListMaterial.addListSelectionListener(this);
		final JScrollPane MaterialScroll = new JScrollPane(this.JListMaterial);
		this.JListMaterial.addMouseListener(this);
		final JLabel matLabel = new JLabel("List of available materials");
		matLabel.setPreferredSize(new Dimension(150, 30));
		bottomPane.add(matLabel, "North");
		bottomPane.add(MaterialScroll, "Center");
		GlobalPane.add(InfoPan, "Center");
		GlobalPane.add(bottomPane, "South");
		this.EastPan.setBottomComponent(new JScrollPane(GlobalPane));
		this.setRightComponent(this.EastPan);
	}

	/**
	 * Update the UI concerning the step. More informations in {@link Scene}.
	 * @param stepDrawVoxel the stepDrawVoxel to set.
	 */
	public void setStepDrawVoxel(final int stepDrawVoxel) {
		this.stepDrawVoxel.setText("1/" + stepDrawVoxel + " Voxel drawn");
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
	 * Make the window to update its components.
	 */
	public void updateYourself() {
		switch (this.grid.getLayerViewMode()) {
			case XY: {
				this.activeLayerSlideNorth.setMaximum(this.VoxelizedScene.getVoxelObject().getNbVoxelZ() - 1);
				break;
			}
			case XZ: {
				this.activeLayerSlideNorth.setMaximum(this.VoxelizedScene.getVoxelObject().getNbVoxelY() - 1);
				break;
			}
			case YZ: {
				this.activeLayerSlideNorth.setMaximum(this.VoxelizedScene.getVoxelObject().getNbVoxelX() - 1);
				break;
			}
		}
		switch (this.vg.getLayerViewMode()) {
			case XY: {
				this.activeLayerSlideSouth.setMaximum(this.VoxelizedScene.getVoxelObject().getNbVoxelZ() - 1);
				break;
			}
			case XZ: {
				this.activeLayerSlideSouth.setMaximum(this.VoxelizedScene.getVoxelObject().getNbVoxelY() - 1);
				break;
			}
			case YZ: {
				this.activeLayerSlideSouth.setMaximum(this.VoxelizedScene.getVoxelObject().getNbVoxelX() - 1);
				break;
			}
		}
		this.VoxelizedScene.getVoxelObject().checkExistenceMaterial(this.father.getListOfMaterial());
		final List<Material> listM = this.father.getListOfMaterial();
		int i = 0;
		for (final Material m : listM) {
			if (this.listModelMaterial.size() <= i) {
				this.listModelMaterial.insertElementAt(new TextureCellRenderer.CheckListItem(m), i);
			}
			else if (!m.toString().equals(this.listModelMaterial.get(i))) {
				this.listModelMaterial.get(i).setLabel(m.getName());
			}
			++i;
		}
		if (this.currentMaterial != null) {
			this.JListMaterial.setSelectedValue(this.currentMaterial, true);
		}
		if (this.activeLayerNorthCombo.getItemCount() <= 0 || this.activeLayerNorthCombo.getItemCount() != this.activeLayerSlideNorth.getMaximum() + 1) {
			if (this.activeLayerNorthCombo.getItemCount() != 0) {
				this.activeLayerNorthCombo.removeAllItems();
			}
			for (int j = 0; j <= this.activeLayerSlideNorth.getMaximum(); ++j) {
				this.activeLayerNorthCombo.addItem(j);
			}
		}
		this.activeLayerNorthCombo.setSelectedItem(this.activeLayerSlideNorth.getValue());
		this.activeModeLayer.setText(" " + this.grid.getLayerViewMode() + " ");
		if (this.activeLayerSouthCombo.getItemCount() <= 0 || this.activeLayerNorthCombo.getItemCount() != this.activeLayerSlideNorth.getMaximum() + 1) {
			if (this.activeLayerSouthCombo.getItemCount() != 0) {
				this.activeLayerSouthCombo.removeAllItems();
			}
			for (int j = 0; j <= this.activeLayerSlideSouth.getMaximum(); ++j) {
				this.activeLayerSouthCombo.addItem(j);
			}
		}
		this.activeLayerSouthCombo.setSelectedItem(this.activeLayerSlideSouth.getValue());
	}

	/**
	 * Update the Swing components.
	 */
	public void updateFrameComponent() {
		if (this.currentMaterial != null) {
			this.nameLabel.setText("<html><b>Name :   " + this.currentMaterial.getName() + "</b></html>");
			this.CurrentRadValues.setXLabel(new StringBuilder(String.valueOf(this.currentMaterial.getCurrentUraniumValue())).toString());
			this.CurrentRadValues.setYLabel(new StringBuilder(String.valueOf(this.currentMaterial.getCurrentThoriumValue())).toString());
			this.CurrentRadValues.setZLabel(new StringBuilder(String.valueOf(this.currentMaterial.getCurrentPotassiumValue())).toString());
			try {
				this.RadField.updateFrameComponent(this.RadField.getValue(TripleFormattedPanel.XYZ.X), this.RadField.getValue(TripleFormattedPanel.XYZ.Y), this.RadField.getValue(TripleFormattedPanel.XYZ.Z));
			}
			catch (Exception ex) {}
			this.ReferenceRadValues.setXLabel(new StringBuilder(String.valueOf(this.currentMaterial.getRefUraniumValue())).toString());
			this.ReferenceRadValues.setYLabel(new StringBuilder(String.valueOf(this.currentMaterial.getRefThoriumValue())).toString());
			this.ReferenceRadValues.setZLabel(new StringBuilder(String.valueOf(this.currentMaterial.getRefPotassiumValue())).toString());
		}
	}

	/**
	 * Triggered when a value in the material list is changed.
	 */
	@Override
	public void valueChanged(final ListSelectionEvent e) {
		if (!this.JListMaterial.isSelectionEmpty()) {
			this.grid.setCurrentMaterial(this.JListMaterial.getSelectedValue().getM());
			this.currentMaterial = this.JListMaterial.getSelectedValue().getM();
			this.currentMatLabel.setText("<html>Current material : <b>" + this.currentMaterial.getName() + "<b></html>");
		}
		this.updateFrameComponent();
	}

	/**
	 * Triggered when the object gain the focus.
	 */
	@Override
	public void focusGained(final FocusEvent arg0) {
		this.updateYourself();
		this.updateFrameComponent();
	}

	/**
	 * Triggered when the object lost the focus.
	 */
	@Override
	public void focusLost(final FocusEvent arg0) {
	}

	/**
	 * Triggered when the user click on the object.
	 */
	@Override
	public void mouseClicked(final MouseEvent event) {
		if (!event.getSource().getClass().equals(JList.class)) {
			return;
		}
		final JList<?> list = (JList<?>)event.getSource();
		final int index = list.locationToIndex(event.getPoint());
		if (index == -1) {
			return;
		}
		final TextureCellRenderer.CheckListItem item = (TextureCellRenderer.CheckListItem)list.getModel().getElementAt(index);
		final Point ptI = MouseInfo.getPointerInfo().getLocation();
		SwingUtilities.convertPointFromScreen(ptI, list);
		if (ptI.x < ((TextureCellRenderer<?>) list.getCellRenderer()).getCheckBox().getWidth()) {
			item.setSelected(!item.isSelected());
			this.grid.repaint();
			this.vg.repaint();
			this.VoxelizedScene.setToUpdate();
		}
		item.getM().setActive(item.isSelected());
		list.repaint(list.getCellBounds(index, index));
		this.repaint();
	}

	/**
	 * Triggered the user moves the mouse to enter in the object space.
	 */
	@Override
	public void mouseEntered(final MouseEvent e) {
	}

	/**
	 * Triggered the user moves the mouse to get out of the object space.
	 */
	@Override
	public void mouseExited(final MouseEvent arg0) {
	}

	/**
	 * Triggered when the user pressed a button mouse within the object space.
	 */
	@Override
	public void mousePressed(final MouseEvent e) {
		if (e.getSource() == this.JListMaterial && e.isPopupTrigger()) {
			this.mouseMultiOsPopupTrigger(e);
		}
	}

	/**
	 * Triggered when the mouse button is released within the object space.
	 */
	@Override
	public void mouseReleased(final MouseEvent e) {
		if (e.getSource() == this.JListMaterial && e.isPopupTrigger()) {
			this.mouseMultiOsPopupTrigger(e);
		}
		
		if(e.getSource() == dragButton)
		{
			lastX = 0; lastY =0;
		}
		
	}

	/**
	 * That function is set for contextual menu, opened with right-click on Windows or Linux,
	 * but MacOS has a special mouse when right-click is nonexistent. 
	 * @param e the Event handling info.
	 */
	private void mouseMultiOsPopupTrigger(final MouseEvent e) {
		this.JListMaterial.setSelectedIndex(this.JListMaterial.locationToIndex(e.getPoint()));
		this.currentMaterial = this.JListMaterial.getSelectedValue().getM();
		try {
			this.ColorTexturePopup = new MaterialPopupMenu(this.listener, this.grid, this.vg, this.VoxelizedScene, this.JListMaterial.getSelectedValue().getM());
		}
		catch (NullPointerException exception) {
			System.err.println("Material Popup menu get a null pointer exception.");
		}
		this.ColorTexturePopup.show(this, this.getMousePosition().x, this.getMousePosition().y);
	}

	/**
	 * Triggered when an action is performed, like clicking on a button.
	 */
	@Override
	public void actionPerformed(final ActionEvent e) {
		if (e.getActionCommand().equals("apply ref value")) {
			if (this.JListMaterial.isSelectionEmpty()) {
				return;
			}
			int retour = JOptionPane.showConfirmDialog(this, "You are about to change all radiation values" +
					"in every voxels.", "WARNING", JOptionPane.YES_NO_CANCEL_OPTION,JOptionPane.WARNING_MESSAGE);
			if(retour == JOptionPane.OK_OPTION)
			{
				final Material m = this.JListMaterial.getSelectedValue().getM();
				m.setCurrentUraniumValue(m.getRefUraniumValue());
				m.setCurrentThoriumValue(m.getRefThoriumValue());
				m.setCurrentPotassiumValue(m.getRefPotassiumValue());
				this.updateFrameComponent();
				this.father.getVoxSample().reloadReferenceRadiationValue(m);
				this.grid.repaint();
				this.vg.updateMaterial();
			}

		}
		if (e.getActionCommand().equals("change view mode")) {
			this.VoxelizedScene.changeLineOrFill();
		}
		if (e.getActionCommand().equals("reset camera")) {
			this.VoxelizedScene.getCamera().reset();
		}
		if (e.getActionCommand().equals("color probe")) {
			final JColorChooser ColorChooser = new JColorChooser();
			final ActionListener cancelAction = new ActionListener() {
				@Override
				public void actionPerformed(final ActionEvent arg0) {
				}
			};
			final ActionListener okAction = new ActionListener() {
				@Override
				public void actionPerformed(final ActionEvent arg0) {
					if (ColorChooser.getColor() != null) {
						EditorPane.this.grid.setProbeColor(ColorChooser.getColor());
						EditorPane.this.vg.setProbeColor(ColorChooser.getColor());
						EditorPane.this.VoxelizedScene.setProbeColor(ColorChooser.getColor());
					}
					else {
						System.err.println("Error with the color chooser: null pointer received.");
					}
				}
			};
			JColorChooser.createDialog(this, "Probe color", true, ColorChooser, okAction, cancelAction).setVisible(true);
		}
		if(e.getActionCommand().equals("keep ratio"))
		{
			this.VoxelizedScene.setKeepRatio(keepRadioCheck.isSelected());
			
			//Dummy algorithm to trigger internal call of reshape
			int trigger = (keepRadioCheck.isSelected() ? 1 : -1);
			this.setDividerLocation(this.getDividerLocation()+trigger);
		}
	}

	/**
	 * Triggered when a key on the keyboard is pressed.
	 */
	@Override
	public void keyPressed(final KeyEvent e) {
		
	}

	/**
	 * Triggered when a key on the keyboard is released.
	 */
	@Override
	public void keyReleased(final KeyEvent arg0) {
	}

	@Override
	public void keyTyped(final KeyEvent arg0) {
	}

	/**
	 * Triggered when the state of the object is changed.
	 */
	@Override
	public void stateChanged(final ChangeEvent e) {
		if (e.getSource() == this.LayerNorthModeXY || e.getSource() == this.LayerNorthModeXZ || e.getSource() == this.LayerNorthModeYZ) {
			if (this.LayerNorthModeXY.isSelected()) {
				this.grid.setLayerViewMode(Grid2D.Layer.XY);
				this.activeLayerSlideNorth.setValue(this.grid.getLastXY());
			}
			else if (this.LayerNorthModeXZ.isSelected()) {
				this.grid.setLayerViewMode(Grid2D.Layer.XZ);
				this.activeLayerSlideNorth.setValue(this.grid.getLastXZ());
			}
			else {
				this.grid.setLayerViewMode(Grid2D.Layer.YZ);
				this.activeLayerSlideNorth.setValue(this.grid.getLastYZ());
			}
		}
		if (e.getSource() == this.LayerSouthModeXY || e.getSource() == this.LayerSouthModeXZ || e.getSource() == this.LayerSouthModeYZ) {
			if (this.LayerSouthModeXY.isSelected()) {
				this.vg.updateMode(Grid2D.Layer.XY, this.vg.getLastXY());
			}
			else if (this.LayerSouthModeXZ.isSelected()) {
				this.vg.updateMode(Grid2D.Layer.XZ, this.vg.getLastXZ());
			}
			else {
				this.vg.updateMode(Grid2D.Layer.YZ, this.vg.getLastYZ());
			}
			this.activeLayerSlideSouth.setValue(this.vg.getActiveLayer());
		}
		if (e.getSource() == this.MatMode || e.getSource() == this.UMode || e.getSource() == this.ThMode || e.getSource() == this.KMode) {
			if (this.MatMode.isSelected()) {
				this.grid.setShowMode(Grid2D.RadMode.Mat);
				this.vg.setShowMode(Grid2D.RadMode.Mat);
			}
			else if (this.UMode.isSelected()) {
				this.grid.setShowMode(Grid2D.RadMode.U);
				this.vg.setShowMode(Grid2D.RadMode.U);
			}
			else if (this.ThMode.isSelected()) {
				this.grid.setShowMode(Grid2D.RadMode.Th);
				this.vg.setShowMode(Grid2D.RadMode.Th);
			}
			else {
				this.grid.setShowMode(Grid2D.RadMode.K);
				this.vg.setShowMode(Grid2D.RadMode.K);
			}
		}
		this.updateYourself();
	}

	/**
	 * Add the {@link ManuallyChangeListener} to go to the forge with pop-up menu.
	 * @param l the listener.
	 */
	public void addManuallyChangeListener(final ManuallyChangeListener l) {
		this.listener = l;
	}

	/**
	 * Triggered when an item of the a list is changed.
	 */
	@Override
	public void itemStateChanged(final ItemEvent e) {
		if (e.getSource() == this.activeLayerNorthCombo && this.activeLayerNorthCombo.getSelectedItem() != null) {
			final int index = (Integer)this.activeLayerNorthCombo.getSelectedItem();
			if (index != -1) {
				this.activeLayerSlideNorth.setValue(index);
			}
		}
		if (e.getSource() == this.activeLayerSouthCombo && this.activeLayerSouthCombo.getSelectedItem() != null) {
			final int index = (Integer)this.activeLayerSouthCombo.getSelectedItem();
			if (index != -1) {
				this.activeLayerSlideSouth.setValue(index);
			}
		}
	}

	private int lastX=0;
	
	private int lastY=0;
	
	@Override
	public void mouseDragged(final MouseEvent e) {
		
		if(e.getSource() == this.dragButton)
		{
			if(lastX == 0 && lastY == 0)
			{
				lastX = e.getXOnScreen(); 
				lastY = e.getYOnScreen();
			}
			else
			{
				int diffX = lastX - e.getXOnScreen();
				int diffY = lastY - e.getYOnScreen();

				this.setDividerLocation( this.getDividerLocation() - (diffX*DRAG_SPEED) );
				this.EastPan.setDividerLocation( this.EastPan.getDividerLocation() - (diffY*DRAG_SPEED));

				lastX = e.getXOnScreen(); 
				lastY = e.getYOnScreen();
			}
			
		}
	}

	/**
	 * Triggered when the mouse is moved into the object space.
	 */
	@Override
	public void mouseMoved(final MouseEvent e) {
		final VoxelizedObject Voxy = this.VoxelizedScene.getVoxelObject();
		final ZoomAndPanListener zoomManager = this.grid.getZoomManager();
		int x = 0;
		int y = 0;
		int z = 0;
		double width,height,depth;
		
		switch (this.grid.getLayerViewMode()) {
			case XY: {
				width = zoomManager.getCoordTransform().getScaleX() * Voxy.getVoxelDimensionX() * VoxelObject.FACTOR_DISPLAY_GRID;
				height = zoomManager.getCoordTransform().getScaleY() * Voxy.getVoxelDimensionY() * VoxelObject.FACTOR_DISPLAY_GRID;
				x = (int)((e.getX() - zoomManager.getCoordTransform().getTranslateX()) / width);
				y = (int)((e.getY() - zoomManager.getCoordTransform().getTranslateY()) / height);
				z = this.grid.getActiveLayer();
				if (x >= Voxy.getNbVoxelX() || x < 0 || y >= Voxy.getNbVoxelY() || y < 0) {
					x = 0;
					y = 0;
					break;
				}
				break;
			}
			case XZ: {
				width = zoomManager.getCoordTransform().getScaleX() * Voxy.getVoxelDimensionX() * VoxelObject.FACTOR_DISPLAY_GRID;
				depth =  zoomManager.getCoordTransform().getScaleY() * Voxy.getVoxelDimensionY() * VoxelObject.FACTOR_DISPLAY_GRID;
				x = (int)((e.getX() - zoomManager.getCoordTransform().getTranslateX()) / width);
				z = (int)((e.getY() - zoomManager.getCoordTransform().getTranslateY()) / depth);
				y = this.grid.getActiveLayer();
				if (x >= Voxy.getNbVoxelX() || x < 0 || z >= Voxy.getNbVoxelZ() || z < 0) {
					x = 0;
					z = 0;
					break;
				}
				break;
			}
			case YZ: {
				height = zoomManager.getCoordTransform().getScaleX() * Voxy.getVoxelDimensionX() * VoxelObject.FACTOR_DISPLAY_GRID ;
				depth = zoomManager.getCoordTransform().getScaleY() * Voxy.getVoxelDimensionY() * VoxelObject.FACTOR_DISPLAY_GRID;
				y = (int)((e.getX() - zoomManager.getCoordTransform().getTranslateX()) / height);
				z = (int)((e.getY() - zoomManager.getCoordTransform().getTranslateY()) / depth);
				x = this.grid.getActiveLayer();
				if (y >= Voxy.getNbVoxelY() || y < 0 || z >= Voxy.getNbVoxelZ() || z < 0) {
					z = 0;
					y = 0;
					break;
				}
				break;
			}
		}
		this.VoxelInfo.setText("x:" + x + " y:" + y + " z:" + z);
	}
}
