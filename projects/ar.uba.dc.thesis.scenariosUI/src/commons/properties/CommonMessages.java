package commons.properties;

import java.util.ResourceBundle;

import sba.common.properties.EnumPropertiesHelper;
import sba.common.properties.EnumProperty;

public enum CommonMessages implements EnumProperty {

	AUTH_LOGIN_MESSAGE,
	AUTH_LOGIN_ERROR_MSG,
	AUTH_LOGIN_TITLE,
	EXIT_DIALOG_MESSAGE,
	NO_SELECTED_ELEMENT,

	QUERY_NO_RESULTS,
	QUERY_ONLY_ONE_RESULT,
	QUERY_MORE_THAN_ONE_RESULT;

	@Override
	public String toString() {
		return EnumPropertiesHelper.getString(messages, this.name());
	}

	public String toString(Object... reemplazos) {
		return EnumPropertiesHelper.getString(messages, this.name(), reemplazos);
	}

	private static ResourceBundle messages = ResourceBundle.getBundle("common_messages");
}
