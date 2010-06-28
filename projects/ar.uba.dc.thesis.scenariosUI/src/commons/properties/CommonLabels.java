package commons.properties;

import java.util.ResourceBundle;

import sba.common.properties.EnumPropertiesHelper;
import sba.common.properties.EnumProperty;

public enum CommonLabels implements EnumProperty {

	AUTH_CHG_PWD,
	AUTH_CHG_PWD_LAST_PWD,
	AUTH_CHG_PWD_NEW_PWD,
	AUTH_CHG_PWD_CONFIRM_NEW_PWD,
	AUTH_USER,
	AUTH_PASSWORD,
	CALENDAR_CLOSE,
	CALENDAR_NONE,
	CALENDAR_TODAY,
	COPYRIGHT_TITLE,
	EXIT_DIALOG_TITLE,
	HELP_SHELL_TEXT,
	MENU_FILE,
	MENU_FILE_EXIT,
	MENU_FILE_PASSWORD_CHANGE,
	MENU_HELP,
	MENU_HELP_ABOUT,
	MENU_HELP_HELP,
	MENU_QUERIES_QUERY,
	SUCCESSFUL_OPERATION,

	NO_LABEL,
	SIN_ESPECIFICAR,
	EMPTY,
	ACEPTAR,
	CANCELAR,
	CLOSE,
	EDIT,
	VIEW,
	ACTIVAR,
	ANULAR,
	ELIMINAR,
	CREATION,
	BUSCAR,
	SELECCIONAR,
	NINGUNO,
	SAVE,
	LIMPIAR_FILTROS,
	CODIGO,
	DATOS_BASICOS,
	DATOS_DOMICILIO,
	DATOS_NOVASCALE,
	DENOMINACION,
	DENOMINACION_ABREVIADA,
	DESCRIPTION,
	DESDE,
	ESTADO,
	FECHA,
	HORA,
	HASTA,
	TIPO,
	OBSERVACIONES,
	FILTROS,
	DURACION,
	PORCENTAJE_LABEL,

	PORCENTAJE_SIMBOLO,
	EJERCICIO,

	ATENTION;

	@Override
	public String toString() {
		return EnumPropertiesHelper.getString(commonLabels, this.name());
	}

	public String toString(Object... reemplazos) {
		return EnumPropertiesHelper.getString(commonLabels, this.name(), reemplazos);
	}

	private static ResourceBundle commonLabels = ResourceBundle.getBundle("common_labels");
}