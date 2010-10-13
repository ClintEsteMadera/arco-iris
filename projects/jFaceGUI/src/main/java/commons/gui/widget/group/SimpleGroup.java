package commons.gui.widget.group;

import org.apache.commons.lang.StringUtils;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;

import commons.gui.widget.DefaultLayoutFactory;
import commons.properties.EnumProperty;

/**
 * Grupo base del que heredan todos los grupos.
 */
public class SimpleGroup {

	private final Group swtGroup;

	protected boolean readOnly;

	private int numColumns;

	private int columnsToSpan;

	private static final int DEFAULT_NUM_COLUMNS = 2;

	private static final int DEFAULT_COLUMN_SPAN = 1;

	public SimpleGroup(Composite composite, EnumProperty title, boolean readOnly) {
		this(composite, title, readOnly, DEFAULT_NUM_COLUMNS, DEFAULT_COLUMN_SPAN);
	}

	public SimpleGroup(Composite composite, EnumProperty title, boolean readOnly, int numColumns) {
		this(composite, title, readOnly, numColumns, DEFAULT_COLUMN_SPAN);
	}

	public SimpleGroup(Composite composite, EnumProperty title, boolean readOnly, int numColumns, int columnsToSpan) {
		this.swtGroup = new Group(composite, SWT.NONE);
		this.numColumns = numColumns;
		this.columnsToSpan = columnsToSpan;
		String groupTitle = title.toString();
		if (StringUtils.isNotBlank(groupTitle)) {
			this.swtGroup.setText(groupTitle);
		}
		applyLayout();
		this.readOnly = readOnly;
	}

	public GridLayout getLayout() {
		return (GridLayout) this.getSwtGroup().getLayout();
	}

	public GridData getLayoutData() {
		return (GridData) this.getSwtGroup().getLayoutData();
	}

	/**
	 * Provee un layout por default, sobreescribir este método si se desea otro layout.
	 * 
	 * @return
	 */
	protected void applyLayout() {
		GridLayout layout = DefaultLayoutFactory.setDefaultGridLayout(this.getSwtGroup(), numColumns);
		if (!StringUtils.isEmpty(this.getSwtGroup().getText())) {
			layout.marginTop = 5;
		}
		layout.marginHeight = 5;
		layout.marginWidth = 10;

		this.getLayoutData().horizontalSpan = columnsToSpan;
	}

	public Group getSwtGroup() {
		return this.swtGroup;
	}

	public int getNumColumns() {
		return numColumns;
	}
}