package commons.gui.action;

import org.eclipse.jface.action.Action;

/**
 * Interfaz que deben implementar todas las acciones de la aplicación que utilize esta librería.
 */
public interface GuiAction<T> {

	/**
	 * Provee el identificador único de la acción. Comúnmente usado como "key" de Maps.El conocimiento de la unicidad
	 * del identificador lo posee el objeto que crea la GuiAction.
	 */
	public String getUniqueId();

	/**
	 * Provee la acción concreta de SWT, para un modelo específico.
	 */
	public Action getActionFor(T model);

	/**
	 * Provee el atajo de teclado para la acción o un String vacío en caso que dicha acción no posea un atajo de teclado
	 * asociado.
	 */
	public String getShortcut();
}
