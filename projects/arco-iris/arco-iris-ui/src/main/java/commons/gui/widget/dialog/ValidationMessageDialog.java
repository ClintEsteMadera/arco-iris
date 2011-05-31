package commons.gui.widget.dialog;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.ArrayUtils;
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

import commons.gui.util.PageHelper;
import commons.gui.util.TextJfaceUtils;

public class ValidationMessageDialog extends MessageDialog {

	private static final String DIALOG_MESSAGE = "Validations:";

	private final String[] validationMessages;

	private String text;

	protected ValidationMessageDialog(String dialogTitle, String dialogMessage, String... messages) {
		super(null, dialogTitle, null, dialogMessage, WARNING, new String[] { IDialogConstants.OK_LABEL }, 0);
		this.validationMessages = messages;
	}

	public static void open(String dialogTitle, List<String> messages) {
		String[] array = null;
		if (CollectionUtils.isNotEmpty(messages)) {
			array = messages.toArray(new String[messages.size()]);
		}
		open(dialogTitle, array);
	}

	public static void open(String dialogTitle, String... messages) {
		if (StringUtils.isBlank(dialogTitle)) {
			dialogTitle = "Validation";
		}
		PageHelper.getDisplay().beep();
		new ValidationMessageDialog(dialogTitle, DIALOG_MESSAGE, messages).open();
	}

	@Override
	protected Control createCustomArea(Composite parent) {
		Composite composite = null;
		text = "";
		if (!ArrayUtils.isEmpty(this.validationMessages)) {
			composite = new Composite(parent, SWT.NONE);
			composite.setLayout(new RowLayout(SWT.VERTICAL));
			StyledText textbox = new StyledText(composite, SWT.LEFT);
			textbox.setEnabled(false);
			textbox.setBackground(parent.getShell().getBackground());
			textbox.setEditable(false);
			for (String msg : this.validationMessages) {
				textbox.setText(textbox.getText() + "\t\t- " + msg + System.getProperty("line.separator"));
			}
			text = textbox.getText();
		}
		return composite;
	}

	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		Button button = createButton(parent, -1, "&Copy all", false);
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
}