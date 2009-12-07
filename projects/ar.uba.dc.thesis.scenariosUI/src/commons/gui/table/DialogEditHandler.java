/*
 * Licencia de Caja de Valores S.A., Versi�n 1.0
 *
 * Copyright (c) 2006 Caja de Valores S.A.
 * 25 de Mayo 362, Ciudad Aut�noma de Buenos Aires, Rep�blica Argentina
 * Todos los derechos reservados.
 *
 * Este software es informaci�n confidencial y propietaria de Caja de Valores S.A. ("Informaci�n
 * Confidencial"). Usted no divulgar� tal Informaci�n Confidencial y la usar� solamente de acuerdo a
 * los t�rminos del acuerdo de licencia que posee con Caja de Valores S.A.
 */

/*
 * $Id: DialogEditHandler.java,v 1.6 2008/02/22 14:00:15 cvschioc Exp $
 */

package commons.gui.table;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Shell;

import sba.common.utils.ClassUtils;

import commons.gui.widget.dialog.OpenableTrayDialog;

/**
 * Handler generico que crea un dialogo por refelction cada vez que se invoca.
 * @author Pablo Pastorino
 * @version $Revision: 1.6 $ $Date: 2008/02/22 14:00:15 $
 */

public abstract class DialogEditHandler<T> implements EditHandler {

	public DialogEditHandler(Shell parentShell) {
		this.parentShell = parentShell;
	}

	@SuppressWarnings("unchecked")
	public Object handleCreation(int index) {
		final Object newItem = createNewItem(index);
		final OpenableTrayDialog dialog = getDialog((T) newItem, index, PROPOSITO_CREACION);
		if (dialog == null) {
			return null;
		}
		boolean dialogResult = dialog.open(newItem);
		return dialogResult ? newItem : null;
	}

	@SuppressWarnings("unchecked")
	public boolean handleUpdate(Object item, int index) {
		final OpenableTrayDialog dialog = getDialog((T) item, index, PROPOSITO_EDICION);
		if (dialog == null) {
			return false;
		}
		return dialog.open(item);
	}

	@SuppressWarnings("unchecked")
	public void handleView(Object item, int index) {
		final OpenableTrayDialog dialog = getDialog((T) item, index, PROPOSITO_VISTA);
		if (dialog == null) {
			return;
		}
		dialog.open(item);
	}

	public boolean handleDelete(Object e, int index) {
		return MessageDialog.openQuestion(parentShell, "Confirmar eliminaci�n",
				"�Confirma que desea eliminar el elemento seleccionado?");
	}

	@SuppressWarnings("unchecked")
	protected T createNewItem(int index) {
		return (T) ClassUtils.newInstance(this.getItemClass());
	}

	protected Shell getParentShell() {
		return parentShell;
	}

	protected abstract Class getItemClass();

	protected abstract OpenableTrayDialog getDialog(T item, int index, int proposito);

	private Shell parentShell;

	public static final int PROPOSITO_CREACION = 0;

	public static final int PROPOSITO_EDICION = 1;

	public static final int PROPOSITO_VISTA = 2;
}
