package commons.gui.model.collection;

import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import commons.gui.model.ValueModel;

/**
 * Modelo que contiene un valor de tipo lista.
 * @author P.Pastorino
 */
public interface ListValueModel<T> extends CollectionModel<T>, ValueModel<List<T>> {

	/**
	 * Obtiene la lista subyacente. NOTA: las modificaciones que se hagan a la lista obtenida no se
	 * van a reflejar necesariamente en el modelo ni van a generar eventos. Si se quiere modificar
	 * el modelo, deben utilizarse los métodos <code>add, remove, clear, etc</code>
	 * @return Lista contenida en el modelo.
	 */
	public List<T> getList();

	/**
	 * Setea el valor del modelo.
	 * @param list
	 */
	public void setList(List<T> list);

	/**
	 * Obtiene el tamaño de la lista.
	 * @return
	 */
	public int size();

	/**
	 * Agrega todos los elementos de una lista.
	 * @param elements
	 *            Lista cuyos elementos se agregan a lal ista del modelo.
	 */
	public void addAll(Collection<T> elements);

	/**
	 * Agrega un elemento a la lista en una posición
	 * @param index
	 *            Posición del nuevo elemento.
	 * @param element
	 *            Elemento.
	 */
	public void add(int index, T element);

	/**
	 * Aggrega un elemento al final de la lista.
	 * @param element
	 */
	public void add(T element);

	/**
	 * Agrega todos los elementos de una lista a partir de una posición.
	 * @param index
	 *            Posición a partir de la cual se agregan los elementos.
	 * @param elements
	 */
	public void addAll(int index, List<T> elements);

	/**
	 * Elimina elementos de una lista.
	 * @param index
	 *            Posición a partir de la cual se quiere eliminar
	 * @param n
	 *            Cantidad de elementos que se eliminana desde la posición <code>index</code>
	 */
	public void remove(int index, int n);

	/**
	 * Elimina un elemento de la lista.
	 * @param index
	 *            Posición del elemento que se quiere eliminar.
	 * @return Objeto eliminado.
	 */
	public T remove(int index);

	/**
	 * Obtiene un elemento de la lista.
	 * @param index
	 *            Posición del elemento que se quiere obtener.
	 * @return
	 */
	public T get(int index);

	/**
	 * Modifica una entrada en la lista.
	 * @param index
	 *            Posición que se quiere modificar.
	 * @param value
	 *            Valor que se quiere asignar a la posición.
	 * @return Valor anterior de la posición.
	 */
	public T set(int index, T value);

	/**
	 * Obtiene el índice de un elemento. Retorna el primer índice <code>i</code> para el cual se
	 * cumple <code>lista[i].equals(element)</code>
	 * @param element
	 *            Elemento cuyo índice se quiere averiguar.
	 * @return Índice que ocupa el elemento o -1 en caso de que no pertenezca a la lista.
	 */
	public int indexOf(T element);

	/**
	 * Obtiene un interador de todos los elementos de la lista.
	 */
	public Iterator<T> iterator();

	/**
	 * Ordena la lista.
	 * @param comp
	 *            Comparador utilizado para el ordenamiento.
	 */
	public void sort(Comparator<T> comp);

	/**
	 * Obtiene el índice de un elemento, utilizando un comparador para la búsqueda. Retorna el
	 * primer índice <code>i</code> para el cual se cumple <code>comp(lista[i],value) == 0</code>
	 * @param value
	 *            Elemento cuyo índice se quiere averiguar.
	 * @param comp
	 *            Comparador.
	 * @return
	 */
	public int indexOf(T value, Comparator<T> comp);

	/**
	 * Elimina todos los elementos de la lsita.
	 */
	public void clear();

	/**
	 * Elimina un elemento de la lista.
	 * @param c
	 *            Elemento que se quiere eliminar.
	 * @return <code>true</code> en caso de que el elemento se haya eliminado o <code>false</code>
	 *         si el elemento no pertencecía a la lista.
	 */
	public boolean remove(T elem);

	/**
	 * Agrega un listener para las notificaciones.
	 * @param listener
	 */
	public void addListChangeListener(ListChangeListener listener);

	/**
	 * Agrega un listener para las notificaciones.
	 * @param listener
	 */
	public void removeListChangeListener(ListChangeListener listener);

}