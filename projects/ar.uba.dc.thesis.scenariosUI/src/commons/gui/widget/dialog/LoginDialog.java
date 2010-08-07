package commons.gui.widget.dialog;

import org.acegisecurity.context.SecurityContextHolder;
import org.apache.commons.lang.StringUtils;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import commons.auth.AuthenticationHelper;
import commons.exception.ValidationException;
import commons.gui.GuiStyle;
import commons.gui.widget.factory.LabelFactory;
import commons.properties.CommonLabels;
import commons.properties.CommonMessages;
import commons.properties.EnumProperty;
import commons.properties.FakeEnumProperty;
import commons.validation.ValidationError;

public class LoginDialog extends Dialog {

	public LoginDialog(Shell parentShell, AuthenticationHelper authHelper) {
		super(parentShell);
		super.setShellStyle(SWT.TITLE);
		this.authHelper = authHelper;
		SecurityContextHolder.setStrategyName(SecurityContextHolder.MODE_INHERITABLETHREADLOCAL);
	}

	@Override
	protected void configureShell(Shell shell) {
		super.configureShell(shell);
		shell.setText(CommonMessages.AUTH_LOGIN_TITLE.toString());
	}

	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		okButton = createButton(parent, 0, IDialogConstants.OK_LABEL, true);
		createButton(parent, 1, IDialogConstants.CANCEL_LABEL, false);
		usernameText.setFocus();
		if (usernameText.getText() != null) {
			usernameText.selectAll();
		}
	}

	@Override
	protected Control createDialogArea(Composite parent) {
		Composite composite = (Composite) super.createDialogArea(parent);
		this.createWelcomeLabel(composite);
		GridData gridData = new GridData(SWT.FILL, SWT.FILL, true, true);
		this.createUsernameTextbox(composite, gridData);
		this.createPasswordTextbox(composite, gridData);
		super.applyDialogFont(composite);

		return composite;
	}

	private void createWelcomeLabel(Composite composite) {
		Label label = new Label(composite, SWT.CENTER);
		label.setText(CommonMessages.AUTH_LOGIN_MESSAGE.toString());
		GridData data = new GridData(SWT.LEFT, SWT.LEFT, false, false);
		data.widthHint = convertHorizontalDLUsToPixels(230);
		data.heightHint = convertVerticalDLUsToPixels(10);
		label.setLayoutData(data);
		label.setFont(composite.getFont());
	}

	private void createUsernameTextbox(Composite composite, GridData gridData) {
		LabelFactory.createLabel(composite, CommonLabels.AUTH_USER, false, true);
		this.usernameText = new Text(composite, GuiStyle.DEFAULT_TEXTBOX_STYLE);
		this.usernameText.setLayoutData(gridData);
	}

	private void createPasswordTextbox(Composite composite, GridData gridData) {
		LabelFactory.createLabel(composite, CommonLabels.AUTH_PASSWORD, false, true);
		this.passwordText = new Text(composite, GuiStyle.PASSWORD_TEXTBOX_STYLE);
		this.passwordText.setLayoutData(gridData);
	}

	@Override
	protected void buttonPressed(int buttonId) {
		if (IDialogConstants.OK_ID == buttonId) {
			// botón ACCEPT
			this.validateInput();
			this.userAuthenticated = this.authenticate();
		} else {
			// botón CANCEL
			passwordText.setText("");
		}
		super.buttonPressed(buttonId);
	}

	@Override
	protected void okPressed() {
		if (this.userAuthenticated) {
			super.okPressed();
		}
	}

	@Override
	protected Button getOKButton() {
		return okButton;
	}

	private void validateInput() {
		String username = this.usernameText.getText();
		String password = this.passwordText.getText();
		if (StringUtils.isEmpty(username) || StringUtils.isEmpty(password)) {
			EnumProperty msg = new FakeEnumProperty("El usuario y la contraseña no pueden estar vacías");
			throw new ValidationException(new ValidationError(msg));
		}
	}

	private boolean authenticate() {
		return authHelper.authenticate(this.usernameText.getText(), this.passwordText.getText());
	}

	private Button okButton;

	private Text usernameText;

	private Text passwordText;

	private boolean userAuthenticated;

	private AuthenticationHelper authHelper;
}