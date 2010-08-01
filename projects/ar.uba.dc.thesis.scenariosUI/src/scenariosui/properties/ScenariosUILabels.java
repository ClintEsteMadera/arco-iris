package scenariosui.properties;

import java.util.ResourceBundle;

import commons.properties.EnumPropertiesHelper;
import commons.properties.EnumProperty;

public enum ScenariosUILabels implements EnumProperty {
	SELF_HEALING_CONFIG,
	CREATE_SELF_HEALING_CONFIG,
	CREATE_SCENARIO,
	SCENARIO,
	SCENARIOS,
	ATAM_SCENARIO_INFO,
	SELF_HEALING_SPECIFIC_INFO,
	ID,
	NAME,
	CONCERN,
	STIMULUS_SOURCE,
	STIMULUS,
	ENVIRONMENT,
	ENVIRONMENTS,
	SELECT_ENVIRONMENTS,
	ARTIFACT,
	ARTIFACTS,
	SELECT_ARTIFACT,
	PROPERTY,
	OPERATOR,
	RESPONSE,
	RESPONSE_MEASURE,
	CONSTRAINT,
	CONSTRAINT_TYPE,
	ENABLED,
	PRIORITY,
	FILE_DIALOG_MESSAGE,
	SYSTEM_NAME,
	FROM,
	TO,
	SUM;

	@Override
	public String toString() {
		return EnumPropertiesHelper.getString(scenariosUILabels, this.name());
	}

	public <T> String toString(T... replacements) {
		return EnumPropertiesHelper.getString(scenariosUILabels, this.name(), replacements);
	}

	private static ResourceBundle scenariosUILabels = ResourceBundle.getBundle("scenariosUI_labels");
}
