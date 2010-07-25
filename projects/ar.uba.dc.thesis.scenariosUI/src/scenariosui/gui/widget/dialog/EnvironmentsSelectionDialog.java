package scenariosui.gui.widget.dialog;

import java.util.List;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;

import scenariosui.gui.query.EnvironmentSearchCriteria;
import scenariosui.gui.widget.composite.EnvironmentsSelectionQueryComposite;
import scenariosui.properties.ScenariosUILabels;
import ar.uba.dc.thesis.atam.scenario.model.Environment;

import commons.gui.Advised;
import commons.gui.util.PageHelper;

public class EnvironmentsSelectionDialog extends Dialog implements Advised<EnvironmentsSelectionQueryComposite> {

	private List<Environment> selectedEnvironments;

	private EnvironmentsSelectionQueryComposite environmentsSelectionQuerycomposite;

	private EnvironmentSearchCriteria criteria;

	public EnvironmentsSelectionDialog() {
		this(new EnvironmentSearchCriteria());
	}

	public EnvironmentsSelectionDialog(EnvironmentSearchCriteria criteria) {
		super(PageHelper.getMainShell());
		this.criteria = criteria;
	}

	@Override
	protected void configureShell(Shell shell) {
		super.configureShell(shell);
		shell.setText(ScenariosUILabels.SELECT_ENVIRONMENTS.toString());
		Rectangle centerLocation = PageHelper.getCenterLocation(800, 500);
		shell.setBounds(centerLocation);
	}

	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		super.createButtonsForButtonBar(parent);
		getButton(IDialogConstants.OK_ID).setEnabled(false);
	}

	@Override
	protected Control createDialogArea(Composite parent) {
		environmentsSelectionQuerycomposite = new EnvironmentsSelectionQueryComposite(this, parent, this.criteria) {
			// TODO ver si se puede evitar esta clase anónima
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

	@Override
	protected void cancelPressed() {
		this.close();
	}

	public List<Environment> getSelectedEnvironments() {
		return selectedEnvironments;

	}

	public void setSelectedEnvironments(List<Environment> selectedEnvironments) {
		this.selectedEnvironments = selectedEnvironments;
	}

	/**
	 * @see commons.gui.Advised#rowSelected(commons.gui.widget.composite.QueryComposite))
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
