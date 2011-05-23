package commons.auth;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.ArrayUtils;

import commons.gui.action.GuiAction;
import commons.session.SessionHelper;

/**
 * This Authorization Manager uses the roles of the connected user to grant or deny access to some action(s)in the GUI.
 */
public class RoleBasedAuthorizationManager implements AuthorizationManager {

	private Map<String, String[]> permissions = new HashMap<String, String[]>();

	public boolean isUserAuthorized(GuiAction guiAction) {
		boolean authorized = false;
		List<String> connectedUserRoles = SessionHelper.rolesDelUsuarioConectado();
		String[] necessaryRoles = permissions.get(guiAction.getUniqueId());

		for (String userRole : connectedUserRoles) {
			if (ArrayUtils.contains(necessaryRoles, userRole)) {
				authorized = true;
				break;
			}
		}
		return authorized;
	}

	public boolean isUserAuthorizedForAny(GuiAction... guiActions) {
		boolean authorized = false;
		for (GuiAction guiAction : guiActions) {
			authorized = authorized || isUserAuthorized(guiAction);
		}
		return authorized;
	}

	public void addGrantedPermissions(String uniqueIdentifier, String... grantedRoles) {
		permissions.put(uniqueIdentifier, grantedRoles);
	}
}