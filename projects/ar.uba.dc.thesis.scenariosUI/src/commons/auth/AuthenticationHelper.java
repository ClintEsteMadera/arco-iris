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
 * $Id: AuthenticationHelper.java,v 1.2 2008/01/17 20:10:56 cvschioc Exp $
 */

package commons.auth;


/**
 * Interfaz que define métodos relacionados con la autenticación de un Usuario.
 * @author Jonathan Chiocchio
 * @version $Revision: 1.2 $ $Date: 2008/01/17 20:10:56 $
 */

public interface AuthenticationHelper {

	boolean authenticate(String username, String password);
	
	public boolean tiempoRemanenteExpirado();
	
	public void cambiarPassword(String oldPassword, String newPassword);
}
