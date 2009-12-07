package commons.gui.widget.dialog;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import sba.common.utils.DateUtils;

/**
 * Added a Details button to the MessageDialog to show the exception stack trace.
 */
public class InternalErrorDialog extends MessageDialog {

	private Throwable detail;

	private int detailButtonID = -1;

	private Text text;

	// Workaround. SWT does not seem to set the default button if
	// there is not control with focus. Bug: 14668
	private int defaultButtonIndex = 0;

	/**
	 * Size of the text in lines.
	 */
	private static final int TEXT_LINE_COUNT = 15;

	/**
	 * Create a new dialog.
	 * @param parentShell
	 *            the parent shell
	 * @param dialogTitle
	 *            the title
	 * @param dialogTitleImage
	 *            the title image
	 * @param dialogMessage
	 *            the message
	 * @param detail
	 *            the error to display
	 * @param dialogImageType
	 *            the type of image
	 * @param dialogButtonLabels
	 *            the button labels
	 * @param defaultIndex
	 *            the default selected button index
	 */
	private InternalErrorDialog(Shell parentShell, String dialogTitle, Image dialogTitleImage,
			String dialogMessage, Throwable detail, int dialogImageType,
			String[] dialogButtonLabels, int defaultIndex) {
		super(parentShell, dialogTitle, dialogTitleImage, dialogMessage, dialogImageType,
				dialogButtonLabels, defaultIndex);
		defaultButtonIndex = defaultIndex;
		this.detail = detail;
		setShellStyle(getShellStyle() | SWT.APPLICATION_MODAL | SWT.RESIZE);
	}

	// Workaround. SWT does not seem to set right the default button if
	// there is not control with focus. Bug: 14668
	@Override
	public int open() {
		create();
		Button b = getButton(defaultButtonIndex);
		b.setFocus();
		b.getShell().setDefaultButton(b);
		return super.open();
	}

	/**
	 * 085: * Set the detail button; 086: *
	 * @param index
	 *            the detail button index 087:
	 */
	public void setDetailButton(int index) {
		detailButtonID = index;
	}

	/*
	 * (non-Javadoc) 093: * Method declared on Dialog. 094:
	 */
	@Override
	protected void buttonPressed(int buttonId) {
		if (buttonId == detailButtonID) {
			toggleDetailsArea();
		} else {
			super.buttonPressed(buttonId);
		}
	}

	/**
	 * 104: * Toggles the unfolding of the details area. This is triggered by 105: * the user
	 * pressing the details button. 106:
	 */
	private void toggleDetailsArea() {
		Point windowSize = getShell().getSize();
		Point oldSize = getContents().computeSize(SWT.DEFAULT, SWT.DEFAULT);
		if (text != null) {
			text.dispose();
			text = null;
			getButton(detailButtonID).setText(IDialogConstants.SHOW_DETAILS_LABEL);
		} else {
			createDropDownText((Composite) getContents());
			getButton(detailButtonID).setText(IDialogConstants.HIDE_DETAILS_LABEL);
		}
		Point newSize = getContents().computeSize(SWT.DEFAULT, SWT.DEFAULT);
		getShell().setSize(new Point(windowSize.x, windowSize.y + (newSize.y - oldSize.y)));
	}

	/**
	 * 131: * Create this dialog's drop-down list component. 132: * 133: *
	 * @param parent
	 *            the parent composite 134:
	 */
	protected void createDropDownText(Composite parent) {
		// create the list
		text = new Text(parent, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
		text.setFont(parent.getFont());

		// print the stacktrace in the text field
		try {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			PrintStream ps = new PrintStream(baos);
			ps.println("[" + DateUtils.getCurrentDatetimeAsString() + "]");
			detail.printStackTrace(ps);
			ps.flush();
			baos.flush();
			text.setText(baos.toString());
		} catch (IOException e) {
		}
		GridData data = new GridData(GridData.HORIZONTAL_ALIGN_FILL | GridData.GRAB_HORIZONTAL
				| GridData.VERTICAL_ALIGN_FILL | GridData.GRAB_VERTICAL);
		data.heightHint = text.getLineHeight() * TEXT_LINE_COUNT;
		data.horizontalSpan = 2;
		text.setLayoutData(data);
	}

	/**
	 * 161: * Convenience method to open a simple Yes/No question dialog. 162: * 163: *
	 * @param parent
	 *            the parent shell of the dialog, or <code>null</code> if none 164: *
	 * @param title
	 *            the dialog's title, or <code>null</code> if none 165: *
	 * @param message
	 *            the message 166: *
	 * @param detail
	 *            the error 167: *
	 * @param defaultIndex
	 *            the default index of the button to select 168: *
	 * @return <code>true</code> if the user presses the OK button, 169: * <code>false</code>
	 *         otherwise 170:
	 */
	public static boolean openQuestion(Shell parent, String title, String message,
			Throwable detail, int defaultIndex) {
		String[] labels;
		if (detail == null) {
			labels = new String[] { IDialogConstants.YES_LABEL, IDialogConstants.NO_LABEL };
		} else {
			labels = new String[] { IDialogConstants.YES_LABEL, IDialogConstants.NO_LABEL,
					IDialogConstants.SHOW_DETAILS_LABEL };
		}

		// accept the default window icon
		InternalErrorDialog dialog = new InternalErrorDialog(parent, title, null, message, detail,
				QUESTION, labels, defaultIndex);
		if (detail != null) {
			dialog.setDetailButton(2);
		}
		return dialog.open() == 0;
	}

	public static int openError(Shell parentShell, String dialogTitle, String dialogMessage,
			Throwable detail) {
		String[] labels = new String[] { IDialogConstants.OK_LABEL,
				IDialogConstants.SHOW_DETAILS_LABEL };

		InternalErrorDialog dialog = new InternalErrorDialog(parentShell, dialogTitle, null,
				dialogMessage, detail, ERROR, labels, IDialogConstants.OK_ID);

		if (detail != null) {
			dialog.setDetailButton(1);
		}
		return dialog.open();
	}
}