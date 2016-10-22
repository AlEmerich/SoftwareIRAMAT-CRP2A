package interfaceObject;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import mainPackage.Loader3DScan;
import mainPackage.Material;
import util.Couple;

/**
 * This class generate a JDialog frame when the user want to load a 3D-scan. 
 * It provides interface to associate grey scale loaded with material of the project.
 * @author alan
 *
 */
public class AssociationFrame extends JDialog implements ActionListener
{
    private static final long serialVersionUID = -9171267089811510393L;
    private Loader3DScan loader;
    private List<Material> listMaterial;
    private JLabel nbVoxelXLabel;
    private JLabel nbVoxelYLabel;
    private JLabel nbImages;
    private JLabel nbGreyLevel;
    private JList<Material> JListMaterial;
    private DefaultListModel<Material> listModelMaterial;
    private TripleFormattedPanel RadField;
    private JList<Integer> JListGreyLevels;
    private DefaultListModel<Integer> listModelGreyLevels;
    private JList<Couple<Material, Integer>> JListAssociations;
    private DefaultListModel<Couple<Material, Integer>> listModelAssoc;
    
    /**
     * Constructor. Call the super constructor JDialog, keep in fields the list material and the loader, init and pack.
     * @param parent the parent JFrame.
     * @param title the title of the window.
     * @param modal true if the window maintains focus, false if not.
     * @param loader  The load which have read the file once to know what and how many grey levels there are.
     * @param listM list of available material.
     * @see JDialog
     */
    public AssociationFrame(final JFrame parent, final String title, final boolean modal, final Loader3DScan loader, final List<Material> listM) {
        super(parent, title, modal);
        this.loader = loader;
        this.listMaterial = listM;
        this.initComponents();
        this.pack();
    }
    
    /**
     * Initialization of all components.
     */
    private void initComponents() {
        this.setLayout(new BorderLayout());
        this.setDefaultCloseOperation(0);
        this.setSize(new Dimension(500, 200));
        this.setResizable(false);
        this.add(Box.createRigidArea(new Dimension(0, 5)), "North");
        final JPanel InfoPanel = new JPanel();
        InfoPanel.setLayout(new BoxLayout(InfoPanel, 3));
        this.nbVoxelXLabel = new JLabel("Nb. of voxels in X: " + this.loader.getFirstSizeX());
        this.nbVoxelYLabel = new JLabel("Nb. of voxels in Y: " + this.loader.getFirstSizeY());
        this.nbImages = new JLabel("Nb. of images: " + this.loader.getFirstSizeZ());
        this.nbGreyLevel = new JLabel("Nb. of grey levels: " + this.loader.getShadesOfGray().size());
        InfoPanel.add(new JLabel("<html><u>Information</u></html>"));
        InfoPanel.add(this.nbVoxelXLabel);
        InfoPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        InfoPanel.add(this.nbVoxelYLabel);
        InfoPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        InfoPanel.add(this.nbImages);
        InfoPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        InfoPanel.add(this.nbGreyLevel);
        InfoPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        this.add(InfoPanel, "West");
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
        RadAndButPanel.setPreferredSize(new Dimension(200, 200));
        RadAndButPanel.add(this.RadField = new TripleFormattedPanel(null, new JLabel(""), "U", "Th", "K", false));
        final JButton AssocButton = new JButton("<html>Associate material<br>with grey level</html>");
        AssocButton.setActionCommand("Associate");
        AssocButton.setAlignmentX(0.5f);
        AssocButton.addActionListener(this);
        RadAndButPanel.add(AssocButton);
        CenterPanel.add(RadAndButPanel);
        this.add(CenterPanel, "Center");
        final JPanel EastPanel = new JPanel(new BorderLayout());
        this.listModelGreyLevels = new DefaultListModel<Integer>();
        final Iterator<Integer> it = this.loader.getShadesOfGray().iterator();
        int j = 0;
        while (it.hasNext()) {
            this.listModelGreyLevels.insertElementAt(it.next(), j++);
        }
        (this.JListGreyLevels = new JList<Integer>(this.listModelGreyLevels)).setSelectedIndex(0);
        EastPanel.add(new JLabel("<html>grey<br>levels         Associations"), "North");
        EastPanel.add(new JScrollPane(this.JListGreyLevels, 20, 30), "West");
        this.listModelAssoc = new DefaultListModel<Couple<Material, Integer>>();
        (this.JListAssociations = new JList<Couple<Material, Integer>>(this.listModelAssoc)).setSelectedIndex(0);
        EastPanel.add(new JScrollPane(this.JListAssociations, 20, 30), "Center");
        final JButton ClearAssocButton = new JButton("Clear the selected association");
        ClearAssocButton.setActionCommand("Clear association");
        ClearAssocButton.setAlignmentX(0.5f);
        ClearAssocButton.addActionListener(this);
        EastPanel.add(ClearAssocButton, "South");
        this.add(EastPanel, "East");
        final JPanel EndPanel = new JPanel();
        final JButton validateAllButton = new JButton("Validate All");
        validateAllButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                if (AssociationFrame.this.checkAssociations()) {
                    AssociationFrame.this.loader.setValid(true);
                    AssociationFrame.this.dispose();
                }
                else {
                    JOptionPane.showMessageDialog(null, "Associations missing");
                }
            }
        });
        final JButton CancelButton = new JButton("Cancel");
        CancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                AssociationFrame.this.dispose();
            }
        });
        EndPanel.setBorder(BorderFactory.createRaisedBevelBorder());
        EndPanel.setBackground(Color.darkGray);
        EndPanel.add(CancelButton);
        EndPanel.add(validateAllButton);
        this.add(EndPanel, "South");
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
            try {
                u = this.RadField.getValue(TripleFormattedPanel.XYZ.X);
                th = this.RadField.getValue(TripleFormattedPanel.XYZ.Y);
                k = this.RadField.getValue(TripleFormattedPanel.XYZ.Z);
            }
            catch (Exception ex) {}
            final Material m = this.JListMaterial.getSelectedValue();
            if (m == null) {
                return;
            }
            m.setCurrentUraniumValue(u);
            m.setCurrentThoriumValue(th);
            m.setCurrentPotassiumValue(k);
            m.setCurrentUserDefinedValue(u);
            m.setRefUraniumValue(u);
            m.setRefThoriumValue(th);
            m.setRefPotassiumValue(k);
            m.setRefUserDefinedValue(u);
            this.listModelAssoc.addElement(new Couple<Material, Integer>(m, this.JListGreyLevels.getSelectedValue()));
        }
        if (e.getActionCommand().equals("Clear association") && !this.JListAssociations.isSelectionEmpty()) {
            this.listModelAssoc.removeElement(this.JListAssociations.getSelectedValue());
        }
        if (e.getActionCommand().equals("Add empty")) {
            final Material i = new Material(this.listMaterial.size());
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
}
