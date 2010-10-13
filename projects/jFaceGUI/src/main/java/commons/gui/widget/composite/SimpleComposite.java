package commons.gui.widget.composite;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

import commons.gui.widget.Alignment;
import commons.gui.widget.DefaultLayoutFactory;
import commons.gui.widget.factory.LabelFactory;
import commons.properties.FakeEnumProperty;

/**
 * Modela un composite básico.
 */

public class SimpleComposite extends Composite {

	private static final int DEFAULT_COLUMN_NUMBER = 2;

	private static final int DEFAULT_COLUMN_SPAN = 1;

	private static final Alignment DEFAULT_ALIGNMENT = Alignment.LEFT;

	protected boolean readOnly;

	private final int numColumns;

	private final int columnsToSpan;

	private int alignment;

	public SimpleComposite(Composite parent, boolean readOnly) {
		this(parent, readOnly, DEFAULT_ALIGNMENT, DEFAULT_COLUMN_NUMBER, DEFAULT_COLUMN_SPAN);
	}

	public SimpleComposite(Composite parent, boolean readOnly, int numColumns) {
		this(parent, readOnly, DEFAULT_ALIGNMENT, numColumns, DEFAULT_COLUMN_SPAN);
	}

	public SimpleComposite(Composite parent, boolean readOnly, int numColumns, int columnsToSpan) {
		this(parent, readOnly, DEFAULT_ALIGNMENT, numColumns, columnsToSpan);
	}

	public SimpleComposite(Composite parent, boolean readOnly, Alignment alignment, int numColumns, int columnsToSpan) {
		super(parent, SWT.NONE);
		this.readOnly = readOnly;
		this.numColumns = numColumns;
		this.columnsToSpan = columnsToSpan;
		this.alignment = alignment.getSWTAlignmentStyle();
		this.applyLayout();
	}

	/**
	 * Habilita o inhabilita todos los controles que tienen como padre a éste Composite.
	 */
	@Override
	public void setEnabled(boolean enabled) {
		Control[] children = getChildren();
		for (int i = 0; i < children.length; i++) {
			children[i].setEnabled(enabled);
		}
		super.setEnabled(enabled);
	}

	/**
	 * Provee un layout por default, sobreescribir este método si se desea otro layout.
	 */
	protected void applyLayout() {
		GridLayout layout = DefaultLayoutFactory.setDefaultGridLayout(this, getNumColumns());
		layout.marginLeft = 0;
		layout.marginWidth = 0;
		layout.marginHeight = 0;

		GridData gridData = (GridData) this.getLayoutData();
		gridData.horizontalSpan = columnsToSpan;
		gridData.horizontalAlignment = alignment;
	}

	protected int getNumColumns() {
		return numColumns;
	}

	/**
	 * Método Helper de conveniencia para las subclases.
	 */
	protected void createSeparatorLabel() {
		LabelFactory.createLabel(this, new FakeEnumProperty(""), false, false);
	}
}