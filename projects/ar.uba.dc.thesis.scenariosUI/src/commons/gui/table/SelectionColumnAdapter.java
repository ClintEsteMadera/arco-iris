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
 * $Id: SelectionColumnAdapter.java,v 1.4 2008/02/05 16:05:15 cvschioc Exp $
 */
package commons.gui.table;

import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.TableColumn;


import commons.pref.PreferencesManager;
import commons.pref.domain.TableInfo;
import commons.properties.EnumProperty;

/**
 * @version $Revision: 1.4 $ $Date: 2008/02/05 16:05:15 $
 */
public class SelectionColumnAdapter extends SelectionAdapter {

	public SelectionColumnAdapter(EnumProperty tableName, int columnIndex, GenericTable table) {
		super();
		this.tableName = tableName;
		this.table = table;
		this.columnIndex = columnIndex;
	}

	@Override
	public void widgetSelected(SelectionEvent event) {
		event.getSource();
		((GenericTableViewerSorter) table.getSorter()).doSort(columnIndex);
		TableColumn column = (TableColumn) event.getSource();
		TableInfo tableInfo = PreferencesManager.getInstance().getTableInfo(tableName);
		if (tableInfo == null) {
			throw new RuntimeException("No se encontr� la tabla " + tableName);
		}
		tableInfo.setOrder((String) column.getData());
		table.refresh();
	}

	private final EnumProperty tableName;

	private final GenericTable table;

	private final int columnIndex;
}
