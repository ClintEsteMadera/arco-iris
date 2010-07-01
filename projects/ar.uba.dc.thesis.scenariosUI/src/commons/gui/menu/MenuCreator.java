/*
 * Licencia de Caja de Valores S.A., Versión 1.0
 *
 * Copyright (c) 2006 Caja de Valores S.A.
 * 25 de Mayo 362, Ciudad Autónoma de Buenos Aires, República Argentina
 * Todos los derechos reservados.
 *
 * Este software es información confidencial y propietaria de Caja de Valores S.A. ("Información
 * Confidencial"). Usted no divulgará tal Información Confidencial y la usará solamente de acuerdo a
 * los términos del acuerdo de licencia que posee con Caja de Valores S.A.
 */

/*
 * $Id: MenuCreator.java,v 1.8 2008/04/18 20:55:06 cvschioc Exp $
 */
package commons.gui.menu;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;

import sba.common.properties.EnumProperty;

import commons.gui.action.GuiAction;
import commons.gui.util.PageHelper;

/**
 * @author Gabriel Tursi
 * @version $Revision: 1.8 $ $Date: 2008/04/18 20:55:06 $
 */
public abstract class MenuCreator {

	public MenuCreator() {
		super();
	}

	protected abstract void createMenu(MenuManager menuManager);

	@SuppressWarnings("unchecked")
	protected void addMenuItem(MenuManager menu, Object menutext, GuiAction guiAction) {
		this.addMenuItem(menu, menutext, guiAction, true);
	}

	@SuppressWarnings("unchecked")
	protected void addMenuItem(MenuManager menu, Object menutext, GuiAction guiAction, boolean enabled) {
		if (PageHelper.getMainWindow().getAuthorizationHelper().isUserAuthorized(guiAction)) {
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