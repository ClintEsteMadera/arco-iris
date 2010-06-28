package scenariosui.gui.menu;

import org.eclipse.jface.action.MenuManager;

import commons.gui.menu.MenuCreator;

import scenariosui.gui.action.ScenariosUIActions;


public class SelfHealingConfigurationMenuCreator extends MenuCreator {

	@Override
	public void createMenu(MenuManager menuFile) {
		addMenuItem(menuFile, "New...", ScenariosUIActions.NEW_SELF_HEALING_CONFIG);

		addMenuItem(menuFile, "Open...", ScenariosUIActions.OPEN_SELF_HEALING_CONFIG);

		// if (ScenariosUIController.isThereAConfigurationOpen()) {
		// addMenuItem(menuFile, "Close", ScenariosUIActions.CLOSE_SELF_HEALING_CONFIG);
		// }
	}
}