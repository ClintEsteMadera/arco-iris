/*
 * $Id: GarantiasMenu.java,v 1.25 2009/07/01 23:00:45 cvstursi Exp $
 *
 * Licencia de Caja de Valores S.A., Versi�n 1.0
 * Copyright (c) 2006 Caja de Valores S.A.
 * 25 de Mayo 362, Ciudad Aut�noma de Buenos Aires, Rep�blica Argentina
 * Todos los derechos reservados.
 * Este software es informaci�n confidencial y propietaria de Caja de Valores S.A. ("Informaci�n
 * Confidencial"). Usted no divulgar� tal Informaci�n Confidencial y la usar� solamente de acuerdo a
 * los t�rminos del acuerdo de licencia que posee con Caja de Valores S.A.
 */
package ar.uba.dc.thesis.scenariosUIWeb.ui.web;

import org.springframework.stereotype.Component;

import ar.uba.dc.thesis.scenariosUIWeb.common.ui.web.AbstractApplicationMenu;
import ar.uba.dc.thesis.scenariosUIWeb.common.ui.web.ApplicationMenu;
import ar.uba.dc.thesis.scenariosUIWeb.common.ui.web.Item;
import ar.uba.dc.thesis.scenariosUIWeb.common.ui.web.Link;
import ar.uba.dc.thesis.scenariosUIWeb.common.ui.web.Menu;
import ar.uba.dc.thesis.scenariosUIWeb.common.ui.web.RootMenu;


/**
 * @author H. Adri�n Uribe
 * @version $Revision: 1.25 $ $Date: 2009/07/01 23:00:45 $
 */
@Component(value = "applicationMenu")
public class ScenariosUIMenu extends AbstractApplicationMenu {
	private ScenariosUIMenu() {
		super(rootMenu, LINKS);
	}

	public static ApplicationMenu getInstance() {
		return applicationMenu;
	}

	/* ITEMS */
	private static final Item UPLOAD_ACME_MODEL = new Item("Upload Acme Model", UploadAcmeModelController.class);

	/* ITEMS */
	private static final Item NEW_SCENARIO = new Item("New Scenario", NewScenarioController.class);

	/* MENUES */
	private static final Menu MENU_ADM = new Menu("Menu", NEW_SCENARIO, UPLOAD_ACME_MODEL);

	private static final RootMenu rootMenu = new RootMenu(MENU_ADM);

	/* LINKS */
	private static final Link[] LINKS = { new Link(NewScenarioController.class), new Link("/index.html") };

	private static ScenariosUIMenu applicationMenu = new ScenariosUIMenu();
}
