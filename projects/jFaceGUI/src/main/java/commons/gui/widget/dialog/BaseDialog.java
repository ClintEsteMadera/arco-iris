package commons.gui.widget.dialog;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;

import commons.gui.util.PageHelper;

public abstract class BaseDialog extends Dialog {

	protected boolean readOnly;

	private final String title;

	public BaseDialog(String title, boolean readOnly) {
		super(PageHelper.getMainShell());
		setShellStyle(getShellStyle() | SWT.RESIZE | SWT.MAX);
		this.readOnly = readOnly;
		this.title = title;
	}

	/**
	 * note: Override this method if you need to specify a different size
	 */
	@Override
	protected Point getInitialSize() {
		return super.getInitialSize();
	}

	@Override
	protected void configureShell(Shell shell) {
		super.configureShell(shell);
		shell.setText(title);
	}

	@Override
	protected Control createDialogArea(Composite parent) {
		applyLayout(parent);
		return addFields(parent);
	}

	protected abstract Control addFields(Composite parent);

	protected void applyLayout(Composite parent) {
		GridLayout gridLayout = new GridLayout();
		gridLayout.marginTop = 10;
		gridLayout.marginBottom = 10;
		gridLayout.marginLeft = 5;
		gridLayout.marginRight = 5;
		gridLayout.verticalSpacing = 10;
		parent.setLayout(gridLayout);

	}
}