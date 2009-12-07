package commons.gui.model;

import commons.gui.model.types.EditType;

/**
 * Interface para Modelos "complejos" (con múltiples propiedades). <br>
 * <br>
 * Estos modelos contienen uno o mas <code>ValueModel</code>, que pueden ser accedidos mediante
 * claves.
 */
public interface ComplexModel {
	/**
	 * Asigna un valor a una propiedad
	 * @param key
	 *            Identificador de la propiedad
	 * @param value
	 *            Valor asignado.
	 * @throws IllegalStateException
	 *             en caso de que el modelo no se encuentre en un estado valido para ser modificado.
	 * @throws IllegalArgumentException
	 *             en caso de la clave no corresponda a un valor modificable del modelo. ser
	 *             accedida.
	 */
	public void setValue(Object key, Object value) throws IllegalStateException,
			IllegalArgumentException;

	/**
	 * Obtiene el valor de una propiedad
	 * @param key
	 *            Identificador de la propiedad
	 * @return El valor de la propiedad
	 * @throws IllegalStateException
	 *             en caso de que el modelo no se encuentre en un estado valido para ser consultado.
	 * @throws IllegalArgumentException
	 *             en caso de la clave no corresponda del modelo.
	 */
	public Object getValue(Object key) throws IllegalStateException, IllegalArgumentException;

	/**
	 * Obtiene el descriptor de una propiedad.
	 * @param key
	 *            Identificador de la propiedad
	 * @param value
	 *            Valor asignado.
	 * @return El descriptor asociado a la propiedad.
	 * @throws IllegalArgumentException
	 *             en caso de la clave no corresponda del modelo.
	 */
	public EditType getValueType(Object key) throws IllegalArgumentException;

	/**
	 * Obtiene un contenedor de valor "generico" para una propiedad. El objetivo de este metodo es
	 * que las implementaciones puedan proveer una via mas eficiente para la manipulacion de un
	 * valor en particular.
	 * @param key
	 *            Identificador de la propiedad
	 * @return El contenedor de valor asociado a la propiedad.
	 * @throws IllegalArgumentException
	 *             en caso de la clave no corresponda del modelo.
	 */
	public ValueModel getValueModel(Object key) throws IllegalArgumentException;

	/**
	 * Agrega un listener para los eventos generados por el modelo.
	 * @param listener
	 */
	public void addComplexValueChangeListener(ComplexValueChangeListener listener);

	/**
	 * Elimina un listener.
	 * @param listener
	 * @see #addComplexValueChangeListener(ComplexValueChangeListener)
	 */
	public void removeComplexValueChangeListener(ComplexValueChangeListener listener);
};
