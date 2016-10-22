package iramat.dosiseed.view;

import interfaceObject.Grid2D;
import iramat.dosiseed.model.Material;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;


/**
 * Class which open a little window when the user want to duplicate a layer of the object.
 * @author alan
 */
public class MaterialChooser extends JDialog implements ActionListener
{
    private static final long serialVersionUID = 487799582557153787L;
    private JList<Material> listLayer;
    private DefaultListModel<Material> listModel;
	private boolean isCanceled;
    
    /**
     * Constructor. Call the super constructor JDialog, keep in fields the parent, the current Layer and the number, and init.
     * @param parent send to super constructor.
     * @param title send to super constructor.
     * @param modal send to super constructor.
     * @param list the list of material.
     * @see Grid2D
     */
    public MaterialChooser(final JFrame parent, final String title, final boolean modal, List<Material> list) {
        super(parent, title, modal);
        this.setSize(new Dimension(500, 200));
        this.setLocationRelativeTo(parent);
        this.setResizable(false);
        this.listModel = new DefaultListModel<Material>();
        int index = 0;
        for (Material m : list) {
            this.listModel.insertElementAt(m, index++);
        }
        (this.listLayer = new JList<Material>(this.listModel)).setSelectedIndex(0);
        this.listLayer.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        final JScrollPane CompScroll = new JScrollPane(this.listLayer, 20, 30);
        CompScroll.setPreferredSize(new Dimension(150, 200));
        final JButton OkButton = new JButton("OK");
        OkButton.setActionCommand("OK");
        OkButton.addActionListener(this);
        this.getContentPane().setLayout(new BorderLayout());
        this.add(CompScroll, "Center");
        this.add(OkButton, "South");
        
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(final WindowEvent e) {
                MaterialChooser.this.actionPerformed(new ActionEvent(this,0,"Quit"));
            }
        });
    }
    
    /**
     * a list of the index of layers the user want to modify with the current one.
     * @return the list of all selected layer.
     */
    public Material getSelectedMaterial() {
        return this.listLayer.getSelectedValue();
    }
    
    public boolean isCanceled()
    {
    	return this.isCanceled;
    }
    /**
     * Triggered when an action is performed.
     */
    @Override
    public void actionPerformed(final ActionEvent e) {
        if (e.getActionCommand().equals("OK")) {
            this.setVisible(false);
        }
        else if(e.getActionCommand().equals("Quit"))
        {
        	this.isCanceled = true;
        	
        }
    }
}
