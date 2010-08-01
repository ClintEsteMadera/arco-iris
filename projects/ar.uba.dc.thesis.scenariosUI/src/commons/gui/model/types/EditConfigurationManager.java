package commons.gui.model.types;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.Format;
import java.util.Calendar;
import java.util.Date;
import java.util.Properties;

/**
 * Diccionario para tipos de datos. Por medio de esta clase, es posible registrar y consultar la informaci�n asociada a
 * los tipos de datos. Si bien existen algunos tipos definidos por default (configuraci�n para num�ricos, string y
 * fechas) las aplicaciones pueden modificar estos defaults y registrar sus propios tipos de datos en el diccionario
 * <br>
 * Esta informaci�n va a ser utilizada por otros componentes del framework principalmente para la creaci�n de m�scaras
 * de edici�n y formateo y para la configuraci�n de componentes visuales.
 * 
 * @author P.Pastorino
 */
public class EditConfigurationManager extends TypeDictionary {

	/**
	 * Obtiene la instancia del diccionario de datos (singleton)
	 * 
	 * @return
	 */
	public static EditConfigurationManager getInstance() {
		return s_instance;
	}

	/**
	 * Obtiene la configuraci�n para un tipo de dato. El procedimiento de b�squeda se describe en
	 * {@link commons.gui.model.types.TypeDictionary#get(EditType)}
	 * 
	 * @param type
	 * @return Configuraci�n asociada al tipo o null en caso de que no se encuentre.
	 */
	public <T> EditConfiguration getConfiguration(EditType<T> type) {
		EditConfiguration ret = (EditConfiguration) get(type);

		// para los enums si no se define explicitamente,
		// se carga la configuraci�n por defecto
		if (ret == null && type.getValueClass().isEnum()) {
			ret = new EnumConfiguration(type.getValueClass(), new Properties());
			s_instance.putConfiguration(type, ret);
		}

		return ret;
	}

	/**
	 * Registra la configuraci�n para un tipo de dato.
	 * 
	 * @param type
	 * @param cfg
	 */
	public void putConfiguration(EditType type, EditConfiguration cfg) {
		put(type, cfg);
	}

	/**
	 * Registra una configuraci�n para tipo num�rico.
	 * 
	 * @param numberClass
	 *            Clase del tipo num�rico
	 * @param type
	 *            Identificador del tipo
	 * @param params
	 *            Configuraci�n del tipo
	 */
	public <T extends Number> void putNumberConfiguration(Class<T> numberClass, String type, NumberEditParameters params) {
		put(new EditType<T>(numberClass, type), new NumberConfiguration(numberClass, params));
	}

	/**
	 * Registra una configuraci�n para tipo string.
	 * 
	 * @param type
	 *            Identificador del tipo
	 * @param params
	 *            Configuraci�n del tipo
	 */
	public void putStringConfiguration(String type, StringEditParameters params) {
		put(new EditType<String>(String.class, type), new StringConfiguration(params));
	}

	/**
	 * Registra una configuraci�n para tipo Date.
	 * 
	 * @param type
	 *            Identificador del tipo
	 * @param params
	 *            Configuraci�n del tipo
	 */
	public void putDateConfiguration(String type, DateEditParameters params) {
		put(new EditType<Date>(Date.class, type), new DateConfiguration(params));
	}

	/**
	 * Obtiene el prototipo asociado a un tipo. Este m�todo de convenciencia obtiene la configuraci�n asociada al tipo y
	 * retorna el prototipo provisto por la configuraci�n.
	 * 
	 * @param type
	 *            Identificador del tipo
	 * @return Prototipo asociado o null si no se encuentra definido.
	 * @see #getConfiguration(EditType)
	 * @see EditConfiguration#getPrototype()
	 */
	public <T> Object getPrototype(EditType<T> type) {
		EditConfiguration cfg = getConfiguration(type);
		return cfg != null ? cfg.getPrototype() : null;
	}

	/**
	 * Obtiene el prototipo para un tipo de dato.
	 * 
	 * @param valueClass
	 *            Clase del tipo de dato.
	 * @param type
	 *            Identificador del tipo de dato.
	 * @return
	 * @see #getPrototype(EditType)
	 */
	public <T> Object getPrototype(Class<T> valueClass, String type) {
		return getPrototype(new EditType<T>(valueClass, type));
	}

	/**
	 * Obtiene el formato asociado a un tipo. Este m�todo de convenciencia obtiene la configuraci�n asociada al tipo y
	 * retorna el formato provisto por la configuraci�n.
	 * 
	 * @param type
	 *            Identificador del tipo
	 * @return Formato asociado o null si no se encuentra definido.
	 * @see #getConfiguration(EditType)
	 * @see EditConfiguration#getFormat()
	 */

	public <T> Format getFormat(EditType<T> type) {
		EditConfiguration cfg = getConfiguration(type);
		return cfg != null ? cfg.getFormat() : null;
	}

	/**
	 * Obtiene el formato asociado a un tipo.
	 * 
	 * @param valueClass
	 *            Clase del tipo de dato.
	 * @param type
	 *            Identificador del tipo de dato.
	 * @return
	 * @see #getFormat(EditType)
	 */
	public <T> Format getFormat(Class<T> valueClass, String type) {
		return getFormat(new EditType<T>(valueClass, type));
	}

	private EditConfigurationManager() {
	}

	private static EditConfigurationManager s_instance;

	private static int calcMaxDigits(Number n) {
		return (int) Math.ceil(Math.log10(n.doubleValue()));
	}

	/**
	 * Inicializacion de tipos basicos.
	 */
	static {
		s_instance = new EditConfigurationManager();
		s_instance.putNumberConfiguration(Integer.class, null, new NumberEditParameters(
				calcMaxDigits(Integer.MAX_VALUE)));
		s_instance.putNumberConfiguration(Long.class, null, new NumberEditParameters(calcMaxDigits(Long.MAX_VALUE)));
		s_instance.putNumberConfiguration(Short.class, null, new NumberEditParameters(calcMaxDigits(Short.MAX_VALUE)));
		s_instance.putNumberConfiguration(Double.class, null, new NumberEditParameters());
		s_instance.putNumberConfiguration(BigDecimal.class, null, new NumberEditParameters());
		s_instance.putNumberConfiguration(BigInteger.class, null, new NumberEditParameters());
		s_instance.putNumberConfiguration(Number.class, null, new NumberEditParameters());

		s_instance.putDateConfiguration(null, new DateEditParameters());
		s_instance.putStringConfiguration(null, new StringEditParameters());
		s_instance.putConfiguration(new EditType<Boolean>(Boolean.class, null), new BooleanConfiguration());
		s_instance.putConfiguration(new EditType<Calendar>(Calendar.class, null), new CalendarConfiguration(
				new DateEditParameters()));
	}
}