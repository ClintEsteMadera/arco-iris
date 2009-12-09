/*
 * $Id: WebAppTemplateApplicationMenuAuthDirective.java,v 1.1 2009/06/23 15:09:06 cvstursi Exp $
 *
 * Licencia de Caja de Valores S.A., Versión 1.0
 * Copyright (c) 2006 Caja de Valores S.A.
 * 25 de Mayo 362, Ciudad Autónoma de Buenos Aires, República Argentina
 * Todos los derechos reservados.
 * Este software es información confidencial y propietaria de Caja de Valores S.A. ("Información
 * Confidencial"). Usted no divulgará tal Información Confidencial y la usará solamente de acuerdo a
 * los términos del acuerdo de licencia que posee con Caja de Valores S.A.
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
