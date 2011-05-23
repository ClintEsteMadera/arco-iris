package commons.properties;

import java.io.Serializable;

public interface EnumProperty extends Serializable {

	/**
	 * See {@link Object#toString()}
	 * 
	 * @return the value of the enum property, <b>already resolved</b>.
	 */
	public String toString();

	/**
	 * See {@link Object#toString()}
	 * 
	 * @return the value of the enum property, <b>already resolved</b>.
	 */
	public <T> String toString(T... replacements);

	/**
	 * See {@link Enum#name()}
	 */
	public String name();
}
