package commons.pref.domain;

import commons.properties.EnumProperty;

/**
 * TODO We could think about using a map instead of an array, in order to improve performance
 */
public class TableInfo {

	private EnumProperty name;

	private String order;

	private ColumnInfo[] columnInfos;

	private int columnIndexOrder;

	/**
	 * Creates an instance of TableInfo.
	 * 
	 * @param name
	 *            table name. <b>Mandatory</b>.
	 * @param order
	 *            this table will be sorted according to this field name. <b>Not Mandatory</b>.
	 * @param columnInfos
	 *            specifies the column this table will have. <b>Mandatory</b>.
	 */
	public TableInfo(EnumProperty name, String order, ColumnInfo[] columnInfos) {
		this.name = name;
		this.columnInfos = columnInfos;
		this.setOrder(order); // by default, the sorting is performed according the first column
	}

	public TableInfo() {
		super();
	}

	public EnumProperty getName() {
		return this.name;
	}

	public void setName(EnumProperty name) {
		this.name = name;
	}

	public String getOrder() {
		return this.order;
	}

	public void setOrder(String order) {
		this.order = order;
		if (columnInfos == null) {
			columnIndexOrder = 0; // default column
		} else {
			for (int i = 0; i < columnInfos.length; i++) {
				if (columnInfos[i].getFieldName().equals(order)) {
					columnIndexOrder = i;
				}
			}
		}
	}

	public ColumnInfo[] getColumnInfos() {
		return columnInfos;
	}

	public void setColumnInfos(ColumnInfo[] columnsInfo) {
		this.columnInfos = columnsInfo;
	}

	/**
	 * @return the index of the column this table must be sorted.
	 */
	public int getColumnIndexOrder() {
		return columnIndexOrder;
	}

	public ColumnInfo getColumnInfo(String fieldName) {
		ColumnInfo result = null;
		for (ColumnInfo columnInfo : this.getColumnInfos()) {
			if (columnInfo.getFieldName().equals(fieldName)) {
				result = columnInfo;
				break;
			}
		}
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		boolean equals = false;
		if (obj instanceof TableInfo) {
			TableInfo tableInfo = (TableInfo) obj;
			equals = this.equalsTo(tableInfo);
		}
		return equals;
	}

	public boolean equalsTo(TableInfo tableInfo) {
		return name.equals(tableInfo.name);
	}

	@Override
	public int hashCode() {
		int result = 17;
		if (this.name != null) {
			result = 37 * result + this.name.hashCode();
		}
		return result;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("<table name=\"").append(this.name.toString()).append("\" ");
		sb.append("order=\"" + this.order).append("\" >");
		if (this.columnInfos != null) {
			for (ColumnInfo columnInfo : this.columnInfos) {
				sb.append("\n").append(columnInfo.toString());
			}
			sb.append("</table>");
		}
		return sb.toString();
	}
}