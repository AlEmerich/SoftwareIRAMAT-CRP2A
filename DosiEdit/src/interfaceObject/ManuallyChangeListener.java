package interfaceObject;

import mainPackage.Material;

/**
 * Provides an easy way to communicate with the {@link OngletManagement}.
 * @author alan
 */
public interface ManuallyChangeListener
{
	/**
	 * Go to the forge with the current material specified.
	 * @param m the material to pass.
	 */
    void goToForgeMaterial(final Material m);
}
