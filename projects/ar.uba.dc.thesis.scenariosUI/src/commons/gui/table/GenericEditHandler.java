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
 * $Id: GenericEditHandler.java,v 1.10 2008/04/01 20:20:14 cvschioc Exp $
 */

package commons.gui.table;

import java.lang.reflect.Constructor;

import org.eclipse.swt.widgets.Shell;

import commons.gui.widget.dialog.OpenableTrayDialog;

/**
 * Handler generico que crea un dialogo por refelction cada vez que se invoca.
 * 
 * @author Pablo Pastorino
 * @version $Revision: 1.10 $ $Date: 2008/04/01 20:20:14 $
 */

public class GenericEditHandler<T> extends DialogEditHandler<T> {

	public GenericEditHandler(Shell shell,Class dialogClass, Class<T> itemClass) {
		super(shell);
		if (!OpenableTrayDialog.class.isAssignableFrom(dialogClass)) {
			throw new IllegalArgumentException("La clase de dialogo no extiende  "
					+ OpenableTrayDialog.class.getName());
		}
		this.dialogClass = dialogClass;
		this.itemClass = itemClass;
	}

	@Override
	protected Class getItemClass() {
		return itemClass;
	}

	@Override
	protected OpenableTrayDialog getDialog(T item,int index,int proposito) {

		final Constructor c = this.getDialogConstructor();

		Object dialog = null;

		boolean readOnly=proposito == PROPOSITO_VISTA;
		
		try {
			if (this.supportsReadOnlyParam) {
				dialog = c.newInstance((Object[]) (new Boolean[] { readOnly }));
			} else {
				dialog = c.newInstance(new Object[] {});
			}
		} catch (Exception e) {
			throw new IllegalArgumentException("No se pudo crear el dialogo: " + e.getMessage(), e);
		}
		return (OpenableTrayDialog) dialog;
	}

	@SuppressWarnings("unchecked")
	private Constructor getDialogConstructor() {

		if (this.dialogContructor == null) {
			Class parameterTypes_1[] = new Class[] { Boolean.TYPE };
			Class parameterTypes_2[] = new Class[] {};

			try {
				this.dialogContructor = dialogClass.getConstructor(parameterTypes_1);
				this.supportsReadOnlyParam = true;
			} catch (Exception e1) {
				try {
					this.dialogContructor = dialogClass.getConstructor(parameterTypes_2);
					this.supportsReadOnlyParam = false;
				} catch (Exception e2) {
					throw new IllegalArgumentException("No se pudo crear el dialogo: "
							+ e2.getMessage(), e2);
				}
			}
		}

		return this.dialogContructor;
	}

	private Constructor dialogContructor;

	private boolean supportsReadOnlyParam;

	private final Class dialogClass;

	private final Class itemClass;
}
