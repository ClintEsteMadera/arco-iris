package commons.auth;

import commons.gui.action.GuiAction;

/**
 * This Authorization Manager grants access to any user, for every possible action.
 */
public class DummyAuthorizationManager implements AuthorizationManager {

	public void addGrantedPermissions(String uniqueIdentifier, String... grantedRoles) {
		// do nothing
	}

	public boolean isUserAuthorized(GuiAction guiAction) {
		return true;
	}

	public boolean isUserAuthorizedForAny(GuiAction... guiActions) {
		return true;
	}
}