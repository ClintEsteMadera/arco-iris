package commons.gui.model.types;

import java.text.FieldPosition;
import java.text.Format;
import java.text.ParsePosition;

/**
 * Clase base para la implementación de {@link java.text.Format}
 * 
 * @author P.Pastorino
 */
public abstract class AbstractFormat extends Format {

	private static final long serialVersionUID = 1L;

	public abstract String valueToString(Object obj);

	public abstract Object stringToValue(String str);

	@Override
	public StringBuffer format(Object obj, StringBuffer buffer, FieldPosition pos) {
		buffer.append(valueToString(obj));
		return buffer;
	}

	@Override
	public Object parseObject(String source, ParsePosition pos) {
		return stringToValue(source);
	}
}
