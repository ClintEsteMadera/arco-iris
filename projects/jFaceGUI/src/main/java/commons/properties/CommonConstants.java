package commons.properties;

import java.util.Properties;

public enum CommonConstants implements EnumProperty {

	/*
	 * Procurar agregar las constantes en orden alfabético, para una búsqueda más rápida.
	 */
	APP_CODE_NAME, APP_NAME, COPYRIGHT_TEXT, HELP_URL, PREFERENCES_FILE, CONTACTOS;

	@Override
	public String toString() {
		return EnumPropertiesHelper.getString(props, this.name());
	}

	public String toString(Object... reemplazos) {
		return EnumPropertiesHelper.getString(props, this.name(), reemplazos);
	}

	private static Properties props = EnumPropertiesHelper.load("/common_constants.properties");
}