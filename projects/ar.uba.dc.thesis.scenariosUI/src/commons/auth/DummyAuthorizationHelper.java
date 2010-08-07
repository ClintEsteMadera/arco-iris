package commons.auth;

import commons.gui.action.GuiAction;

/**
 * Authorization Helper que provee acceso a todos los usuarios, para todas las acciones posibles.
 * 
 * 
 */

public class DummyAuthorizationHelper implements AuthorizationHelper {

	public void addGrantedPermissions(String idUnicoGuiAction, String... grantedRoles) {
		// do nothing
	}

	public boolean isUserAuthorized(GuiAction guiAction) {
		return true;
	}

	public boolean isUserAuthorized(GuiAction... guiActions) {
		return true;
	}
}