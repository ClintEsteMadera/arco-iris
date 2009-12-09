/*
 * $Id: Link.java,v 1.5 2009/05/07 21:20:07 cvstursi Exp $
 *
 * Licencia de Caja de Valores S.A., Versi�n 1.0
 * Copyright (c) 2006 Caja de Valores S.A.
 * 25 de Mayo 362, Ciudad Aut�noma de Buenos Aires, Rep�blica Argentina
 * Todos los derechos reservados.
 * Este software es informaci�n confidencial y propietaria de Caja de Valores S.A. ("Informaci�n
 * Confidencial"). Usted no divulgar� tal Informaci�n Confidencial y la usar� solamente de acuerdo a
 * los t�rminos del acuerdo de licencia que posee con Caja de Valores S.A.
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
