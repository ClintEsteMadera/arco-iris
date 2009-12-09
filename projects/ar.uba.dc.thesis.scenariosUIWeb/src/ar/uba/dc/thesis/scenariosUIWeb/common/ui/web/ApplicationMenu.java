/*
 * $Id: ApplicationMenu.java,v 1.5 2009/04/30 18:37:13 cvsuribe Exp $
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
 * @version $Revision: 1.5 $ $Date: 2009/04/30 18:37:13 $
 */
public interface ApplicationMenu {
	List<MenuLine> getMenuLines();

	boolean isUrlAuthorized(String url);

	/**
	 * Retorna los roles autorizados a invocar la URL especificada, o <b>null</b> si no se
	 * encuentra una definici�n para la misma.
	 * @param url URL
	 * @return Roles autorizados
	 */
	String[] retrieveRolesForUrl(String url);
}
