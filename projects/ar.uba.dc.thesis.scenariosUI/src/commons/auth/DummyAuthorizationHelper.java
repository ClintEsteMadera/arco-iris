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
 * $Id: DummyAuthorizationHelper.java,v 1.1 2008/04/21 15:06:25 cvschioc Exp $
 */

package commons.auth;

import commons.gui.action.GuiAction;

/**
 * Authorization Helper que provee acceso a todos los usuarios, para todas las acciones posibles.
 * 
 * @author Jonathan Chiocchio
 * @version $Revision: 1.1 $ $Date: 2008/04/21 15:06:25 $
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