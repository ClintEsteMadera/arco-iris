package commons.auth;

import commons.gui.action.GuiAction;

/**
 * This Authorization Manager grants access to any user, for every possible action.
 */
@SuppressWarnings("unchecked")
public class DummyAuthorizationManager implements AuthorizationManager {

	public void addGrantedPermissions(String uniqueIdentifier, String... grantedRoles) {
		// do nothing
	}

	public boolean isUserAuthorized(GuiAction guiAction) {
		return true;
	}

	public boolean isUserAuthorized(GuiAction... guiActions) {
		return true;
	}
}