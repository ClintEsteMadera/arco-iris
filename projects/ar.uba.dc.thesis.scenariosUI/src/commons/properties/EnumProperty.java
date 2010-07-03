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
 * $Id: EnumProperty.java,v 1.3 2008/01/31 17:09:57 cvschioc Exp $
 */

package commons.properties;

import java.io.Serializable;

/**
 * @author Jonathan Chiocchio
 * @version $Revision: 1.3 $ $Date: 2008/01/31 17:09:57 $
 */

public interface EnumProperty extends Serializable {

	/**
	 * Ver {@link Object#toString()}
	 * @return el valor de la propiedad RESUELTO (i.e. listo para ser usado)
	 */
	public String toString();

	/**
	 * Ver {@link Object#toString()}
	 * @return el valor de la propiedad RESUELTO con los reemplazos hechos(i.e. listo para ser usado)
	 */
	public <T> String toString(T... replacements);

	/**
	 * Ver {@link Enum#name()}
	 * @return el valor de la constante enumerada
	 */
	public String name();
}
