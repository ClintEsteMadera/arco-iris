package commons.gui.table;

import commons.gui.widget.Alignment;

import org.eclipse.swt.events.ControlAdapter;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.widgets.TableColumn;

import commons.pref.PreferencesManager;
import commons.pref.domain.ColumnInfo;
import commons.pref.domain.TableInfo;
import commons.properties.EnumProperty;

/**
 * @author Margarita Buriano
 *
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