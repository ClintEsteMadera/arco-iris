/*
 * $Id: RootMenu.java,v 1.3 2009/04/30 18:37:13 cvsuribe Exp $
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
public class RootMenu extends Menu {
	public RootMenu(MenuComponent... childrens) {
		super("<root-menu>", childrens);
	}
}
