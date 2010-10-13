package commons.gui.model.types;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.FieldPosition;
import java.text.Format;
import java.text.ParsePosition;

/**
 * Configuracion de edicion y rendering para tipos numericos.
 */
public class NumberConfiguration implements EditConfiguration {

	public NumberConfiguration(Class valueClass, NumberEditParameters parameters) {
		m_valueClass = valueClass;
		m_parameters = new NumberEditParameters(parameters);
		if (m_parameters.maxIntDigits < 0) {
			m_parameters.maxIntDigits = getMaxDigits(valueClass);
		}
	}

	public NumberConfiguration(Class valueClass) {
		m_valueClass = valueClass;
		m_parameters = new NumberEditParameters();
	}

	public Class getValueClass() {
		return m_valueClass;
	}

	public Object getPrototype() {
		if (m_prototype == null) {
			m_prototype = m_parameters.getPrototype();
		}
		return m_prototype;
	}

	public Object getColumnPrototype() {
		if (m_columnPrototype == null) {
			m_columnPrototype = m_parameters.columnPrototype;
			if (m_columnPrototype == null) {
				m_columnPrototype = this.getPrototype();
			}
		}
		return m_columnPrototype;
	}

	public Format getFormat() {
		return format;
	}

	public Format getExportableFormat() {
		return m_parameters.getExportableFormat(m_valueClass);
	}

	public boolean isRightAligned() {
		return true;
	}

	public NumberEditParameters getParameters() {
		return m_parameters;
	}

	private static int getMaxDigits(Class valueClass) {
		if (Integer.class.isAssignableFrom(valueClass)) {
			return MAX_INTEGER_DIGITS;
		} else if (Long.class.isAssignableFrom(valueClass)) {
			return MAX_LONG_DIGITS;
		} else if (Short.class.isAssignableFrom(valueClass)) {
			return MAX_SHORT_DIGITS;
		} else if (Float.class.isAssignableFrom(valueClass)) {
			return MAX_FLOAT_DIGITS;
		} else if (Double.class.isAssignableFrom(valueClass)) {
			return MAX_DOUBLE_DIGITS;
		}
		return -1;
	}

	@Override
	public String toString() {
		return new StringBuffer(getClass().getName()).append(" [valueClass=").append(m_valueClass.getName())
				.append(" prototype=").append(m_prototype).append(" parameters={").append(m_parameters).append("}]")
				.toString();
	}

	private class FormatImpl extends Format {

		private static final long serialVersionUID = 1L;

		@Override
		public StringBuffer format(Object obj, StringBuffer toAppendTo, FieldPosition pos) {
			return m_parameters.getDecimalFormat(m_valueClass).format(obj, toAppendTo, pos);
		}

		@Override
		public Object parseObject(String source, ParsePosition pos) {
			final Object n = m_parameters.getDecimalFormat(m_valueClass).parseObject(source, pos);

			if (n == null) {
				return null;
			}

			if (n instanceof Number) {
				if (m_valueClass.equals(Integer.class) && !(n instanceof Integer)) {
					return new Integer(((Number) n).intValue());
				} else if (m_valueClass.equals(Long.class) && !(n instanceof Long)) {
					return new Long(((Number) n).longValue());
				} else if (m_valueClass.equals(Float.class)) {
					return new Float(((Number) n).floatValue());
				} else if (m_valueClass.equals(Double.class) && !(n instanceof Double)) {
					return new Double(((Number) n).doubleValue());
				} else if (m_valueClass.equals(Byte.class)) {
					return new Byte(((Number) n).byteValue());
				} else if (m_valueClass.equals(Short.class)) {
					return new Short(((Number) n).shortValue());
				} else if (m_valueClass.equals(BigDecimal.class) && !(n instanceof BigDecimal)) {
					return new BigDecimal(((Number) n).doubleValue());
				} else if (m_valueClass.equals(BigInteger.class) && !(n instanceof BigInteger)) {
					return new BigInteger(source);
				}
			}

			return n;
		}
	}

	private static int MAX_INTEGER_DIGITS = Integer.toString(Integer.MAX_VALUE).length();

	private static int MAX_SHORT_DIGITS = Short.toString(Short.MAX_VALUE).length();

	private static int MAX_LONG_DIGITS = Long.toString(Long.MAX_VALUE).length();

	private static int MAX_DOUBLE_DIGITS = Double.toString(Math.floor(Double.MAX_VALUE)).length();

	private static int MAX_FLOAT_DIGITS = Double.toString(Math.floor(Float.MAX_VALUE)).length();

	private Object m_prototype;

	private Object m_columnPrototype;

	private Class m_valueClass;

	private NumberEditParameters m_parameters;

	private FormatImpl format = new FormatImpl();
}
