package commons.properties;

import java.io.Serializable;

/**
 * 
 */

public interface EnumProperty extends Serializable {

	/**
	 * Ver {@link Object#toString()}
	 * 
	 * @return el valor de la propiedad RESUELTO (i.e. listo para ser usado)
	 */
	public String toString();

	/**
	 * Ver {@link Object#toString()}
	 * 
	 * @return el valor de la propiedad RESUELTO con los reemplazos hechos(i.e. listo para ser usado)
	 */
	public <T> String toString(T... replacements);

	/**
	 * Ver {@link Enum#name()}
	 * 
	 * @return el valor de la constante enumerada
	 */
	public String name();
}
