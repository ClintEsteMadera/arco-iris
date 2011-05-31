package commons.pref.domain;

import commons.properties.EnumProperty;

/**
 * User visual preferences.<br>
 * TODO We could think about using a map instead of an array, in order to improve performance
 * 
 */
public class Preferences {

	private TableInfo[] tables;

	public TableInfo[] getTables() {
		return this.tables;
	}

	public void setTables(TableInfo[] tables) {
		this.tables = tables;
	}

	public TableInfo getTableInfo(EnumProperty tableName) {
		TableInfo result = null;
		for (TableInfo tableInfo : this.tables) {
			if (tableInfo.getName().equals(tableName)) {
				result = tableInfo;
				break;
			}
		}
		return result;
	}

	@Override
	public String toString() {
		if (this.tables != null) {
			return tables.toString();
		}
		return super.toString();
	}
}