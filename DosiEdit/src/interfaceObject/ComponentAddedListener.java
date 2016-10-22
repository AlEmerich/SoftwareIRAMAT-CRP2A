package interfaceObject;

import java.util.EventListener;

import mainPackage.Component;

/**
 * Providing a way for communicate between classes, when it is about component. 
 * It is usefull when two class uses two different JList but the same list of component. 
 * All classes must have known about the change of the component list.
 * @author alan
 * @see EventListener
 */
public interface ComponentAddedListener extends EventListener
{
	/**
	 * Call when a component is added to the primary component list.
	 * @param p0 the component in question.
	 */
    void componentAdded(final Component p0);
}
