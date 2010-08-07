package commons.gui.model;

/**
 * Conjunto de atributos accesibles por clave.
 * 
 * @author P.Pastorino
 * 
 */
public interface Attributes<K> {

	/**
	 * Obtiene el atributo asociado a la clave.
	 * 
	 * @param key
	 * @return
	 */
	public Attribute getAttribute(K key);
}
