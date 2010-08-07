package commons.gui.model.bean;

import commons.gui.util.ValueConversor;

/**
 * @author P.Pastorino
 */
class TypeConversionInfo {

	Class m_wrapperClass;

	Object m_nullValue;

	ValueConversor m_conversor;

	boolean m_isPrimitive;

	// clase a la que se debe convertir siempre el valor
	// (por ejemplo si siempre se quiere ver Calendar como Date)
	Class m_targetClass;

	public Class getWrapperClass() {
		return m_wrapperClass;
	}

	public Object getNullValue() {
		return m_nullValue;
	}

	public ValueConversor getConversor() {
		return m_conversor;
	}

	public boolean isPrimitve() {
		return m_isPrimitive;
	}
}
