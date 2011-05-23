package commons.auth;

import commons.gui.action.GuiAction;

/**
 * Specifies the interface of any object with the responsibility of authorizing actions in this GUI.
 * 
 */
public interface AuthorizationManager {

	public boolean isUserAuthorized(GuiAction guiAction);

	public boolean isUserAuthorizedForAny(GuiAction... guiActions);

	public void addGrantedPermissions(String guiActionUniqueId, String... grantedRoles);
}
