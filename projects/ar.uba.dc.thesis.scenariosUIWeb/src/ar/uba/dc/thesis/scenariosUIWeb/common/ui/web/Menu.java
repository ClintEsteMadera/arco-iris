/*
 * $Id: Menu.java,v 1.3 2009/04/30 18:37:13 cvsuribe Exp $
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

/**
 * @author H. Adrián Uribe
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
