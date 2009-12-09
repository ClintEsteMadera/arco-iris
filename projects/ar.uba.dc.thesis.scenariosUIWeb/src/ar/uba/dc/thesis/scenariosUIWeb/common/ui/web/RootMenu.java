/*
 * $Id: RootMenu.java,v 1.3 2009/04/30 18:37:13 cvsuribe Exp $
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
public class RootMenu extends Menu {
	public RootMenu(MenuComponent... childrens) {
		super("<root-menu>", childrens);
	}
}
