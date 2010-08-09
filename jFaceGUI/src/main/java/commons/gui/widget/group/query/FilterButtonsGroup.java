package commons.gui.widget.group.query;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;

import commons.gui.widget.group.SimpleGroup;
import commons.properties.CommonLabels;

/**
 * Modela un grupo con botones "Buscar" y "Limpiar"
 * 
 */

public class FilterButtonsGroup extends SimpleGroup {

	public FilterButtonsGroup(Composite composite, SelectionListener filterListener, SelectionListener cleanUpListener) {
		super(composite, CommonLabels.EMPTY, false);
		super.getSwtGroup().setLayout(new GridLayout(1, true));
		super.getSwtGroup().setLayoutData(new GridData(GridData.FILL_BOTH));

		this.filterButton = new Button(super.getSwtGroup(), SWT.CENTER);
		this.filterButton.setText(CommonLabels.SEARCH.toString());
		this.filterButton.setLayoutData(new GridData(GridData.FILL_HORIZONTAL | GridData.GRAB_VERTICAL));
		this.filterButton.addSelectionListener(filterListener);

		this.cleanUpButton = new Button(super.getSwtGroup(), SWT.CENTER);
		this.cleanUpButton.setText(CommonLabels.CLEAN_FILTERS.toString());
		this.cleanUpButton.setLayoutData(new GridData(GridData.FILL_HORIZONTAL | GridData.GRAB_VERTICAL));
		this.cleanUpButton.addSelectionListener(cleanUpListener);
	}

	public Button getFilterButton() {
		return this.filterButton;
	}

	public Button getCleanUpButton() {
		return this.cleanUpButton;
	}

	private final Button filterButton;

	private final Button cleanUpButton;
}