/*
 * Licencia de Caja de Valores S.A., Versión 1.0
 *
 * Copyright (c) 2006 Caja de Valores S.A.
 * 25 de Mayo 362, Ciudad Autónoma de Buenos Aires, República Argentina
 * Todos los derechos reservados.
 *
 * Este software es información confidencial y propietaria de Caja de Valores S.A. ("Información
 * Confidencial"). Usted no divulgará tal Información Confidencial y la usará solamente de acuerdo a
 * los términos del acuerdo de licencia que posee con Caja de Valores S.A.
 */

/*
 * $Id: OpenDialogAction.java,v 1.1 2008/04/18 20:55:06 cvschioc Exp $
 */

package commons.gui.action;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.Dialog;
import org.springframework.util.Assert;

/**
 * Modela una acción de apertura de diálogo.
 * 
 * @author Jonathan Chiocchio
 * @version $Revision: 1.1 $ $Date: 2008/04/18 20:55:06 $
 */

public abstract class OpenDialogAction<T> implements GuiAction<T> {

	protected OpenDialogAction(String identificadorUnico, String shortcut) {
		super();
		Assert.notNull(identificadorUnico, "El identificador único no puede ser nulo");

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