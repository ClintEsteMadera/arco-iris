package ar.uba.dc.arcoirisui.gui.widget.dialog;

import java.util.List;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;

import ar.uba.dc.arcoirisui.gui.widget.composite.query.EnvironmentsSelectionQueryComposite;
import ar.uba.dc.arcoirisui.properties.ArcoIrisUILabels;
import ar.uba.dc.arcoiris.atam.scenario.model.Environment;

import commons.gui.Advised;
import commons.gui.util.PageHelper;
import commons.query.BaseSearchCriteria;
import commons.query.SearchCriteria;

public class EnvironmentsSelectionDialog extends Dialog implements Advised<EnvironmentsSelectionQueryComposite> {

	private List<Environment> selectedEnvironments;

	private EnvironmentsSelectionQueryComposite environmentsSelectionQuerycomposite;

	private SearchCriteria<Environment> criteria;

	public EnvironmentsSelectionDialog() {
		this(new BaseSearchCriteria<Environment>());
	}

	public EnvironmentsSelectionDialog(SearchCriteria<Environment> criteria) {
		super(PageHelper.getMainShell());
		this.criteria = criteria;
	}

	@Override
	protected void configureShell(Shell shell) {
		super.configureShell(shell);
		shell.setText(ArcoIrisUILabels.SELECT_ENVIRONMENTS.toString());
	}

	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		super.createButtonsForButtonBar(parent);
		getButton(IDialogConstants.OK_ID).setEnabled(false);
	}

	@Override
	protected Control createDialogArea(Composite parent) {
		environmentsSelectionQuerycomposite = new EnvironmentsSelectionQueryComposite(this, parent, this.criteria) {
			@Override
			protected ISelectionChangedListener getTableSelectionChangedListener() {
				ISelectionChangedListener listener = new ISelectionChangedListener() {
					public void selectionChanged(SelectionChangedEvent event) {
						Button acceptButton = getButton(IDialogConstants.OK_ID);
						if (acceptButton != null) {
							acceptButton.setEnabled(!getTable().getSelectedElements().isEmpty());
						}
					}
				};
				return listener;
			}
		};
		return parent;
	}

	@Override
	protected void okPressed() {
		rowDoubleClicked(environmentsSelectionQuerycomposite);
	}

	public List<Environment> getSelectedEnvironments() {
		return selectedEnvironments;

	}

	public void setSelectedEnvironments(List<Environment> selectedEnvironments) {
		this.selectedEnvironments = selectedEnvironments;
	}

	/**
	 * @see jface.gui.gui.Advised#rowSelected(jface.gui.gui.widget.composite.QueryComposite))
	 */
	public void rowSelected(EnvironmentsSelectionQueryComposite queryComposite) {
		this.setSelectedEnvironments(queryComposite.getSelectedElements());
		this.close();
	}

	public void rowDoubleClicked(EnvironmentsSelectionQueryComposite queryComposite) {
		this.setSelectedEnvironments(queryComposite.getSelectedElements());
		this.close();
	}
}
