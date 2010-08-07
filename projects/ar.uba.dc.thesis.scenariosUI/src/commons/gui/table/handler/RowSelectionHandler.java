package commons.gui.table.handler;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import commons.gui.Openable;
import commons.gui.command.RowSelectionHandlerCommand;
import commons.utils.ClassUtils;

public class RowSelectionHandler<T extends Object, COMMAND extends RowSelectionHandlerCommand<T>> {

	public RowSelectionHandler(Class<T> aClass, Openable<T> dialog, COMMAND command) {
		super();
		this.command = command;
		this.clazz = aClass;
		this.dialog = dialog;
	}

	public boolean handleView(T element) {
		return dialog.open(element);
	}

	public boolean handleCreate() {
		boolean handleCreate = false;
		T element;
		try {
			element = ClassUtils.newInstance(clazz);
			if (dialog.open(element)) {
				element = dialog.getModel();
				command.store(element);
				handleCreate = true;
			}
		} catch (Exception e) {
			log.fatal(e.getMessage(), e);
		}
		return handleCreate;
	}

	public boolean handleUpdate(int rowIndex, T element) {
		boolean handleUpdate = false;
		element = command.cloneElement(element);
		if (dialog.open(element)) {
			element = dialog.getModel();
			command.update(rowIndex, element);
			handleUpdate = true;
		}
		return handleUpdate;
	}

	public COMMAND command;

	private final Class<T> clazz;

	public Openable<T> dialog;

	private static final Log log = LogFactory.getLog(RowSelectionHandler.class);
}