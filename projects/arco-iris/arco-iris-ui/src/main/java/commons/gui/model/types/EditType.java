package commons.gui.model.types;

/**
 * Identificador de un tipo de dato. <br>
 * <br>
 * Un tipo de dato se identifica mediante una clase y un identificador. Por ejemplo para los tipos de la clase
 * <code>BigDecimal</code> que representen "montos" se puede construir el identificador
 * <code>new EditType(BigDecimal.class,"monto")</code> <br>
 * El identificador <code>null</code> se utiliza para la configuración por default de la clase. Es decir que el
 * identificador <code>new EditType(Date.class,null)</code> será utilizado para todos los valores de tipo fecha que no
 * tengan asociado un identificador.
 */
public class EditType<T> {

	/**
	 * Construye un identificador.
	 * 
	 * @param valueClass
	 *            Clase asociada al tipo
	 * @param configuration
	 *            identificador de una determinada configuracion para la clase.
	 */
	public EditType(Class<T> valueClass, String configuration) {
		m_valueClass = valueClass;
		m_configuration = configuration;
	}

	/**
	 * Construye un identificador por default para la clase. Asigna un
	 * <code>null<null> a la propiedad <code>classConfiguration</code>
	 * 
	 * @param valueClass
	 *            Clase asociada al tipo
	 */
	public EditType(Class<T> valueClass) {
		this(valueClass, null);
	}

	/**
	 * Obtiene el identificador de la configuración.
	 * 
	 * @return String
	 */
	public String getClassConfiguration() {
		return m_configuration;
	}

	/**
	 * Obtiene la clase asociada al tipo.
	 * 
	 * @return Class
	 */
	public Class<T> getValueClass() {
		return m_valueClass;
	}

	@Override
	public boolean equals(Object o) {
		if (o == null || !(o instanceof EditType)) {
			return false;
		}
		final EditType editType = (EditType) o;
		return equalsValue(m_valueClass, editType.m_valueClass)
				&& equalsValue(m_configuration, editType.m_configuration);
	}

	@Override
	public int hashCode() {
		return hashValue(m_valueClass) * (31 ^ 0) + hashValue(m_configuration) * (31 ^ 1);
	}

	private boolean equalsValue(Object a, Object b) {
		return a == b || (a != null && a.equals(b));
	}

	private int hashValue(Object o) {
		return o == null ? 0 : o.hashCode();
	}

	@Override
	public String toString() {
		return "[" + m_valueClass + "," + "configuration=" + m_configuration + "]";
	}

	private Class<T> m_valueClass;

	private String m_configuration;
}
