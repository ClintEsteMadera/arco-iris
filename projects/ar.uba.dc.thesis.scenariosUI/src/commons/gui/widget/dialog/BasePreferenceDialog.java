/*
 * $Id: BasePreferenceDialog.java,v 1.12 2008/05/16 14:34:06 cvspasto Exp $
 */

package commons.gui.widget.dialog;

import java.util.ArrayList;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.preference.IPreferenceNode;
import org.eclipse.jface.preference.PreferenceDialog;
import org.eclipse.jface.preference.PreferenceManager;
import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;

import commons.gui.model.validation.ValidationChangedListener;
import commons.gui.model.validation.ValidationSource;
import commons.gui.thread.GUIUncaughtExceptionHandler;
import commons.gui.widget.PreferenceNode;
import commons.gui.widget.page.BasePreferencesPage;
import commons.properties.EnumProperty;
import commons.properties.FakeEnumProperty;
import commons.validation.ValidationError;

public abstract class BasePreferenceDialog extends PreferenceDialog implements ValidationSource {

	private final EnumProperty title;

	private Button okButton;

	private ArrayList<ValidationChangedListener> validationListeners;

	private ValidationError[] validationErrors;

	public BasePreferenceDialog(Shell parentShell, EnumProperty title) {
		super(parentShell, new PreferenceManager());
		super.setSelectedNode(null);
		this.title = title;
		this.validationListeners = new ArrayList<ValidationChangedListener>();
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
	public void updateButtons() {
		if (this.okButton != null) {
			this.okButton.setEnabled(isCurrentPageValid());
		}
	}

	@Override
	protected void configureShell(Shell shell) {
		super.configureShell(shell);
		if (this.title != null) {
			shell.setText(this.title.toString());
		}
	}

	@Override
	protected Control createTreeAreaContents(Composite parent) {
		Control control = super.createTreeAreaContents(parent);
		getTreeViewer().expandAll();
		return control;
	}

	@Override
	protected void okPressed() {
		ValidationSource prev = GUIUncaughtExceptionHandler.getInstance().getValidationSource();
		GUIUncaughtExceptionHandler.getInstance().setValidationSource(this);
		try {
			if (performOK()) {
				close();
			}
		} finally {
			GUIUncaughtExceptionHandler.getInstance().setValidationSource(prev);
		}
	}

	@Override
	protected Control createContents(Composite parent) {
		this.createNodes();
		return super.createContents(parent);
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

	/**
	 * Crea Nodos asociados al dialogo (los mostrados a la izquierda de la página) y a cada uno le asocia la
	 * {@link BasePreferencesPage} correspondiente
	 */
	protected abstract void createNodes();

	protected IPreferenceNode addNode(IPreferenceNode parent, String identif, PreferencePage page) {
		PreferenceNode node = new PreferenceNode(identif, page);
		if (parent == null) {
			this.getPreferenceManager().addToRoot(node);
		} else {
			parent.add(node);
		}

		if (page instanceof BasePreferencesPage) {
			((BasePreferencesPage) page).setValidationSource(this);
		}
		return node;
	}

	protected abstract boolean performOK();

	public void addValidationChangedListener(ValidationChangedListener listener) {
		this.validationListeners.add(listener);
	}

	public ValidationError[] getValidationErrors() {
		return this.validationErrors;
	}

	public void removeValidationChangedListener(ValidationChangedListener listener) {
		this.validationListeners.remove(listener);
	}

	public void setValidationErrors(ValidationError[] errors) {
		this.validationErrors = errors;
		for (ValidationChangedListener l : this.validationListeners) {
			l.validationChanged(this);
		}
	}
}