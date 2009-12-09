/*
 * $Id: Link.java,v 1.5 2009/05/07 21:20:07 cvstursi Exp $
 *
 * Licencia de Caja de Valores S.A., Versión 1.0
 * Copyright (c) 2006 Caja de Valores S.A.
 * 25 de Mayo 362, Ciudad Autónoma de Buenos Aires, República Argentina
 * Todos los derechos reservados.
 * Este software es información confidencial y propietaria de Caja de Valores S.A. ("Información
 * Confidencial"). Usted no divulgará tal Información Confidencial y la usará solamente de acuerdo a
 * los términos del acuerdo de licencia que posee con Caja de Valores S.A.
 */
package ar.uba.dc.thesis.scenariosUIWeb.common.ui.web;

import ar.uba.dc.thesis.scenariosUIWeb.common.util.SbaConventions;

public class Link {

	public Link(final String url, final String... authorizedRoles) {
		this.url = url;
		this.authorizedRoles = authorizedRoles;
	}

	public Link(final Class<?> controllerClass, final String... authorizedRoles) {
		url = "/service/" + SbaConventions.controllerToViewName(controllerClass);
		this.authorizedRoles = authorizedRoles;
	}

	public boolean isUrlAuthorized(String urlParam) {
		return true;
	}

	public String getUrl() {
		return url;
	}

	public String[] getAuthorizedRoles() {
		return authorizedRoles;
	}

	private boolean isAuthorized(final String[] userRoles) {
		return true;
	}

	private final String url;

	private final String[] authorizedRoles;
}
