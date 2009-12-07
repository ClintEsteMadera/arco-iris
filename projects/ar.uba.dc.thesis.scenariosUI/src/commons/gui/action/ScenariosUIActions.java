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
 * $Id: EmisoresActions.java,v 1.1 2008/04/18 20:55:15 cvschioc Exp $
 */

package commons.gui.action;

import commons.auth.AuthorizationHelper;
import commons.gui.util.proposito.ScenariosUIProposito;

/**
 * Conglomera las Acciones de la GUI inherentes al sistema Scenarios UI y
 * especifica los permisos necesarios para poder ejecutar dichas Acciones.
 *
 * @author Jonathan Chiocchio
 * @version $Revision: 1.1 $ $Date: 2008/04/18 20:55:15 $
 */

public abstract class ScenariosUIActions {

	private static final String NO_SHORTCUT = "";

	public static void initialize(AuthorizationHelper authHelper) {
		authHelper.addGrantedPermissions(ID_SCENARIO_CREATE, "EVERYONE");
	}

	public static final String ID_SCENARIO_CREATE = "scenarioUI.scenario.create";

	public static ScenarioCreationAction SCENARIO_CREATION = new ScenarioCreationAction(ID_SCENARIO_CREATE,
			NO_SHORTCUT, ScenariosUIProposito.ALTA);
}