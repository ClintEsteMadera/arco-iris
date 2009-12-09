/*
 * $Id: Item.java,v 1.5 2009/05/07 21:20:07 cvstursi Exp $
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

/**
 * @author H. Adri�n Uribe
 * @version $Revision: 1.5 $ $Date: 2009/05/07 21:20:07 $
 */
public class Item extends MenuComponent {
	/*
	 * Implements the "Comosite Pattern" (Participant: Leaf)
	 */

	public Item(String title, String url, String... authorizedRoles) {
		super(title);
		if (url == null || url.length() == 0 || authorizedRoles == null || authorizedRoles.length == 0) {
			throw new IllegalArgumentException();
		}
		this.url = url;
		this.authorizedRoles = authorizedRoles;
	}

	public Item(String title, final Class<?> controllerClass, final String... authorizedRoles) {
		super(title);
		url = "/service/" + SbaConventions.controllerToViewName(controllerClass);
		this.authorizedRoles = authorizedRoles;
	}

	public String getUrl() {
		return url;
	}

	public String[] getAuthorizedRoles() {
		return authorizedRoles;
	}

	@Override
	public MenuComponent[] getChildrens() {
		return null;
	}

	@Override
	public boolean isAuthorized(final String[] userRoles) {
		return true;
	}

	@Override
	public boolean isUrlAuthorized(String urlParam, String[] userRoles) {
		return url.equals(urlParam) && isAuthorized(userRoles);
	}

	@Override
	public String[] retrieveRolesForUrl(String urlParam) {
		if (url.equals(urlParam)) {
			return authorizedRoles;
		}
		return null;
	}

	private final String url;
	private final String[] authorizedRoles;
}
