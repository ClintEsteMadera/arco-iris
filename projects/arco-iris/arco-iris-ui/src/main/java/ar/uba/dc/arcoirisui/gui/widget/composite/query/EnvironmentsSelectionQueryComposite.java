package ar.uba.dc.arcoirisui.gui.widget.composite.query;

import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;

import ar.uba.dc.arcoirisui.properties.UniqueTableIdentifier;
import ar.uba.dc.arcoiris.atam.scenario.model.Environment;

import commons.gui.Advised;
import commons.gui.table.GenericTable;
import commons.query.BaseSearchCriteria;
import commons.query.SearchCriteria;

public class EnvironmentsSelectionQueryComposite extends EnvironmentQueryComposite {

	private final Advised<EnvironmentsSelectionQueryComposite> advised;

	public EnvironmentsSelectionQueryComposite(Advised<EnvironmentsSelectionQueryComposite> advised, Composite parent) {
		this(advised, parent, new BaseSearchCriteria<Environment>());
	}

	public EnvironmentsSelectionQueryComposite(Advised<EnvironmentsSelectionQueryComposite> advised, Composite parent,
			SearchCriteria<Environment> criteria) {
		super(parent, UniqueTableIdentifier.ENVIRONMENT_SELECTION, criteria);
		this.advised = advised;
	}

	@Override
	protected ISelectionChangedListener getTableSelectionChangedListener() {
		return new ISelectionChangedListener() {
			public void selectionChanged(SelectionChangedEvent event) {
				if (advised != null) {
					advised.rowSelected(EnvironmentsSelectionQueryComposite.this);
				}
			}
		};
	}

	@Override
	protected IDoubleClickListener getTableDoubleClickListener() {
		return new IDoubleClickListener() {
			public void doubleClick(DoubleClickEvent event) {
				advised.rowDoubleClicked(EnvironmentsSelectionQueryComposite.this);
			}
		};
	}

	/**
	 * This kind of selection query composite is designed to select more than one item.
	 */
	@Override
	protected int getTableStyle() {
		return GenericTable.DEFAULT_TABLE_STYLE | SWT.MULTI;
	}
}