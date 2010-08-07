package commons.gui.model.table;

import commons.gui.model.AbstractAttribute;
import commons.gui.model.Attribute;
import commons.gui.model.Attributes;
import commons.gui.model.bean.BeanAttributes;
import commons.properties.EnumProperty;

/**
 * Extensión de {@link sba.ui.model.table.AbstractRowAdapter} que utiliza "atributos" para asignar y obtener los valores
 * de las columnas. <br>
 * <br>
 * 
 * @author P.Pastorino
 */
public class AttributesRowAdapter extends AbstractRowAdapter {

	/**
	 * Construye el adapter con una instancia de {@link sba.ui.model.bean.BeanAttributes}
	 * 
	 * @param beanClass
	 *            Clase de los objetos a los que se aplica el adapter.
	 * @param properties
	 *            Propiedades asociadas a cada columna.
	 */
	public AttributesRowAdapter(Class beanClass, EnumProperty tableName) {
		this(new BeanAttributes(beanClass), tableName);
	}

	/**
	 * Construye el adapter en base a un conjunto de atributos.
	 * 
	 * @param attributeSet
	 *            Conjunto de atributos.
	 * @param keys
	 *            Claves asociadas a cada columna.
	 */
	@SuppressWarnings("unchecked")
	public AttributesRowAdapter(Attributes attributeSet, EnumProperty tableName) {
		super(tableName);

		m_attributes = new Attribute[this.getColumnCount()];
		for (int k = 0; k < m_attributes.length; k++) {
			m_attributes[k] = attributeSet.getAttribute(this.getColumnKey(k));
		}
	}

	public Object getValueAt(Object row, int col) {
		Attribute attr = m_attributes[col];
		return attr != null ? attr.getValue(row) : null;

	}

	public void setValueAt(Object value, Object row, int col) {
		Attribute attr = m_attributes[col];

		if (attr != null)
			attr.setValue(row, value);
	}

	public Class getColumnClass(int col) {
		Attribute attr = m_attributes[col];
		return attr != null ? attr.getValueType().getValueClass() : Object.class;
	}

	@Override
	public String getColumnEditConfiguration(int col) {
		Attribute attr = m_attributes[col];
		return attr.getValueType().getClassConfiguration();
	}

	/**
	 * Obtiene el atributo asociado a una clave
	 */
	protected Attribute getAttribute(Object key) {
		final int i = getColumnIndex(key);
		if (i >= 0) {
			return m_attributes[i];
		}
		return null;
	}

	/**
	 * Asigan un tipo de dato a una columna.
	 * 
	 * @param key
	 * @param cfg
	 */
	protected void setAttributeEditConfiguration(Object key, String cfg) {
		Attribute attr = getAttribute(key);
		if (key != null && attr instanceof AbstractAttribute) {
			((AbstractAttribute) attr).setEditConfiguration(cfg);
		}
	}

	private Attribute[] m_attributes;
}
