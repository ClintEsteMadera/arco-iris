package commons.gui.widget.dialog;

import org.acegisecurity.context.SecurityContextHolder;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.springframework.util.Assert;

import sba.common.session.SessionHelper;

import commons.auth.AuthenticationManager;
import commons.exception.ValidationException;
import commons.gui.GuiStyle;
import commons.gui.widget.factory.LabelFactory;
import commons.properties.CommonLabels;
import commons.properties.EnumProperty;
import commons.properties.FakeEnumProperty;
import commons.validation.ValidationError;

public class ChangePwdDialog extends Dialog {

	private String username;

	private Button okButton;

	private Text oldPwdText;

	private Text newPwdText;

	private Text newPwdConfirmText;

	private Label errorMessageLabel;

	private boolean changeForced;

	private AuthenticationManager authManager;

	private static final Log log = LogFactory.getLog(ChangePwdDialog.class);
	
	public ChangePwdDialog(Shell parentShell, String username, boolean forceChange, AuthenticationManager authManager) {
		super(parentShell);
		super.setShellStyle(SWT.TITLE);
		SecurityContextHolder.setStrategyName(SecurityContextHolder.MODE_INHERITABLETHREADLOCAL);
		this.username = username;
		this.changeForced = forceChange;
		this.authManager = authManager;
	}

	public static void openWithUserLoggedIn(Shell parentShell, boolean forceChange, AuthenticationManager authManager) {
		String usrName = SessionHelper.nombreDeUsuarioConectado();
		new ChangePwdDialog(parentShell, usrName, forceChange, authManager).open();
	}

	@Override
	protected void configureShell(Shell shell) {
		super.configureShell(shell);
		shell.setText(CommonLabels.AUTH_CHG_PWD.toString());
	}

	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		okButton = createButton(parent, 0, IDialogConstants.OK_LABEL, true);
		createButton(parent, 1, IDialogConstants.CANCEL_LABEL, false);
		oldPwdText.setFocus();
	}

	@Override
	protected Control createDialogArea(Composite parent) {
		Composite composite = (Composite) super.createDialogArea(parent);
		GridData gridData = new GridData(SWT.FILL, SWT.FILL, true, true);
		this.createOldPasswordTextbox(composite, gridData);
		this.createNewPasswordTextbox(composite, gridData);
		this.createNewPasswordConfirmationTextbox(composite, gridData);

		this.errorMessageLabel = new Label(composite, 8);
		this.errorMessageLabel.setLayoutData(new GridData(768));
		this.errorMessageLabel.setBackground(errorMessageLabel.getDisplay().getSystemColor(22));
		super.applyDialogFont(composite);
		return composite;
	}

	private void createOldPasswordTextbox(Composite composite, GridData gridData) {
		LabelFactory.createLabel(composite, CommonLabels.AUTH_CHG_PWD_LAST_PWD, false, true);
		this.oldPwdText = new Text(composite, GuiStyle.PASSWORD_TEXTBOX_STYLE);
		this.oldPwdText.setLayoutData(gridData);
	}

	private void createNewPasswordTextbox(Composite composite, GridData gridData) {
		LabelFactory.createLabel(composite, CommonLabels.AUTH_CHG_PWD_NEW_PWD, false, true);
		this.newPwdText = new Text(composite, GuiStyle.PASSWORD_TEXTBOX_STYLE);
		this.newPwdText.setLayoutData(gridData);
	}

	private void createNewPasswordConfirmationTextbox(Composite composite, GridData gridData) {
		LabelFactory.createLabel(composite, CommonLabels.AUTH_CHG_PWD_CONFIRM_NEW_PWD, false, true);
		this.newPwdConfirmText = new Text(composite, GuiStyle.PASSWORD_TEXTBOX_STYLE);
		this.newPwdConfirmText.setLayoutData(gridData);
	}

	@Override
	protected void buttonPressed(int buttonId) {
		if (IDialogConstants.OK_ID == buttonId && this.changePassword()) {
			super.buttonPressed(buttonId);
		} else if (IDialogConstants.CANCEL_ID == buttonId) {
			if (this.changeForced) {
				MessageDialog.openError(super.getShell(), "Cambio de Contraseña forzado",
						"No se puede cancelar un cambio de contraseña");
				System.exit(0);
			} else {
				super.buttonPressed(buttonId);
			}
		}
	}

	@Override
	protected Button getOKButton() {
		return okButton;
	}

	private void validateInput() {
		Assert.notNull(this.oldPwdText.getText(), "La contraseña actual no puede ser nula");
		Assert.notNull(this.newPwdText.getText(), "La nueva contraseña no puede ser nula");
		Assert.notNull(this.newPwdConfirmText.getText(), "La confirmación de la nueva contraseña no puede ser nula");

		if (!this.oldPwdText.getText().equals(SessionHelper.passwordDeUsuarioConectado())) {
			EnumProperty msg = new FakeEnumProperty("La contraseña actual ingresada es inválida");
			throw new ValidationException(new ValidationError(msg));
		}

		if (!this.newPwdText.getText().equals(this.newPwdConfirmText.getText())) {
			EnumProperty msg = new FakeEnumProperty("La nueva contraseña y su confirmación no coinciden");
			throw new ValidationException(new ValidationError(msg));
		}
	}

	private boolean changePassword() {
		boolean operationSuccessful = false;
		try {
			validateInput();
			authManager.changePassword(this.oldPwdText.getText(), this.newPwdText.getText());
			operationSuccessful = true;
			MessageDialog.openInformation(super.getShell(), "Cambio de contraseña",
					"Se ha cambiado la contraseña exitosamente.");
		} catch (Exception ex) {
			log.error("Error en cambio de password del usuario " + this.username + ": " + ex.getMessage(), ex
					.getCause());
			final Status status = new Status(IStatus.ERROR, "dummy plugin", IStatus.ERROR, ex.getLocalizedMessage(), ex
					.getCause());
			ErrorDialog.openError(super.getShell(), "Error", null, status);
		}
		return operationSuccessful;
	}
}