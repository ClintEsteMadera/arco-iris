package commons.gui.table;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Shell;

import commons.gui.util.purpose.BasePurpose;
import commons.gui.util.purpose.Purpose;
import commons.gui.widget.dialog.OpenableTrayDialog;
import commons.utils.ClassUtils;

/**
 * Handler generico que crea un dialogo por reflection cada vez que se invoca.<br>
 */
public abstract class DialogEditHandler<T> implements EditHandler<T> {

	public DialogEditHandler(Shell parentShell) {
		this.parentShell = parentShell;
	}

	public T handleCreation(int index) {
		final T newItem = createNewItem();
		final OpenableTrayDialog<T> dialog = getDialog(newItem, index, BasePurpose.CREATION);
		if (dialog == null) {
			return null;
		}
		// TODO we may need to do this when we finally decide how this whole thing of opening a dialog w/ an item works
		// boolean dialogResult = dialog.open(newItem);
		boolean dialogResult = dialog.open() == Dialog.OK;
		return dialogResult ? newItem : null;
	}

	public boolean handleUpdate(T item, int index) {
		final OpenableTrayDialog<T> dialog = getDialog(item, index, BasePurpose.EDIT);
		if (dialog == null) {
			return false;
		}
		return dialog.open() == Dialog.OK;
	}

	public void handleView(T item, int index) {
		final OpenableTrayDialog<T> dialog = getDialog(item, index, BasePurpose.EDIT);
		if (dialog == null) {
			return;
		}
		dialog.open();
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
