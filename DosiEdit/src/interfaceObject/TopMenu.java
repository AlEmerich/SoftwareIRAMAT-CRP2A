package interfaceObject;

import java.awt.event.ActionListener;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;

/**
 * Class providing the menu of the applications.
 * @author alan
 * 
 */
public class TopMenu extends JMenuBar
{
    private static final long serialVersionUID = -999749328862179118L;
    
    /**
     * The help menu, "About...".
     */
    private JMenu Help;
    
    /**
     * Constructor with the TopLevelInterface in arguments.
     * @param parent the top level father.
     */
    public TopMenu(final ActionListener parent) {
        final JMenu Project = new JMenu("Project");
        final JMenuItem newProject = new JMenuItem("New project");
        newProject.setActionCommand("New project");
        newProject.addActionListener(parent);
        Project.add(newProject);
        final JMenuItem LoadFile = new JMenuItem("Open Project...");
        LoadFile.setActionCommand("Load file");
        LoadFile.addActionListener(parent);
        Project.add(LoadFile);
        final JMenuItem Save = new JMenuItem("Save");
        Save.setActionCommand("Save");
        Save.setAccelerator(KeyStroke.getKeyStroke(83, 2));
        Save.addActionListener(parent);
        Project.add(Save);
        final JMenuItem SaveAs = new JMenuItem("Save as...");
        SaveAs.setActionCommand("Save as");
        SaveAs.addActionListener(parent);
        Project.add(SaveAs);
        final JMenuItem GeneratePTF = new JMenuItem("Generate Pilot Text File");
        GeneratePTF.setActionCommand("Generate");
        GeneratePTF.addActionListener(parent);
        Project.add(GeneratePTF);
        final JMenuItem Quit = new JMenuItem("Quit");
        Quit.setActionCommand("Quit");
        Quit.addActionListener(parent);
        Project.add(Quit);
        this.add(Project);
        this.Help = new JMenu("Help");
        final JMenuItem about = new JMenuItem("About...");
        about.addActionListener(parent);
        about.setActionCommand("help");
        this.Help.add(about);
        this.add(this.Help);
    }
}
