package interfaceObject;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import mainPackage.TopLevelInterface;

/**
 * Panel shown at the opening of the software to let the user know about the latest update.
 * @author alan
 */
public class WelcomePanel extends JPanel
{
    private static final long serialVersionUID = 7158999747151178527L;
    
    /**
     * Constructor of the welcome page.
     * @param father the top-level father.
     */
    public WelcomePanel(final TopLevelInterface father) {
        this.setLayout(new BorderLayout());
        final JPanel CenterPan = new JPanel();
        final JLabel welcoming = new JLabel("<html>Welcome to DosiEdit</html>");
        final JLabel lab = new JLabel("This interface providing an ergonomic way to create and edit PTF's files to work with the DosiVox Software.");
        final JLabel lab2 = new JLabel("<html>When you open or create a project, you will see three tabs:<ul><li> Global information tab: gathers all parameters usefull to DosiVox but unusefull to the edition of the world.</li><li> Forge: This is where you will create your materials and components.</li><li> Editor: This is where you will edit your world with the material you created</li></ul></html>");
        final JLabel lab3 = new JLabel("<html>Improvements from the DosiVox interface described in the DosiVox Manual:<ul><li>You can create or load a world as big as you want. <br>If it is really big, the 3 dimensions view will be adapted to stay fast, ignoring some voxels according to a step.</li><li>You have a 3D view to visualize your project the best you want.</li><li>You can save your project even if your world is incomplete.</li></ul></html>");
        final JLabel lab4 = new JLabel("To create a new project, click here: ");
        final JButton NewPButton = new JButton("Create a new project");
        final JLabel voxelpng = new JLabel(TopLevelInterface.createImageIcon("/resources/bigvoxel.png"));
        welcoming.setFont(new Font("Arial", 1, 50));
        voxelpng.setSize(new Dimension(100, 100));
        CenterPan.add(voxelpng);
        CenterPan.add(welcoming);
        lab.setFont(new Font("Arial", 0, 20));
        CenterPan.add(lab);
        lab2.setFont(new Font("Arial", 0, 20));
        CenterPan.add(lab2);
        lab3.setFont(new Font("Arial", 0, 20));
        CenterPan.add(lab3);
        final JPanel SouthPan = new JPanel();
        lab4.setFont(new Font("Arial", 0, 20));
        SouthPan.add(lab4);
        NewPButton.setActionCommand("New project");
        NewPButton.addActionListener(father);
        SouthPan.add(NewPButton);
        this.add(CenterPan, "Center");
        this.add(SouthPan, "South");
        this.setVisible(true);
    }
}
