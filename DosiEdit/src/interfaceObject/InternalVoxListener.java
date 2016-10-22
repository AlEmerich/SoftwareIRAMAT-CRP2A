package interfaceObject;

import java.util.EventListener;

/**
 * A class implementing this interface has to implementing the create world function.
 * @author alan
 */
public interface InternalVoxListener extends EventListener
{
	/**
	 * Call to load the internal 3D world.
	 */
    void createWorld();
}
