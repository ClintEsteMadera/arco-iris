/*
 * $Id: Menu.java,v 1.3 2009/04/30 18:37:13 cvsuribe Exp $
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
 * @version $Revision: 1.3 $ $Date: 2009/04/30 18:37:13 $
 */
public class Menu extends MenuComponent {
	/*
	 * Implements the "Composite Pattern" (Participant: Composite)
	 */

	public Menu(String title, MenuComponent... childrens) {
		super(title);
		if (childrens == null || childrens.length == 0) {
			throw new IllegalArgumentException();
		}
		this.childrens = childrens;
	}

	@Override
	public MenuComponent[] getChildrens() {
		return childrens;
	}

	@Override
	public boolean isAuthorized(String[] userRoles) {
		for (MenuComponent element : childrens) {
			if (element.isAuthorized(userRoles)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean isUrlAuthorized(String url, String[] userRoles) {
		for (MenuComponent element : childrens) {
			if (element.isUrlAuthorized(url, userRoles)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public String[] retrieveRolesForUrl(String url) {
		for (MenuComponent element : childrens) {
			String[] rolesForUrl = element.retrieveRolesForUrl(url);
			if (rolesForUrl != null && rolesForUrl.length > 0) {
				return rolesForUrl;
			}
		}
		return null;
	}

	private final MenuComponent[] childrens;
}
