package commons.gui.table;

import java.text.Format;

import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.swt.graphics.Image;

import commons.gui.model.table.TableRowAdapter;
import commons.gui.model.types.EditConfiguration;
import commons.gui.model.types.EditConfigurationManager;
import commons.gui.model.types.EditType;

/**
 * This Provider is based on reflection.<br>
 * TODO cache the values.
 */
public class GenericLabelProvider implements ITableLabelProvider {

	private TableRowAdapter rowAdapter;

	public GenericLabelProvider(TableRowAdapter rowAdapter) {
		this.rowAdapter = rowAdapter;
	}

	public Image getColumnImage(Object o, int index) {
		return null;
	}

	public String getColumnText(Object o, int index) {
		if (index < 0 || index >= rowAdapter.getColumnCount()) {
			return "";
		}

		if (o == null) {
			return "";
		}

		final EditType eType = new EditType(rowAdapter.getColumnClass(index),
				rowAdapter.getColumnEditConfiguration(index));

		final EditConfiguration config = EditConfigurationManager.getInstance().getConfiguration(eType);

		Format format = null;

		if (config != null) {
			format = config.getFormat();
		}

		final Object value = rowAdapter.getValueAt(o, index);

		final String s = value == null ? "" : format != null ? format.format(value) : value.toString();
		return s;
	}

	public void addListener(ILabelProviderListener listener) {
	}

	public void dispose() {
	}

	public boolean isLabelProperty(Object o, String property) {
		return false;
	}

	public void removeListener(ILabelProviderListener arg0) {
	}
}