package commons.gui.table;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Shell;

import sba.common.utils.ClassUtils;
import scenariosui.gui.util.purpose.ScenariosUIPurpose;

import commons.gui.util.purpose.Purpose;
import commons.gui.widget.dialog.OpenableTrayDialog;

/**
 * Handler generico que crea un dialogo por reflection cada vez que se invoca.
 */
public abstract class DialogEditHandler<T> implements EditHandler<T> {

	public DialogEditHandler(Shell parentShell) {
		this.parentShell = parentShell;
	}

	public T handleCreation(int index) {
		final T newItem = createNewItem();
		final OpenableTrayDialog<T> dialog = getDialog(newItem, index, ScenariosUIPurpose.CREATION);
		if (dialog == null) {
			return null;
		}
		boolean dialogResult = dialog.open(newItem);
		return dialogResult ? newItem : null;
	}

	public boolean handleUpdate(T item, int index) {
		final OpenableTrayDialog<T> dialog = getDialog(item, index, ScenariosUIPurpose.EDIT);
		if (dialog == null) {
			return false;
		}
		return dialog.open(item);
	}

	public void handleView(T item, int index) {
		final OpenableTrayDialog<T> dialog = getDialog(item, index, ScenariosUIPurpose.EDIT);
		if (dialog == null) {
			return;
		}
		dialog.open(item);
	}

	public boolean handleDelete(T e, int index) {
		return MessageDialog.openQuestion(parentShell, "Confirm Deletion",
				"Do you really want to delete the selected element?");
	}

	protected T createNewItem() {
		return ClassUtils.newInstance(this.getItemClass());
	}

	protected Shell getParentShell() {
		return parentShell;
	}

	protected abstract Class<T> getItemClass();

	protected abstract OpenableTrayDialog<T> getDialog(T item, int index, Purpose purpose);

	private final Shell parentShell;
}
