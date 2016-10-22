package interfaceObject;

import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * Class providing an easy way to use three fields together, not editable. 
 * Useful for three coordinates informations (X, Y, Z or U, Th, K, or anything else)
 * @author alan
 * @see TripleFormattedPanel
 */
public class TripleLabelizedPanel extends JPanel
{
    private static final long serialVersionUID = 2199863818506963233L;
    
    /**
     * The title of the panel.
     */
    JLabel label;
    
    /**
     * The X field.
     */
    private JLabel XLabel;
    
    /**
     * The Y field.
     */
    private JLabel YLabel;
    
    /**
     * The Z field.
     */
    private JLabel ZLabel;
    
    /**
     * Constructor with X, Y an Z on the title fields.
     * @param label the title of the panel.
     * @param d the dimension of the panel.
     */
    public TripleLabelizedPanel(final JLabel label, final Dimension d) {
        this.label = label;
        this.construct("X", "Y", "Z", d);
    }
    
    /**
     * Constructor with x, y and z parameters to add as a title of the fields.
     * @param label the title of the panel.
     * @param x the title of the X field.
     * @param y the title of the Y field.
     * @param z the title of the Z field.
     * @param d the dimension of the panel.
     */
    public TripleLabelizedPanel(final JLabel label, final String x, final String y, final String z, final Dimension d) {
        this.label = label;
        this.construct(x, y, z, d);
    }
    
    /**
     * Construct fields.
     * @param x the title of the X field.
     * @param y the title of the Y field.
     * @param z the title of the Z field.
     * @param d the dimension of the panel.
     */
    private void construct(final String x, final String y, final String z, final Dimension d) {
        if (this.label != null) {
            this.add(this.label);
        }
        this.XLabel = new JLabel();
        if (d == null) {
            this.XLabel.setPreferredSize(new Dimension(60, 40));
        }
        else {
            this.XLabel.setPreferredSize(d);
        }
        this.XLabel.setMaximumSize(this.XLabel.getPreferredSize());
        this.XLabel.setBorder(BorderFactory.createTitledBorder(x));
        this.XLabel.setToolTipText(this.XLabel.getText());
        this.add(this.XLabel);
        this.YLabel = new JLabel();
        if (d == null) {
            this.YLabel.setPreferredSize(new Dimension(60, 40));
        }
        else {
            this.YLabel.setPreferredSize(d);
        }
        this.YLabel.setMaximumSize(this.YLabel.getPreferredSize());
        this.YLabel.setBorder(BorderFactory.createTitledBorder(y));
        this.YLabel.setToolTipText(this.YLabel.getText());
        this.add(this.YLabel);
        this.ZLabel = new JLabel();
        if (d == null) {
            this.ZLabel.setPreferredSize(new Dimension(60, 40));
        }
        else {
            this.ZLabel.setPreferredSize(d);
        }
        this.ZLabel.setMaximumSize(this.ZLabel.getPreferredSize());
        this.ZLabel.setBorder(BorderFactory.createTitledBorder(z));
        this.ZLabel.setToolTipText(this.ZLabel.getText());
        this.add(this.ZLabel);
    }
    
    /**
     * Set the value in the XLabel.
     * @param str the value in the XLabel
     */
    public void setXLabel(final String str) {
        this.XLabel.setText(str);
        this.XLabel.setToolTipText(this.XLabel.getText());
    }
    
    /**
     * Set the value in the YLabel.
     * @param str the value in the YLabel
     */
    public void setYLabel(final String str) {
        this.YLabel.setText(str);
        this.YLabel.setToolTipText(this.YLabel.getText());
    }
    
    /**
     * Set the value in the ZLabel.
     * @param str the value in the ZLabel
     */
    public void setZLabel(final String str) {
        this.ZLabel.setText(str);
        this.ZLabel.setToolTipText(this.ZLabel.getText());
    }
    
    /**
     * @return the X label.
     */
    public JLabel getXLabel() {
        return this.XLabel;
    }
    
    /**
     * @return the Y label.
     */
    public JLabel getYLabel() {
        return this.YLabel;
    }
    
    /**
     * @return the Z label.
     */
    public JLabel getZLabel() {
        return this.ZLabel;
    }
}
