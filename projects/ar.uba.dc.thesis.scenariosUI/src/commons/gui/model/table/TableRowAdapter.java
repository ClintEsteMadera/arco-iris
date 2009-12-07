package commons.gui.model.table;

/**
 * Adapter para convertir un objeto a una "fila" de una tabla.
 * 
 * <br>
 * <br>
 * Define "la estructura" o meta-información asociada a la
 * tabla (cantidad de columnas, nombre de las columnas, tipo de dato
 * de cada columna, etc) y también permite obtener los valores de cada
 * columna. De esta forma dada una colección de items y una instancia de esta clase,
 * es posible desplegar una tabla que muestre la colección en formato tabular. 
 */
public interface TableRowAdapter {
	
	/**
	 * Obtiene la cantidad de columnas.
	 * 
	 * @return
	 */
	public int getColumnCount();
	
	/**
	 * Obtiene el valor de una propiedad para un "objeto-fila".
	 * 
	 * @param row Objeto del cual se quiere obtener la propiedad.
	 * @param col Indica de la columna asociada a la propiedad.
	 */
	public Object getValueAt(Object row, int col);

	/**
	* Modifica el valor de una propiedad para un "objeto-fila".
	* 
	* @param value Valor que se asigan a la propiedad.
	* @param row Objeto que se quiere modificar.
	* @param col Indica de la columna asociada a la propiedad.
	*/
	public void setValueAt(Object value, Object row, int col);

	/**
	 * Obtiene la calve asociada a una columna. 
	 * 
	 * @return
	 */
	public Object getColumnKey(int index);

	/**
	 * Obtiene el nombre de la columna.
	 * 
	 * @return Un arreglo de Strings
	 */
	public String getColumnTitle(int col);

	/**
	 * Obtiene un prototipo para la columna.
	 * 
	 * El prototipo puede ser utilizado para configurar (por ejemplo) el 
	 * tamaño de la columna.
	 * 
	 * @param col
	 * @return
	 */
	public Object getColumnPrototype(int col);

	/**
	 * Obtiene el identificador del "tipo de dato" asociado a la columna.
	 * 
	 * A partir de este valor es posible obtener meta-información asociada a
	 * la columna ({@link sba.ui.model.types.EditConfiguration}) a través de
	 * la clase {@link sba.ui.model.types.EditConfigurationManager}.
	 * 
	 * Este valor puede ser utilizado (por ejemplo) para formatear los
	 * valores de la columna (seteando la cantidad de decimales, caracteres
	 * de relleno, etc)
	 * 
	 * @param col
	 * @return
	 */
	public String getColumnEditConfiguration(int col);

	/**
	 * Obtiene el anco de la columna
	 * 
	 * @param index
	 * @return ancho de columna o -1 si no está especificado
	 * 
	 */
	public int getColumnWidth(int index);
	
	/**
	 * Obtiene la clase asociada a una columna.
	 * @param column
	 * @return Class
	 */
	public Class getColumnClass(int col);
	
	/**
	 * Obtiene el indice de la columna sobre la cual ordenar o -1 si no está especificado.
	 * 
	 * @return
	 */
	public int getSortColumn();
}