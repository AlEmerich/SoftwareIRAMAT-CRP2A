package interfaceObject;

import java.awt.BorderLayout;

import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import mainPackage.Material;
import mainPackage.Scene;
import mainPackage.TopLevelInterface;
import mainPackage.VoxelizedObject;

import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.util.Animator;

/**
 * Class managing all the tab of the interface.
 * @author alan
 * @see ChangeListener
 * @see InternalVoxListener
 * @see ManuallyChangeListener
 * @see MainInfoVoxel
 * @see ForgeWindow
 * @see EditorPane
 */
public class OngletManagement extends JTabbedPane implements ChangeListener, InternalVoxListener, ManuallyChangeListener
{
    private static final long serialVersionUID = -7912433881201092484L;
    private TopLevelInterface father;
    private MainInfoVoxel mainWindow;
    private ForgeWindow forgeWindow;
    private EditorPane editExternalWindow;
    private EditorPane editInternalWindow;
    
    /**
     * Tab icons.
     */
    private ImageIcon imgHome;
    private ImageIcon imgForge;
    private ImageIcon imgEditor;
    
    /**
     * Animator of the 3D scene.
     */
    private Animator animator;
    
    /**
     * Constructor. Call the super default constructor and save fields.
     * @param father the TopLevelInterface.
     * @param imgH icon of home tab.
     * @param imgF icon of forge tab. 
     * @param imgE icon of editor tab.
     */
    public OngletManagement(final TopLevelInterface father, final ImageIcon imgH, final ImageIcon imgF, final ImageIcon imgE) {
        this.editInternalWindow = null;
        this.father = father;
        this.imgHome = imgH;
        this.imgForge = imgF;
        this.imgEditor = imgE;
    }
    
    /**
     * Add the tabs passed in parameters to the tab manager.
     * @param main the home tab.
     * @param f the forge tab.
     * @param edit the editor tab.
     */
    public void add(final MainInfoVoxel main, final ForgeWindow f, final EditorPane edit) {
        this.mainWindow = main;
        this.forgeWindow = f;
        this.editExternalWindow = edit;
        JPanel dummyPanel = new JPanel(new BorderLayout());
        dummyPanel.add(main,BorderLayout.NORTH);
        this.addTab("Global Information", this.imgHome, new JScrollPane(dummyPanel));
        this.addTab("Forge", this.imgForge, new JScrollPane(f));
        this.addTab("External", this.imgEditor, edit);
        if (this.father.getVoxSample().getInternalVoxelizedObject() != null) {
            this.createWorld();
        }
        this.forgeWindow.addmaterialAddedListener(this.mainWindow);
        this.mainWindow.addInternalVoxListener(this);
        this.validate();
    }
    
    /**
     * Do the linking with itself and with the external EditorPane.
     */
    public void doLinking() {
        this.addChangeListener(this);
        this.editExternalWindow.addManuallyChangeListener(this);
    }
    
    @Override
    public void stateChanged(final ChangeEvent arg0) {
        if (this.father.getVoxSample().getInternalVoxelizedObject() == null && this.getTabCount() == 4) {
            this.remove(3);
        }
        this.forgeWindow.update();
        this.editExternalWindow.updateYourself();
        if (this.editInternalWindow != null) {
            this.editInternalWindow.updateYourself();
        }
    }
    
    /**
     * This override method is set to check if the tab is in a valid state before changing it and if not, the tab keep the focus.
     */
    @Override
    public void setSelectedIndex(final int index) {
        this.mainWindow.updateFrameComponent();
        this.forgeWindow.update();
        if (index != 1 && !this.forgeWindow.IsValidTab()) {
            super.setSelectedIndex(1);
            return;
        }
        super.setSelectedIndex(index);
        
        if (this.mainWindow.checkIntegrityofProject()) {
        	if(index >= this.getTabCount())
        		super.setSelectedIndex(index - 1);
        	else
        		super.setSelectedIndex(index);
            return;
        }
        super.setSelectedIndex(0);
    }
    
    @Override
    public void createWorld() {
        final VoxelizedObject vox = this.father.getVoxSample().getInternalVoxelizedObject();
        final Scene Scene = new Scene(vox, this.getPreferredSize());
        this.animator = new Animator((GLAutoDrawable)Scene);
        if (this.editInternalWindow != null) {
            final int option = JOptionPane.showConfirmDialog(null, "You are about to erase the current internal world with a blank one. Still want to do it ?", "Confirm your will.", 1, JOptionPane.OK_CANCEL_OPTION);
            if (option == JOptionPane.CANCEL_OPTION) {
                return;
            }
            this.remove(3);
            this.editInternalWindow = null;
        }
        this.editInternalWindow = new EditorPane(this.father, Scene);
        this.addTab("Internal", this.imgEditor, this.editInternalWindow);
        this.animator.start();
    }
    
    @Override
    public void goToForgeMaterial(final Material m) {
        this.setSelectedIndex(1);
        this.forgeWindow.setCurrentMaterial(m);
    }
    
    /**
     * @return the animator of the internal 3D scene.
     */
    public Animator getAnimatorInternal() {
        return this.animator;
    }
    
    /**
     * Set the sub-voxelized probe to the project.
     */
    public void setSubVoxelizedDetector() {
        this.mainWindow.setSubVoxelizedProbe();
    }
    
    /**
     * Check the validity of the informations present in the object.
     * @return true if informations of project is coherent, false if not.
     */
    public boolean checkIntegrityOfTabs() {
        return this.mainWindow.checkIntegrityofProject() && this.forgeWindow.IsValidTab();
    }
}
