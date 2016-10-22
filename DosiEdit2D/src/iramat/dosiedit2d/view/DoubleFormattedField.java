package iramat.dosiedit2d.view;

import iramat.dosiseed.view.FormattedLabelField;
import iramat.dosiseed.view.TripleFormattedField;
import iramat.dosiseed.view.TripleFormattedField.XYZ;

import java.awt.Dimension;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.BorderFactory;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;

/**
 * Class gathering two JFormattedTextField, adding X Y title or user-defined title.
 * @author alan
 *
 */
public class DoubleFormattedField extends JPanel implements KeyListener,MouseListener
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -4518907850606431335L;

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
    
    /**Focus
     * Constructor with X, Y on the title fields.
     * @param father the top-level father.
     * @param label the description of the panel.
     * @param allowsNegative true for allowing negative value, false if not.
     */
    public DoubleFormattedField(JLabel desc,final boolean allowsNegative)
    {
    	this.label = desc;
        this.construct("X", "Y", allowsNegative);
    }
    
    /**
     * Constructor with x, y and z parameters to add as a title of the fields.
     * @param father the top-level father.
     * @param label the description of the panel.
     * @param x the title of the X field.
     * @param y the title of the Y field.
     * @param allowsNegative true for allowing negative value, false if not.
     */
    public DoubleFormattedField(final JLabel desc, final String x, final String y,  final boolean allowsNegative) {
        this.label = desc;
        this.construct(x, y, allowsNegative);
    }
    
	private void construct(String x, String y,
			boolean allowsNegative)
	{
		if (this.label != null) {
            this.add(this.label);
        }
        (this.XField = new JFormattedTextField(TripleFormattedField.createmask(allowsNegative))).setFocusLostBehavior(3);
        this.XField.setPreferredSize(new Dimension(60, 40));
        this.XField.setMaximumSize(this.XField.getPreferredSize());
        this.XField.setBorder(BorderFactory.createTitledBorder(FormattedLabelField.greenBorder, x));
        this.add(this.XField);
        (this.YField = new JFormattedTextField(TripleFormattedField.createmask(allowsNegative))).setFocusLostBehavior(3);
        this.YField.setPreferredSize(new Dimension(60, 40));
        this.YField.setMaximumSize(this.YField.getPreferredSize());
        this.YField.setBorder(BorderFactory.createTitledBorder(FormattedLabelField.greenBorder, y));
        this.add(this.YField);
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
    
    @Override
    public void addKeyListener(final KeyListener k) {
        this.XField.addKeyListener(k);
        this.XField.addKeyListener(this);
        this.XField.addMouseListener(this);
        
        this.YField.addKeyListener(k);
        this.YField.addKeyListener(this);
        this.YField.addMouseListener(this);
    }
    
	@Override
	public void keyPressed(KeyEvent e)
	{
		if (e.getKeyCode() != 10) {
            if (e.getSource() == this.XField) {
                ((TitledBorder)this.XField.getBorder()).setBorder(FormattedLabelField.redBorder);
            }
            if (e.getSource() == this.YField) {
                ((TitledBorder)this.YField.getBorder()).setBorder(FormattedLabelField.redBorder);
            }
        }
	}

	@Override
	public void keyReleased(KeyEvent arg0)
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyTyped(KeyEvent e)
	{
		if (e.getKeyCode() != 10) {
            if (e.getSource() == this.XField) {
                ((TitledBorder)this.XField.getBorder()).setBorder(FormattedLabelField.redBorder);
            }
            if (e.getSource() == this.YField) {
                ((TitledBorder)this.YField.getBorder()).setBorder(FormattedLabelField.redBorder);
            }
        }
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
                default: {
                    return -1.0f;
                }
            }
        }
        catch (NumberFormatException e) {
            throw new Exception(value + " field");
        }
    }
	
    /**
     * Change borders of the field to red.
     */
    public void setToUpdate() {
        ((TitledBorder)this.XField.getBorder()).setBorder(FormattedLabelField.redBorder);
        ((TitledBorder)this.YField.getBorder()).setBorder(FormattedLabelField.redBorder);
        this.repaint();
    }
    
	/**
     * Update the swing with value passed in parameters.
     * @param x the X value.
     * @param y the Y value.
     */
    public void updateFrameComponent(final float x, final float y) {
        this.XField.setText(new StringBuilder(String.valueOf(x)).toString());
        ((TitledBorder)this.XField.getBorder()).setBorder(FormattedLabelField.greenBorder);
        this.YField.setText(new StringBuilder(String.valueOf(y)).toString());
        ((TitledBorder)this.YField.getBorder()).setBorder(FormattedLabelField.greenBorder);
        this.repaint();
    }
    
    /**
     * Update the swing with value passed in parameters.
     * @param x the X value.
     * @param y the Y value.
     */
    public void updateFrameComponent(final String x, final String y) {
        this.XField.setText(x);
        ((TitledBorder)this.XField.getBorder()).setBorder(FormattedLabelField.greenBorder);
        this.YField.setText(y);
        ((TitledBorder)this.YField.getBorder()).setBorder(FormattedLabelField.greenBorder);
        this.repaint();
    }

	@Override
	public void mouseClicked(MouseEvent e)
	{
		TripleFormattedField.removeExtraSpaceFromField((JFormattedTextField) e.getSource());
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
		TripleFormattedField.removeExtraSpaceFromField((JFormattedTextField) e.getSource());
	}

	@Override
	public void mouseReleased(MouseEvent e)
	{
		// TODO Auto-generated method stub
		
	}

}
