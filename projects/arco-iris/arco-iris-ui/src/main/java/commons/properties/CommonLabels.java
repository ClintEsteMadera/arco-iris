package commons.properties;

import java.util.ResourceBundle;

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
	NOT_SPECIFIED,
	EMPTY,
	ACCEPT,
	CANCEL,
	YES,
	NO,
	CLOSE,
	EDIT,
	EDIT_SELECTED_ITEM,
	VIEW,
	ACTIVATE,
	ANNUL,
	DELETE,
	DELETE_SELECTED_ITEM,
	ADD,
	ADD_NEW,
	INSERT,
	INSERT_NEW,
	CREATION,
	SEARCH,
	SELECT,
	NONE,
	SAVE,
	LOAD,
	CLEAN_FILTERS,
	CODE,
	BASIC_INFORMATION,
	ADDRESS,
	DENOMINATION,
	BRIEF_DENOMINATION,
	DESCRIPTION,
	SINCE,
	STATE,
	DATE,
	HOUR,
	TO,
	TYPE,
	NOTES,
	FILTERS,
	LENGTH,
	ATTENTION,
	PERCENTAGE_LABEL,
	PERCENTAGE_SYMBOL,
	DOT,
	CLEAR;

	@Override
	public String toString() {
		return EnumPropertiesHelper.getString(commonLabels, this.name());
	}

	public String toString(Object... reemplazos) {
		return EnumPropertiesHelper.getString(commonLabels, this.name(), reemplazos);
	}

	private static ResourceBundle commonLabels = ResourceBundle.getBundle("common_labels");
}