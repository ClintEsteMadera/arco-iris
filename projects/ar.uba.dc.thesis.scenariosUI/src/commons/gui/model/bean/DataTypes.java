package commons.gui.model.bean;

import java.util.Hashtable;

class DataTypes {

	static class PrimitiveTypeInfo {
		private Class m_wrapperClass;

		private Object m_nullValue;

		public PrimitiveTypeInfo(Class wrapperClass, Object nullValue) {
			m_wrapperClass = wrapperClass;
			m_nullValue = nullValue;
		}

		Class getWrapperClass() {
			return m_wrapperClass;
		}

		Object getNullValue() {
			return m_nullValue;
		}
	};

	static public PrimitiveTypeInfo getPrimitiveType(Class clazz) {
		return s_table.get(clazz.getName());
	}

	static Hashtable<String, PrimitiveTypeInfo> s_table;
	static {
		s_table = new Hashtable<String, PrimitiveTypeInfo>();
		s_table.put(Integer.TYPE.getName(),
				new PrimitiveTypeInfo(Integer.class, Integer.valueOf(0)));
		s_table.put(Boolean.TYPE.getName(), new PrimitiveTypeInfo(Boolean.class, Boolean.FALSE));
		s_table.put(Character.TYPE.getName(), new PrimitiveTypeInfo(Character.class, Character.MIN_VALUE));
		s_table.put(Byte.TYPE.getName(), new PrimitiveTypeInfo(Byte.class, Byte.valueOf((byte) 0)));
		s_table.put(Short.TYPE.getName(), new PrimitiveTypeInfo(Short.class, Short.valueOf((short) 0)));
		s_table.put(Long.TYPE.getName(), new PrimitiveTypeInfo(Long.class, Long.valueOf(0)));
		s_table.put(Float.TYPE.getName(), new PrimitiveTypeInfo(Float.class, Float.valueOf(0)));
		s_table.put(Double.TYPE.getName(), new PrimitiveTypeInfo(Double.class, Double.valueOf(0)));
	}
}