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
 * $Id: OpenableTrayDialog.java,v 1.2 2007/11/22 16:50:20 cvspasto Exp $
 */
package commons.gui.widget.dialog;

import org.eclipse.jface.dialogs.TrayDialog;
import org.eclipse.swt.widgets.Shell;

import commons.gui.Openable;

/**
 * 
 * @author Luisina Marconi
 * @version $Revision: 1.2 $ $Date: 2007/11/22 16:50:20 $
 */
public abstract class OpenableTrayDialog<T extends Object> extends TrayDialog implements
		Openable<T> {

	protected OpenableTrayDialog(Shell shell) {
		super(shell);
	}

	protected OpenableTrayDialog(Shell shell,boolean readOnly) {
		super(shell);
		this.readOnly=readOnly;
	}

	public boolean open(T element) {
		createBeanModel(element);
		int result = super.open();
		return result == OK;
	}

	protected abstract void createBeanModel(T element);
	
	protected boolean readOnly;
}
