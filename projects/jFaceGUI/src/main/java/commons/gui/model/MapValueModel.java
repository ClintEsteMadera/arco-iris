package commons.gui.model;

import java.beans.PropertyChangeListener;
import java.util.HashMap;
import java.util.Map;

import commons.gui.model.types.EditType;

/**
 * Modelo complejo basado en un <code>Map</code> de <code>ValueModel</code>. <br>
 * <br>
 * Este tipo de modelo puede ser útil para construir dinámicamente un modelo a partir de objetos "sueltos" (no
 * contenidos en un mismo objeto "raíz")
 * 
 * @author P.Pastorino
 */
public class MapValueModel extends ValueHolder implements CompositeModel {

	public CompositeModel getNestedModel(String key, Class clazz) {
		// TODO MapValueModel::getNestedModel() IMPLEMENTAR!
		throw new RuntimeException("Este método no se encuentra implementado!");
	}

	@SuppressWarnings("unchecked")
	public MapValueModel() {
		super(Map.class);
	}

	@SuppressWarnings("unchecked")
	public void setValue(Object key, Object value) {
		getValueModel(key).setValue(value);
	}

	public Object getValue(Object key) {
		return getValueModel(key).getValue();
	}

	public EditType getValueType(Object key) {
		return getValueModel(key).getValueType();
	}

	public ValueModel getValueModel(Object key) {
		final ValueModel v = m_map.get(key);
		if (v == null) {
			throw new IllegalArgumentException("Atributo invalido: " + key);
		}
		return v;
	}

	/**
	 * Agrega un {@link ValueModel} al modelo
	 * 
	 * @param key
	 *            Identificador del modelo
	 * @param value
	 *            Modelo
	 */
	public void putValueModel(Object key, ValueModel value) {
		m_map.put(key, value);
	}

	/**
	 * No implementado.
	 * 
	 * @see jface.gui.gui.model.ComplexModel#addPropertyChangeListener(PropertyChangeListener)
	 */
	public void addComplexValueChangeListener(ComplexValueChangeListener listener) {
		// TODO MapValueModel::addComplexValueChangeListener(listener) IMPLEMENTAR!
	}

	/**
	 * No implementado.
	 * 
	 * @see jface.gui.gui.model.ComplexModel#removePropertyChangeListener(PropertyChangeListener)
	 */
	public void removeComplexValueChangeListener(ComplexValueChangeListener listener) {
		// TODO MapValueModel::removeComplexValueChangeListener(listener) IMPLEMENTAR!
	}

	private Map<Object, ValueModel> m_map = new HashMap<Object, ValueModel>();
}