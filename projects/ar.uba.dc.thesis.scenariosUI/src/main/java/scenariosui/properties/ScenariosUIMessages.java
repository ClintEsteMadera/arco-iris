package scenariosui.properties;

import java.text.MessageFormat;
import java.util.ResourceBundle;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import commons.properties.EnumProperty;

public enum ScenariosUIMessages implements EnumProperty {

	SUCCESSFUL_SCENARIO,
	SUCCESSFUL_ENVIRONMENT,
	SUCCESSFUL_ARTIFACT,
	SUCCESSFUL_CONSTRAINT,
	SELECT_STITCH_DIRECTORY,
	SELECT_REPAIR_STRATEGIES,
	NO_REPAIR_STRATEGIES_FOUND,
	INVALID_DIRECTORY,
	FILE_DIALOG_MESSAGE;

	@Override
	public String toString() {
		return messages.getString(this.name());
	}

	public <T> String toString(T... replacements) {
		String result = "";
		try {
			result = MessageFormat.format(this.toString(), replacements);
		} catch (IllegalArgumentException ex) {
			log.error("Cannot format the message: " + this.name());
		}
		return result;
	}

	private static ResourceBundle messages = ResourceBundle.getBundle("scenariosUImessages");

	private static final Log log = LogFactory.getLog(ScenariosUIMessages.class);
}