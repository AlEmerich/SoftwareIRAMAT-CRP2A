package iramat.dosiseed.view;

import java.awt.Dimension;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.text.ParseException;

import javax.swing.BorderFactory;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;
import javax.swing.text.MaskFormatter;

/**
 * Class providing an easy way to use three fields together. Useful for three coordinates informations (X, Y, Z or U, Th, K, or anything else).
 * @author alan
 * @see KeyListener
 */
public class TripleFormattedField extends JPanel implements KeyListener, MouseListener
{
    private static final long serialVersionUID = 5336458777190363391L;
    
    /**
     * The description of the panel.
     */
    private JLabel label;
    
    /**
     * The X field.
     */
    private JFormattedTextField XField;
    
    /**
     * The Y field.
     */
    private JFormattedTextField YField;
    
    /**
     * The Z field.
     */
    private JFormattedTextField ZField;
    
    /**
     * Constructor with X, Y an Z on the title fields.
     * @param label the description of the panel.
     * @param allowsNegative true for allowing negative value, false if not.
     */
    public TripleFormattedField(final JLabel label, final boolean allowsNegative) {
        this.label = label;
        this.construct("X", "Y", "Z", allowsNegative);
    }
    
    /**
     * Constructor with x, y and z parameters to add as a title of the fields.
     * @param label the description of the panel.
     * @param x the title of the X field.
     * @param y the title of the Y field.
     * @param z the title of the Z field.
     * @param allowsNegative true for allowing negative value, false if not.
     */
    public TripleFormattedField(final JLabel label, final String x, final String y, final String z, final boolean allowsNegative) {
        this.label = label;
        this.construct(x, y, z, allowsNegative);
    }
    
    /**
     * Create the mask of the fields. Accept just number and points.
     * @param allowsNegative true for allowing negative value, false if not.
     * @return the mask created.
     */
    public static MaskFormatter createmask(final boolean allowsNegative) {
        MaskFormatter mask = null;
        try {
            mask = new MaskFormatter("***************");
        }
        catch (ParseException e) {
            e.printStackTrace();
        }
        if (allowsNegative) {
            mask.setValidCharacters("0123456789.-");
        }
        else {
            mask.setValidCharacters("0123456789.");
        }
        mask.setAllowsInvalid(false);
        return mask;
    }
    
    /**
     * Construct fields.
     * @param x the title of the X field.
     * @param y the title of the Y field.
     * @param z the title of the Z field.
     * @param allowsNegative true for allowing negative value, false if not.
     */
    private void construct(final String x, final String y, final String z, final boolean allowsNegative) {
        if (this.label != null) {
            this.add(this.label);
        }
        (this.XField = new JFormattedTextField(createmask(allowsNegative))).setFocusLostBehavior(3);
        this.XField.setPreferredSize(new Dimension(60, 40));
        this.XField.setMaximumSize(this.XField.getPreferredSize());
        this.XField.setBorder(BorderFactory.createTitledBorder(FormattedLabelField.greenBorder, x));
        this.add(this.XField);
        (this.YField = new JFormattedTextField(createmask(allowsNegative))).setFocusLostBehavior(3);
        this.YField.setPreferredSize(new Dimension(60, 40));
        this.YField.setMaximumSize(this.YField.getPreferredSize());
        this.YField.setBorder(BorderFactory.createTitledBorder(FormattedLabelField.greenBorder, y));
        this.add(this.YField);
        (this.ZField = new JFormattedTextField(createmask(allowsNegative))).setFocusLostBehavior(3);
        this.ZField.setPreferredSize(new Dimension(60, 40));
        this.ZField.setMaximumSize(this.ZField.getPreferredSize());
        this.ZField.setBorder(BorderFactory.createTitledBorder(FormattedLabelField.greenBorder, z));
        this.add(this.ZField);
    }
    
    /**
     * @return the X field.
     */
    public JFormattedTextField getXField() {
        return this.XField;
    }
    
    /**
     * @return the Y field.
     */
    public JFormattedTextField getYField() {
        return this.YField;
    }
    
    /**
     * @return the Z field.
     */
    public JFormattedTextField getZField() {
        return this.ZField;
    }
    
    /**
     * Update the swing with value passed in parameters.
     * @param x the X value.
     * @param y the Y value.
     * @param z the Z value.
     */
    public void updateFrameComponent(final float x, final float y, final float z) {
        this.XField.setText(new StringBuilder(String.valueOf(x)).toString());
        ((TitledBorder)this.XField.getBorder()).setBorder(FormattedLabelField.greenBorder);
        this.YField.setText(new StringBuilder(String.valueOf(y)).toString());
        ((TitledBorder)this.YField.getBorder()).setBorder(FormattedLabelField.greenBorder);
        this.ZField.setText(new StringBuilder(String.valueOf(z)).toString());
        ((TitledBorder)this.ZField.getBorder()).setBorder(FormattedLabelField.greenBorder);
        this.repaint();
    }
    
    /**
     * Update the swing with value passed in parameters.
     * @param x the X value.
     * @param y the Y value.
     * @param z the Z value.
     */
    public void updateFrameComponent(final String x, final String y, final String z) {
        this.XField.setText(x);
        ((TitledBorder)this.XField.getBorder()).setBorder(FormattedLabelField.greenBorder);
        this.YField.setText(y);
        ((TitledBorder)this.YField.getBorder()).setBorder(FormattedLabelField.greenBorder);
        this.ZField.setText(z);
        ((TitledBorder)this.ZField.getBorder()).setBorder(FormattedLabelField.greenBorder);
        this.repaint();
    }
    
    /**
     * Get one of the value.
     * @param value Which value do you want.
     * @return the value in the field specified.
     * @throws Exception
     */
    public float getValue(final XYZ value) throws Exception {
        try {
            switch (value) {
                case X: {
                    return Float.parseFloat(this.XField.getText().replaceAll("\\s+", ""));
                }
                case Y: {
                    return Float.parseFloat(this.YField.getText().replaceAll("\\s+", ""));
                }
                case Z: {
                    return Float.parseFloat(this.ZField.getText().replaceAll("\\s+", ""));
                }
                default: {
                    return -1.0f;
                }
            }
        }
        catch (NumberFormatException e) {
            throw new Exception(value + " field");
        }
    }
    
    @Override
    public void addKeyListener(final KeyListener k) {
        this.XField.addKeyListener(k);
        this.XField.addKeyListener(this);
        this.XField.addMouseListener(this);
        this.YField.addKeyListener(k);
        this.YField.addKeyListener(this);
        this.YField.addMouseListener(this);
        this.ZField.addKeyListener(k);
        this.ZField.addKeyListener(this);
        this.ZField.addMouseListener(this);
    }
    
    @Override
    public void keyPressed(final KeyEvent e) {
        if (e.getKeyCode() != 10) {
            if (e.getSource() == this.XField) {
                ((TitledBorder)this.XField.getBorder()).setBorder(FormattedLabelField.redBorder);
            }
            if (e.getSource() == this.YField) {
                ((TitledBorder)this.YField.getBorder()).setBorder(FormattedLabelField.redBorder);
            }
            if (e.getSource() == this.ZField) {
                ((TitledBorder)this.ZField.getBorder()).setBorder(FormattedLabelField.redBorder);
            }
        }
    }
    
    @Override
    public void keyReleased(final KeyEvent e) {
    }
    
    @Override
    public void keyTyped(final KeyEvent e) {
        if (e.getKeyCode() != 10) {
            if (e.getSource() == this.XField) {
                ((TitledBorder)this.XField.getBorder()).setBorder(FormattedLabelField.redBorder);
            }
            if (e.getSource() == this.YField) {
                ((TitledBorder)this.YField.getBorder()).setBorder(FormattedLabelField.redBorder);
            }
            if (e.getSource() == this.ZField) {
                ((TitledBorder)this.ZField.getBorder()).setBorder(FormattedLabelField.redBorder);
            }
        }
    }
    
    public void setToUpdate() {
        ((TitledBorder)this.XField.getBorder()).setBorder(FormattedLabelField.redBorder);
        ((TitledBorder)this.YField.getBorder()).setBorder(FormattedLabelField.redBorder);
        ((TitledBorder)this.ZField.getBorder()).setBorder(FormattedLabelField.redBorder);
        this.repaint();
    }
    
    public static void removeExtraSpaceFromField(JFormattedTextField field)
    {
    	field.setText(field.getText().trim());
    }
    
    public enum XYZ
    {
        X("X", 0), 
        Y("Y", 1), 
        Z("Z", 2);
        
        private XYZ(final String name, final int ordinal) {
        }
    }

	@Override
	public void mouseClicked(MouseEvent e)
	{
		removeExtraSpaceFromField((JFormattedTextField) e.getSource());
	}

	@Override
	public void mouseEntered(MouseEvent e)
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e)
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent e)
	{
		removeExtraSpaceFromField((JFormattedTextField) e.getSource());
	}

	@Override
	public void mouseReleased(MouseEvent e)
	{
		// TODO Auto-generated method stub
		
	}
}
