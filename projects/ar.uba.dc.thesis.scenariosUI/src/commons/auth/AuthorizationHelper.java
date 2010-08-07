package commons.auth;

import commons.gui.action.GuiAction;

/**
 * Interfaz que debe implementarse para llevar a cabo la autorizaci�n de acciones en la GUI.
 * 
 * 
 */

public interface AuthorizationHelper {

	/**
	 * Decide si el usuario conectado posee autorizaci�n para ejecutar la acci�n pasada por par�metro.
	 * 
	 * @param guiAction
	 *            la acci�n en cuesti�n.
	 * @return <code>true</code> si y solo s� el usuario posee autorizaci�n para ejecutar la acci�n especificada en el
	 *         par�metro.
	 */
	public boolean isUserAuthorized(GuiAction guiAction);

	/**
	 * Decide si el usuario conectado posee autorizaci�n para ejecutar ALGUNA las acciones pasadas por par�metro.
	 * 
	 * @param guiActions
	 *            las acciones en cuesti�n.
	 * @return <code>true</code> si y solo s� el usuario posee autorizaci�n para ejecutar ALGUNA de las acciones
	 *         especificadas en los par�metros.
	 */
	public boolean isUserAuthorized(GuiAction... guiActions);

	/**
	 * Especifica qu� roles de usuario tienen autorizaci�n para ejecutar la acci�n pasada por par�metro.
	 * 
	 * @param idUnicoGuiAction
	 *            el identificador �nico de la acci�n que se est� autorizando.
	 * @param grantedRoles
	 *            los roles que poseen autorizaci�n para ejecutar la acci�n.
	 */
	public void addGrantedPermissions(String idUnicoGuiAction, String... grantedRoles);
}
