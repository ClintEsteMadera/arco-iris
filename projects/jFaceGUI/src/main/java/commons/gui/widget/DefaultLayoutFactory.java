package commons.gui.widget;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowData;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;

import commons.gui.util.PageHelper;

/**
 * 
 * 
 */
public abstract class DefaultLayoutFactory {

	public static void setDefaultGridLayout(Composite composite) {
		setDefaultGridLayout(composite, 2);
	}

	public static void setDefaultGridLayout(Composite composite, int numColumns, GridData gridData) {
		boolean makeColumnsEqualWidth = false;
		GridLayout layout = new GridLayout(numColumns, makeColumnsEqualWidth);
		layout.marginWidth = 5;
		layout.marginHeight = 5;
		layout.verticalSpacing = 6;
		composite.setLayout(layout);
		composite.setLayoutData(gridData);
	}

	public static void setDefaultGridLayout(Composite composite, int numColumns) {
		DefaultLayoutFactory.setDefaultGridLayout(composite, numColumns, getDefaultGridData());
	}

	public static void setGridHSpan(Control control, int n) {
		GridData grid = getGridData(control);
		grid.horizontalSpan = n;
		control.setLayoutData(grid);
	}

	public static void setGridVerticalIndent(Control control, int n) {
		GridData grid = getGridData(control);
		grid.verticalIndent = n;
		control.setLayoutData(grid);
	}

	public static Control addHorizontalSeparator(Composite parent) {
		GridLayout layout = ((GridLayout) parent.getLayout());
		int n = layout != null ? layout.numColumns : 1;

		Label separator = new Label(parent, SWT.SEPARATOR | SWT.HORIZONTAL);

		GridData gridSeparator = getGridData(separator);
		gridSeparator.horizontalSpan = n;
		gridSeparator.horizontalAlignment = SWT.FILL;
		separator.setLayoutData(gridSeparator);

		return separator;
	}

	public static GridData getGridData(Control control) {
		GridData grid = ((GridData) control.getLayoutData());
		if (grid == null) {
			grid = getDefaultGridData();
			control.setLayoutData(grid);
		}
		return grid;
	}

	public static GridData getGridData(int hSpan) {
		GridData data = new GridData(DEFAULT_GRID_DATA_STYLE);
		data.horizontalSpan = hSpan;
		return data;
	}

	public static void setGrabAllExcessesAndFillBothGridData(Composite composite, boolean cascadeToChildren) {
		GridData gridData = new GridData(SWT.FILL, SWT.FILL, true, true);
		composite.setLayoutData(gridData);
		if (cascadeToChildren) {
			for (Control child : composite.getChildren()) {
				child.setLayoutData(gridData);
			}
		}
	}

	public static void setDefaultRowLayout(Composite composite) {
		RowLayout layout = new RowLayout(SWT.HORIZONTAL);
		layout.marginWidth = 5;
		layout.marginHeight = 10;
		composite.setLayout(layout);
	}

	public static void setButtonRowLayoutData(Button button) {
		RowData data = new RowData();
		int widthHint = PageHelper.convertHorizontalDLUsToPixels(IDialogConstants.BUTTON_WIDTH);
		Point minSize = button.computeSize(SWT.DEFAULT, SWT.DEFAULT, true);
		data.width = Math.max(widthHint, minSize.x);
		button.setLayoutData(data);
	}

	public static void setButtonGridLayoutData(Button button) {
		GridData data = new GridData();
		int widthHint = PageHelper.convertHorizontalDLUsToPixels(IDialogConstants.BUTTON_WIDTH);
		Point minSize = button.computeSize(SWT.DEFAULT, SWT.DEFAULT, true);
		data.widthHint = Math.max(widthHint, minSize.x);
		button.setLayoutData(data);
	}

	public static void makeEqualsHeight(Control[] controls) {
		int maxH = 0;

		for (Control b : controls) {
			if (b != null) {
				Point p = b.computeSize(SWT.DEFAULT, SWT.DEFAULT, true);
				if (p.y > maxH) {
					maxH = p.y;
				}
			}
		}

		for (Control b : controls) {
			if (b != null) {
				GridData gd = DefaultLayoutFactory.getGridData(b);
				gd.heightHint = maxH;
				gd.horizontalAlignment = GridData.HORIZONTAL_ALIGN_BEGINNING;
				gd.grabExcessHorizontalSpace = false;
			}
		}
	}

	private static GridData getDefaultGridData() {
		return new GridData(DEFAULT_GRID_DATA_STYLE);
	}

	public static final int DEFAULT_GRID_DATA_STYLE = GridData.GRAB_HORIZONTAL | GridData.FILL_HORIZONTAL
			| GridData.VERTICAL_ALIGN_BEGINNING;

}
