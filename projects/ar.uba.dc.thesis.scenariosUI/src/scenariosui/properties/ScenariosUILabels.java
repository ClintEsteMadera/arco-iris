package scenariosui.properties;

import java.util.ResourceBundle;

import sba.common.properties.EnumPropertiesHelper;
import sba.common.properties.EnumProperty;

public enum ScenariosUILabels implements EnumProperty {
	SELF_HEALING_CONFIG,
	CREATE_SELF_HEALING_CONFIG,
	CREATE_SCENARIO,
	SCENARIO,
	SCENARIOS,
	BASIC_DATA,
	ID,
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

	public String toString(Object... replacements) {
		return EnumPropertiesHelper.getString(scenariosUILabels, this.name(), replacements);
	}

	private static ResourceBundle scenariosUILabels = ResourceBundle.getBundle("scenariosUI_labels");
}
