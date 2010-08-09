package commons.auth;

import commons.gui.action.GuiAction;

/**
 * Interfaz que debe implementarse para llevar a cabo la autorización de acciones en la GUI.
 * 
 * 
 */

public interface AuthorizationHelper {

	/**
	 * Decide si el usuario conectado posee autorización para ejecutar la acción pasada por parámetro.
	 * 
	 * @param guiAction
	 *            la acción en cuestión.
	 * @return <code>true</code> si y solo sí el usuario posee autorización para ejecutar la acción especificada en el
	 *         parámetro.
	 */
	public boolean isUserAuthorized(GuiAction guiAction);

	/**
	 * Decide si el usuario conectado posee autorización para ejecutar ALGUNA las acciones pasadas por parámetro.
	 * 
	 * @param guiActions
	 *            las acciones en cuestión.
	 * @return <code>true</code> si y solo sí el usuario posee autorización para ejecutar ALGUNA de las acciones
	 *         especificadas en los parámetros.
	 */
	public boolean isUserAuthorized(GuiAction... guiActions);

	/**
	 * Especifica qué roles de usuario tienen autorización para ejecutar la acción pasada por parámetro.
	 * 
	 * @param idUnicoGuiAction
	 *            el identificador único de la acción que se está autorizando.
	 * @param grantedRoles
	 *            los roles que poseen autorización para ejecutar la acción.
	 */
	public void addGrantedPermissions(String idUnicoGuiAction, String... grantedRoles);
}
