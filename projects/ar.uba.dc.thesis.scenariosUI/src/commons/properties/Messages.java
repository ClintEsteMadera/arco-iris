package commons.properties;

import java.text.MessageFormat;
import java.util.ResourceBundle;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 
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