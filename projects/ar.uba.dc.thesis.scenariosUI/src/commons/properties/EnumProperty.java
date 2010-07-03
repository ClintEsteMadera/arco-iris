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
