/*
 * Licencia de Caja de Valores S.A., Versión 1.0
 *
 * Copyright (c) 2006 Caja de Valores S.A.
 * 25 de Mayo 362, Ciudad Autónoma de Buenos Aires, República Argentina
 * Todos los derechos reservados.
 *
 * Este software es información confidencial y propietaria de Caja de Valores S.A. ("Información
 * Confidencial"). Usted no divulgará tal Información Confidencial y la usará solamente de acuerdo a
 * los términos del acuerdo de licencia que posee con Caja de Valores S.A.
 */

/*
 * $Id: AuthorizationHelper.java,v 1.1 2008/04/18 20:55:06 cvschioc Exp $
 */

package commons.auth;

import commons.gui.action.GuiAction;

/**
 * Interfaz que debe implementarse para llevar a cabo la autorización de acciones en la GUI.
 * 
 * @author Jonathan Chiocchio
 * @version $Revision: 1.1 $ $Date: 2008/04/18 20:55:06 $
 */

public interface AuthorizationHelper {

	/**
	 * Decide si el usuario conectado posee autorización para ejecutar la acción pasada por
	 * parámetro.
	 * 
	 * @param guiAction
	 *            la acción en cuestión.
	 * @return <code>true</code> si y solo sí el usuario posee autorización para ejecutar la
	 *         acción especificada en el parámetro.
	 */
	public boolean isUserAuthorized(GuiAction guiAction);

	/**
	 * Decide si el usuario conectado posee autorización para ejecutar ALGUNA las acciones pasadas
	 * por parámetro.
	 * 
	 * @param guiActions
	 *            las acciones en cuestión.
	 * @return <code>true</code> si y solo sí el usuario posee autorización para ejecutar ALGUNA
	 *         de las acciones especificadas en los parámetros.
	 */
	public boolean isUserAuthorized(GuiAction... guiActions);

	/**
	 * Especifica qué roles de usuario tienen autorización para ejecutar la acción pasada por
	 * parámetro.
	 * 
	 * @param idUnicoGuiAction
	 *            el identificador único de la acción que se está autorizando.
	 * @param grantedRoles
	 *            los roles que poseen autorización para ejecutar la acción.
	 */
	public void addGrantedPermissions(String idUnicoGuiAction, String... grantedRoles);
}
