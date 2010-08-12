package commons.auth;

import commons.gui.action.GuiAction;

/**
 * Specifies the interface of any object with the responsibility of authorizing
 * actions in this GUI.
 * 
 */
@SuppressWarnings("unchecked")
public interface AuthorizationManager {

	/**
	 * Decide si el usuario conectado posee autorización para ejecutar la acción
	 * pasada por parámetro.
	 * 
	 * @param guiAction
	 *            la acción en cuestión.
	 * @return <code>true</code> si y solo sí el usuario posee autorización para
	 *         ejecutar la acción especificada en el parámetro.
	 */
	public boolean isUserAuthorized(GuiAction guiAction);

	/**
	 * Decide si el usuario conectado posee autorización para ejecutar ALGUNA
	 * las acciones pasadas por parámetro.
	 * 
	 * @param guiActions
	 *            las acciones en cuestión.
	 * @return <code>true</code> si y solo sí el usuario posee autorización para
	 *         ejecutar ALGUNA de las acciones especificadas en los parámetros.
	 */
	public boolean isUserAuthorized(GuiAction... guiActions);

	/**
	 * Especifica qué roles de usuario tienen autorización para ejecutar la
	 * acción pasada por parámetro.
	 * 
	 * @param idUnicoGuiAction
	 *            el identificador único de la acción que se está autorizando.
	 * @param grantedRoles
	 *            los roles que poseen autorización para ejecutar la acción.
	 */
	public void addGrantedPermissions(String idUnicoGuiAction,
			String... grantedRoles);
}
