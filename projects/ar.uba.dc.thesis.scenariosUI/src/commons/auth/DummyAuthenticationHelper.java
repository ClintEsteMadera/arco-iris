/*
 * Licencia de Caja de Valores S.A., Versi�n 1.0
 *
 * Copyright (c) 2006 Caja de Valores S.A.
 * 25 de Mayo 362, Ciudad Aut�noma de Buenos Aires, Rep�blica Argentina
 * Todos los derechos reservados.
 *
 * Este software es informaci�n confidencial y propietaria de Caja de Valores S.A. ("Informaci�n
 * Confidencial"). Usted no divulgar� tal Informaci�n Confidencial y la usar� solamente de acuerdo a
 * los t�rminos del acuerdo de licencia que posee con Caja de Valores S.A.
 */

/*
 * $Id: DummyAuthenticationHelper.java,v 1.3 2008/01/17 20:10:56 cvschioc Exp $
 */

package commons.auth;

/**
 * Helper que no realiza ninguna acci�n (dummy)
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