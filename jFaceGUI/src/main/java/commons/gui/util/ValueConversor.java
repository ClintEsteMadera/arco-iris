package commons.gui.util;

/**
 * Conversor de valores.
 * 
 * @author P.Pastorino
 */
public interface ValueConversor {

	public Object convertTo(Object value);

	public Object convertFrom(Object value);

}
