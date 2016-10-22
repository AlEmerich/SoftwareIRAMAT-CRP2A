package iramat.dosiedit2d.view;

import java.awt.Dimension;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.border.TitledBorder;

import iramat.dosiseed.view.FormattedLabelField;
import iramat.dosiseed.view.TripleFormattedField;

/**
 * Class extending {@link TripleFormattedField} to add new {@link JFormattedTextField}
 * @author alan
 *
 */
public class ExtensibleFormattedPanel extends TripleFormattedField
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 455910676774722258L;

	/**
	 * List of new fields.
	 */
	List<JFormattedTextField> fields;
	
	/**
	 * Constructor.
	 * @param label the description of fields.
	 * @param allowsNegative true if negative value is admit, false if not.
	 */
	public ExtensibleFormattedPanel(JLabel label, boolean allowsNegative)
	{
		super(label, allowsNegative);
		fields = new ArrayList<>();
	}

	/**
	 * Constructor.
	 * @param label the description of fields.
	 * @param x the title border of first field.
	 * @param y the title border of second field.
	 * @param z the title border of third field.
	 * @param allowsNegative true if negative value is admit, false if not.
	 */
	public ExtensibleFormattedPanel(JLabel label, String x, String y, String z,
			boolean allowsNegative)
	{
		super(label, x, y, z, allowsNegative);
		fields = new ArrayList<>();
	}
	
	/**
	 * Add new field.
	 * @param desc the description to set in title.
	 * @param allowsNegative true if negative value is admit, false if not.
	 */
	public void addFields(String desc,boolean allowsNegative)
	{
		JFormattedTextField field = new JFormattedTextField(createmask(allowsNegative));
		field.setFocusLostBehavior(3);
        field.setPreferredSize(new Dimension(60, 40));
        field.setMaximumSize(field.getPreferredSize());
        field.setBorder(BorderFactory.createTitledBorder(FormattedLabelField.greenBorder, desc));
        this.add(field);
        fields.add(field);
	}
	
	/**
	 * Get the field to the specified index.
	 * @param index the index of the asked field.
	 * @return the field.
	 */
	public JFormattedTextField getFieldFromList(int index)
	{
		return fields.get(index);
	}
	
	/**
     * Update the swing with value passed in parameters.
     * @param x the X value.
     * @param y the Y value.
     * @param z the Z value.
     */
    public void updateFrameComponent(final float x, final float y, final float z, final float ud) {
        super.updateFrameComponent(x, y, z);
        
        for(JFormattedTextField field : fields)
        {
        	field.setText(new StringBuilder(String.valueOf(ud)).toString());
            ((TitledBorder)field.getBorder()).setBorder(FormattedLabelField.greenBorder);
        }
        this.repaint();
    }
    
    /**
     * Update the swing with value passed in parameters.
     * @param x the X value.
     * @param y the Y value.
     * @param z the Z value.
     */
    public void updateFrameComponent(final String x, final String y, final String z,final String ud) {
        super.updateFrameComponent(x, y, z);
        
        for(JFormattedTextField field : fields)
        {
        	field.setText(new StringBuilder(String.valueOf(ud)).toString());
            ((TitledBorder)field.getBorder()).setBorder(FormattedLabelField.greenBorder);
        }
        this.repaint();
    }
    
    @Override
    public void addKeyListener(final KeyListener k) {
    	super.addKeyListener(k);
    	for(JFormattedTextField field : fields)
        {
    		field.addKeyListener(k);
    		field.addKeyListener(this);
    		field.addMouseListener(this);
        }
    }
    
    @Override
    public void keyPressed(final KeyEvent e) {
    	
        if (e.getKeyCode() != 10) {
        	super.keyPressed(e);
        	for(JFormattedTextField field : fields)
            {
        		if (e.getSource() == field) {
                    ((TitledBorder)field.getBorder()).setBorder(FormattedLabelField.redBorder);
                }
            }
        }
    }
    
    @Override
    public void keyReleased(final KeyEvent e) {
    }
    
    @Override
    public void keyTyped(final KeyEvent e) {
    	if (e.getKeyCode() != 10) {
        	super.keyPressed(e);
        	for(JFormattedTextField field : fields)
            {
        		if (e.getSource() == field) {
                    ((TitledBorder)field.getBorder()).setBorder(FormattedLabelField.redBorder);
                }
            }
        }
    }
    
    @Override
    public void setToUpdate() {
    	super.setToUpdate();
    	for(JFormattedTextField field : fields)
            ((TitledBorder)field.getBorder()).setBorder(FormattedLabelField.redBorder);
        this.repaint();
    }
}
