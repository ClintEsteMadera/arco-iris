/*
 * $Id: MenuComponent.java,v 1.5 2009/04/30 18:37:13 cvsuribe Exp $
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

/**
 * @author H. Adri�n Uribe
 * @version $Revision: 1.5 $ $Date: 2009/04/30 18:37:13 $
 */
public abstract class MenuComponent {
	/*
	 * Implements the "Comosite Pattern" (Participant: Component)
	 */

	public MenuComponent(String title) {
		if (title == null) {
			throw new IllegalArgumentException();
		}
		this.title = title;
	}

	public String getTitle() {
		return title;
	}

	public abstract MenuComponent[] getChildrens();

	public abstract boolean isAuthorized(String[] roles);

	public abstract boolean isUrlAuthorized(String url, String[] roles);

	/**
	 * Retorna los roles autorizados a invocar la URL especificada, o <b>null</b> si no se
	 * encuentra una definici�n para la misma.
	 * @param url URL
	 * @return Roles autorizados
	 */
	public abstract String[] retrieveRolesForUrl(String url);

	private final String title;
}
