package commons.properties;

import java.util.ResourceBundle;

import sba.common.properties.EnumPropertiesHelper;
import sba.common.properties.EnumProperty;

public enum CommonTooltips implements EnumProperty {

	ABOUT,
	EXIT,
	HELP;
	
	@Override
	public String toString() {
		return EnumPropertiesHelper.getString(tooltips, this.name());
	}

	public String toString(Object... reemplazos) {
		return EnumPropertiesHelper.getString(tooltips, this.name(), reemplazos);
	}
	
	private static ResourceBundle tooltips = ResourceBundle.getBundle("common_tooltips");
}