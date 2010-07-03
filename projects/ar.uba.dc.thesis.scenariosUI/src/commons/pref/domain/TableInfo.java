/*
 * Licencia de Caja de Valores S.A., Versi�n 1.0
 *
 * Copyright (c) 2006 Caja de Valores S.A.
 * 25 de Mayo 362, Ciudad Aut�noma de Buenos Aires, Rep�blica Argentina
 * Todos los derechos reservados.
 *
 * Este software es informaci�n confidencial y propietaria de Caja de Valores S.A. ("Informaci�n
 * Confidencial"). Usted no divulgar� tal Informaci�n Confidencial y la usar� solamente de acuerdo a
 * los t�rminos del acuerdo de licencia que posee con Caja de Valores S.A.
 */
/*
 * $Id: TableInfo.java,v 1.2 2008/02/05 16:05:16 cvschioc Exp $
 */
package commons.pref.domain;

import commons.properties.EnumProperty;


/**
 * Modela la informaci�n b�sica para una tabla visual.<br>
 * TODO Pensar en usar un mapa en lugar de un arreglo para las columnInfos para optimizar la
 * performance.
 * @author Margarita Buriano
 * @author Jonathan Chiocchio
 * @version $Revision: 1.2 $ $Date: 2008/02/05 16:05:16 $
 */
public class TableInfo {

	/**
	 * Crea un TableInfo.
	 * @param name
	 *            especifica el nombre de la tabla. <b>OBLIGATORIO</b>.
	 * @param order
	 *            especifica por qu� campo se ordena la tabla. <b>NO OBLIGATORIO</b>.
	 * @param columnInfos
	 *            especifica las columnas que tendra la tabla. <b>OBLIGATORIO</b>.
	 */
	public TableInfo(EnumProperty name, String order, ColumnInfo[] columnInfos) {
		this.name = name;
		this.columnInfos = columnInfos;
		this.setOrder(order); // default (ordena por la primer columna de la tabla)
	}

	public TableInfo() {
		super();
	}

	public EnumProperty getName() {
		return this.name;
	}

	/**
	 * @param name
	 *            especifica el nombre de la tabla. <b>OBLIGATORIO</b>.
	 */
	public void setName(EnumProperty name) {
		this.name = name;
	}

	public String getOrder() {
		return this.order;
	}

	/**
	 * @param order
	 *            especifica por qu� campo se ordena la tabla. <b>NO OBLIGATORIO</b>.
	 */
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

	/**
	 * @param columnInfos
	 *            especifica las columnas que tendra la tabla. <b>OBLIGATORIO</b>.
	 */
	public void setColumnInfos(ColumnInfo[] columnsInfo) {
		this.columnInfos = columnsInfo;
	}

	/**
	 * @return el �ndice de columna por la cual se ordena
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

	/**
	 * Este m�todo est� sobreescrito �nicamente a fines de debugging.
	 */
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

	private EnumProperty name;

	private String order;

	private ColumnInfo[] columnInfos;

	private int columnIndexOrder;
}