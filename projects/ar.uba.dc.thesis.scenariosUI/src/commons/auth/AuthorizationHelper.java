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
 * $Id: AuthorizationHelper.java,v 1.1 2008/04/18 20:55:06 cvschioc Exp $
 */

package commons.auth;

import commons.gui.action.GuiAction;

/**
 * Interfaz que debe implementarse para llevar a cabo la autorizaci�n de acciones en la GUI.
 * 
 * @author Jonathan Chiocchio
 * @version $Revision: 1.1 $ $Date: 2008/04/18 20:55:06 $
 */

public interface AuthorizationHelper {

	/**
	 * Decide si el usuario conectado posee autorizaci�n para ejecutar la acci�n pasada por
	 * par�metro.
	 * 
	 * @param guiAction
	 *            la acci�n en cuesti�n.
	 * @return <code>true</code> si y solo s� el usuario posee autorizaci�n para ejecutar la
	 *         acci�n especificada en el par�metro.
	 */
	public boolean isUserAuthorized(GuiAction guiAction);

	/**
	 * Decide si el usuario conectado posee autorizaci�n para ejecutar ALGUNA las acciones pasadas
	 * por par�metro.
	 * 
	 * @param guiActions
	 *            las acciones en cuesti�n.
	 * @return <code>true</code> si y solo s� el usuario posee autorizaci�n para ejecutar ALGUNA
	 *         de las acciones especificadas en los par�metros.
	 */
	public boolean isUserAuthorized(GuiAction... guiActions);

	/**
	 * Especifica qu� roles de usuario tienen autorizaci�n para ejecutar la acci�n pasada por
	 * par�metro.
	 * 
	 * @param idUnicoGuiAction
	 *            el identificador �nico de la acci�n que se est� autorizando.
	 * @param grantedRoles
	 *            los roles que poseen autorizaci�n para ejecutar la acci�n.
	 */
	public void addGrantedPermissions(String idUnicoGuiAction, String... grantedRoles);
}
