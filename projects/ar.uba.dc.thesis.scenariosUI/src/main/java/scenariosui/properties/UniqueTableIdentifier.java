package scenariosui.properties;

import java.util.Properties;

import commons.properties.EnumPropertiesHelper;
import commons.properties.EnumProperty;

/**
 * These names will be used in the preferences xml file.
 */

public enum UniqueTableIdentifier implements EnumProperty {

	SCENARIOS,
	ENVIRONMENTS,
	ENVIRONMENT_SELECTION,
	ARTIFACTS,
	ARTIFACT_SELECTION,
	REPAIR_STRATEGY_SELECTION,
	ENVIRONMENT_CONSTRAINTS;

	@Override
	public String toString() {
		return EnumPropertiesHelper.getString(props, this.name());
	}

	public String toString(Object... reemplazos) {
		return EnumPropertiesHelper.getString(props, this.name(), reemplazos);
	}

	private static Properties props = EnumPropertiesHelper.load("/uniquetableidentifier.properties");
}