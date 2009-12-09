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
* $Id: BackgroundInvocationException.java,v 1.1 2008/01/29 13:57:54 cvspasto Exp $
*/

package commons.gui.background;


/**
 *
 * @author Pablo Pastorino
 * @version $Revision: 1.1 $ $Date: 2008/01/29 13:57:54 $
 */

public class BackgroundInvocationException 
extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public BackgroundInvocationException(String message, Throwable cause) {
		super(message, cause);
}

	public BackgroundInvocationException(Throwable cause) {
		super(cause);
	}
	
	
}