package commons.gui.widget.dialog;

import org.eclipse.jface.dialogs.TrayDialog;
import org.eclipse.swt.widgets.Shell;

import commons.gui.Openable;

public abstract class OpenableTrayDialog<T> extends TrayDialog implements Openable<T> {

	protected OpenableTrayDialog(Shell shell) {
		super(shell);
	}

	protected OpenableTrayDialog(Shell shell, boolean readOnly) {
		super(shell);
		this.readOnly = readOnly;
	}

	public boolean open(T element) {
		createBeanModel(element);
		int result = super.open();
		return result == OK;
	}

	protected abstract void createBeanModel(T element);

	protected boolean readOnly;
}
