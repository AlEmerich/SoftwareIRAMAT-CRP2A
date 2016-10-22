package iramat.dosiedit2d.view;

import iramat.dosiseed.model.ColoredMaterial;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

import javax.swing.JColorChooser;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.JTabbedPane;
import javax.swing.filechooser.FileNameExtensionFilter;


/**
 * Pop menu displayed when a right clic is performs on an item of the list material in the EditorPane, 
 * providing a quick change tab to the forge of that material, changing color menu and changing texture menu.
 * @author alan
 */
public class MaterialPopupMenu extends JPopupMenu implements ActionListener
{
    private static final long serialVersionUID = -2311466976867532041L;
    
    /**
     * The material the user is about to change.
     */
    private ColoredMaterial material;
    
    /**
     * Viewers 2D and 3D.
     */
    private Grid2D grid;
    
    /**
     * Listeners.
     */
    private JTabbedPane forgeListener;

	private JComponent listToRepaint;
    
    /**
     * The choosers of files and colors.
     */
    private static final JColorChooser ColorChooser;
    private static final JFileChooser TextureChooser;
    
    static {
        ColorChooser = new JColorChooser();
        TextureChooser = new JFileChooser();
    }
    
    /**
     * Constructor. Call the super default constructor, save fields and initialize the Swing components.
     * @param listToRepaint the list repainted after the update.
     * @param listener listened to set.
     * @param grid the editable grid. 
     * @param m the material in question.
     * @throws NullPointerException
     */
    public MaterialPopupMenu(JComponent listToRepaint,final TabManager listener, final Grid2D grid, final ColoredMaterial m) throws NullPointerException {
        this.forgeListener = listener;
        this.material = m;
        this.grid = grid;
        final JMenuItem modifyMenu = new JMenuItem("Modify " + m.getName() + "...");
        final JMenuItem applyColorMenu = new JMenuItem("Apply new color...");
        final JMenuItem applyTextureMenu = new JMenuItem("Apply new texture...");
        this.add(modifyMenu);
        modifyMenu.setActionCommand("modify material");
        this.add(applyColorMenu);
        applyColorMenu.setActionCommand("apply color");
        this.add(applyTextureMenu);
        applyTextureMenu.setActionCommand("apply texture");
        modifyMenu.addActionListener(this);
        applyColorMenu.addActionListener(this);
        applyTextureMenu.addActionListener(this);
        MaterialPopupMenu.ColorChooser.setColor(this.material.getColor());
        MaterialPopupMenu.ColorChooser.setLocation(listener.getLocation());
        MaterialPopupMenu.TextureChooser.setLocation(listener.getLocation());
        this.listToRepaint = listToRepaint;
    }
    
    @Override
    public void actionPerformed(final ActionEvent e) {
        if (e.getActionCommand().equals("modify material")) {
            ((TabManager)this.forgeListener).goToForgeMaterial(this.material);
        }
        if (e.getActionCommand().equals("apply color")) {
            JColorChooser.createDialog(this, this.material.toString(), true, MaterialPopupMenu.ColorChooser, null, null).setVisible(true);
            if (MaterialPopupMenu.ColorChooser.getColor() != null) {
                this.material.setColor(MaterialPopupMenu.ColorChooser.getColor());
                this.listToRepaint.repaint();
                this.grid.repaint();
            }
            else {
                System.err.println("Error with the color chooser: null pointer received.");
            }
        }
        if (e.getActionCommand().equals("apply texture")) {
            final FileNameExtensionFilter filter = new FileNameExtensionFilter(null, new String[] { "png", "PNG", "jpg", "jpeg" });
            MaterialPopupMenu.TextureChooser.setFileFilter(filter);
            final int returnVal = MaterialPopupMenu.TextureChooser.showOpenDialog(this);
            if (returnVal == 0) {
                final File file = MaterialPopupMenu.TextureChooser.getSelectedFile();
                if (!file.getName().endsWith(".png") && !file.getName().endsWith(".PNG") && !file.getName().endsWith(".jpg")) {
                    if (!file.getName().endsWith(".jpeg")) {
                        JOptionPane.showMessageDialog(null, "Wrong type file. Please load an image file if you want to texturing.", "Wrong file type", 0);
                        return;
                    }
                }
                try {
                    this.material.loadTexture(file);
                    this.listToRepaint.repaint();
                }
                catch (IOException e2) {
                    JOptionPane.showMessageDialog(null, "An error occurs during the reading of the file.", "IOException", 0);
                }
            }
        }
    }
}
