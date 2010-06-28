package commons.gui.table;

import java.lang.reflect.Constructor;

import org.eclipse.swt.widgets.Shell;

import commons.gui.util.purpose.Purpose;
import commons.gui.widget.dialog.OpenableTrayDialog;

/**
 * Handler generico que crea un dialogo por reflection cada vez que se invoca.
 */
public class GenericEditHandler<ITEM, DIALOG extends OpenableTrayDialog<ITEM>> extends DialogEditHandler<ITEM> {

	private Constructor<DIALOG> dialogContructor;

	private boolean supportsReadOnlyParam;

	private final Class<DIALOG> dialogClass;

	private final Class<ITEM> itemClass;

	public GenericEditHandler(Shell shell, Class<DIALOG> dialogClass, Class<ITEM> itemClass) {
		super(shell);
		if (!OpenableTrayDialog.class.isAssignableFrom(dialogClass)) {
			throw new IllegalArgumentException("La clase de dialogo no extiende  " + OpenableTrayDialog.class.getName());
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

		boolean readOnly = purpose.isView();

		try {
			if (this.supportsReadOnlyParam) {
				dialog = c.newInstance((Object[]) (new Boolean[] { readOnly }));
			} else {
				dialog = c.newInstance(new Object[] {});
			}
		} catch (Exception e) {
			throw new IllegalArgumentException("No se pudo crear el dialogo: " + e.getMessage(), e);
		}
		return dialog;
	}

	@SuppressWarnings("unchecked")
	private Constructor<DIALOG> getDialogConstructor() {

		if (this.dialogContructor == null) {
			try {
				this.dialogContructor = dialogClass.getConstructor(Boolean.TYPE);
				this.supportsReadOnlyParam = true;
			} catch (Exception e1) {
				try {
					this.dialogContructor = dialogClass.getConstructor();
					this.supportsReadOnlyParam = false;
				} catch (Exception e2) {
					throw new IllegalArgumentException("No se pudo crear el dialogo: " + e2.getMessage(), e2);
				}
			}
		}

		return this.dialogContructor;
	}
}
