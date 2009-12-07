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
 * $Id: OpenDialogWithPropositoAction.java,v 1.1 2008/04/18 20:55:06 cvschioc Exp $
 */

package commons.gui.action;

import org.springframework.util.Assert;

import commons.gui.util.proposito.Proposito;

/**
 * Modela una acci�n de apertura de di�logo con prop�sito.
 *
 * @author Jonathan Chiocchio
 * @version $Revision: 1.1 $ $Date: 2008/04/18 20:55:06 $
 */

public abstract class OpenDialogWithPropositoAction<T, P extends Proposito> extends OpenDialogAction<T> {

	protected OpenDialogWithPropositoAction(String identificadorUnico, String shortcut, P proposito) {
		super(identificadorUnico, shortcut);
		Assert.notNull(proposito, "El prop�sito no puede ser nulo");
		this.proposito = proposito;
	}

	public P getProposito() {
		return this.proposito;
	}

	private P proposito;
}