/*
 * Licencia de Caja de Valores S.A., Versión 1.0
 *
 * Copyright (c) 2006 Caja de Valores S.A.
 * 25 de Mayo 362, Ciudad Autónoma de Buenos Aires, República Argentina
 * Todos los derechos reservados.
 *
 * Este software es información confidencial y propietaria de Caja de Valores S.A. ("Información
 * Confidencial"). Usted no divulgará tal Información Confidencial y la usará solamente de acuerdo a
 * los términos del acuerdo de licencia que posee con Caja de Valores S.A.
 */

/*
 * $Id: ValidationMessageDialog.java,v 1.8 2008/05/09 12:35:22 cvscalab Exp $
 */
package commons.gui.widget.dialog;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.springframework.util.CollectionUtils;

import commons.gui.util.PageHelper;
import commons.gui.util.TextJfaceUtils;

/**
 * @author Gabriel Tursi
 * @version $Revision: 1.8 $ $Date: 2008/05/09 12:35:22 $
 */
public class ValidationMessageDialog extends MessageDialog {

	protected ValidationMessageDialog(String dialogTitle, String dialogMessage, String... messages) {
		super(null, dialogTitle, null, dialogMessage, WARNING,
				new String[] { IDialogConstants.OK_LABEL }, 0);
		validationMessages = messages;
	}

	public static void open(String dialogTitle, List<String> messages) {
		String[] array = null;
		if (!CollectionUtils.isEmpty(messages)) {
			array = messages.toArray(new String[messages.size()]);
		}
		open(dialogTitle, array);
	}

	public static void open(String dialogTitle, String... messages) {
		if (StringUtils.isBlank(dialogTitle)) {
			dialogTitle = "Validación";
		}
		PageHelper.getDisplay().beep();
		new ValidationMessageDialog(dialogTitle, DIALOG_MESSAGE, messages).open();
	}

	@Override
	protected Control createCustomArea(Composite parent) {
		Composite composite = null;
		text = "";
		if (validationMessages != null && validationMessages.length > 0) {
			composite = new Composite(parent, SWT.NONE);
			composite.setLayout(new RowLayout(SWT.VERTICAL));
			StyledText textbox = new StyledText(composite, SWT.LEFT);
			textbox.setEnabled(false);
			textbox.setBackground(parent.getShell().getBackground());
			textbox.setEditable(false);
			for (String msg : validationMessages) {
				textbox.setText(textbox.getText() + "\t\t- " + msg
						+ System.getProperty("line.separator"));
			}
			text = textbox.getText();
		}
		return composite;
	}

	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		Button button = createButton(parent, -1, "&Copiar Todo", false);
		super.createButtonsForButtonBar(parent);
		button.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent event) {
				TextJfaceUtils.copyToClipboard(text);
				setBlockOnOpen(true);
			}
		});
		getButton(IDialogConstants.OK_ID).setFocus();
	}

	@Override
	protected void buttonPressed(int buttonId) {
		setReturnCode(buttonId);
		if (buttonId == OK) {
			close();
		}
	}

	private static final String DIALOG_MESSAGE = "Validaciones:";

	private final String[] validationMessages;

	private String text;
}