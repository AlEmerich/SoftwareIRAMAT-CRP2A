package interfaceObject;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JScrollPane;

/**
 * Class which open a little window when the user want to duplicate a layer of the object.
 * @author alan
 */
public class DuplicateWindow extends JDialog implements ActionListener
{
    private static final long serialVersionUID = 487799582557153787L;
    private JList<Integer> listLayer;
    private DefaultListModel<Integer> listModel;
    
    /**
     * Constructor. Call the super constructor JDialog, keep in fields the parent, the current Layer and the number, and init.
     * @param parent send to super constructor.
     * @param title send to super constructor.
     * @param modal send to super constructor.
     * @param currentLayer in Grid2D
     * @param nbLayer number of layer in the object
     * @see Grid2D
     */
    public DuplicateWindow(final JFrame parent, final String title, final boolean modal, final int currentLayer, final int nbLayer) {
        super(parent, title, modal);
        this.setSize(new Dimension(150, 200));
        this.setLocationRelativeTo(null);
        this.setResizable(false);
        this.listModel = new DefaultListModel<Integer>();
        int index = 0;
        for (int i = 0; i < nbLayer; ++i) {
            if (i != currentLayer) {
                this.listModel.insertElementAt(i, index++);
            }
        }
        (this.listLayer = new JList<Integer>(this.listModel)).setSelectedIndex(0);
        this.listLayer.setSelectionMode(2);
        final JScrollPane CompScroll = new JScrollPane(this.listLayer, 20, 30);
        CompScroll.setPreferredSize(new Dimension(150, 200));
        final JButton OkButton = new JButton("Duplicate");
        OkButton.setActionCommand("OK");
        OkButton.addActionListener(this);
        this.getContentPane().setLayout(new BorderLayout());
        this.add(CompScroll, "Center");
        this.add(OkButton, "South");
    }
    
    /**
     * a list of the index of layers the user want to modify with the current one.
     * @return the list of all selected layer.
     */
    public List<Integer> getSelectedLayer() {
        return this.listLayer.getSelectedValuesList();
    }
    
    /**
     * Triggered when an action is performed.
     */
    @Override
    public void actionPerformed(final ActionEvent e) {
        if (e.getActionCommand().equals("OK")) {
            this.setVisible(false);
        }
    }
}
