package commons.gui.table;

import java.lang.reflect.Constructor;

import org.eclipse.swt.widgets.Shell;

import commons.gui.util.purpose.Purpose;
import commons.gui.widget.dialog.OpenableTrayDialog;

/**
 * Generic Handler that creates a dialog (using reflection) every time it is invoked
 */
public class GenericEditHandler<ITEM, DIALOG extends OpenableTrayDialog<ITEM>> extends DialogEditHandler<ITEM> {

	private Constructor<DIALOG> dialogContructor;

	private boolean dialogSupportsPurposeParam;

	private final Class<DIALOG> dialogClass;

	private final Class<ITEM> itemClass;

	public GenericEditHandler(Shell shell, Class<DIALOG> dialogClass, Class<ITEM> itemClass) {
		super(shell);
		if (!OpenableTrayDialog.class.isAssignableFrom(dialogClass)) {
			throw new IllegalArgumentException("The dialog class does not inherit from "
					+ OpenableTrayDialog.class.getName());
		}
		this.dialogClass = dialogClass;
		this.itemClass = itemClass;
	}

	@Override
	protected Class<ITEM> getItemClass() {
		return itemClass;
	}

	@Override
	protected DIALOG getDialog(ITEM item, int index, Purpose purpose) {

		final Constructor<DIALOG> c = this.getDialogConstructor();

		DIALOG dialog = null;

		try {
			if (this.dialogSupportsPurposeParam) {
				dialog = c.newInstance(item, purpose);
			} else {
				dialog = c.newInstance();
			}
		} catch (Exception e) {
			throw new IllegalArgumentException("Could not create dialog: " + e.getMessage(), e);
		}
		return dialog;
	}

	private Constructor<DIALOG> getDialogConstructor() {

		if (this.dialogContructor == null) {
			try {
				this.dialogContructor = dialogClass.getConstructor(this.getItemClass(), Purpose.class);
				this.dialogSupportsPurposeParam = true;
			} catch (Exception e1) {
				try {
					this.dialogContructor = dialogClass.getConstructor(this.getItemClass());
					this.dialogSupportsPurposeParam = false;
				} catch (Exception e2) {
					throw new IllegalArgumentException("Could not create dialog: " + e2.getMessage(), e2);
				}
			}
		}

		return this.dialogContructor;
	}
}
