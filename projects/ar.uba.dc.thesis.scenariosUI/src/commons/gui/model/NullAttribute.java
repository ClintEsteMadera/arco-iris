package commons.gui.model;

import commons.gui.model.types.EditType;

/**
 * 
 * 
 */

@SuppressWarnings("unchecked")
public class NullAttribute implements Attribute {

	public Object getValue(Object target) {
		return null;
	}

	public EditType getValueType() {
		return type;
	}

	public void setValue(Object target, Object value) {
	}

	private static final EditType type = new EditType(Object.class);
}
