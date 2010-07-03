package commons.gui.util.purpose;

import commons.properties.EnumProperty;

public interface Purpose {

	boolean isCreation();

	boolean isEdition();

	boolean isView();

	boolean isReadOnly();

	/**
	 * Provides the text used in the button that opens the dialog
	 */
	EnumProperty getTextForLauncherButton();

	/**
	 * Whether the button that opens the dialog should be initially enabled or not.
	 */
	boolean getLauncherButtonInitialEnabledState();
}
