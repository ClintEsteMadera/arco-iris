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
 * $Id: OpenDialogAction.java,v 1.1 2008/04/18 20:55:06 cvschioc Exp $
 */

package commons.gui.action;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.Dialog;
import org.springframework.util.Assert;

/**
 * Modela una acci�n de apertura de di�logo.
 * 
 * @author Jonathan Chiocchio
 * @version $Revision: 1.1 $ $Date: 2008/04/18 20:55:06 $
 */

public abstract class OpenDialogAction<T> implements GuiAction<T> {

	protected OpenDialogAction(String identificadorUnico, String shortcut) {
		super();
		Assert.notNull(identificadorUnico, "El identificador �nico no puede ser nulo");

		this.identificadorUnico = identificadorUnico;
		this.shortcut = shortcut == null ? "" : shortcut;
	}

	public Action getActionFor(final T model) {
		return new OpenPageAction() {
			@Override
			protected void openPage() {
				getDialogFor(model).open();
			}
		};
	}

	public String getIdentificadorUnico() {
		return this.identificadorUnico;
	}

	public String getShortcut() {
		return this.shortcut;
	}

	protected abstract Dialog getDialogFor(T model);

	private final String shortcut;

	private String identificadorUnico;
}