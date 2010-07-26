package commons.gui.action;

import org.eclipse.jface.action.Action;

/**
 * Interfaz que deben implementar todas las acciones de la aplicaci�n que utilize esta librer�a.
 */
public interface GuiAction<T> {

	/**
	 * Provee el identificador �nico de la acci�n. Com�nmente usado como "key" de Maps.El conocimiento de la unicidad
	 * del identificador lo posee el objeto que crea la GuiAction.
	 */
	public String getUniqueId();

	/**
	 * Provee la acci�n concreta de SWT, para un modelo espec�fico.
	 */
	public Action getActionFor(T model);

	/**
	 * Provee el atajo de teclado para la acci�n o un String vac�o en caso que dicha acci�n no posea un atajo de teclado
	 * asociado.
	 */
	public String getShortcut();
}
