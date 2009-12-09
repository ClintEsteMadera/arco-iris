/*
 * $Id: WebAppTemplateApplicationMenuAuthDirective.java,v 1.1 2009/06/23 15:09:06 cvstursi Exp $
 *
 * Licencia de Caja de Valores S.A., Versi�n 1.0
 * Copyright (c) 2006 Caja de Valores S.A.
 * 25 de Mayo 362, Ciudad Aut�noma de Buenos Aires, Rep�blica Argentina
 * Todos los derechos reservados.
 * Este software es informaci�n confidencial y propietaria de Caja de Valores S.A. ("Informaci�n
 * Confidencial"). Usted no divulgar� tal Informaci�n Confidencial y la usar� solamente de acuerdo a
 * los t�rminos del acuerdo de licencia que posee con Caja de Valores S.A.
 */
package ar.uba.dc.thesis.scenariosUIWeb.common.ui.web.velocity;

import ar.uba.dc.thesis.scenariosUIWeb.common.velocity.AuthorizeDirective;
import ar.uba.dc.thesis.scenariosUIWeb.ui.web.ScenariosUIMenu;

/**
 * @author Rodrigo A. Peinado
 * @version $Revision: 1.1 $ $Date: 2009/06/23 15:09:06 $
 */
public class WebAppTemplateApplicationMenuAuthDirective extends AuthorizeDirective {

	public WebAppTemplateApplicationMenuAuthDirective() {
		super();
	}

	@Override
	public boolean isAuthorized(final String text) {
		String url = text;
		int start = url.indexOf('?');
		if (start >= 0) {
			url = url.substring(0, start);
		}
		return ScenariosUIMenu.getInstance().isUrlAuthorized(url);
	}
}
