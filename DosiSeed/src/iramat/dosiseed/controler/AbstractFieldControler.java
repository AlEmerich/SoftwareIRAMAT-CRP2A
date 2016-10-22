package iramat.dosiseed.controler;

import iramat.dosiseed.model.AbstractModel;

import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.EventObject;

import javax.swing.JFormattedTextField;


public abstract class AbstractFieldControler implements KeyListener, FocusListener
{
	protected AbstractModel model;
	
	public AbstractFieldControler(AbstractModel model)
	{
		this.model = model;
	}

	/*************************************************************************
	 * 			GETTER METHODS FROM THE VIEW (GLOBAL) 
	 *************************************************************************/
    protected float getFloatOfSource(EventObject source)
    {
    	JFormattedTextField field = (JFormattedTextField) source.getSource();
    	if (!field.getText().replaceAll("\\s+", "").isEmpty()) {
            return Float.parseFloat(field.getText().replaceAll("\\s+", ""));
        }
        return -1.0f;
    }
    
    protected String getStringOfSource(EventObject source)
    {
    	JFormattedTextField field = (JFormattedTextField) source.getSource();
    	return field.getText().trim();
    }
    
	@Override
	public abstract void focusGained(FocusEvent arg0);

	@Override
	public abstract void focusLost(FocusEvent arg0);
	
	@Override
	public abstract void keyPressed(KeyEvent arg0);

	@Override
	public abstract void keyReleased(KeyEvent arg0);

	@Override
	public abstract void keyTyped(KeyEvent arg0);

}
