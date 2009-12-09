/*
 * $Id: ApplicationMenu.java,v 1.5 2009/04/30 18:37:13 cvsuribe Exp $
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
 * @version $Revision: 1.5 $ $Date: 2009/04/30 18:37:13 $
 */
public interface ApplicationMenu {
	List<MenuLine> getMenuLines();

	boolean isUrlAuthorized(String url);

	/**
	 * Retorna los roles autorizados a invocar la URL especificada, o <b>null</b> si no se
	 * encuentra una definición para la misma.
	 * @param url URL
	 * @return Roles autorizados
	 */
	String[] retrieveRolesForUrl(String url);
}
