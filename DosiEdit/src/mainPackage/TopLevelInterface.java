package mainPackage;

import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.util.Animator;
import interfaceObject.*;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.*;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * <b>Main class. Handling all the components of this software, in terms of OpenGL and Swing.</b>
 * @author alan
 *
 */
public class TopLevelInterface extends JFrame implements ActionListener, MaterialComponentProviderInterface
{	
    private static final long serialVersionUID = 4320411940353929077L;
    
    private static final String FILE_EXTENSION = ".dvi";
    
    private boolean windowSavingClosed = false;
    private ArrayList<mainPackage.Component> ListOfComponent;
    private ArrayList<Material> ListOfMaterials;
    private ExternalVoxelizedWorld ExternalVoxelizedWorld;
    private OngletManagement OngletManager;
    private final JFileChooser FileChooser;
    
    private Animator animator;
    private ImageIcon imgHome;
    private ImageIcon imgForge;
    private ImageIcon imgEditor;
    public static int DEFAULT_CUTTING_VALUE;
    public String CURRENT_PATH;
    private java.awt.Component wp;
    
    static {
        TopLevelInterface.DEFAULT_CUTTING_VALUE = 1;
    }
    
    /**
     * Initialization of built-in and read-only components.
     * @return the list of built-in component.
     */
    private ArrayList<mainPackage.Component> initializeListOfComponent() {
        final ArrayList<mainPackage.Component> list = new ArrayList<mainPackage.Component>();
        mainPackage.Component c = new mainPackage.Component("Water", 1.0f, true);
        c.addAtomToFormula(PeriodicTable.H, 2);
        c.addAtomToFormula(PeriodicTable.O);
        list.add(c);
        c = new mainPackage.Component("Silicon dioxide", 2.65f, true);
        c.addAtomToFormula(PeriodicTable.Si);
        c.addAtomToFormula(PeriodicTable.O, 2);
        list.add(c);
        c = new mainPackage.Component("Alumina", 3.96f, true);
        c.addAtomToFormula(PeriodicTable.Al, 2);
        c.addAtomToFormula(PeriodicTable.O, 3);
        list.add(c);
        c = new mainPackage.Component("Calcium carbonate", 2.711f, true);
        c.addAtomToFormula(PeriodicTable.Ca);
        c.addAtomToFormula(PeriodicTable.C);
        c.addAtomToFormula(PeriodicTable.O, 3);
        list.add(c);
        c = new mainPackage.Component("Magnesium carbonate", 3.0f, true);
        c.addAtomToFormula(PeriodicTable.Mg);
        c.addAtomToFormula(PeriodicTable.C);
        c.addAtomToFormula(PeriodicTable.O, 3);
        list.add(c);
        c = new mainPackage.Component("Limestone", 2.7f, true);
        list.add(c);
        c = new mainPackage.Component("Zircon", 4.0f, true);
        c.addAtomToFormula(PeriodicTable.Zr);
        c.addAtomToFormula(PeriodicTable.Si);
        c.addAtomToFormula(PeriodicTable.O, 4);
        list.add(c);
        c = new mainPackage.Component("K-Feldspar", 2.6f, true);
        c.addAtomToFormula(PeriodicTable.K);
        c.addAtomToFormula(PeriodicTable.F);
        list.add(c);
        c = new mainPackage.Component("H_apatite", 3.8f, true);
        list.add(c);
        c = new mainPackage.Component("Iron(III) oxide", 5.242f, true);
        c.addAtomToFormula(PeriodicTable.Fe, 2);
        c.addAtomToFormula(PeriodicTable.O, 3);
        list.add(c);
        c = new mainPackage.Component("Iron(II) oxide", 5.745f, true);
        c.addAtomToFormula(PeriodicTable.Fe);
        c.addAtomToFormula(PeriodicTable.O);
        list.add(c);
        c = new mainPackage.Component("Magnesium oxide", 3.58f, true);
        c.addAtomToFormula(PeriodicTable.Mg);
        c.addAtomToFormula(PeriodicTable.O);
        list.add(c);
        c = new mainPackage.Component("Calcium oxide", 3.35f, true);
        c.addAtomToFormula(PeriodicTable.Ca);
        c.addAtomToFormula(PeriodicTable.O);
        list.add(c);
        c = new mainPackage.Component("Sodium oxide", 2.27f, true);
        c.addAtomToFormula(PeriodicTable.Na, 2);
        c.addAtomToFormula(PeriodicTable.O);
        list.add(c);
        c = new mainPackage.Component("Potassium oxide", 2.35f, true);
        c.addAtomToFormula(PeriodicTable.K, 2);
        c.addAtomToFormula(PeriodicTable.O);
        list.add(c);
        c = new mainPackage.Component("air", 0.00129f, true);
        list.add(c);
        c = new mainPackage.Component("vaccum", 0.0f, true);
        list.add(c);
        c = new mainPackage.Component("lead", 11.35f, true);
        list.add(c);
        return list;
    }
    
    /**
     * Default constructor. Initialize the basic list of 18 components and an empty voxelized object.
     */
    public TopLevelInterface() {
    	super();
        this.ExternalVoxelizedWorld = null;
        this.CURRENT_PATH = "";
        this.ListOfComponent = this.initializeListOfComponent();
        this.FileChooser = new JFileChooser();
        this.createJOGLInterface();
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(final WindowEvent e) {
                TopLevelInterface.this.Quit();
            }
        });
        
        this.setPreferredSize(new Dimension(1200,730));
        super.validate();
    }
    
    /**
     * Prepare the frame and add the usual component.
     */
    public void createJOGLInterface() {
        this.imgHome = createImageIcon("/resources/home.png");
        this.imgForge = createImageIcon("/resources/forge.jpeg");
        this.imgEditor = createImageIcon("/resources/voxel.png");
        this.setTitle("DosiEdit - Editing Pilot Text File (PTF) for DosiVox Software ("+this.CURRENT_PATH+")");
        this.setIconImage(Toolkit.getDefaultToolkit().getImage(this.getClass().getResource("/resources/voxel.png")));
        this.setExtendedState(NORMAL);
        
        this.setDefaultCloseOperation(0);
        
        this.setJMenuBar(new TopMenu(this));
        this.OngletManager = new OngletManagement(this, this.imgHome, this.imgForge, this.imgEditor);
        this.wp = this.add(new WelcomePanel(this));
        
        this.setMinimumSize(new Dimension(600, 400));
        this.setPreferredSize(new Dimension(1200,730));
        Dimension screenDimension = Toolkit.getDefaultToolkit().getScreenSize();
        this.setLocation(screenDimension.width/2-this.getSize().width/2,
        		screenDimension.height/2-this.getSize().height/2);
        this.validate();
        this.pack();
        this.setVisible(true);
    }
    
    @Override
    public void actionPerformed(final ActionEvent e) {
        if (e.getActionCommand().equals("New project")) {
            if (this.ExternalVoxelizedWorld != null) {
                final int result = yesnocancel("Be sure you saved your project. If you continue, all unsaved data will be lost.");
                if (result != 0) {
                    return;
                }
            }
            this.remove(this.wp);
            this.add(this.OngletManager);
            this.ListOfMaterials = new ArrayList<Material>(0);
            this.ListOfComponent = this.initializeListOfComponent();
            if (this.ExternalVoxelizedWorld != null) {
                this.OngletManager.removeAll();
            }
            this.ExternalVoxelizedWorld = new ExternalVoxelizedWorld();
            final Scene Scene = new Scene(this.ExternalVoxelizedWorld, this.getPreferredSize());
            this.animator = new Animator((GLAutoDrawable)Scene);
            this.OngletManager.add(new MainInfoVoxel(this), new ForgeWindow(this), new EditorPane(this, Scene));
            this.OngletManager.doLinking();
            this.pack();
            this.CURRENT_PATH = "";
            this.animator.start();
        }
        else if (e.getActionCommand().equals("Quit")) {
            this.Quit();
        }
        else if (e.getActionCommand().equals("Load file")) {
        	String file_extension_letters = FILE_EXTENSION.substring(1);
            final FileNameExtensionFilter filter = new FileNameExtensionFilter(null, new String[] {file_extension_letters});
            this.FileChooser.setFileFilter(filter);
            final int returnVal = this.FileChooser.showOpenDialog(this);
            if (returnVal == 0) {
            	this.remove(this.wp);
            	this.add(OngletManager);
                this.load(this.FileChooser.getSelectedFile().getAbsolutePath());
                
                final Scene Scene2 = new Scene(this.ExternalVoxelizedWorld, this.getPreferredSize());
                this.animator = new Animator((GLAutoDrawable)Scene2);
                
                this.OngletManager.removeAll();
                this.OngletManager.add(new MainInfoVoxel(this), new ForgeWindow(this), new EditorPane(this, Scene2));

                this.OngletManager.doLinking();
                this.setTitle("DosiEdit - Editing Pilot Text File (PTF) for DosiVox Software ("+this.CURRENT_PATH+")");
                this.pack();
                this.animator.start();
                
            }
        }
        else if (e.getActionCommand().equals("Save")) {
            if (this.CURRENT_PATH.equals("")) {
                this.actionPerformed(new ActionEvent(this, 0, "Save as"));
            }
            else {
                this.save(this.CURRENT_PATH);
            }
        }
        else if (e.getActionCommand().equals("Save as")) {
            if (this.ExternalVoxelizedWorld == null) {
                return;
            }
            final int returnVal2 = this.FileChooser.showSaveDialog(this);
            if (returnVal2 == 0) {
                final String nameFile = this.FileChooser.getSelectedFile().getAbsolutePath();
                if (nameFile.endsWith(FILE_EXTENSION)) {
                    this.save(this.FileChooser.getSelectedFile().getAbsolutePath());
                }
                else {
                    this.save(String.valueOf(this.FileChooser.getSelectedFile().getAbsolutePath()) + FILE_EXTENSION);
                }
                windowSavingClosed = false;
                this.setTitle("DosiEdit - Editing Pilot Text File (PTF) for DosiVox Software ("+this.CURRENT_PATH+")");
            } else 
            	windowSavingClosed = true;
        }
        else if (e.getActionCommand().equals("Generate")) {
            if (this.checkIntegrityOfAllProject()) {
                final WriterPTF writer = new WriterPTF(this);
                final int returnVal = this.FileChooser.showSaveDialog(this);
                if (returnVal == 0) {
                    final File file = new File(this.FileChooser.getSelectedFile().getAbsolutePath());
                    
                    final JDialog modalWaiting = new JDialog((Frame)this, "Please wait", true);
                    modalWaiting.setSize(400, 80);
                    modalWaiting.setLocationRelativeTo(this);
                    modalWaiting.setResizable(true);
                    modalWaiting.setDefaultCloseOperation(0);
                    final JLabel modalMessage = new JLabel("Writing global informations...");
                    modalWaiting.add(modalMessage);
                    Thread writingThread = new Thread(){
                    	@Override
                    	public void run()
                    	{
                    		writer.generatePTF(modalMessage,file);
                    		modalWaiting.setVisible(false);
                    	}
                    };
                    writingThread.start();
                    modalWaiting.setVisible(true);
                    
                    int result = yesnocancel("Open the PTF file to check it ? (Read only)");
                    if(result == JOptionPane.OK_OPTION)
                    	openPTFfile(file);
                }
            }
        }
        else if (e.getActionCommand().equals("help")) {
            this.Help();
        }
    }
    
    private void openPTFfile(File file) {
		
		JFrame tabbedWindow = new JFrame("PTF file");
		JTabbedPane tabs = new JTabbedPane();
		String suffix = "";
		switch(ExternalVoxelizedWorld.getPrimaryParticle())
		{
		case Alpha:
			suffix = "_a";
			break;
		case Beta:
			suffix = "_b";
			break;
		case Gamma:
			suffix = "_g";
			break;
			default:
				break;
		}
		JTextArea KFile = new JTextArea();
		JTextArea UFile = new JTextArea();
		JTextArea ThFile = new JTextArea();
		JTextArea UdFile = new JTextArea();
		try {
			KFile.read(new FileReader(file.getAbsolutePath()+"_K"+suffix), null);
			UFile.read(new FileReader(file.getAbsolutePath()+"_U"+suffix), null);
			ThFile.read(new FileReader(file.getAbsolutePath()+"_Th"+suffix), null);
			UdFile.read(new FileReader(file.getAbsolutePath()+"_ud"+suffix), null);
			KFile.setEditable(false);
			UFile.setEditable(false);
			ThFile.setEditable(false);
			UdFile.setEditable(false);
		} catch (IOException e) {
			e.printStackTrace();
		}
		tabs.addTab("*_K"+suffix, new JScrollPane(KFile));
		tabs.addTab("*_U"+suffix, new JScrollPane(UFile));
		tabs.addTab("*_Th"+suffix, new JScrollPane(ThFile));
		tabs.addTab("*_ud"+suffix, new JScrollPane(UdFile));
		
		tabbedWindow.add(tabs);
		tabbedWindow.setMinimumSize(new Dimension(600,400));
		tabbedWindow.setLocationRelativeTo(this);
		tabbedWindow.setVisible(true);
	}

	/**
     * Open the help window.
     * @see JDialog
     */
    private void Help() {
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
    
    private boolean requestSaveProject()
    {
    	final int result = yesnocancel("Do you want to save the current project ?");
        if (result == JOptionPane.OK_OPTION)
        	this.actionPerformed(new ActionEvent(this, 0, "Save"));
        else if(result == JOptionPane.CANCEL_OPTION || result == JOptionPane.CLOSED_OPTION)
        	return false;
        return true;
    }
    
    /**
     * The action when Quit is performs in the {@link TopMenu}.
     */
    public void Quit() {
        if (this.ExternalVoxelizedWorld != null) {
            if(!requestSaveProject())
            	return;
        }
        if (this.animator != null && this.animator.isAnimating()) {
            this.stopAnimatorStarted();
        }
        
        if(!this.windowSavingClosed)
        {
        	this.dispose();
        	System.exit(0);
        }
        this.windowSavingClosed = false;
    }
    
    /**
     * Function which load a serializable object indicates by the filename path. 
     * This function is not generic, it loads a {@link SaveBox} object.
     * @param filename the path to the file to load.
     */
    public void load(final String filename) {
        if (this.ExternalVoxelizedWorld != null) {
            if(!requestSaveProject())
            	return;
            
            this.OngletManager.setSelectedIndex(0);
        }
        final JDialog modalWaiting = new JDialog((Frame)this, "Please wait", true);
        modalWaiting.setSize(200, 80);
        modalWaiting.setLocationRelativeTo(this);
        modalWaiting.setResizable(false);
        modalWaiting.setDefaultCloseOperation(0);
        modalWaiting.add(new JLabel("Loading file..."));
        final Thread t = new Thread() {
            @Override
            public void run() {
                SaveBox sb = null;
                ObjectInputStream ois = null;
                try {
                    ois = new ObjectInputStream(new FileInputStream(new File(filename)));
                    sb = (SaveBox)ois.readObject();
                    TopLevelInterface.access$0(TopLevelInterface.this, sb.getVox());
                    TopLevelInterface.access$3(TopLevelInterface.this, sb.getListComponent());
                    TopLevelInterface.access$4(TopLevelInterface.this, sb.getListMaterial());
                    TopLevelInterface.this.CURRENT_PATH = sb.getPath();
                }
                catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                catch (IOException e2) {
                    e2.printStackTrace();
                    JOptionPane.showMessageDialog(null, e2.getMessage(), "Error during the reading of the file", 0);
                }
                catch (ClassNotFoundException e3) {
                    e3.printStackTrace();
                    JOptionPane.showMessageDialog(null, e3.getMessage(), "Class not found", 0);
                }
                finally {
                    if (ois != null) {
                        try {
                            ois.close();
                            modalWaiting.setVisible(false);
                        }      
                        catch (IOException e4) {
                            e4.printStackTrace();
                            JOptionPane.showMessageDialog(null, e4.getMessage(), "Error during the closing of the file reader", 0);
                        }
                    }
                }
                if (ois != null) {
                    try {
                        ois.close();
                        modalWaiting.setVisible(false);
                    }
                    catch (IOException e4) {
                        e4.printStackTrace();
                        JOptionPane.showMessageDialog(null, e4.getMessage(), "Error during the closing of the file reader", 0);
                    }
                }
            }
        };
        t.start();
        modalWaiting.setVisible(true);
    }
    
    /**
     * Save the current project. All informations needed to be saved is gathered in {@link SaveBox} object, 
     * which implements the {@link Serializable} interface.
     * @param filePath the path to the file to load.
     */
    public void save(final String filePath) {
        int index = -1;
        if (this.OngletManager != null) {
            index = this.OngletManager.getSelectedIndex();
            this.OngletManager.setSelectedIndex(0);
        }
        final JDialog modalWaiting = new JDialog((Frame)null, "Please wait", true);
        final JLabel saveLab = new JLabel("Prepare to save file...");
        modalWaiting.setSize(200, 80);
        modalWaiting.setLayout(new BorderLayout());
        modalWaiting.setLocationRelativeTo(null);
        modalWaiting.setResizable(false);
        modalWaiting.setDefaultCloseOperation(0);
        modalWaiting.add(saveLab);
        final Thread t = new Thread(() -> {
            ObjectOutputStream oos = null;
            try {
                final File file = new File(filePath);
                oos = new ObjectOutputStream(new FileOutputStream(file));
                final SaveBox sb = new SaveBox();
                saveLab.setText("Saving Voxel");
                sb.setVox(TopLevelInterface.this.ExternalVoxelizedWorld);
                saveLab.setText("Saving materials");
                sb.setListMaterial(TopLevelInterface.this.ListOfMaterials);
                saveLab.setText("Saving components");
                sb.setListComponent(TopLevelInterface.this.ListOfComponent);
                sb.setPath(file.getAbsolutePath());
                oos.writeObject(sb);
                TopLevelInterface.this.CURRENT_PATH = file.getAbsolutePath();
            }
            catch (IOException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(null, e.getMessage(), "Error during the reading of the file", 0);
                if (oos != null) {
                    try {
                        oos.close();
                        modalWaiting.setVisible(false);
                    }
                    catch (IOException e2) {
                        e2.printStackTrace();
                        JOptionPane.showMessageDialog(null, e2.getMessage(), "Error during the closing of the file reader", 0);
                    }
                }
                return;
            }
            finally {
                if (oos != null) {
                    try {
                        oos.close();
                    }
                    catch (IOException e2) {
                        e2.printStackTrace();
                        JOptionPane.showMessageDialog(null, e2.getMessage(), "Error during the closing of the file reader", 0);
                    }
                    finally
                    {
                        modalWaiting.setVisible(false);
                    }
                }
            }
            if (oos != null) {
                try {
                    oos.close();
                }
                catch (IOException e2) {
                    e2.printStackTrace();
                    JOptionPane.showMessageDialog(null, e2.getMessage(), "Error during the closing of the file reader", 0);
                }
                finally
                {
                    modalWaiting.setVisible(false);
                }
            }
        });
        t.start();
        modalWaiting.setVisible(true);
        if (index != -1) {
            this.OngletManager.setSelectedIndex(index);
        }
    }
    
    @SuppressWarnings("unused")
	private void startAnimatorStopped() {
        if (this.animator != null) {
            this.animator.start();
        }
        if (this.ExternalVoxelizedWorld != null && this.ExternalVoxelizedWorld.getInternalVoxelizedObject() != null) {
            this.OngletManager.getAnimatorInternal().start();
        }
    }
    
    private void stopAnimatorStarted() {
        if (this.animator != null) {
            this.animator.stop();
        }
        if (this.ExternalVoxelizedWorld != null && this.ExternalVoxelizedWorld.getInternalVoxelizedObject() != null) {
            this.OngletManager.getAnimatorInternal().stop();
        }
    }
    
    public JFileChooser getFileSearcher() {
        return this.FileChooser;
    }
    
    public List<mainPackage.Component> getListOfComponent() {
        return this.ListOfComponent;
    }
    
    public List<Material> getListOfMaterial() {
        return this.ListOfMaterials;
    }
    
    public ExternalVoxelizedWorld getVoxSample() {
        return this.ExternalVoxelizedWorld;
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
     * Static method which shows a YES or NO window.
     * @param theMessage displayed by the window.
     * @return the result the user wants.
     */
    public static int yesnocancel(final String theMessage) {
        final int result = JOptionPane.showConfirmDialog(null, theMessage, "Alert", JOptionPane.YES_NO_CANCEL_OPTION);
        return result;
    }
    
    /**
     * Check the integrity of the whole project. Check if {@link VoxelizedObject} has a nullpointer,
     * if there are complete, and check the integrity of the tabs.
     * @return true if project is in valid state, false if not.
     * @see OngletManagement
     */
    public boolean checkIntegrityOfAllProject() {
        if (this.ExternalVoxelizedWorld == null) {
            return false;
        }
        if (!this.ExternalVoxelizedWorld.isComplete()) {
            JOptionPane.showMessageDialog(null, "The external world is not complete.", "Incomplete project", 0);
            return false;
        }
        if (this.ExternalVoxelizedWorld.getInternalVoxelizedObject() != null && !this.ExternalVoxelizedWorld.getInternalVoxelizedObject().isComplete()) {
            JOptionPane.showMessageDialog(null, "The internal world is not complete.", "Incomplete project", 0);
            return false;
        }
        return this.OngletManager.checkIntegrityOfTabs();
    }
    
    static /* synthetic */ void access$0(final TopLevelInterface topLevelInterface, final ExternalVoxelizedWorld externalVoxelizedWorld) {
        topLevelInterface.ExternalVoxelizedWorld = externalVoxelizedWorld;
    }
    
    static /* synthetic */ void access$3(final TopLevelInterface topLevelInterface, final ArrayList<Component> listOfComponent) {
        topLevelInterface.ListOfComponent = (ArrayList<mainPackage.Component>)listOfComponent;
    }
    
    static /* synthetic */ void access$4(final TopLevelInterface topLevelInterface, final ArrayList<Material> listOfMaterials) {
        topLevelInterface.ListOfMaterials = (ArrayList<Material>)listOfMaterials;
    }

    /**
     * The very first function of the software.
     * @param args the argument of the software.
     */
    public static void main(final String[] args) {
    	System.err.println("args "+args.length);

        try {
            UIManager.LookAndFeelInfo[] installedLookAndFeels;
            for (int length = (installedLookAndFeels = UIManager.getInstalledLookAndFeels()).length, i = 0; i < length; ++i) {
                final UIManager.LookAndFeelInfo info = installedLookAndFeels[i];
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        }
        catch (Exception e) {
            try {
                UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");
            }
            catch (ClassNotFoundException ex) {}
            catch (InstantiationException ex2) {}
            catch (IllegalAccessException ex3) {}
            catch (UnsupportedLookAndFeelException ex4) {}
        }

    	if (args.length == 0) {  
            try {  
                // re-launch the app itself with VM option passed

            	final Process proc = Runtime.getRuntime().exec("java -Dsun.awt.noerasebackground=true -jar "
            			+TopLevelInterface.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath()+" test\n");
    			
            	new Thread(() -> {
                 BufferedReader input = new BufferedReader(new InputStreamReader(proc.getInputStream()));
                 String line;

                 try {
                    while ((line = input.readLine()) != null)
                        System.out.println(line);
                 } catch (IOException e) {
                        e.printStackTrace();
                 }
                }).start();
            	
    			proc.waitFor();
    			
            } catch (IOException ioe) {  
            	System.err.println("error");
                ioe.printStackTrace();  
            } catch (URISyntaxException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InterruptedException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}  
            
            
            System.exit(0);
        }
       
        new TopLevelInterface();
    }
    
    
}
