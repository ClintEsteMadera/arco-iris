/*
 * Licencia de Caja de Valores S.A., Versi�n 1.0
 *
 * Copyright (c) 2006 Caja de Valores S.A.
 * 25 de Mayo 362, Ciudad Aut�noma de Buenos Aires, Rep�blica Argentina
 * Todos los derechos reservados.
 *
 * Este software es informaci�n confidencial y propietaria de Caja de Valores S.A. ("Informaci�n
 * Confidencial"). Usted no divulgar� tal Informaci�n Confidencial y la usar� solamente de acuerdo a
 * los t�rminos del acuerdo de licencia que posee con Caja de Valores S.A.
 */

/*
 * $Id: MenuEmisoresCreator.java,v 1.12 2008/04/18 20:55:13 cvschioc Exp $
 */
package commons.gui.menu;

import org.eclipse.jface.action.MenuManager;

import commons.auth.AuthorizationHelper;
import commons.gui.action.ScenarioCreationAction;
import commons.gui.action.ScenariosUIActions;
import commons.gui.util.PageHelper;

/**
 * @author Jonathan Chiocchio
 * @version $Revision: 1.12 $ $Date: 2008/04/18 20:55:13 $
 */
public class ScenarioMenuCreator extends MenuCreator {

	public static ScenarioCreationAction SCENARIO_CREATION = ScenariosUIActions.SCENARIO_CREATION;

	@Override
	public void createMenu(MenuManager menuManager) {
		AuthorizationHelper authHelper = PageHelper.getMainWindow().getAuthorizationHelper();

		boolean currentUserCanCreate = authHelper.isUserAuthorized(SCENARIO_CREATION);

		if (currentUserCanCreate) {
			addMenuItem(menuManager, "Create Scenario", SCENARIO_CREATION);
		}
	}
}