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
 * $Id: DummyAuthenticationHelper.java,v 1.3 2008/01/17 20:10:56 cvschioc Exp $
 */

package commons.auth;

/**
 * Helper que no realiza ninguna acción (dummy)
 * @author Pablo Pastorino
 * @version $Revision: 1.3 $ $Date: 2008/01/17 20:10:56 $
 */

public class DummyAuthenticationHelper implements AuthenticationHelper {

	public boolean authenticate(String username, String password) {
		return true;
	}

	public boolean tiempoRemanenteExpirado() {
		return false;
	}

	public void cambiarPassword(String oldPassword, String newPassword) {
		// do nothing		
	}
}