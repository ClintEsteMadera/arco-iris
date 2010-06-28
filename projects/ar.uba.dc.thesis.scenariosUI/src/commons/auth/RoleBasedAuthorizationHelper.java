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
 * $Id: RoleBasedAuthorizationHelper.java,v 1.1 2008/04/18 20:55:06 cvschioc Exp $
 */

package commons.auth;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.ArrayUtils;

import sba.common.session.SessionHelper;

import commons.gui.action.GuiAction;

/**
 * Utiliza los roles del usuario conectado para verificar si posee permisos para efectuar una
 * determinada acción en la GUI.
 * 
 * @author Jonathan Chiocchio
 * @version $Revision: 1.1 $ $Date: 2008/04/18 20:55:06 $
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