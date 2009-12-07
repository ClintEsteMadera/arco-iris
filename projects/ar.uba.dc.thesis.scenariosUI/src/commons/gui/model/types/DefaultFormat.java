package commons.gui.model.types;

import java.text.ParsePosition;

/**
 * @author P.Pastorino
 */
public class DefaultFormat extends AbstractFormat {

	public static DefaultFormat getInstance() {
		return s_instance;
	}

	protected DefaultFormat() {}

	@Override
	public String valueToString(Object obj) {
		return obj == null ? NULL_VALUE_STR : obj.toString();
	}

	@Override
	public Object stringToValue(String str) {
		return str;
	}
	
	@Override
	public Object parseObject(String source, ParsePosition pos){
		pos.setIndex(source.length()+1);
		return stringToValue(source);
	}

	private static final String NULL_VALUE_STR = "";

	private static final long serialVersionUID = 1L;

	private static DefaultFormat s_instance = new DefaultFormat();
}
