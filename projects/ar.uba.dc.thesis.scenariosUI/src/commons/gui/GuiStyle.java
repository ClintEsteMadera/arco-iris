package commons.gui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;

/**
 * Clase que posee información sobre estilos de la GUI.
 * 
 */

public abstract class GuiStyle {

	public static final int DEFAULT_TEXTBOX_STYLE = SWT.BORDER;

	public static final int DEFAULT_MULTILINE_TEXTBOX_STYLE = SWT.MULTI | SWT.BORDER | SWT.WRAP;

	public static final int DEFAULT_GRID_DATA_STYLE = GridData.FILL_HORIZONTAL;

	public static final int PASSWORD_TEXTBOX_STYLE = SWT.BORDER | SWT.PASSWORD;

	// TODO Hacer que la precision sea configurable usando preferences
	public static final int DEFAULT_SCALE = 2;

}
