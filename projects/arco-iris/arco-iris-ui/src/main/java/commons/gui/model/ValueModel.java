package commons.gui.model;

import commons.gui.model.types.EditType;

/**
 * Modelo para valores simples. <br>
 * <br>
 * Esta interfaz especifica una forma uniforme para manipular valores y recibir eventos de notificación de cambios. Cada
 * <code>ValueModel</code> contiene un valor de tipo <code>Object</code> y puede estar contenido en un modelo más
 * complejo (ver {@link sba.ui.model.ComplexValueModel}) o contener valores con características específicas (por
 * ejemplo: {@link sba.ui.model.collection.ListValueModel}) <br>
 * <br>
 * Provee métodos para: <br>
 * <ul>
 * <li>Consultar el valor del modelo</li>
 * <li>Modificar el valor del modelo</li>
 * <li>Recibir notificaciones de los cambios sobre el modelo</li>
 * <li>Obtener meta-información asociada al modelo</li>
 * </ul>
 */
public interface ValueModel<T> extends ValueChangeNotifier<T> {
	/**
	 * Asigna un valor al modelo
	 * 
	 * @param value
	 *            Nuevo valor del modelo.
	 */
	public void setValue(T value);

	/**
	 * Obtiene el valor del modelo
	 */
	public T getValue();

	/**
	 * Obtiene el descriptor para las propiedades de los valores que puede contener el modelo.
	 */
	public EditType<T> getValueType();

	/**
	 * Notifica a los listeners que cambio el valor.
	 * 
	 * Este metodo debe ser usado solo en aquellos en los casos en que el modelo se modifique en forma "externa" (es
	 * decir cuando no se utiliza {@link #setValue(T)} Si se utliza {@link #setValue(T)} la notificación es automática.
	 * 
	 */
	public void notifyChange();
};
