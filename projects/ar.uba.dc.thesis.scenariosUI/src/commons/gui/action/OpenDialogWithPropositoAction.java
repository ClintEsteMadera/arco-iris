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
 * $Id: OpenDialogWithPropositoAction.java,v 1.1 2008/04/18 20:55:06 cvschioc Exp $
 */

package commons.gui.action;

import org.springframework.util.Assert;

import commons.gui.util.proposito.Proposito;

/**
 * Modela una acción de apertura de diálogo con propósito.
 *
 * @author Jonathan Chiocchio
 * @version $Revision: 1.1 $ $Date: 2008/04/18 20:55:06 $
 */

public abstract class OpenDialogWithPropositoAction<T, P extends Proposito> extends OpenDialogAction<T> {

	protected OpenDialogWithPropositoAction(String identificadorUnico, String shortcut, P proposito) {
		super(identificadorUnico, shortcut);
		Assert.notNull(proposito, "El propósito no puede ser nulo");
		this.proposito = proposito;
	}

	public P getProposito() {
		return this.proposito;
	}

	private P proposito;
}