/*
 * $Id: AbstractApplicationMenu.java,v 1.7 2009/04/30 18:37:13 cvsuribe Exp $
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

import java.util.List;

import ar.uba.dc.thesis.scenariosUIWeb.common.ui.web.view.MenuLine;


/**
 * @author H. Adrián Uribe
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
		// si no encontré en el menú busco en los links
		for (Link element : links) {
			if (element.getUrl().equals(url)) {
				return element.getAuthorizedRoles();
			}
		}
		// si no encontré la URL retorno null
		return null;
	}

	private final RootMenu rootMenu;

	private final Link[] links;
}
