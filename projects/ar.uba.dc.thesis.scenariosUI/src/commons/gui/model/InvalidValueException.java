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
 * $Id: InvalidValueException.java,v 1.2 2008/01/28 17:00:04 cvschioc Exp $
 */

package commons.gui.model;

/**
 * Excepcion que se�ala un cambio de valor incorrecto (que debe ser revertido)
 * @author Pablo Pastorino
 * @version $Revision: 1.2 $ $Date: 2008/01/28 17:00:04 $
 */
public class InvalidValueException extends RuntimeException {

	public InvalidValueException() {
		super();
		// TODO InvalidValueException IMPLEMENTAR!
	}

	public InvalidValueException(String message, Throwable cause) {
		super(message, cause);
		// TODO InvalidValueException IMPLEMENTAR!
	}

	public InvalidValueException(String message) {
		super(message);
		// TODO InvalidValueException IMPLEMENTAR!
	}

	public InvalidValueException(Throwable cause) {
		super(cause);
		// TODO InvalidValueException IMPLEMENTAR!
	}

	private static final long serialVersionUID = 1L;
}