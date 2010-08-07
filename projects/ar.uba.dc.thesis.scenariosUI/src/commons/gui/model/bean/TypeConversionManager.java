package commons.gui.model.bean;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import commons.gui.util.ValueConversor;

/**
 * Adminsitador de conversores de tipos. <br>
 * <br>
 * Esta clase permite registrar conversores de tipos que son utilizados por otros componentes del framework
 * (especialmente por {@link commons.gui.model.bean.BeanAttribute}). De esta forma es posible, por ejemplo, manipular
 * objetos de tipos "no-standard" (<code>NumeroDeDocumento</code>,<code>Cuit</code>,etc) como si fueran objetos
 * "standard" (int,String) facilitando el data-binding.
 * 
 */
public class TypeConversionManager {

	/**
	 * Registra un conversor de tipos. A los efectos del data-binding las propiedades de los beans que sean del tipo
	 * especificado como <code>sourceClass</code> se van a comportar como si fueran de tipo <code>targetClass</code>
	 * 
	 * @param sourceClass
	 *            Clase original. Si se quiere manipular la propiedad de un bean que tiene un tipo no standard, debe
	 *            especificarse el tipo no standard en este parámetro. Por ejemplo: <code>NumeroDeDocumento.class</code>
	 * @param targetClass
	 *            Clase "destino" a la cual se quiere convertir. Si se quiere manipular la propiedad de un bean que
	 *            tiene un tipo no standard, debe especificarse el standard en este parámetro. Por ejemplo:
	 *            <code>Integer.class</code>
	 * @param conversor
	 *            Conversor que convierte de la clase original a la clase destino mediante el método
	 *            {@link sba.ui.util.ValueConversor#convertTo(java.lang.Object)} y de la clase destino a la clase
	 *            original mediante el método {@link sba.ui.util.ValueConversor#convertTo(java.lang.Object)}
	 */
	static public void addConvertibleType(Class sourceClass, Class targetClass, ValueConversor conversor) {
		s_table.put(sourceClass.getName(), createConvertible(targetClass, conversor));
	}

	private static TypeConversionInfo createPrimitive(Class wrapperClass, Object nullValue) {
		TypeConversionInfo ret = new TypeConversionInfo();
		ret.m_isPrimitive = true;
		ret.m_nullValue = nullValue;
		ret.m_wrapperClass = wrapperClass;
		return ret;
	}

	private static TypeConversionInfo createConvertible(Class wrapperClass, ValueConversor conversor) {
		TypeConversionInfo ret = new TypeConversionInfo();
		ret.m_isPrimitive = false;
		ret.m_wrapperClass = wrapperClass;
		ret.m_conversor = conversor;
		return ret;
	}

	static TypeConversionInfo getConversionInfo(Class clazz) {
		return s_table.get(clazz.getName());
	}

	static Map<String, TypeConversionInfo> s_table;

	static {
		s_table = new HashMap<String, TypeConversionInfo>();
		s_table.put(Integer.TYPE.getName(), createPrimitive(Integer.class, Integer.valueOf(0)));
		s_table.put(Boolean.TYPE.getName(), createPrimitive(Boolean.class, Boolean.FALSE));
		s_table.put(Character.TYPE.getName(), createPrimitive(Character.class, Character.valueOf((char) 0)));
		s_table.put(Byte.TYPE.getName(), createPrimitive(Byte.class, Byte.valueOf((byte) 0)));
		s_table.put(Short.TYPE.getName(), createPrimitive(Short.class, Short.valueOf((short) 0)));
		s_table.put(Long.TYPE.getName(), createPrimitive(Long.class, Long.valueOf(0)));
		s_table.put(Float.TYPE.getName(), createPrimitive(Float.class, Float.valueOf(0)));
		s_table.put(Double.TYPE.getName(), createPrimitive(Double.class, Double.valueOf(0)));
		s_table.put(Calendar.class.getName(), createConvertible(Date.class, new CalendarConversor()));
	}
}