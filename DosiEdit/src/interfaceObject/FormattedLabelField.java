package interfaceObject;

import java.awt.Dimension;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.text.ParseException;

import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.text.MaskFormatter;

/**
 * Class to have a field formatted by a mask. This is usefull to have the same mask on all fields. 
 * You can specified a description in JLabel to the left, or not. The field just authorizes the characters "123456789.".
 * @author alan
 *
 */
public class FormattedLabelField extends JPanel implements KeyListener, MouseListener
{
    private static final long serialVersionUID = 7391273176647912266L;
    
    /**
     * UI objects.
     */
    private JLabel label;
    private JFormattedTextField field;
    
    /**
     * The format of the text.
     */
    private MaskFormatter mask;
    
    /**
     * The parent of the object.
     */
    private MainInfoVoxel father;
    
    /**
     * Constructor.
     * @param mainInfoVoxel the father.
     */
    public FormattedLabelField(final MainInfoVoxel mainInfoVoxel) {
        this.label = null;
        this.mask = null;
        this.father = mainInfoVoxel;
        this.init();
    }
    
    /**
     * Constructor with a JLabel description.
     * @param mainInfoVoxel the father.
     * @param description the title of the field.
     */
    public FormattedLabelField(final MainInfoVoxel mainInfoVoxel, final JLabel description) {
        this.label = null;
        this.mask = null;
        this.father = mainInfoVoxel;
        this.label = description;
        this.init();
    }
    
    /**
     * Initialization of all components.
     */
    private void init() {
        try {
            this.mask = new MaskFormatter("***************");
        }
        catch (ParseException e) {
            e.printStackTrace();
        }
        this.mask.setValidCharacters("0123456789.");
        this.mask.setAllowsInvalid(false);
        (this.field = new JFormattedTextField(this.mask)).setFocusLostBehavior(3);
        this.field.setPreferredSize(new Dimension(60, 25));
        this.field.setMaximumSize(this.field.getPreferredSize());
        this.field.setBorder(MainInfoVoxel.greenBorder);
        if (this.label != null) {
            this.add(this.label);
        }
        this.add(this.field);
        this.field.addKeyListener(this);
        this.field.addMouseListener(this);
    }
    
    /**
     * @return the JLabel description.
     */
    public JLabel getLabel() {
        return this.label;
    }
    
    /**
     * Change the description.
     * @param label the new description
     */
    public void setLabel(final JLabel label) {
        this.label = label;
    }
    
    /**
     * @return the JFormattedTextField containing the value.
     */
    public JFormattedTextField getField() {
        return this.field;
    }
    
    /**
     * @return the value in {@link FormattedLabelField$field} parsing as an Integer.
     * @throws NumberFormatException 
     */
    public int getFieldAsInt() throws NumberFormatException {
        if (!this.fieldIsEmpty()) {
            return (int)Float.parseFloat(this.field.getText().replaceAll("\\s+", ""));
        }
        return -1;
    }
    
    /**
     * @return the value in {@link FormattedLabelField$field} parsing as an Float.
     * @throws NumberFormatException 
     */
    public float getFieldAsFloat() throws NumberFormatException {
        if (!this.fieldIsEmpty()) {
            return Float.parseFloat(this.field.getText().replaceAll("\\s+", ""));
        }
        return -1.0f;
    }
    
    /**
     * @return true is the field containing no character (the spaces doesn't count), false if not.
     */
    public boolean fieldIsEmpty() {
        return this.field.getText().replaceAll("\\s+", "").isEmpty();
    }
    
    /**
     * Change the border to green.
     */
    public void isUpdate() {
        this.field.setBorder(MainInfoVoxel.greenBorder);
    }
    
    /**
     * Triggered when the key is pressed within fields.
     */
    @Override
    public void keyPressed(final KeyEvent e) {
        if (e.getKeyCode() != 10) {
            this.field.setBorder(MainInfoVoxel.redBorder);
        }
        else {
            this.field.setBorder(MainInfoVoxel.greenBorder);
            this.father.validateFields();
        }
    }
    
    /**
     * Triggered when a key is released within fields.
     */
    @Override
    public void keyReleased(final KeyEvent e) {
    }
    
    @Override
    public void keyTyped(final KeyEvent e) {
        if (e.getKeyCode() != 10) {
            this.field.setBorder(MainInfoVoxel.redBorder);
        }
        else {
            this.field.setBorder(MainInfoVoxel.greenBorder);
            this.father.validateFields();
        }
    }
    
    public static void removeExtraSpaceFromField(JFormattedTextField field)
    {
    	field.setText(field.getText().trim());
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
