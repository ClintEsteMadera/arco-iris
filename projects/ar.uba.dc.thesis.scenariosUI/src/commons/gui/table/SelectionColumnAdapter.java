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
			throw new RuntimeException("No se encontró la tabla " + tableName);
		}
		tableInfo.setOrder((String) column.getData());
		table.refresh();
	}

	private final EnumProperty tableName;

	private final GenericTable table;

	private final int columnIndex;
}
