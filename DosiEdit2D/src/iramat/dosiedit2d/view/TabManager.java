package iramat.dosiedit2d.view;

import iramat.dosiedit2d.controler.GlobalControler;
import iramat.dosiseed.controler.ForgeControler;
import iramat.dosiseed.model.AbstractModel;
import iramat.dosiseed.model.ColoredMaterial;
import iramat.dosiseed.view.ForgeWindow;

import javax.swing.ImageIcon;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;

/**
 * Class extending {@link JTabbedPane} to handles forge tab panel and editor tab panel.
 * The {@link JTabbedPane#setSelectedIndex(int)} is overridden to check validation before
 * changing the current tab. The user must not work with inconsistent material.
 * @author alan
 *
 */
public class TabManager extends JTabbedPane
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 626887498883972663L;

	/**
	 * The speediness of the scrolling bar.
	 */
	private static int UNIT_INCREMENT_SCROLL = 10;
	
	/**
	 * The view for the forge.
	 */
	private ForgeWindow forge;
	
	/**
	 * The view for the edition of the world.
	 */
	private EditorGridPane editor;

	/**
	 * Constructor.
	 * @param forgeControler the forge listener.
	 * @param controler the global listener.
	 * @param model the model to work with.
	 * @param forgeIcon the icon to put to the forge tab.
	 * @param editorIcon the icon to put to the editor tab.
	 */
	public TabManager(ForgeControler forgeControler, GlobalControler controler,
			AbstractModel model, ImageIcon forgeIcon,
			ImageIcon editorIcon)
	{
		JScrollPane scroll1 = new JScrollPane(forge = new ForgeWindow(forgeControler,model,true));
		scroll1.getVerticalScrollBar().setUnitIncrement(UNIT_INCREMENT_SCROLL);
		this.addTab("Forge", forgeIcon, scroll1);
		
		editor = new EditorGridPane(this,model,controler);
		this.addTab("Editor", editorIcon, editor);
	}
	
	public EditorGridPane getEditorPane()
	{
		return editor;
	}
	
	/**
	 * Explicitly demands to go the the forge material at the specified material. Call by {@link MaterialPopupMenu}
	 * in contextual menu in {@link EditorGridPane}
	 * @param material the current material to go.
	 */
	public void goToForgeMaterial(ColoredMaterial material)
	{
		this.setSelectedIndex(0);
		forge.setCurrentMaterial(material);
	}

}
