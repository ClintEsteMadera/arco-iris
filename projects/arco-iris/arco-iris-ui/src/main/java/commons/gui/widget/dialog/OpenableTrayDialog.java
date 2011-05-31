package commons.gui.widget.dialog;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.TrayDialog;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;

import commons.gui.Openable;
import commons.properties.EnumProperty;
import commons.properties.FakeEnumProperty;

public abstract class OpenableTrayDialog<T> extends TrayDialog implements Openable<T> {

	protected boolean readOnly;

	private final EnumProperty title;

	private Button okButton;

	public OpenableTrayDialog(Shell parentShell, EnumProperty title, boolean readOnly) {
		super(parentShell);
		this.title = title;
		this.readOnly = readOnly;
	}

	protected abstract boolean performOK();

	/**
	 * This implementation is temporary until we determine if we really use this or not
	 */
	@Override
	public boolean open(T element) {
		return this.open() == Dialog.OK;
	}

	@Override
	protected void configureShell(Shell shell) {
		super.configureShell(shell);
		if (this.title != null) {
			shell.setText(this.title.toString());
		}
	}

	/**
	 * Sobreescribe el comportamiento de la superclase, permitiendo no crear el botón de "Aceptar" o "Cancelar" y
	 * permitiendo setear el texto personalizado de los botones.
	 */
	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		Button defaultButton = null;
		if (isOkButtonAllowed()) {
			this.okButton = createButton(parent, IDialogConstants.OK_ID, this.getAcceptButtonText().toString(), true);
			defaultButton = this.okButton;
		}
		if (isCancelButtonAllowed()) {
			Button cancelButton = createButton(parent, IDialogConstants.CANCEL_ID, this.getCancelButtonText()
					.toString(), false);
			if (!isOkButtonAllowed()) {
				defaultButton = cancelButton;
			}
		}
		if (defaultButton != null) {
			getShell().setDefaultButton(defaultButton);
		}
	}

	/**
	 * Permite especificar si se desea crear el botón "Aceptar". Por defecto, retorna <code>true</code>
	 */
	protected boolean isOkButtonAllowed() {
		return true;
	}

	/**
	 * Permite especificar si se desea crear el botón "Cancelar". Por defecto, retorna <code>true</code>
	 */
	protected boolean isCancelButtonAllowed() {
		return true;
	}

	@Override
	protected void okPressed() {
		if (performOK()) {
			close();
		}
	}

	/**
	 * Notifies that the window's close button was pressed, the close menu was selected, or the ESCAPE key pressed.
	 * <p>
	 * The default implementation of this framework method sets the window's return code to <code>CANCEL</code> and
	 * closes the window using <code>close</code>. Subclasses may extend or reimplement.
	 * </p>
	 */
	@Override
	protected void handleShellCloseEvent() {
		// handle the same as pressing cancel
		cancelPressed();
	}

	/**
	 * Los diálogos que requieran cambiar el nombre por defecto del botón "Aceptar" deberán sobreescribir este método.
	 * 
	 * @return el nombre del botón "Aceptar" para el diálogo en particular
	 */
	protected EnumProperty getAcceptButtonText() {
		return new FakeEnumProperty(IDialogConstants.OK_LABEL);
	}

	/**
	 * Los diálogos que requieran cambiar el nombre por defecto del botón "Cancelar" deberán sobreescribir este método.
	 * 
	 * @return el nombre del botón "Cancelar" para el diálogo en particular
	 */
	protected EnumProperty getCancelButtonText() {
		return new FakeEnumProperty(IDialogConstants.CANCEL_LABEL);
	}
}