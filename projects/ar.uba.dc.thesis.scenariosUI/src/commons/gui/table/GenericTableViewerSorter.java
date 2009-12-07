package commons.gui.table;

import java.text.RuleBasedCollator;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerSorter;

import commons.gui.model.table.TableRowAdapter;

class GenericTableViewerSorter extends ViewerSorter {

	public GenericTableViewerSorter(TableRowAdapter row) {
		super();
		this.row = row;
	}

	public void doSort(int column) {
		if ((columnIndex != null) && (column == this.columnIndex)) {
			// Same column as last sort; toggle the direction
			direction = 1 - direction;
		} else {
			// New column; do an ascending sort
			this.columnIndex = column;
			direction = ASCENDING;
		}
	}

	/**
	 * Compara dos objetos, bajo ciertas circunstancias, delega la comparación de String en
	 * RuleBasedCollator.
	 * @see RuleBasedCollator
	 */
	@Override
	@SuppressWarnings("unchecked")
	public int compare(Viewer viewer, Object element1, Object element2) {
		int result = 0;

		Object value1 = element1 != null ? row.getValueAt(element1, columnIndex) : null;
		Object value2 = element2 != null ? row.getValueAt(element2, columnIndex) : null;

		// los enums los ordeno "alfabeticamente"
		if (value1 instanceof Enum) {
			value1 = value1.toString();
		}
		if (value2 instanceof Enum) {
			value2 = value2.toString();
		}

		try {
			if (value1 == null) {
				result = -1;
			} else if (value2 == null) {
				result = 1;
			} else if (value1 instanceof String) {
				try {
					// intento comparar como enteros, si ambos no son enteros, comparo como Strings.
					Integer intValue1 = Integer.parseInt((String) value1);
					Integer intValue2 = Integer.parseInt((String) value2);
					result = intValue1.compareTo(intValue2);
				} catch (NumberFormatException e) {
					result = super.getComparator().compare(value1, value2);
				}
			} else if (value1 instanceof Comparable) {
				result = ((Comparable) value1).compareTo(value2);
			}
		} catch (Exception e) {
			log.fatal("Error al comparar 2 campos para ordenar: " + row.getColumnKey(columnIndex),
					e);
		}

		// If descending order, flip the direction
		if (direction == DESCENDING) {
			result = -result;
		}

		return result;
	}

	private TableRowAdapter row;
	
	private Integer columnIndex;

	private int direction;

	private static final int ASCENDING = 0;

	private static final int DESCENDING = 1;

	private static final Log log = LogFactory.getLog(GenericTableViewerSorter.class);
}