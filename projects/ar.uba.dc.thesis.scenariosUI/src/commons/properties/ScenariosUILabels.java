package commons.properties;

import java.util.ResourceBundle;

import sba.common.properties.EnumPropertiesHelper;
import sba.common.properties.EnumProperty;

public enum ScenariosUILabels implements EnumProperty {
	CREATE_SCENARIO,
	SCENARIO,
	BASIC_DATA,
	NAME,
	CONCERN,
	STIMULUS_SOURCE,
	STIMULUS,
	ENVIRONMENT,
	ARTIFACT,
	RESPONSE,
	ENABLED,
	PRIORITY;

	@Override
	public String toString() {
		return EnumPropertiesHelper.getString(scenariosUILabels, this.name());
	}

	public String toString(Object... reemplazos) {
		return EnumPropertiesHelper.getString(scenariosUILabels, this.name(), reemplazos);
	}

	private static ResourceBundle scenariosUILabels = ResourceBundle.getBundle("scenariosUI_labels");
}
