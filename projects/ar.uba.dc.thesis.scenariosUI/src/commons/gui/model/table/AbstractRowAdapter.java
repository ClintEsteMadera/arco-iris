package commons.gui.model.table;

import java.util.Arrays;


import commons.gui.model.types.EditConfigurationManager;
import commons.pref.PreferencesManager;
import commons.pref.domain.ColumnInfo;
import commons.pref.domain.TableInfo;
import commons.properties.EnumProperty;

/**
 * Clase base para la implementación de {@link sba.ui.model.table.TableRowAdapter}
 * @author P.Pastorino
 */
public abstract class AbstractRowAdapter implements FormattedTableRowAdapter {

	public AbstractRowAdapter(EnumProperty tableName) {
		this.tableName = tableName;
		this.configure(tableName);
	}

	public int getColumnCount() {
		final ColumnInfo[] columnInfo = this.tableInfo != null
				? this.tableInfo.getColumnInfos()
				: null;
		return columnInfo != null ? columnInfo.length : 0;
	}

	/**
	 * Obtiene el índice asociado a una clave.
	 * @param key
	 * @return
	 */
	public Object getColumnKey(int column) {
		final ColumnInfo[] columnInfo = this.tableInfo != null
				? this.tableInfo.getColumnInfos()
				: null;

		if (columnInfo != null && columnInfo[column] != null
				&& columnInfo[column].getFieldName() != null) {
			return columnInfo[column].getFieldName();
		}
		throw new IllegalArgumentException("No se definio el id para la columna " + column);
	}

	/**
	 * Obtiene el índice asociado a una clave.
	 * @param key
	 * @return
	 */
	public int getColumnIndex(Object key) {

		final int n = this.getColumnCount();

		for (int i = 0; i < n; i++) {
			Object columnKey = this.getColumnKey(i);
			if (key == columnKey || (key != null && key.equals(columnKey))) {
				return i;
			}
		}
		return -1;
	}

	/**
	 * Obtiene el nombre de la columna.
	 * @param column
	 *            índice d ela columna
	 */
	public String getColumnTitle(int column) {
		final ColumnInfo[] columnInfo = this.tableInfo != null
				? this.tableInfo.getColumnInfos()
				: null;

		if (columnInfo != null && columnInfo[column] != null
				&& columnInfo[column].getLabel() != null) {
			return columnInfo[column].getLabel().toString();
		}
		return "Col " + column;
	}

	/**
	 * Obtiene el nombre de la columna.
	 * @param columnKey
	 *            identificador de la columna
	 * @return
	 */
	public String getColumnName(Object columnKey) {
		final int i = getColumnIndex(columnKey);
		return i >= 0 ? getColumnTitle(i) : "";
	}

	/**
	 * Obtiene el prototipo para la columna
	 */
	@SuppressWarnings("unchecked")
	public Object getColumnPrototype(int column) {
		if (m_prototypes != null && m_prototypes[column] != null) {
			return m_prototypes[column];
		}
		return EditConfigurationManager.getInstance().getPrototype(getColumnClass(column),
				getColumnEditConfiguration(column));
	}

	/**
	 * Obtiene el tipo de dato de la columna
	 */
	public String getColumnEditConfiguration(int column) {
		return null;
	}

	/**
	 * Asigna un prototipo a la columna.
	 * @param key
	 *            Identificador de la columna
	 * @param prototype
	 *            Prototipo
	 */
	protected void setColumnPrototype(Object key, Object prototype) {
		final int i = getColumnIndex(key);
		if (i >= 0) {
			setColumnPrototype(i, prototype);
		}
	}

	/**
	 * Asigna un prototipo a la columna.
	 * @param col
	 *            Indice de la columna
	 * @param prototype
	 *            Prototipo
	 */
	protected void setColumnPrototype(int col, Object prototype) {
		if (col < 0 || col >= getColumnCount()) {
			return;
		}
		if (m_prototypes == null) {
			m_prototypes = new Object[getColumnCount()];
		}
		m_prototypes[col] = prototype;
	}

	/**
	 * Asigna el flag de "formateo" a una columna
	 * @param key
	 * @param b
	 */
	protected void setFormatted(Object key, boolean b) {
		final int i = getColumnIndex(key);
		if (i >= 0) {
			setFormatted(i, b);
		}
	}

	/**
	 * Asigna el flag de "formateo" a la columna.
	 * @param col
	 * @param b
	 */
	protected void setFormatted(int col, boolean b) {
		if (col < 0 || col >= getColumnCount()) {
			return;
		}
		if (m_formatted == null) {
			m_formatted = new boolean[getColumnCount()];
			Arrays.fill(m_formatted, true);
		}
		m_formatted[col] = b;
	}

	/**
	 * Informa si la columna es una columna "formateada"
	 */
	public boolean isFormatted(int column) {
		if (m_formatted != null) {
			return m_formatted[column];
		}
		return true;
	}

	public int getColumnWidth(int index) {
		final ColumnInfo[] columnInfo = this.tableInfo != null
				? this.tableInfo.getColumnInfos()
				: null;

		if (columnInfo != null && columnInfo[index] != null && columnInfo[index].getWidth() != null) {
			return columnInfo[index].getWidth().intValue();
		}
		return -1;
	}

	public int getSortColumn() {
		return tableInfo != null ? tableInfo.getColumnIndexOrder() : -1;
	}

	public EnumProperty getTableName() {
		return tableName;
	}

	/**
	 * Configura el adapter a partir de preferences.xml
	 * @param config
	 */
	protected void configure(EnumProperty aTableName) {
		this.tableInfo = PreferencesManager.getInstance().getTableInfo(aTableName);
	}

	private TableInfo tableInfo;

	private Object[] m_prototypes;

	private boolean[] m_formatted;

	private EnumProperty tableName;
}
