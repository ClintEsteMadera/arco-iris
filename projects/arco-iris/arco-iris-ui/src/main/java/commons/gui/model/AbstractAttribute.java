package commons.gui.model;

import commons.gui.model.types.EditType;

/**
 * Clase base para los atributos.
 */
public abstract class AbstractAttribute implements Attribute {

	protected abstract Class getAttributeClass();

	@SuppressWarnings("unchecked")
	public void setEditConfiguration(String cfg) {
		m_editType = new EditType(getAttributeClass(), cfg);
	}

	@SuppressWarnings("unchecked")
	public EditType getValueType() {
		if (m_editType == null) {
			m_editType = new EditType(getAttributeClass(), null);
		}
		return m_editType;
	}

	public EditType m_editType;
}
