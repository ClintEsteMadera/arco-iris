/*
 * Licencia de Caja de Valores S.A., Versión 1.0
 *
 * Copyright (c) 2006 Caja de Valores S.A.
 * 25 de Mayo 362, Ciudad Autónoma de Buenos Aires, República Argentina
 * Todos los derechos reservados.
 *
 * Este software es información confidencial y propietaria de Caja de Valores S.A. ("Información
 * Confidencial"). Usted no divulgará tal Información Confidencial y la usará solamente de acuerdo a
 * los términos del acuerdo de licencia que posee con Caja de Valores S.A.
 */

/*
 * $Id: Preferences.java,v 1.2 2008/02/05 16:05:16 cvschioc Exp $
 */

package commons.pref.domain;

import commons.properties.EnumProperty;

/**
 * Modela las preferencias visuales del usuario.<br>
 * TODO Pensar en usar un mapa en lugar de un arreglo para las tables para optimizar la
 * performance.
 * @author Jonathan Chiocchio
 * @version $Revision: 1.2 $ $Date: 2008/02/05 16:05:16 $
 */

public class Preferences {

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

	/**
	 * Este método está sobreescrito únicamente a fines de debugging.
	 */
	@Override
	public String toString() {
		if(this.tables != null) {
			return tables.toString();
		}
		return super.toString();
	}

	private TableInfo[] tables;
}