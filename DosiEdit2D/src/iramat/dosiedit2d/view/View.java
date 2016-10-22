package iramat.dosiedit2d.view;

import iramat.dosiedit2d.controler.GlobalControler;
import iramat.dosiseed.controler.ForgeControler;
import iramat.dosiseed.model.AbstractModel;
import iramat.dosiseed.model.Incoherence;
import iramat.dosiseed.model.Incoherence.Inc;
import iramat.dosiseed.view.TopMenu;

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
import java.util.Arrays;
import java.util.Observable;
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

/**
 * The global view of the software. Handles tab manager and some static view window.
 * @author alan
 *
 */
public class View extends JFrame implements Observer
{   
	/**
	 * 
	 */
	private static final long serialVersionUID = -4120981469295981812L;
	
	/**
	 * The file extension of the serialized model.
	 */
	private static final String FILE_EXTENSION = ".ddvi";
	
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
		this.setTitle("DosiEdit2D - Editing Pilot Text File (PTF) for DosiVox2D Software ("+model.getCURRENT_PATH()+")");
        this.setDefaultCloseOperation(0);
        this.setLocationByPlatform(true);
		this.setJMenuBar(new TopMenu(controler,false));
		this.setMinimumSize(new Dimension(600, 400));
		this.setPreferredSize(new Dimension(1200,900));
        this.validate();
        this.pack();
        this.setVisible(true);
        model.notifyObservers();
        
	}
	
	/**
	 * Initializes the tab manager.
	 * @param forgeControler the controller of the forge of material and component.
	 * @param model the model to work with.
	 */
	public void initGUI(final ForgeControler forgeControler, final AbstractModel model)
	{
		tabManager = new TabManager(forgeControler, controler,model,
				createImageIcon("/resources/home.png"),createImageIcon("/resources/forge.jpeg"));
		this.add(tabManager);
	}
	
	/*************************************************************************
	 * 						USUAL METHODS 
	 *************************************************************************/
    
	public void setLoadTitle(String title)
	{
		this.tabManager.getEditorPane().setLoadTitle(title);
	}
	
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
    
    /**
     * Show error message which said there is no available material.
     * @param parent where to show the error message.
     */
    public static void pleaseCreateMaterial(final Component parent)
    {
    	JOptionPane.showMessageDialog(parent,
				"Cannot add grain, there are no available material. Please create a new one.");
    }
    
    /**
     * Show error message which said the same material cannot be used twice.
     * @param parent where to show the error message.
     */
    public static void doNotUseSameMaterial(final Component parent)
    {
    	JOptionPane.showMessageDialog(parent, 
    			"The same material cannot be used twice");
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
    
    /**
     * Show error message when the model is checked for validation.
     * @param parent where to show the error message.
     * @param type the type of the incoherence
     * @param message the message to show.
     * @see Incoherence
     */
    public static void showIncoherenceWindow(Component parent,Inc type,String message)
    {
    	JOptionPane.showMessageDialog(parent, message,type.getDescription().toUpperCase(),JOptionPane.ERROR_MESSAGE);
    }
    
    /**
     * Show {@link JFileChooser} to let the user choose where to saved the project.
     * @return the chosen path.
     */
    public String showSavingWindow()
    {
    	final int returnVal2 = this.FileChooser.showSaveDialog(this);
        if (returnVal2 == 0) {
            String nameFile = this.FileChooser.getSelectedFile().getAbsolutePath();
            if (!nameFile.endsWith(FILE_EXTENSION)) 
            	nameFile += FILE_EXTENSION;
            
            return nameFile;
        }
        return "CLOSED";
    }
    
    /**
     * Show {@link JFileChooser} to let the user choose where to generates the pilot text file.
     * @return the chosen path.
     */
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
    
    /**
     * Show {@link JFileChooser} to let the user choose where is the project to load.
     * @return the chosen path.
     */
    public String showLoadingWindow()
    {
    	String file_extension_number = FILE_EXTENSION.substring(1);
    	final FileNameExtensionFilter filter = new FileNameExtensionFilter("File saved by DosiEdit2D", new String[] {file_extension_number});
        this.FileChooser.setFileFilter(filter);
        
        final int returnVal = this.FileChooser.showOpenDialog(this);
        if (returnVal == 0)
            return this.FileChooser.getSelectedFile().getAbsolutePath();
      
    	return "";
    }
    
    /**
     * Show {@link JFileChooser} to let the user choose where is the directory containing images to load,
     * then show {@link ScanningWindow} to let the user choose one image.
     * @param view where to show the chooser.
     * @return the {@link ScanningWindow} when the user has chosen an image.
     */
    public static ScanningWindow showScanningWindow(View view)
	{
		final JFileChooser FileChooser = new JFileChooser();
        FileChooser.setFileSelectionMode(1);
        final int returnVal = FileChooser.showOpenDialog(null);
        if(returnVal == 0)
        {
        	final File directory = FileChooser.getSelectedFile();
        	if (directory.isFile()) {
                JOptionPane.showMessageDialog(null, "Please select the directory which contains the files, not the file itself.", "Not a directory", 0);
                return null;
            }
        	
        	ScanningWindow scanWin = new ScanningWindow(view,true);
        	File[] sorted = directory.listFiles();
        	Arrays.sort(sorted);
        	scanWin.showAvailableScan(sorted);
        	scanWin.setVisible(true);
        	
        	return scanWin;
        }
        return null;
	}
    
    /**
     * Display a window to show ptf file newly generated in read-only.
     * @param file the file to show.
     */
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
    
    /**
     * Show a HTML text which contains credits. 
     */
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

	@Override
	public void update(Observable o, Object arg)
	{
		AbstractModel model = (AbstractModel) o;
		this.setTitle("DosiEdit2D - Editing Pilot Text File (PTF) for DosiVox2D Software ("+model.getCURRENT_PATH()+")");
		this.repaint();
	}
}

