package scenariosui.properties;

import java.util.ResourceBundle;

import commons.properties.EnumPropertiesHelper;
import commons.properties.EnumProperty;

public enum ScenariosUILabels implements EnumProperty {
	SELF_HEALING_CONFIG,
	CREATE_SELF_HEALING_CONFIG,
	CREATE_SCENARIO,
	SELF_HEALING_SCENARIO,
	SCENARIOS,
	ATAM_SCENARIO_INFO,
	SELF_HEALING_SPECIFIC_INFO,
	ID,
	NAME,
	CONCERN,
	ANY_STIMULUS,
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
	REPAIR_STRATEGIES,
	ENABLED,
	PRIORITY,
	SYSTEM_NAME,
	FROM,
	TO,
	SUM,
	CONDITIONS;

	@Override
	public String toString() {
		return EnumPropertiesHelper.getString(scenariosUILabels, this.name());
	}

	public <T> String toString(T... replacements) {
		return EnumPropertiesHelper.getString(scenariosUILabels, this.name(), replacements);
	}

	private static ResourceBundle scenariosUILabels = ResourceBundle.getBundle("scenariosUI_labels");
}
