package commons.gui.model.types;

import java.util.HashMap;
import java.util.Map;

/**
 * Clase base para diccionarios indexados por "tipos de dato".
 */
public class TypeDictionary {

	public TypeDictionary() {
		m_map = new HashMap<EditType, Object>();
	}

	/**
	 * Obtiene la entrada asociada a un tipo. El procedimiento de búsqueda es recursivo, de forma tal que si no
	 * encuentra una entrada para el tipo especificado, busca en en los tipos asociados a la super clase del tipo
	 * original (por ejemplo, si no se encuentra un tipo de clase <code>Integer<code>,
	 * se buscarán los tipos de clase <code>Number</code>). <br>
	 * En caso de no encontrarse el tipo de esta forma, se buscará el tipo por default para la clase (es decir el tipo
	 * con identificador nulo)
	 * 
	 * <br>
	 * Más precisamente, el proceso de búsqueda es el siguiente: <br>
	 * 
	 * <ul>
	 * 
	 * <li>
	 * 1 - Si se encuenta una entrada para el tipo especificado, se retorna el valor encontrado.</li>
	 * 
	 * <li>
	 * 2 - Si la clase asociada al <code>EditType<code> no es <code>Object<code>, 
	 * se invoca el procedimiento de búsqueda utilizando el identificador 
	 * <code>new EditType(superClass,id)</code>, donde <code>superClass</code> es la clase base del
	 * <code>EditType<code> original y <code>id</code> es el identificador del <code>EditType<code> original.</li>
	 * 
	 * 3 - Si no se encuentra con el precedimeinto anterior, se invoca la búsqueda utilizando el identificador
	 * <code>new EditType(clazz,null)</code>, donde <code>clazz</code> es la clase del
	 * <code>EditType<code> original. De esta forma se obtiene la 
	 * configuración por default para la clase.
	 * </li>
	 * 
	 * </ul>
	 * 
	 * @param type
	 *            Clave
	 * @return
	 */
	@SuppressWarnings("unchecked")
	protected <T> Object get(EditType<T> type) {
		Object value = null;
		Class<T> clazz = type.getValueClass();

		do {
			value = m_map.get(new EditType<T>(clazz, type.getClassConfiguration()));

			// FIXME: No se chequea si la variable "clazz" es nula o no (probable NPE)
			final Class<? super T> superClass = clazz.getSuperclass();
			if (superClass != null && (clazz.equals(Object.class) || superClass.equals(clazz))) {
				clazz = null;
			} else {
				clazz = (Class<T>) superClass;
			}
		} while (value == null && clazz != null);

		if (value == null && type.getClassConfiguration() != null) {
			return get(new EditType<T>(type.getValueClass(), null));
		}
		return value;
	}

	/**
	 * Registra un tipo.
	 * 
	 * @param type
	 * @param value
	 */
	protected void put(EditType type, Object value) {
		m_map.put(type, value);
	}

	private Map<EditType, Object> m_map;
}