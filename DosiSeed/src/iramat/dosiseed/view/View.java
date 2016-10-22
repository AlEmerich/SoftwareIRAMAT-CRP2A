package iramat.dosiseed.view;

import iramat.dosiseed.controler.ForgeControler;
import iramat.dosiseed.controler.GlobalControler;
import iramat.dosiseed.model.AbstractModel;
import iramat.dosiseed.model.Incoherence.Inc;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.Observer;

import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JEditorPane;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.filechooser.FileNameExtensionFilter;

import mainPackage.TopLevelInterface;

public class View extends JFrame
{   
	/**
	 * 
	 */
	private static final long serialVersionUID = -4120981469295981812L;
	
	/**
	 * The file chooser.
	 */
	private JFileChooser FileChooser;
	
	/**
	 * The controller of the MVC pattern.
	 */
	private GlobalControler controler;
	
	/**
	 * The object handling tabs.
	 */
	private TabManager tabManager;
	
	/**
	 * Constructor of a view.
	 * @param controler the controller of the MVC pattern.
	 */
	public View(final ForgeControler forgeControler,
			final GlobalControler controler,final AbstractModel model)
	{
		this.controler = controler;	
		this.FileChooser = new JFileChooser();
		
		this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(final WindowEvent e) {
                View.this.controler.actionPerformed(new ActionEvent(this,0,"Quit"));
            }
        });
		
		initGUI(forgeControler,model);
		this.setTitle("DosiSeed - Editing Pilot Text File (PTF) for DosiSeed Software ("+model.getCURRENT_PATH()+")");
        this.setDefaultCloseOperation(0);
        this.setLocationRelativeTo(null);
		this.setJMenuBar(new TopMenu(controler,true));
		this.setMinimumSize(new Dimension(600, 400));
        this.validate();
        this.pack();
        this.setVisible(true);
        model.notifyObservers();
        
	}
	
	public void initGUI(final ForgeControler forgeControler, final AbstractModel model)
	{
		tabManager = new TabManager(forgeControler, controler,model,
				createImageIcon("/resources/home.png"),createImageIcon("/resources/forge.jpeg"));
		this.add(tabManager);
		
	}
	
	public Observer getGlobalObserver()
	{
		return tabManager.getHighGlobal().getGlobalPanel();
	}
	
	public Observer getCoarseFractionObserver()
	{
		return tabManager.getHighGlobal().getCoarseFractionPanel();
	}
	
	public Observer getFineFractionObserver()
	{
		return tabManager.getHighGlobal().getFineFractionPanel();
	}
	
	/*************************************************************************
	 * 						USUAL METHODS 
	 *************************************************************************/
    
    /**
     * Static method which shows a YES or NO window.
     * @param theMessage displayed by the window.
     * @return the result the user wants.
     */
    public static int yesnocancel(final Component parent,final String theMessage) {
        final int result = JOptionPane.showConfirmDialog(parent, theMessage,
        		"Alert", JOptionPane.YES_NO_CANCEL_OPTION);
        return result;
    }
    
    public static void pleaseCreateMaterial(final Component parent)
    {
    	JOptionPane.showMessageDialog(parent,
				"Cannot add grain, the are no available material. Please create a new one.");
    }
    
    public static void doNotUseSameMaterial(final Component parent)
    {
    	JOptionPane.showMessageDialog(parent, 
    			"The same matrial cannot be used twice");
    }
    
    /**
     * Returns an ImageIcon, or null if the path was invalid.
     * @param path the path to the file.
     * @return the image loaded.
     */
    public static ImageIcon createImageIcon(final String path) {
        final URL imgURL = TopLevelInterface.class.getResource(path);
        if (imgURL != null) {
            return new ImageIcon(imgURL);
        }
        System.err.println("Couldn't find file: " + path);
        return null;
    }
    
    public static void showIncoherenceWindow(Component parent,Inc type,String message)
    {
    	JOptionPane.showMessageDialog(parent, message,type.getDescription().toUpperCase(),JOptionPane.ERROR_MESSAGE);
    }
    
    public String showSavingWindow()
    {
    	final int returnVal2 = this.FileChooser.showSaveDialog(this);
        if (returnVal2 == 0) {
            String nameFile = this.FileChooser.getSelectedFile().getAbsolutePath();
            if (!nameFile.endsWith(".cf")) 
            	nameFile += ".cf";
            
            return nameFile;
        }
        return "CLOSED";
    }
    
    public String showGenerateWindow()
    {
    	final int returnVal2 = this.FileChooser.showSaveDialog(this);
        if (returnVal2 == 0) {
            String nameFile = this.FileChooser.getSelectedFile().getAbsolutePath();
            if (!nameFile.endsWith(".txt")) 
            	nameFile += ".txt";
            
            return nameFile;
        }
        return "";
    }
    
    public String showLoadingWindow()
    {
    	final FileNameExtensionFilter filter = new FileNameExtensionFilter(null, new String[] { "cf", "CF" });
        this.FileChooser.setFileFilter(filter);
        
        final int returnVal = this.FileChooser.showOpenDialog(this);
        if (returnVal == 0)
            return this.FileChooser.getSelectedFile().getAbsolutePath();
      
    	return "";
    }
    
    public void openPTFFile(File file)
    {
    	JFrame tabbedWindow = new JFrame("PTF file");
		JTabbedPane tabs = new JTabbedPane();
		
		JTextArea textarea = new JTextArea();
		try
		{
			textarea.read(new FileReader(file.getAbsolutePath()),null);
			textarea.setEditable(false);
			tabs.addTab(file.getName(), new JScrollPane(textarea));
			
			tabbedWindow.add(tabs);
			tabbedWindow.setMinimumSize(new Dimension(600,400));
			tabbedWindow.setLocationRelativeTo(this);
			tabbedWindow.setVisible(true);
		} catch (FileNotFoundException e)
		{
			e.printStackTrace();
			JOptionPane.showMessageDialog(this, "File not found.");
		} catch (IOException e)
		{
			e.printStackTrace();
		}	
    }
    
    public void Help() {
        final JDialog JDWindow = new JDialog(this, "About...", true);
        JDWindow.setSize(new Dimension(800, 400));
        JDWindow.setResizable(false);
        JDWindow.setLocationRelativeTo(null);
        final JEditorPane editorPane = new JEditorPane();

        editorPane.setContentType("text/html");
        editorPane.setEditable(false);
        final URL helpURL = this.getClass().getResource("/resources/About.html");
        
        if (helpURL != null) {
            try {
                editorPane.setPage(helpURL);
            }
            catch (IOException e) {
                System.err.println("Attempted to read a bad URL: " + helpURL);
            }
        }
        else {
            System.err.println("Couldn't find file: About.html");
        }
        final JScrollPane editorScrollPane = new JScrollPane(editorPane);
        editorScrollPane.setVerticalScrollBarPolicy(22);
        JDWindow.add(editorScrollPane);
        JDWindow.setVisible(true);
    }

	public int getSelectedType()
	{
		return this.tabManager.getSelectedType();
	}
}
