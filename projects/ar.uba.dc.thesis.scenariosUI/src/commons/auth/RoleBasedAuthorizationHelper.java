package commons.auth;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.ArrayUtils;

import sba.common.session.SessionHelper;

import commons.gui.action.GuiAction;

/**
 * Utiliza los roles del usuario conectado para verificar si posee permisos para efectuar una determinada acción en la
 * GUI.
 * 
 * 
 */

public class RoleBasedAuthorizationHelper implements AuthorizationHelper {

	public boolean isUserAuthorized(GuiAction guiAction) {
		boolean authorized = false;
		List<String> rolesDelUsuarioConectado = SessionHelper.rolesDelUsuarioConectado();
		String[] rolesNecesarios = permisos.get(guiAction.getUniqueId());

		for (String rolDelUsuario : rolesDelUsuarioConectado) {
			if (ArrayUtils.contains(rolesNecesarios, rolDelUsuario)) {
				authorized = true;
				break;
			}
		}
		return authorized;
	}

	public boolean isUserAuthorized(GuiAction... guiActions) {
		boolean authorized = false;
		for (GuiAction guiAction : guiActions) {
			authorized = authorized || isUserAuthorized(guiAction);
		}
		return authorized;
	}

	public void addGrantedPermissions(String identificadorUnicoGuiAction, String... grantedRoles) {
		permisos.put(identificadorUnicoGuiAction, grantedRoles);
	}

	private Map<String, String[]> permisos = new HashMap<String, String[]>();
}