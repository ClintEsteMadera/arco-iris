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
 * $Id: SizeControlAdapter.java,v 1.4 2008/02/05 16:05:15 cvschioc Exp $
 */
package commons.gui.table;

import org.eclipse.swt.events.ControlAdapter;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.widgets.TableColumn;


import commons.gui.widget.Alignment;
import commons.pref.PreferencesManager;
import commons.pref.domain.ColumnInfo;
import commons.pref.domain.TableInfo;
import commons.properties.EnumProperty;

/**
 * @author Margarita Buriano
 * @author Jonathan Chiocchio
 * @version $Revision: 1.4 $ $Date: 2008/02/05 16:05:15 $
 */
public class SizeControlAdapter extends ControlAdapter {

	public SizeControlAdapter(EnumProperty tableName) {
		super();
		this.tableName = tableName;
	}

	@Override
	public void controlResized(ControlEvent event) {
		TableColumn tableColumn = (TableColumn) event.getSource();
		TableInfo tableInfo = PreferencesManager.getInstance().getTableInfo(this.tableName);
		ColumnInfo columnInfo = tableInfo.getColumnInfo((String) tableColumn.getData());
		columnInfo.setAlignment(Alignment.getAlignmentFrom(tableColumn.getStyle()));
		columnInfo.setWidth(tableColumn.getWidth());
	}

	private final EnumProperty tableName;
}