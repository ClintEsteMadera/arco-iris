package commons.gui.menu;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;

import commons.gui.action.GuiAction;
import commons.gui.util.PageHelper;
import commons.properties.EnumProperty;

/**
 * 
 */
public abstract class MenuCreator {

	public MenuCreator() {
		super();
	}

	protected abstract void createMenu(MenuManager menuManager);

	protected void addMenuItem(MenuManager menu, Object menutext, GuiAction guiAction) {
		this.addMenuItem(menu, menutext, guiAction, true);
	}

	@SuppressWarnings("unchecked")
	protected void addMenuItem(MenuManager menu, Object menutext, GuiAction guiAction, boolean enabled) {
		if (PageHelper.getMainWindow().getAuthorizationManager().isUserAuthorized(guiAction)) {
			Action action = guiAction.getActionFor(null); // no se dispone del modelo todavía
			action.setEnabled(enabled);
			action.setText(menutext.toString() + "@" + guiAction.getShortcut());
			menu.add(action);
		}
	}

	protected MenuManager addSubmenu(MenuManager parent, EnumProperty label) {
		MenuManager menu = new MenuManager(label.toString(), label.toString());
		parent.add(menu);
		return menu;
	}

	/**
	 * Única instancia de un separador. Pensada para ser usada por las subclases.
	 */
	protected static final Separator SEPARATOR = new Separator();
}