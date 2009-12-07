package sba.common.anotaciones.main;

import java.util.Properties;

import sba.common.properties.EnumPropertiesHelper;
import sba.common.properties.EnumProperty;

public enum Constants implements EnumProperty {

	/*
	 * Procurar agregar las constantes en orden alfabético, para una búsqueda más rápida.
	 */
	DESCRIPCION,
	NOMBRE,
	APELLIDO,
	EMPLEADO, DESCRIPCION_CORTA, TIPO_EMPLEADO_INVALIDO, DOCUMENTO
	;

	@Override
	public String toString() {
		return EnumPropertiesHelper.getString(props, this.name());
	}

	public String toString(Object... reemplazos) {
		return EnumPropertiesHelper.getString(props, this.name(), reemplazos);
	}

	private static Properties props = EnumPropertiesHelper.load(Constants.class);
}