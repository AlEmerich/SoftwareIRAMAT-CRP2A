package interfaceObject;

import java.util.EventListener;

/**
 * Providing a way for communicate between classes, when it is about material. 
 * It is usefull when two class uses two different JList but the same list of material. 
 * All classes must have known about the change of the material list.
 * @author alan
 * @see EventListener
 */
public interface MaterialAddedListener extends EventListener
{
	/**
	 * Call when a material is added to the primary list.
	 */
    void materialAdded();
}
