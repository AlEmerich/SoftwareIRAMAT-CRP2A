 package interfaceObject;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JRadioButton;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import mainPackage.ExternalVoxelizedWorld;
import mainPackage.Loader3DScan;
import mainPackage.Material;
import mainPackage.PrimaryParticles;
import mainPackage.ScanException;
import mainPackage.TopLevelInterface;
import mainPackage.VoxelizedObject;

/**
 * One of the three panel the {link OngletManagement} show in its tabs. 
 * It contains all the Swing components providing the easiest way to change the general properties of the project. 
 * Basically, informations changeable in that panel changes not very often, that is why there are not in the {link EditorPane}.
 * @author alan
 * @see KeyListener
 * @see ItemListener
 * @see ChangeListener
 * @see MaterialAddedListener
 * @see ActionListener
 */
public class MainInfoVoxel extends JPanel implements KeyListener, ItemListener, ChangeListener, MaterialAddedListener, ActionListener
{
    private static final long serialVersionUID = 2928752523581480724L;
    
    /**
     * Default cutting value.
     */
    private static float DEFAULT_CUTTING_VALUE_ALPHA;
    private static float DEFAULT_CUTTING_VALUE_BETA;
    private static float DEFAULT_CUTTING_VALUE_GAMMA;
    
    /**
     * UI fields.
     */
    private JRadioButton[] PrimaryParticleRadio;
    private FormattedLabelField nbPriMantissaTextField;
    private FormattedLabelField nbPriSignificandTextField;
    private TripleFormattedPanel ExternalDimensionPanel;
    private JComboBox<String> ExternalunitCombo;
    private TripleFormattedPanel ExternalNbVoxelPanel;
    private JPanel GlobalSubVoxelInfo;
    private JCheckBox ScanOrManualCheck;
    private JPanel VoxelItselInternalfInfo;
    private TripleFormattedPanel InternalDimensionPanel;
    private JLabel InternalunitCombo;
    private TripleFormattedPanel InternalNbVoxelPanel;
    private FormattedLabelField CutRangeValueField;
    private JComboBox<String> CutRangeUnitCombo;
    private JCheckBox CheckDefault;
    private TripleFormattedPanel DirectionPanel;
    private FormattedLabelField clockField;
    private JRadioButton none;
    private JRadioButton SubVoxelizedVoxel;
    private JRadioButton SimpleGrainPacking;
    private JRadioButton SuccessiveGrainPacking;
    private LockablePanel VerticalProbePanel;
    private TripleFormattedPanel locationPanel;
    private JFormattedTextField XoffsetField;
    private JFormattedTextField YoffsetField;
    private FormattedLabelField nbVertCellPan;
    private FormattedLabelField diameterPan;
    private LockablePanel GrainPackingPanel;
    private TripleFormattedPanel sizeBoxPan;
    private TripleFormattedPanel offsetPan;
    private JRadioButton fromGrainRadio;
    private JRadioButton fromMatrixRadio;
    private LockablePanel subVoxelPanel;
    private JComboBox<Material> MaterialForMapping;
    private JButton createInternalButton;
    
    /**
     * Border sets to the out-dated fields.
     */
    public static LineBorder redBorder;
    
    /**
     * Border sets to the up-dated fields.
     */
    public static LineBorder greenBorder;
    
    /**
     * Brute data.
     */ 
    private TopLevelInterface father;
    private Loader3DScan loader;
    private OngletManagement fatherInterface;
    
    private GridBagConstraints gbc = new GridBagConstraints();
    static {
        MainInfoVoxel.DEFAULT_CUTTING_VALUE_ALPHA = 0.01f;
        MainInfoVoxel.DEFAULT_CUTTING_VALUE_BETA = 0.001f;
        MainInfoVoxel.DEFAULT_CUTTING_VALUE_GAMMA = 1000.0f;
        MainInfoVoxel.redBorder = (LineBorder)BorderFactory.createLineBorder(Color.red, 2);
        MainInfoVoxel.greenBorder = (LineBorder)BorderFactory.createLineBorder(Color.green, 2);
    }
    
    /**
     * Constructor passing the father as a parameter, to be able to get the voxelized object.
     * @param father the father of the object.
     */
    public MainInfoVoxel(final TopLevelInterface father) {
        this.father = father;
        
        this.setLayout(new GridBagLayout());
        this.initNorth();
        this.initSouth();
        this.updateFrameComponent();
        int i = 1;
        if ((i = father.getVoxSample().getIndexPrimaryParticle()) == -1) {
            i = 1;
        }
        this.PrimaryParticleRadio[i].setSelected(true);
        this.PrimaryParticleRadio[0].addItemListener(this);
        this.PrimaryParticleRadio[1].addItemListener(this);
        this.PrimaryParticleRadio[2].addItemListener(this);
    }
    
    /**
     * Initialization of north components.
     */
    private void initNorth() {

        final JPanel NorthPanel = new JPanel();
        NorthPanel.setLayout(new BoxLayout(NorthPanel, BoxLayout.LINE_AXIS));
        final JPanel priParPanel = new JPanel(new GridLayout(3, 1));
        priParPanel.setBorder(BorderFactory.createTitledBorder("PRIMARY PARTICLES TYPE"));
        
        final ButtonGroup bg = new ButtonGroup();
        this.PrimaryParticleRadio = new JRadioButton[3];
        bg.add(this.PrimaryParticleRadio[0] = new JRadioButton("Alpha"));
        this.PrimaryParticleRadio[0].setToolTipText("<html>index defining the particle type (alpha- (1), b\u00eata- (2) or gamma-particles (3) <br>can be simulated using DosiVox, but only one particle type in a simulation run).</html>");
        bg.add(this.PrimaryParticleRadio[1] = new JRadioButton("B\u00eata"));
        this.PrimaryParticleRadio[1].setToolTipText("<html>index defining the particle type (alpha- (1), b\u00eata- (2) or gamma-particles (3) <br>can be simulated using DosiVox, but only one particle type in a simulation run).</html>");
        bg.add(this.PrimaryParticleRadio[2] = new JRadioButton("Gamma"));
        this.PrimaryParticleRadio[2].setToolTipText("<html>index defining the particle type (alpha- (1), b\u00eata- (2) or gamma-particles (3) <br>can be simulated using DosiVox, but only one particle type in a simulation run).</html>");
        final JPanel radioPan = new JPanel();
        radioPan.setLayout(new BoxLayout(radioPan, 2));
        radioPan.setAlignmentX(0.5f);
        radioPan.add(this.PrimaryParticleRadio[0]);
        radioPan.add(this.PrimaryParticleRadio[1]);
        radioPan.add(this.PrimaryParticleRadio[2]);
        priParPanel.add(radioPan);


        final JLabel NbPriLabel = new JLabel("Number of particles emitted :");
        NbPriLabel.setAlignmentX(0.5f);
        NbPriLabel.setToolTipText("Number of primary particles created during the simulation");
        this.nbPriMantissaTextField = new FormattedLabelField(this);
        this.nbPriSignificandTextField = new FormattedLabelField(this, new JLabel("* 10^ "));
        final JPanel ScientificPan = new JPanel();
        ScientificPan.setLayout(new BoxLayout(ScientificPan, 2));
        ScientificPan.setAlignmentX(0.5f);
        priParPanel.add(NbPriLabel);
        
        ScientificPan.add(this.nbPriMantissaTextField);
        ScientificPan.add(this.nbPriSignificandTextField);
        priParPanel.add(ScientificPan);
        //NorthPanel.add(priParPanel);
        
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridheight = 1;
        gbc.gridwidth = 1;
        gbc.fill = GridBagConstraints.VERTICAL;
        this.add(priParPanel,gbc);
        
        final JPanel CuttingPan = new JPanel(new BorderLayout());
        CuttingPan.setBorder(BorderFactory.createTitledBorder("CUT-IN-RANGE VALUE"));
        this.CutRangeValueField = new FormattedLabelField(this);
        (this.CutRangeUnitCombo = new JComboBox<String>()).addItem("cm");
        this.CutRangeUnitCombo.addItem("mm");
        this.CutRangeUnitCombo.addItem("\u03bcm");
        this.CutRangeUnitCombo.addItem("nm");
        this.CutRangeUnitCombo.setSelectedIndex(2);
        this.CutRangeUnitCombo.addItemListener(this);
        this.CutRangeUnitCombo.setMaximumSize(new Dimension(50, 20));
        final JPanel CutFirstLine = new JPanel();
        CutFirstLine.setLayout(new BoxLayout(CutFirstLine, 2));
        CutFirstLine.add(this.CutRangeValueField);
        CutFirstLine.add(this.CutRangeUnitCombo);
        CutFirstLine.setAlignmentX(0.5f);
        (this.CheckDefault = new JCheckBox("Use default value (1 \u03bcm)")).addChangeListener(this);
        final JPanel pan = new JPanel();
        pan.add(CutFirstLine);
        pan.add(this.CheckDefault);
        
        final JLabel lab = new JLabel("<html>The cut in range value for the production of secondary  " +
        		"particles, in mm. If the emission of a secondary particle is calculated " +
        		"during the simulation, and its range in the material is superior to the" +
        		" cut value, the particle is simulated. If the particle range is inferior" +
        		" to the cut value, the simulation of this secondary particle is replaced" +
        		" by a local deposit of the kinetic energy.</html>");

        CuttingPan.add(lab);
        CuttingPan.add(pan,BorderLayout.SOUTH);
        CuttingPan.setPreferredSize(new Dimension(450,175));
        gbc.gridx = 2;
        gbc.gridwidth = 2;
        this.add(CuttingPan,gbc);
        
        (this.DirectionPanel = new TripleFormattedPanel(this, new JLabel("<html>Direction of particle emission (in units of voxel):<br>(0,0,0 for random emission)</html>"), true)).setToolTipText("<html>The three following numbers are the cartesian coordinates of the row vector<br> defining the emission directions of the primary particles <br>(0 0 0 means a random vector of emission for each particle).<br> This vector can be used to define a unidirectional particle flux.</html>");
        this.DirectionPanel.addKeyListener(this);
        (this.clockField = new FormattedLabelField(this, new JLabel("Clock: "))).setToolTipText("<html>The update frequency of the simulation progress bar in the terminal where DosiVox <br>is running. It is given in % of the total number of particles emitted.</html>");
        final JPanel SpecialParamPanel = new JPanel();
        SpecialParamPanel.add(this.DirectionPanel);
        SpecialParamPanel.add(this.clockField);
        SpecialParamPanel.add(new JLabel("%"));
        
        final JPanel VoxelItselExternalfInfo = new JPanel();
        VoxelItselExternalfInfo.setBorder(BorderFactory.createTitledBorder("EXTERNAL VOXELIZATION"));
        VoxelItselExternalfInfo.setLayout(new BoxLayout(VoxelItselExternalfInfo, 2));
        
        this.ExternalDimensionPanel = new TripleFormattedPanel(this, new JLabel(""), false);
        JPanel boxPageExternalInfo = new JPanel();
        boxPageExternalInfo.setLayout(new BoxLayout(boxPageExternalInfo, BoxLayout.PAGE_AXIS));
        boxPageExternalInfo.setBorder(BorderFactory.createTitledBorder("Voxel dimensions"));
        
        this.ExternalDimensionPanel.addKeyListener(this);
        final JPanel ExteralUnitPan = new JPanel();
        ExteralUnitPan.setLayout(new BoxLayout(ExteralUnitPan, 2));
        (this.ExternalunitCombo = new JComboBox<String>()).addItem("1 \u03bcm");
        this.ExternalunitCombo.addItem("10 \u03bcm");
        this.ExternalunitCombo.addItem("100 \u03bcm");
        this.ExternalunitCombo.addItem("1 mm");
        this.ExternalunitCombo.addItem("1 cm");
        this.ExternalunitCombo.setSelectedIndex(3);
        this.ExternalunitCombo.addItemListener(this);
        
        ExteralUnitPan.add(new JLabel("Unit: "));
        ExteralUnitPan.add(this.ExternalunitCombo);
        ExteralUnitPan.setMaximumSize(new Dimension(110, 30));
        boxPageExternalInfo.add(this.ExternalDimensionPanel);
        boxPageExternalInfo.add(ExteralUnitPan);
        
        (this.ExternalNbVoxelPanel = new TripleFormattedPanel(this, new JLabel(""), false)).setBorder(BorderFactory.createTitledBorder("Number of voxel"));
        
        this.ExternalNbVoxelPanel.addKeyListener(this);
        VoxelItselExternalfInfo.add(boxPageExternalInfo);
        VoxelItselExternalfInfo.add(this.ExternalNbVoxelPanel);
        //NorthPanel.add(VoxelItselExternalfInfo);
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.gridx = 4;
        
        this.add(VoxelItselExternalfInfo,gbc);
        /**
         * CENTER
         */
        final JButton validateButton = new JButton("Validate values in the fields into the object");
        validateButton.setActionCommand("validate");
        validateButton.addActionListener(this);
        final JButton resetButton = new JButton("Reload object's informations into fields");
        resetButton.setActionCommand("update frame component");
        resetButton.addActionListener(this);
        final JPanel buttonPan = new JPanel();
        buttonPan.add(validateButton, "North");
        buttonPan.add(resetButton, "South");
        
        JPanel pageSpecialButton = new JPanel(new GridLayout(2,1));
        pageSpecialButton.add(SpecialParamPanel);
        pageSpecialButton.add(buttonPan);
        (this.GlobalSubVoxelInfo = new LockablePanel()).setLayout(new BoxLayout(this.GlobalSubVoxelInfo, 3));
        this.GlobalSubVoxelInfo.setBorder(BorderFactory.createTitledBorder("INTERNAL VOXELIZATION"));
        (this.ScanOrManualCheck = new JCheckBox("Check to load a grey scale image", false)).setAlignmentX(0.5f);
        this.ScanOrManualCheck.addChangeListener(this);
        this.GlobalSubVoxelInfo.add(this.ScanOrManualCheck);
        (this.VoxelItselInternalfInfo = new LockablePanel()).setLayout(new BoxLayout(this.VoxelItselInternalfInfo, 2));
        JPanel pageDimInternPan = new JPanel();
        pageDimInternPan.setLayout(new BoxLayout(pageDimInternPan,BoxLayout.PAGE_AXIS));
        this.InternalDimensionPanel = new TripleFormattedPanel(this, null, false);
        this.InternalDimensionPanel.getXField().setEnabled(false);
        this.InternalDimensionPanel.getYField().setEnabled(false);
        this.InternalDimensionPanel.getZField().setEnabled(false);
        
        final JPanel InternalUnitPan = new JPanel();
        InternalUnitPan.setLayout(new BoxLayout(InternalUnitPan, 2));
        this.InternalunitCombo = new JLabel("1 mm");
        InternalUnitPan.add(new JLabel("Unit: "));
        InternalUnitPan.add(this.InternalunitCombo);
        pageDimInternPan.add(InternalDimensionPanel);
        pageDimInternPan.add(InternalUnitPan);
        pageDimInternPan.setBorder(BorderFactory.createTitledBorder("Voxel dimensions"));
        
        (this.InternalNbVoxelPanel = new TripleFormattedPanel(this, new JLabel(""), false)).setBorder(BorderFactory.createTitledBorder("Number of voxel"));
        //this.InternalNbVoxelPanel.setPreferredSize(new Dimension(200, 60));
        //this.InternalNbVoxelPanel.setMaximumSize(this.InternalNbVoxelPanel.getPreferredSize());
        this.InternalNbVoxelPanel.addKeyListener(this);
        this.VoxelItselInternalfInfo.add(pageDimInternPan);
        this.VoxelItselInternalfInfo.add(this.InternalNbVoxelPanel);
        this.VoxelItselInternalfInfo.setPreferredSize(new Dimension(465,110));
        this.GlobalSubVoxelInfo.add(this.VoxelItselInternalfInfo);
        //this.add(NorthPanel);
        
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.LINE_AXIS));
        centerPanel.add(pageSpecialButton);
        centerPanel.add(this.GlobalSubVoxelInfo);
        
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridheight = 1;
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.anchor = GridBagConstraints.LINE_END;
        this.add(centerPanel,gbc);
        
        
    }
    
    /**
     * Initialization of south components.
     */
    private void initSouth() {
        final JPanel SouthPanel = new JPanel();
        SouthPanel.setLayout(new BoxLayout(SouthPanel,BoxLayout.PAGE_AXIS));
        
        JPanel boxLinePanel = new JPanel();
        boxLinePanel.setLayout(new BoxLayout(boxLinePanel, BoxLayout.LINE_AXIS));
        SouthPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.BLACK, 3), " DETECTOR "));
        final JPanel SensitiveRadioPanel = new JPanel();
        SensitiveRadioPanel.setLayout(new BoxLayout(SensitiveRadioPanel, 2));
        final ButtonGroup bg = new ButtonGroup();
        bg.add(this.none = new JRadioButton("Vertical probe only"));
        bg.add(this.SubVoxelizedVoxel = new JRadioButton("Sub-Voxelized Voxel"));
        bg.add(this.SimpleGrainPacking = new JRadioButton("Simple grain packing"));
        bg.add(this.SuccessiveGrainPacking = new JRadioButton("Successive grain packing"));
        SensitiveRadioPanel.add(this.none);
        SensitiveRadioPanel.add(Box.createRigidArea(new Dimension(120,10)));
        SensitiveRadioPanel.add(this.SubVoxelizedVoxel);
        SensitiveRadioPanel.add(Box.createRigidArea(new Dimension(120,10)));
        SensitiveRadioPanel.add(this.SimpleGrainPacking);
        SensitiveRadioPanel.add(Box.createRigidArea(new Dimension(120,10)));
        SensitiveRadioPanel.add(this.SuccessiveGrainPacking);
        this.none.addChangeListener(this);
        this.SubVoxelizedVoxel.addChangeListener(this);
        this.SimpleGrainPacking.addChangeListener(this);
        this.SuccessiveGrainPacking.addChangeListener(this);
        this.initVerticalProbe(this.VerticalProbePanel = new LockablePanel());
        this.initSimpleGrainProbe(this.GrainPackingPanel = new LockablePanel());
        this.initSubVoxelProbe(this.subVoxelPanel = new LockablePanel());
        this.none.setSelected(true);
        SouthPanel.add(SensitiveRadioPanel);
        boxLinePanel.add(this.VerticalProbePanel);
        boxLinePanel.add(this.GrainPackingPanel);
        boxLinePanel.add(this.subVoxelPanel);
        SouthPanel.add(boxLinePanel);
        
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridheight = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        this.add(SouthPanel,gbc);
    }
    
    /**
     * Initialization of the default probe.
     * @param VerticalProbePanel panel to modify.
     */
    private void initVerticalProbe(final JPanel VerticalProbePanel) {
        VerticalProbePanel.setLayout(new BoxLayout(VerticalProbePanel, 3));
        VerticalProbePanel.setBorder(BorderFactory.createTitledBorder("Vertical Probe"));
        ((TitledBorder)VerticalProbePanel.getBorder()).setTitleColor(Color.RED);
        (this.locationPanel = new TripleFormattedPanel(this, new JLabel("Probe Location: "), false)).addKeyListener(this);
        this.nbVertCellPan = new FormattedLabelField(this, new JLabel("Number of vertical segments: "));
        this.nbVertCellPan.getField().addKeyListener(this);
        this.diameterPan = new FormattedLabelField(this, new JLabel("diameter: "));
        this.diameterPan.getField().addKeyListener(this);
        this.diameterPan.add(new JLabel("mm"));
        final JPanel offsetPan = new JPanel();
        offsetPan.setLayout(new BoxLayout(offsetPan, 2));
        offsetPan.setAlignmentX(0.5f);
        (this.XoffsetField = new JFormattedTextField(TripleFormattedPanel.createmask(true))).setFocusLostBehavior(3);
        this.XoffsetField.setBorder(BorderFactory.createTitledBorder(MainInfoVoxel.greenBorder, "X-offset"));
        this.XoffsetField.setMaximumSize(new Dimension(80, 40));
        this.XoffsetField.addKeyListener(this);
        offsetPan.add(this.XoffsetField);
        offsetPan.add(new JLabel("%  "));
        (this.YoffsetField = new JFormattedTextField(TripleFormattedPanel.createmask(true))).setFocusLostBehavior(3);
        this.YoffsetField.setBorder(BorderFactory.createTitledBorder(MainInfoVoxel.greenBorder, "Y-offset"));
        this.YoffsetField.setMaximumSize(new Dimension(80, 40));
        this.YoffsetField.addKeyListener(this);
        offsetPan.add(this.YoffsetField);
        offsetPan.add(new JLabel("%  "));
        VerticalProbePanel.add(this.locationPanel);
        VerticalProbePanel.add(Box.createRigidArea(new Dimension(0, 5)));
        VerticalProbePanel.add(this.nbVertCellPan);
        VerticalProbePanel.add(Box.createRigidArea(new Dimension(0, 5)));
        VerticalProbePanel.add(this.diameterPan);
        VerticalProbePanel.add(Box.createRigidArea(new Dimension(0, 5)));
        VerticalProbePanel.add(offsetPan);
        VerticalProbePanel.add(Box.createRigidArea(new Dimension(0, 5)));
        final JPanel labelPan = new JPanel();
        labelPan.add(new JLabel("( relative to voxel center,set at 50% )"));
        VerticalProbePanel.add(labelPan);
    }
    
    /**
     * Initialization of the simple grain probe.
     * @param SimpleGrainProbePanel panel to modify.
     */
    private void initSimpleGrainProbe(final JPanel SimpleGrainProbePanel) {
        SimpleGrainProbePanel.setLayout(new BoxLayout(SimpleGrainProbePanel, 3));
        SimpleGrainProbePanel.setBorder(BorderFactory.createTitledBorder("Grain Packing"));
        ((TitledBorder)SimpleGrainProbePanel.getBorder()).setTitleColor(Color.BLUE);
        (this.sizeBoxPan = new TripleFormattedPanel(this, new JLabel("Size of packing box ( % of X, Y, Z units )"), false)).addKeyListener(this);
        (this.offsetPan = new TripleFormattedPanel(this, new JLabel("Offset ( % of the voxel size, center at 50 )"), false)).addKeyListener(this);
        final JPanel EmissionPan = new JPanel();
        EmissionPan.add(new JLabel("Emission of particules :"));
        (this.fromGrainRadio = new JRadioButton("from grain")).addChangeListener(this);
        (this.fromMatrixRadio = new JRadioButton("from matrix")).addChangeListener(this);
        final ButtonGroup bg = new ButtonGroup();
        bg.add(this.fromGrainRadio);
        bg.add(this.fromMatrixRadio);
        EmissionPan.add(this.fromGrainRadio);
        EmissionPan.add(this.fromMatrixRadio);
        SimpleGrainProbePanel.add(this.sizeBoxPan);
        SimpleGrainProbePanel.add(this.offsetPan);
        SimpleGrainProbePanel.add(EmissionPan);
    }
    
    /**
     * Initialization of the sub-voxelized probe.
     * @param SubVoxelProbePanel the panel to modify.
     */
    private void initSubVoxelProbe(final JPanel SubVoxelProbePanel) {
        SubVoxelProbePanel.setBorder(BorderFactory.createTitledBorder(" Sub-Voxelization "));
        ((TitledBorder)SubVoxelProbePanel.getBorder()).setTitleColor(Color.darkGray);
        SubVoxelProbePanel.setLayout(new BoxLayout(SubVoxelProbePanel, 3));
        SubVoxelProbePanel.add(new JLabel("Material for mapping:"));
        (this.MaterialForMapping = new JComboBox<Material>()).setMaximumSize(new Dimension(300, 20));
        this.materialAdded();
        (this.createInternalButton = new JButton("Create internal 3D scene")).setActionCommand("create intern voxel");
        this.createInternalButton.setPreferredSize(new Dimension(250, 30));
        this.createInternalButton.addActionListener(this);
        SubVoxelProbePanel.add(this.MaterialForMapping);
        SubVoxelProbePanel.add(this.createInternalButton);
    }
    
    /**
     * Update the Swing components.
     */
    public void updateFrameComponent() {
        final ExternalVoxelizedWorld vox = this.father.getVoxSample();
        this.nbPriMantissaTextField.getField().setText(new StringBuilder(String.valueOf(vox.getNbPrimaryUnit())).toString());
        this.nbPriMantissaTextField.isUpdate();
        this.nbPriSignificandTextField.getField().setText("3");
        this.nbPriSignificandTextField.isUpdate();
        this.CutRangeValueField.getField().setText(new StringBuilder(String.valueOf(vox.getCuttingValue() * 1000.0f)).toString());
        this.CutRangeValueField.isUpdate();
        this.CutRangeUnitCombo.setSelectedIndex(2);
        this.ExternalDimensionPanel.updateFrameComponent(vox.getVoxelDimensionX(), vox.getVoxelDimensionY(), vox.getVoxelDimensionZ());
        this.ExternalunitCombo.setSelectedItem(vox.getDimensionUnit());
        this.ExternalNbVoxelPanel.updateFrameComponent(vox.getNbVoxelX(), vox.getNbVoxelY(), vox.getNbVoxelZ());
        if (vox.getInternalVoxelizedObject() != null) {
            this.InternalNbVoxelPanel.updateFrameComponent(vox.getInternalVoxelizedObject().getNbVoxelX(), vox.getInternalVoxelizedObject().getNbVoxelY(), vox.getInternalVoxelizedObject().getNbVoxelZ());
        }
        this.ExternalNbVoxelPanel.updateFrameComponent(vox.getNbVoxelX(), vox.getNbVoxelY(), vox.getNbVoxelZ());
        this.DirectionPanel.updateFrameComponent(vox.getMomentumX(), vox.getMomentumY(), vox.getMomentumZ());
        this.clockField.getField().setText(new StringBuilder(String.valueOf(vox.getClockValue())).toString());
        this.clockField.isUpdate();
        this.locationPanel.updateFrameComponent(vox.getProbeLocationX(), vox.getProbeLocationY(), vox.getProbeLocationZ());
        this.nbVertCellPan.getField().setText(new StringBuilder(String.valueOf(vox.getNumberCells())).toString());
        this.nbVertCellPan.isUpdate();
        this.diameterPan.getField().setText(new StringBuilder(String.valueOf(vox.getProbeDiameter())).toString());
        this.diameterPan.isUpdate();
        this.XoffsetField.setText(new StringBuilder(String.valueOf(vox.getProbeOffsetX())).toString());
        this.XoffsetField.setBorder(BorderFactory.createTitledBorder(MainInfoVoxel.greenBorder, "X-offset"));
        this.YoffsetField.setText(new StringBuilder(String.valueOf(vox.getProbeOffsetY())).toString());
        this.YoffsetField.setBorder(BorderFactory.createTitledBorder(MainInfoVoxel.greenBorder, "Y-offset"));
        this.sizeBoxPan.updateFrameComponent(vox.getSizeOfPackingBoxX(), vox.getSizeOfPackingBoxY(), vox.getSizeOfPackingBoxZ());
        this.offsetPan.updateFrameComponent(vox.getOffsetX(), vox.getOffsetY(), vox.getOffsetZ());
        if (vox.isEmissionFromGrain()) {
            this.fromGrainRadio.setSelected(true);
        }
        else {
            this.fromMatrixRadio.setSelected(true);
        }
    }
    
    /**
     * Load the value in fields into the object, if these values are correct, and then call updateFrameComponent().
     */
    public void validateFields() {
        final ExternalVoxelizedWorld vox = this.father.getVoxSample();
        if (!this.nbPriMantissaTextField.fieldIsEmpty() && !this.nbPriSignificandTextField.fieldIsEmpty()) {
            final int mantissa = this.nbPriMantissaTextField.getFieldAsInt();
            final int significand = this.nbPriSignificandTextField.getFieldAsInt() - 3;
            int nbZeros = 1;
            for (int i = 0; i < significand; ++i) {
                nbZeros *= 10;
            }
            vox.setNbPrimaryUnit(mantissa * nbZeros);
        }
        if (!this.CutRangeValueField.fieldIsEmpty()) {
            final float nbValueCut = this.CutRangeValueField.getFieldAsFloat();
            final String unit = ((String)this.CutRangeUnitCombo.getSelectedItem()).replaceAll("\\s+", "");
            if (unit.equals("cm")) {
                vox.setCuttingValue(nbValueCut * 10.0f);
            }
            else if (unit.equals("mm")) {
                vox.setCuttingValue(nbValueCut);
            }
            else if (unit.equals("nm")) {
                vox.setCuttingValue(nbValueCut / 1000000.0f);
            }
            else {
                vox.setCuttingValue(nbValueCut / 1000.0f);
            }
        }
        try {
            if (vox.getVoxelDimensionX() != this.ExternalDimensionPanel.getValue(TripleFormattedPanel.XYZ.X)) {
                vox.resizeVoxelDimensionX(this.ExternalDimensionPanel.getValue(TripleFormattedPanel.XYZ.X));
            }
        }
        catch (Exception ex) {}
        try {
            if (vox.getVoxelDimensionY() != this.ExternalDimensionPanel.getValue(TripleFormattedPanel.XYZ.Y)) {
                vox.resizeVoxelDimensionY(this.ExternalDimensionPanel.getValue(TripleFormattedPanel.XYZ.Y));
            }
        }
        catch (Exception ex2) {}
        try {
            if (vox.getVoxelDimensionZ() != this.ExternalDimensionPanel.getValue(TripleFormattedPanel.XYZ.Z)) {
                vox.resizeVoxelDimensionZ(this.ExternalDimensionPanel.getValue(TripleFormattedPanel.XYZ.Z));
            }
        }
        catch (Exception ex3) {}
        vox.setDimensionUnit((String)this.ExternalunitCombo.getSelectedItem());
        try {
            if (vox.getNbVoxelX() != this.ExternalNbVoxelPanel.getValue(TripleFormattedPanel.XYZ.X)) {
                vox.resizeDimensionX((int)this.ExternalNbVoxelPanel.getValue(TripleFormattedPanel.XYZ.X));
            }
        }
        catch (Exception ex4) {}
        try {
            if (vox.getNbVoxelY() != this.ExternalNbVoxelPanel.getValue(TripleFormattedPanel.XYZ.Y)) {
                vox.resizeDimensionY((int)this.ExternalNbVoxelPanel.getValue(TripleFormattedPanel.XYZ.Y));
            }
        }
        catch (Exception ex5) {}
        try {
            if (vox.getNbVoxelZ() != this.ExternalNbVoxelPanel.getValue(TripleFormattedPanel.XYZ.Z)) {
                vox.resizeDimensionZ((int)this.ExternalNbVoxelPanel.getValue(TripleFormattedPanel.XYZ.Z));
            }
        }
        catch (Exception ex6) {}
        if (vox.getInternalVoxelizedObject() != null) {
            final VoxelizedObject InVox = vox.getInternalVoxelizedObject();
            try {
                if (InVox.getNbVoxelX() != this.InternalNbVoxelPanel.getValue(TripleFormattedPanel.XYZ.X)) {
                    InVox.resizeDimensionX((int)this.InternalNbVoxelPanel.getValue(TripleFormattedPanel.XYZ.X));
                    this.calculateDimensionOfInternalVoxel();
                    InVox.resizeVoxelDimensionX(this.InternalDimensionPanel.getValue(TripleFormattedPanel.XYZ.X) * 50.0f);
                }
            }
            catch (Exception ex7) {}
            try {
                if (InVox.getNbVoxelY() != this.InternalNbVoxelPanel.getValue(TripleFormattedPanel.XYZ.Y)) {
                    InVox.resizeDimensionY((int)this.InternalNbVoxelPanel.getValue(TripleFormattedPanel.XYZ.Y));
                    this.calculateDimensionOfInternalVoxel();
                    InVox.resizeVoxelDimensionY(this.InternalDimensionPanel.getValue(TripleFormattedPanel.XYZ.Y) * 50.0f);
                }
            }
            catch (Exception ex8) {}
            try {
                if (InVox.getNbVoxelZ() != this.InternalNbVoxelPanel.getValue(TripleFormattedPanel.XYZ.Z)) {
                    InVox.resizeDimensionZ((int)this.InternalNbVoxelPanel.getValue(TripleFormattedPanel.XYZ.Z));
                    this.calculateDimensionOfInternalVoxel();
                    InVox.resizeVoxelDimensionZ(this.InternalDimensionPanel.getValue(TripleFormattedPanel.XYZ.Z) * 50.0f);
                }
            }
            catch (Exception ex9) {}
        }
        try {
            vox.setMomentumX((int)this.DirectionPanel.getValue(TripleFormattedPanel.XYZ.X));
        }
        catch (Exception ex10) {}
        try {
            vox.setMomentumY((int)this.DirectionPanel.getValue(TripleFormattedPanel.XYZ.Y));
        }
        catch (Exception ex11) {}
        try {
            vox.setMomentumZ((int)this.DirectionPanel.getValue(TripleFormattedPanel.XYZ.Z));
        }
        catch (Exception ex12) {}
        if (!this.clockField.fieldIsEmpty()) {
            vox.setClockValue(this.clockField.getFieldAsFloat());
        }
        try {
            vox.setProbeLocationX((int)this.locationPanel.getValue(TripleFormattedPanel.XYZ.X));
        }
        catch (Exception ex13) {}
        try {
            vox.setProbeLocationY((int)this.locationPanel.getValue(TripleFormattedPanel.XYZ.Y));
        }
        catch (Exception ex14) {}
        try {
            vox.setProbeLocationZ((int)this.locationPanel.getValue(TripleFormattedPanel.XYZ.Z));
        }
        catch (Exception ex15) {}
        try {
            vox.setNumberCells(this.nbVertCellPan.getFieldAsInt());
        }
        catch (NumberFormatException ex16) {}
        try {
            vox.setProbeDiameter(this.diameterPan.getFieldAsFloat());
        }
        catch (NumberFormatException ex17) {}
        if (!this.XoffsetField.getText().replaceAll("\\s+", "").isEmpty()) {
            vox.setProbeOffsetX(Float.parseFloat(this.XoffsetField.getText().replaceAll("\\s+", "")));
        }
        if (!this.YoffsetField.getText().replaceAll("\\s+", "").isEmpty()) {
            vox.setProbeOffsetY(Float.parseFloat(this.YoffsetField.getText().replaceAll("\\s+", "")));
        }
        try {
            vox.setSizeOfPackingBoxX(this.sizeBoxPan.getValue(TripleFormattedPanel.XYZ.X));
        }
        catch (Exception ex18) {}
        try {
            vox.setSizeOfPackingBoxY(this.sizeBoxPan.getValue(TripleFormattedPanel.XYZ.Y));
        }
        catch (Exception ex19) {}
        try {
            vox.setSizeOfPackingBoxZ(this.sizeBoxPan.getValue(TripleFormattedPanel.XYZ.Z));
        }
        catch (Exception ex20) {}
        try {
            vox.setOffsetX(this.offsetPan.getValue(TripleFormattedPanel.XYZ.X));
        }
        catch (Exception ex21) {}
        try {
            vox.setOffsetY(this.offsetPan.getValue(TripleFormattedPanel.XYZ.Y));
        }
        catch (Exception ex22) {}
        try {
            vox.setOffsetZ(this.offsetPan.getValue(TripleFormattedPanel.XYZ.Z));
        }
        catch (Exception ex23) {}
        int mat = -1;
        if (this.MaterialForMapping.getSelectedItem() != null) {
            mat = this.MaterialForMapping.getSelectedIndex();
        }
        vox.setMaterialForMap(mat);
        this.updateFrameComponent();
    }
    
    /**
     * Triggered when a key is pressed.
     */
    @Override
    public void keyPressed(final KeyEvent e) {
        if (e.getKeyCode() != 10) {
            if (e.getSource() == this.XoffsetField) {
                this.XoffsetField.setBorder(BorderFactory.createTitledBorder(MainInfoVoxel.redBorder, "X-offset"));
            }
            if (e.getSource() == this.YoffsetField) {
                this.YoffsetField.setBorder(BorderFactory.createTitledBorder(MainInfoVoxel.redBorder, "Y-offset"));
            }
        }
        else {
            this.validateFields();
        }
    }
    
    @Override
    public void keyReleased(final KeyEvent arg0) {
    }
    
    @Override
    public void keyTyped(final KeyEvent e) {
        if (e.getKeyCode() != 10) {
            if (e.getSource() == this.XoffsetField) {
                this.XoffsetField.setBorder(BorderFactory.createTitledBorder(MainInfoVoxel.redBorder, "X-offset"));
            }
            if (e.getSource() == this.YoffsetField) {
                this.YoffsetField.setBorder(BorderFactory.createTitledBorder(MainInfoVoxel.redBorder, "Y-offset"));
            }
        }
        else {
            this.validateFields();
        }
    }
    
    /**
     * Triggered when the state of an item has changed.
     */
    @Override
    public void itemStateChanged(final ItemEvent e) {
        final ExternalVoxelizedWorld vox = this.father.getVoxSample();
        if (e.getSource() == this.PrimaryParticleRadio[0] || e.getSource() == this.PrimaryParticleRadio[1] || e.getSource() == this.PrimaryParticleRadio[2]) {
            if (this.CheckDefault.isSelected()) {
                if (this.PrimaryParticleRadio[0].isSelected()) {
                    vox.setCuttingValue(MainInfoVoxel.DEFAULT_CUTTING_VALUE_ALPHA);
                }
                else if (this.PrimaryParticleRadio[1].isSelected()) {
                    vox.setCuttingValue(MainInfoVoxel.DEFAULT_CUTTING_VALUE_BETA);
                }
                else {
                    vox.setCuttingValue(MainInfoVoxel.DEFAULT_CUTTING_VALUE_GAMMA);
                }
            }
            vox.setPrimaryParticle(PrimaryParticles.valueOf(((JRadioButton)e.getSource()).getText()));
        }
        if (e.getSource() == this.CutRangeUnitCombo) {
            this.CutRangeValueField.getField().setBorder(MainInfoVoxel.redBorder);
        }
        if (e.getSource() == this.ExternalunitCombo) {
            this.ExternalDimensionPanel.setToUpdate();
        }
    }
    
    /**
     * Triggered when the state of the object has changed.
     */
    @Override
    public void stateChanged(final ChangeEvent e) {
        final ExternalVoxelizedWorld vox = this.father.getVoxSample();
        if (e.getSource() == this.CheckDefault) {
            if (this.CheckDefault.isSelected()) {
                if (this.PrimaryParticleRadio[0].isSelected()) {
                    vox.setCuttingValue(MainInfoVoxel.DEFAULT_CUTTING_VALUE_ALPHA);
                }
                else if (this.PrimaryParticleRadio[1].isSelected()) {
                    vox.setCuttingValue(MainInfoVoxel.DEFAULT_CUTTING_VALUE_BETA);
                }
                else {
                    vox.setCuttingValue(MainInfoVoxel.DEFAULT_CUTTING_VALUE_GAMMA);
                }
                this.CutRangeValueField.getField().setEnabled(false);
                this.CutRangeUnitCombo.setEnabled(false);
                this.CutRangeValueField.isUpdate();
            }
            else {
                this.CutRangeValueField.getField().setEnabled(true);
                this.CutRangeUnitCombo.setEnabled(true);
            }
        }
        if (e.getSource() == this.none || e.getSource() == this.SimpleGrainPacking || e.getSource() == this.SuccessiveGrainPacking || e.getSource() == this.SubVoxelizedVoxel) {
            if (this.none.isSelected()) {
                this.GrainPackingPanel.setEnabled(false);
                this.subVoxelPanel.setEnabled(false);
                this.GlobalSubVoxelInfo.setEnabled(false);
                vox.setIndexDetectorDefinition(0);
            }
            else if (this.SimpleGrainPacking.isSelected() || this.SuccessiveGrainPacking.isSelected()) {
                this.GrainPackingPanel.setEnabled(true);
                this.subVoxelPanel.setEnabled(false);
                this.GlobalSubVoxelInfo.setEnabled(false);
                if (this.SimpleGrainPacking.isSelected()) {
                    vox.setIndexDetectorDefinition(2);
                }
                else {
                    vox.setIndexDetectorDefinition(3);
                }
            }
            else {
                this.GrainPackingPanel.setEnabled(false);
                this.subVoxelPanel.setEnabled(true);
                this.GlobalSubVoxelInfo.setEnabled(true);
                this.InternalDimensionPanel.getXField().setEnabled(false);
                this.InternalDimensionPanel.getYField().setEnabled(false);
                this.InternalDimensionPanel.getZField().setEnabled(false);
                vox.setIndexDetectorDefinition(1);
            }
        }
        if (e.getSource() == this.fromGrainRadio || e.getSource() == this.fromMatrixRadio) {
            vox.setEmissionFromGrain(this.fromGrainRadio.isSelected());
        }
    }
    
    @Override
    public void materialAdded() {
        this.MaterialForMapping.removeAllItems();
        ((DefaultComboBoxModel<Material>)this.MaterialForMapping.getModel()).insertElementAt(null, 0);
        final List<Material> matList = this.father.getListOfMaterial();
        for (final Material m : matList) {
            this.MaterialForMapping.addItem(m);
        }
    }
    
    @Override
    public void actionPerformed(final ActionEvent e)
    {
    	final int ERROR_ALLOWED = 2;
        if (e.getActionCommand().equals("validate")) {
            this.validateFields();
        }
        if (e.getActionCommand().equals("update frame component")) {
            this.updateFrameComponent();
        }
        Label_0588: {
        	String nameGroupFile="";
            if (e.getActionCommand().equals("create intern voxel")) {
                final JDialog modalWaiting = new JDialog((Frame)null, "Please wait", true);
                modalWaiting.setSize(200, 80);
                modalWaiting.setLocationRelativeTo(null);
                modalWaiting.setResizable(false);
                modalWaiting.setDefaultCloseOperation(0);
                modalWaiting.add(new JLabel("Processing..."));
                int nbVoxX = 0;
                int nbVoxY = 0;
                int nbVoxZ = 0;
                float dimX = 0.0f;
                float dimY = 0.0f;
                float dimZ = 0.0f;
                if (!this.ScanOrManualCheck.isSelected()) {
                    try {
                        nbVoxX = (int)this.InternalNbVoxelPanel.getValue(TripleFormattedPanel.XYZ.X);
                        nbVoxY = (int)this.InternalNbVoxelPanel.getValue(TripleFormattedPanel.XYZ.Y);
                        nbVoxZ = (int)this.InternalNbVoxelPanel.getValue(TripleFormattedPanel.XYZ.Z);
                        
                        this.calculateDimensionOfInternalVoxel();
                        dimX = this.InternalDimensionPanel.getValue(TripleFormattedPanel.XYZ.X);
                        dimY = this.InternalDimensionPanel.getValue(TripleFormattedPanel.XYZ.Y);
                        dimZ = this.InternalDimensionPanel.getValue(TripleFormattedPanel.XYZ.Z);
                        this.father.getVoxSample().createInternalWorldIntheSpecifiedVoxel(nbVoxX, nbVoxY, nbVoxZ, dimX * 50.0f, dimY * 50.0f, dimZ * 50.0f);
                        this.fatherInterface.createWorld();
                        break Label_0588;
                    }
                    catch (Exception e2) {
                        JOptionPane.showMessageDialog(null, "Unable to create internal world, some values are missing.\n" + e2.getMessage(), "Error during internal creation", 0);
                        return;
                    }
                }
                final JFileChooser FileChooser = new JFileChooser();
                FileChooser.setDialogTitle("Select image containing directory");
                FileChooser.setFileSelectionMode(1);
                final int returnVal = FileChooser.showOpenDialog(null);
                if (returnVal == 0) {
                    final File directory = FileChooser.getSelectedFile();
                    try {
                        if (directory.isFile()) {
                            JOptionPane.showMessageDialog(null, "Please select the directory which contains the files, not the file itself.", "Not a directory", 0);
                            return;
                        }
                        /**
                         * File name analysis
                         */
                        nameGroupFile = this.analyseFileInDirectory(directory);
                        final String nameGroup = nameGroupFile;
                        
                        /**
                         * 
                         */
                        final Thread t = new Thread() {
                            @Override
                            public void run() {
                                MainInfoVoxel.access$0(MainInfoVoxel.this, new Loader3DScan());
                                try {
                                	String str="";
                                	if(nameGroup.length() >= 3)
                                		str = nameGroup.substring(0, nameGroup.length()-ERROR_ALLOWED);
                                	MainInfoVoxel.this.loader.loadScan(directory,str);
                                }
                                catch (ScanException e) {
                                    JOptionPane.showMessageDialog(null, e.getMessage(), "Error occurs in 3D scan", 0);
                                    e.printStackTrace();
                                    modalWaiting.setVisible(false);
                                    return;
                                } catch (FileNotFoundException e)
								{
                                	JOptionPane.showMessageDialog(null, e.getMessage(), "Error occurs in 3D scan", 0);
									e.printStackTrace();
									modalWaiting.setVisible(false);
									return;
								} catch (IOException e)
								{
									JOptionPane.showMessageDialog(null, e.getMessage(), "Error occurs in 3D scan", 0);
									e.printStackTrace();
									modalWaiting.setVisible(false);
									return;
								} catch (NumberFormatException e)
                                {
									JOptionPane.showMessageDialog(null, e.getMessage(), "Error occurs in 3D scan", 0);
									e.printStackTrace();
									modalWaiting.setVisible(false);
									return;
                                }
                                MainInfoVoxel.this.loader.showJDialog(null, MainInfoVoxel.this.father.getListOfMaterial());
                                modalWaiting.setVisible(false);
                            }
                        };
                        t.start();
                        modalWaiting.setVisible(true);
                    }
                    catch (Exception e3) {
                        JOptionPane.showMessageDialog(null, e3.getMessage(), "Error occurs in 3D scan", 0);
                        return;
                    }
                }
                if (this.loader != null && this.loader.isValid()) {
                    final JDialog progressDialog = new JDialog((Frame)null, "Processing...", true);
                    progressDialog.setSize(200, 50);
                    progressDialog.setLocationRelativeTo(null);
                    progressDialog.setResizable(false);
                    final JProgressBar bar = new JProgressBar();
                    bar.setMaximum(this.loader.getFirstSizeZ());
                    bar.setMinimum(0);
                    bar.setStringPainted(true);
                    bar.setSize(200, 50);
                    progressDialog.add(bar);
                    final int option = JOptionPane.showConfirmDialog(null, "You have the possibility to load your scan in the main world. By this way, you can load your scan and chose another probe for your expirements.\n Still want to load your scan in a sub-voxelized object ? \n YES: load in intern world ; NO: load in the main world", "Which world ?", 1, 3);
                    Thread t2 = null;
                    final String nameGroup = nameGroupFile;
                    if (option != 1 && option != 2 && option != -1) {
                        t2 = new Thread() {
                            @Override
                            public void run() {
                            	String str="";
                            	if(nameGroup.length() >= 3)
                            		str = nameGroup.substring(0, nameGroup.length()-ERROR_ALLOWED);
                                MainInfoVoxel.this.father.getVoxSample()
                                	.createInternalWorldIntheSpecifiedVoxel(MainInfoVoxel.this.loader, bar
                                			,str);
                                progressDialog.add(new JLabel("Creation of the world in process..."));
                                MainInfoVoxel.this.fatherInterface.createWorld();
                                progressDialog.setVisible(false);
                            }
                        };
                    }
                    else {
                        if (this.SubVoxelizedVoxel.isSelected()) {
                            this.none.setSelected(true);
                        }
                        t2 = new Thread() {
                            @Override
                            public void run() {
                            	progressDialog.add(new JLabel("Creation of the world in process..."));
                                try {
                                	String str="";
                                	if(nameGroup.length() >= 3)
                                		str = nameGroup.substring(0, nameGroup.length()-ERROR_ALLOWED);
                                    MainInfoVoxel.this.father.getVoxSample().ScanToVoxel(MainInfoVoxel.this.loader, bar,
                                    			str);
                                }
                                catch (NumberFormatException e) {
                                    JOptionPane.showMessageDialog(null, e.getMessage(), "NumberFormatException during filling scan in voxel", 0);
                                    e.printStackTrace();
                                }
                                catch (IOException e2) {
                                    JOptionPane.showMessageDialog(null, e2.getMessage(), "IOException during filling scan in voxel", 0);
                                    e2.printStackTrace();
                                }
                                
                                progressDialog.setVisible(false);
                            }
                        };
                    }
                    t2.start();
                    progressDialog.setVisible(true);
                    this.materialAdded();
                }
            }
        }
        this.loader = null;
    }
    
    private String analyseFileInDirectory(File directory)
	{
    	File[] listFiles;
    	Map<String,Integer> recurentString = new HashMap<String,Integer>();
    	
        for (int length = (listFiles = directory.listFiles()).length, j = 0; j < length; ++j) {
            final File fileEntry = listFiles[j];
            
            for(int k=0; k <length;++k)
            {
            	if(k != j)
            	{
            		final File secondEntry = listFiles[k];
            		String str = getEquality(fileEntry.getName(), secondEntry.getName());
            		if(recurentString.containsKey(str))
            			{
            				int value = recurentString.get(str).intValue() + 1;
            				recurentString.put(str, value);
            			}
            		else
            			recurentString.put(str, 1);
            	}
            }
        }
        
        Set<Entry<String,Integer>> entries = recurentString.entrySet();
        Entry<String,Integer> winner = null;
        for(Entry<String,Integer> entry : entries)
        	if((winner == null) ||
        			entry.getValue().intValue() > winner.getValue().intValue())
        		winner = entry;
        
        if(winner == null)
        	return "";
        return winner.getKey();      		
	}

    private String getEquality(String str1,String str2)
    {
    	if (str1 == null) {
            return str2;
        }
        if (str2 == null) {
            return str1;
        }
        int at = indexOfDifference(str1, str2);
        if (at == -1) {
            return "";
        }
     return str1.substring(0, at);
    }
    
    private static int indexOfDifference(String str1, String str2) {
        if (str1 == str2) {
            return -1;
        }
        if (str1 == null || str2 == null) {
            return 0;
        }
        int i;
        for (i = 0; i < str1.length() && i < str2.length(); ++i) {
            if (str1.charAt(i) != str2.charAt(i)) {
                break;
            }
        }
        if (i < str2.length() || i < str1.length()) {
            return i;
        }
        return -1;
    }
    
	/**
     * @return true if all value in the object are coherent, false if not. The method shows JDialog error windows to describe what it is wrong.
     */
    public boolean checkIntegrityofProject() {
        final ExternalVoxelizedWorld vox = this.father.getVoxSample();
        try {
            if (vox.getProbeLocationX() > vox.getNbVoxelX() || vox.getProbeLocationY() > vox.getNbVoxelY() || vox.getProbeLocationZ() > vox.getNbVoxelZ()) {
                JOptionPane.showMessageDialog(null, "The location of the detector is out of the world.", "Invalid parameters.", 0);
                return false;
            }
            if (vox.getIndexDetectorDefinition() == 1 && vox.getInternalVoxelizedObject() == null) {
                JOptionPane.showMessageDialog(null, "You want sub-voxelized detector but you didn't create the interal world.", "Invalid parameters.", 0);
                return false;
            }
            if (vox.getIndexDetectorDefinition() != 1 && vox.getInternalVoxelizedObject() != null) {

                String[] options = {"Set the detector as sub-voxelized","Delete the intern voxelized world"};
                int option = JOptionPane.showOptionDialog(this, "There is an internal world created but the sub-voxelized detector is not set.",
                		"Invalid parameters", JOptionPane.YES_NO_CANCEL_OPTION,
                		JOptionPane.DEFAULT_OPTION, null, options, options[0]);
                if (option == JOptionPane.CLOSED_OPTION) {
                    return false;
                } else
                {
                	if(option == 0)
                		this.setSubVoxelizedProbe();
                	else if(option == 1)
                	{
                		this.fatherInterface.remove(3);
                		vox.removeInternalObject();
                	}
                }
            }
        }
        catch (Exception e) {
            JOptionPane.showMessageDialog(null, "The settings of the project is incomplete. Maybe a field is empty.", "Invalid parameters.", 0);
            return false;
        }
        return true;
    }
    
    /**
     * Do the linking with the eventual internal world. Usually used to dynamically changes its properties, like dimensions or number of voxels.
     * @param ongletManagement the tab manager.
     */
    public void addInternalVoxListener(final OngletManagement ongletManagement) {
        this.fatherInterface = ongletManagement;
    }
    
    /**
     * Change the type of the probe to sub voxelized voxel.
     */
    public void setSubVoxelizedProbe() {
        this.SubVoxelizedVoxel.setSelected(true);
        this.father.getVoxSample().setIndexDetectorDefinition(1);
    }
    
    /**
     * Calculate the dimension of the internal voxel, because it can't be anything.
     * @throws Exception
     */
    private void calculateDimensionOfInternalVoxel() throws Exception {
        final float ExtVoxelDimX = this.ExternalDimensionPanel.getValue(TripleFormattedPanel.XYZ.X);
        final float ExtVoxelDimY = this.ExternalDimensionPanel.getValue(TripleFormattedPanel.XYZ.Y);
        final float ExtVoxelDimZ = this.ExternalDimensionPanel.getValue(TripleFormattedPanel.XYZ.Z);
        this.InternalunitCombo.setText((String)this.ExternalunitCombo.getSelectedItem());
        this.InternalDimensionPanel.updateFrameComponent(ExtVoxelDimX / this.InternalNbVoxelPanel.getValue(TripleFormattedPanel.XYZ.X),
        		ExtVoxelDimY / this.InternalNbVoxelPanel.getValue(TripleFormattedPanel.XYZ.Y),
        		ExtVoxelDimZ / this.InternalNbVoxelPanel.getValue(TripleFormattedPanel.XYZ.Z));
    }
    
    static /* synthetic */ void access$0(final MainInfoVoxel mainInfoVoxel, final Loader3DScan loader) {
        mainInfoVoxel.loader = loader;
    }
}
