package scenariosui.properties;

import java.text.MessageFormat;
import java.util.ResourceBundle;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import commons.properties.EnumProperty;

public enum ScenariosUIMessages implements EnumProperty {

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

	private static ResourceBundle messages = ResourceBundle.getBundle("scenariosUImessages");

	private static final Log log = LogFactory.getLog(ScenariosUIMessages.class);
}