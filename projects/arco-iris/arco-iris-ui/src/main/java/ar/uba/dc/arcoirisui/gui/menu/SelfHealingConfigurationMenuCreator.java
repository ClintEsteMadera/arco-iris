package ar.uba.dc.arcoirisui.gui.menu;

import org.eclipse.jface.action.MenuManager;

import ar.uba.dc.arcoirisui.gui.action.ArcoIrisUIActions;

import commons.gui.menu.MenuCreator;

public class SelfHealingConfigurationMenuCreator extends MenuCreator {

	@Override
	public void createMenu(MenuManager menuFile) {
		addMenuItem(menuFile, "New...", ArcoIrisUIActions.NEW_SELF_HEALING_CONFIG);

		addMenuItem(menuFile, "Open...", ArcoIrisUIActions.OPEN_SELF_HEALING_CONFIG);

		addMenuItem(menuFile, "Close", ArcoIrisUIActions.CLOSE_SELF_HEALING_CONFIG, false);

		menuFile.add(SEPARATOR);
	}
}