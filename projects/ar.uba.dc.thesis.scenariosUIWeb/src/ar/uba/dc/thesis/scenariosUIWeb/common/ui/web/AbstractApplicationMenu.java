/*
 * $Id: AbstractApplicationMenu.java,v 1.7 2009/04/30 18:37:13 cvsuribe Exp $
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

import java.util.List;

import ar.uba.dc.thesis.scenariosUIWeb.common.ui.web.view.MenuLine;


/**
 * @author H. Adri�n Uribe
 * @version $Revision: 1.7 $ $Date: 2009/04/30 18:37:13 $
 */
public abstract class AbstractApplicationMenu implements ApplicationMenu {

	public AbstractApplicationMenu(RootMenu rootMenu, Link[] links) {
		this.rootMenu = rootMenu;
		this.links = links;
	}

	public List<MenuLine> getMenuLines() {
		return MenuLine.toMenuLines(rootMenu);
	}

	public boolean isUrlAuthorized(String url) {
		for (Link element : links) {
			if (element.isUrlAuthorized(url)) {
				return true;
			}
		}
		// return rootMenu.isUrlAuthorized(url, userRoles);
		return true;
	}

	public String[] retrieveRolesForUrl(String url) {
		String[] rolesForUrl = rootMenu.retrieveRolesForUrl(url);
		if ((rolesForUrl != null) && (rolesForUrl.length > 0)) {
			return rolesForUrl;
		}
		// si no encontr� en el men� busco en los links
		for (Link element : links) {
			if (element.getUrl().equals(url)) {
				return element.getAuthorizedRoles();
			}
		}
		// si no encontr� la URL retorno null
		return null;
	}

	private final RootMenu rootMenu;

	private final Link[] links;
}
