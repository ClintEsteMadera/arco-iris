package commons.gui.util.purpose;

import commons.properties.EnumProperty;

public interface Purpose {

	boolean isCreation();

	boolean isEdition();

	boolean isView();

	boolean isDeletion();

	boolean isReadOnly();

	/**
	 * Provides the text used in the button that opens the dialog
	 */
	EnumProperty getTextForLauncherButton();

	/**
	 * Whether the button that opens the dialog should be initially enabled or not.
	 */
	boolean getLauncherButtonInitialEnabledState();

	/**
	 * @return the text for the "Accept" button.
	 */
	EnumProperty getAcceptButtonText();

	/**
	 * @return the text for the "Cancel" button.
	 */
	EnumProperty getCancelButtonText();
}
