package interfaceObject;

import java.awt.Component;
import java.awt.Container;

import javax.swing.JPanel;

/**
 * This class is a JPanel overriding the JComponent.setEnabled(boolean) method. 
 * When this method is performs, all the sub swing-component is set to the same state.
 * @author alan
 */
public class LockablePanel extends JPanel
{
    private static final long serialVersionUID = 6036278217191583915L;
    
    /**
     * Enabling or disabling the edition of the panel.
     */
    @Override
    public void setEnabled(final boolean en) {
        super.setEnabled(en);
        this.setFamilyEnabled(this, en);
    }
    
    /**
     * Recursively enabling or disabling the edition.
     * @param c the top container.
     * @param en true if enabling editions, false if not.
     */
    private void setFamilyEnabled(final Container c, final boolean en) {
        final Component[] components = c.getComponents();
        Component[] array;
        for (int length = (array = components).length, i = 0; i < length; ++i) {
            final Component comp = array[i];
            if (comp instanceof Container) {
                this.setFamilyEnabled((Container)comp, en);
            }
            comp.setEnabled(en);
        }
    }
}
