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
 * $Id: Messages.java,v 1.20 2008/04/09 21:06:43 cvscalab Exp $
 */

package commons.properties;

import java.text.MessageFormat;
import java.util.ResourceBundle;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author Jonathan Chiocchio
 * @version $Revision: 1.20 $ $Date: 2008/04/09 21:06:43 $
 */
public enum Messages implements EnumProperty {

	SUCCESSFUL_SCENARIO, SUCCESSFUL_ENVIRONMENT, SUCCESSFUL_ARTIFACT;

	@Override
	public String toString() {
		return messages.getString(this.name());
	}

	public String toString(Object... reemplazos) {
		String result = "";
		try {
			result = MessageFormat.format(this.toString(), reemplazos);
		} catch (IllegalArgumentException ex) {
			log.error("No se pudo formatear el mensaje: " + this.name());
		}
		return result;
	}

	private static ResourceBundle messages = ResourceBundle.getBundle("messages");

	private static final Log log = LogFactory.getLog(Messages.class);
}