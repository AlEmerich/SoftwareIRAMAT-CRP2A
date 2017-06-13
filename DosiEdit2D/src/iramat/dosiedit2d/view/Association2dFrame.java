package iramat.dosiedit2d.view;

import iramat.dosiedit2d.view.ImagePanel.ImageBackground;
import iramat.dosiseed.model.ColoredMaterial;
import iramat.dosiseed.model.Material;
import iramat.dosiseed.view.TripleFormattedField;
import util.Couple;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * This class generate a JDialog frame when the user want to load a 2D-scan. 
 * It provides interface to associate grey scale loaded with material of the project.
 * @author alan
 *
 */
public class Association2dFrame extends JDialog implements ActionListener, ListSelectionListener
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -9171267089811510393L;
	
	/**
	 * true if associations is valid, false if not.
	 * It is valid if all shades has one material associates with, and two shades don't have
	 * the same material. 
	 */
	private boolean valid = false;
	
	/**
	 * The window shown befor juste before that association frame.
	 * @see ScanningWindow
	 */
	private ScanningWindow scan;
	
	/**
	 * Inner class of {@link ScanningWindow} which handles image and title file where the image comes from.
	 * @see ImageBackground
	 */
	private ImageBackground image;
	
	/**
	 * A copy of the list of material of the model.
	 */
    private List<Material> listMaterial;
    
    /**
     * The view of the number of voxel in the X-axis.
     */
    private JLabel nbVoxelXLabel;
    
    /**
     * The view of the number of voxel in the Y-axis.
     */
    private JLabel nbVoxelYLabel;
    
    /**
     * The view of the number of grey levels.
     */
    private JLabel nbGreyLevel;
    
    /**
     * The view of {@link #listMaterial}
     */
    private JList<Material> JListMaterial;
    
    /**
     * The model of {@link #JListMaterial}
     */
    private DefaultListModel<Material> listModelMaterial;
    
    /**
     * The view field to set radiation to the material.
     */
    private ExtensibleFormattedPanel RadField;
    
    /**
     * The view of {@link #listModelGreyLevels}
     */
    private JList<Integer> JListGreyLevels;
    
    /**
     * The model of {@link #JListGreyLevels}
     */
    private DefaultListModel<Integer> listModelGreyLevels;
    
    /**
     * The view of {@link #listModelAssoc}
     */
    private JList<Couple<Material, Integer>> JListAssociations;
    
    /**
     * The view of {@link #JListAssociations}
     */
    private DefaultListModel<Couple<Material, Integer>> listModelAssoc;
    
    /**
     * Constructor. Call the super constructor JDialog, keep in fields the list material and the loader, init and pack.
     * @param parent the parent JFrame.
     * @param title the title of the window.
     * @param modal true if the window maintains focus, false if not.
     * @param scan  The load which have read the file once to know what and how many grey levels there are.
     * @param listM list of available material.
     * @see JDialog
     */
    public Association2dFrame(final JFrame parent, final String title, 
    		final boolean modal, final ScanningWindow scan, final List<Material> listM) {
        super(parent, title, modal);
        this.listMaterial = listM;
        this.scan = scan;
        this.image = scan.getSelectedPanel().getImagePanelBackground();
        this.initComponents();
        
        this.JListGreyLevels.addListSelectionListener(this);
        this.pack();
    }
    
    /**
     * Initialization of all components.
     */
    private void initComponents() {
        this.setLayout(new BorderLayout());
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        
        this.setResizable(false);
        JPanel NorthPanelPanel = new JPanel(new BorderLayout());
        NorthPanelPanel.add(Box.createRigidArea(new Dimension(0, 5)), "North");
        final JPanel InfoPanel = new JPanel();
        InfoPanel.setLayout(new BoxLayout(InfoPanel, 3));
        this.nbVoxelXLabel = new JLabel("Nb. of voxels in X: " + image.getWidth());
        this.nbVoxelYLabel = new JLabel("Nb. of voxels in Y: " + image.getHeight());

        this.nbGreyLevel = new JLabel("Nb. of grey levels: " + this.scan.getShadesOfGray().size());
        InfoPanel.add(new JLabel("<html><u>Information</u></html>"));
        InfoPanel.add(this.nbVoxelXLabel);
        InfoPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        InfoPanel.add(this.nbVoxelYLabel);
        InfoPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        InfoPanel.add(this.nbGreyLevel);
        InfoPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        NorthPanelPanel.add(InfoPanel, "West");
        final JPanel CenterPanel = new JPanel();
        CenterPanel.setLayout(new BoxLayout(CenterPanel, 2));
        CenterPanel.add(Box.createRigidArea(new Dimension(5, 0)));
        final JPanel MatPanel = new JPanel(new BorderLayout());
        this.listModelMaterial = new DefaultListModel<Material>();
        for (int i = 0; i < this.listMaterial.size(); ++i) {
            this.listModelMaterial.insertElementAt(this.listMaterial.get(i), i);
        }
        (this.JListMaterial = new JList<Material>(this.listModelMaterial)).setSelectedIndex(0);
        
        MatPanel.add(new JLabel("List of materials"), "North");
        MatPanel.add(new JScrollPane(this.JListMaterial, 20, 30), "Center");
        final JPanel butPan = new JPanel(new BorderLayout());
        final JButton addEmptyMatButton = new JButton("Add empty material");
        addEmptyMatButton.setActionCommand("Add empty");
        addEmptyMatButton.addActionListener(this);
        butPan.add(addEmptyMatButton);
        butPan.add(Box.createRigidArea(new Dimension(0, 10)), "South");
        MatPanel.add(addEmptyMatButton, "South");
        CenterPanel.add(MatPanel);
        final JPanel RadAndButPanel = new JPanel();
        RadAndButPanel.setPreferredSize(new Dimension(300, 200));
        this.RadField = new ExtensibleFormattedPanel(new JLabel(""),"U","Th","K", false);

        this.RadField.addFields("Ud", false);
        RadAndButPanel.add(Box.createRigidArea(new Dimension(0,50)));
        RadAndButPanel.add(new JLabel("Change reference values"));
        RadAndButPanel.add(this.RadField);
        final JButton AssocButton = new JButton("<html>Associate material<br>with grey level</html>");
        AssocButton.setActionCommand("Associate");
        AssocButton.setAlignmentX(0.5f);
        AssocButton.addActionListener(this);
        RadAndButPanel.add(AssocButton);
        CenterPanel.add(RadAndButPanel);
        NorthPanelPanel.add(CenterPanel, "Center");
        final JPanel EastPanel = new JPanel(new BorderLayout());
        this.listModelGreyLevels = new DefaultListModel<>();
        final Iterator<Integer> it = this.scan.getShadesOfGray().iterator();
        int j = 0;
        while (it.hasNext()) {
            this.listModelGreyLevels.insertElementAt(it.next(), j++);
        }
        (this.JListGreyLevels = new JList<>(this.listModelGreyLevels)).setSelectedIndex(0);
        EastPanel.add(new JLabel("<html>grey<br>levels         Associations"), "North");
        EastPanel.add(new JScrollPane(this.JListGreyLevels, 20, 30), "West");
        this.listModelAssoc = new DefaultListModel<>();
        (this.JListAssociations = new JList<>(this.listModelAssoc)).setSelectedIndex(0);
        EastPanel.add(new JScrollPane(this.JListAssociations, 20, 30), "Center");
        final JButton ClearAssocButton = new JButton("Clear the selected association");
        ClearAssocButton.setActionCommand("Clear association");
        ClearAssocButton.setAlignmentX(0.5f);
        ClearAssocButton.addActionListener(this);
        EastPanel.add(ClearAssocButton, "South");
        NorthPanelPanel.add(EastPanel, "East");
        final JPanel EndPanel = new JPanel();
        final JButton validateAllButton = new JButton("Validate All");
        validateAllButton.addActionListener(e -> {
            if (Association2dFrame.this.checkAssociations()) {
                Association2dFrame.this.valid = true;
                Association2dFrame.this.dispose();
            }
            else {
                JOptionPane.showMessageDialog(null, "Associations missing");
            }
        });
        final JButton CancelButton = new JButton("Cancel");
        CancelButton.addActionListener(e -> Association2dFrame.this.dispose());
        EndPanel.setBorder(BorderFactory.createRaisedBevelBorder());
        EndPanel.setBackground(Color.darkGray);
        EndPanel.add(CancelButton);
        EndPanel.add(validateAllButton);
        NorthPanelPanel.add(EndPanel, "South");
        
        this.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0; 
        gbc.gridy = 0; 
        gbc.gridwidth = GridBagConstraints.REMAINDER; 
        gbc.gridheight = 1;
        this.add(NorthPanelPanel,gbc);
        gbc.gridy = 1;
        this.add(this.image,gbc);
    }
    
    /**
     * Triggered when an action is performed by the user like
     * clicking on a button.
     */
    @Override
    public void actionPerformed(final ActionEvent e) {
        if (e.getActionCommand().equals("Associate")) {
            float u = 0.0f;
            float th = 0.0f;
            float k = 0.0f;
            float ud = 0.0f;
            try {
                u = this.RadField.getValue(TripleFormattedField.XYZ.X);
                th = this.RadField.getValue(TripleFormattedField.XYZ.Y);
                k = this.RadField.getValue(TripleFormattedField.XYZ.Z);
                ud = Float.parseFloat(this.RadField.getFieldFromList(0).getText().replaceAll("\\s+", ""));
            }
            catch (Exception ex) {}
            final Material m = this.JListMaterial.getSelectedValue();
            if (m == null) {
                return;
            }
            m.setCurrentUraniumValue(u);
            m.setCurrentThoriumValue(th);
            m.setCurrentPotassiumValue(k);
            m.setCurrentUserDefinedValue(ud);
            m.setRefUraniumValue(u);
            m.setRefThoriumValue(th);
            m.setRefPotassiumValue(k);
            m.setRefUserDefinedValue(ud);
            this.listModelAssoc.addElement(new Couple<>(m, this.JListGreyLevels.getSelectedValue()));
        }
        if (e.getActionCommand().equals("Clear association") && !this.JListAssociations.isSelectionEmpty()) {
            this.listModelAssoc.removeElement(this.JListAssociations.getSelectedValue());
        }
        if (e.getActionCommand().equals("Add empty")) {
            final Material i = new ColoredMaterial("Unknown_"+this.listMaterial.size(),0f,this.listMaterial.size());
            this.listMaterial.add(i);
            this.listModelMaterial.addElement(i);
        }
    }
    
    /**
     * Check if associations are correct.
     * @return true if all grey levels has been associated with a material, false if not.
     */
    private boolean checkAssociations() {
        for (int i = 0; i < this.listModelGreyLevels.getSize(); ++i) {
            final int shades = this.listModelGreyLevels.get(i);
            boolean foundIt = false;
            for (int j = 0; j < this.listModelAssoc.getSize(); ++j) {
                if (shades == this.listModelAssoc.get(j).getValeur2()) {
                    foundIt = true;
                    break;
                }
            }
            if (!foundIt) {
                return false;
            }
        }
        return true;
    }
    
    /**
     * Get the list of associations written as couple.
     * @return the list of associations.
     * @see Couple
     */
    public ArrayList<Couple<Material, Integer>> getListAssociations() {
        final ArrayList<Couple<Material, Integer>> assocTab = new ArrayList<Couple<Material, Integer>>();
        for (int i = 0; i < this.listModelAssoc.getSize(); ++i) {
            assocTab.add(this.listModelAssoc.get(i));
        }
        return assocTab;
    }
    
    /**
     * Check if association is valid.
     * @return true if associations is valid, false if not.
     */
    public boolean isAssocValid()
    {
    	return valid;
    }

	@Override
	public void valueChanged(ListSelectionEvent e)
	{
		this.image.setHightlight(this.JListGreyLevels.getSelectedValue());
	}
}
