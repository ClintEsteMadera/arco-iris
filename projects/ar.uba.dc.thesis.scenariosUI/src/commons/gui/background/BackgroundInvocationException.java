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
