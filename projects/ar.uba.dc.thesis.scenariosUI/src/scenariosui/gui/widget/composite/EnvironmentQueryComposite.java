package scenariosui.gui.widget.composite;

import java.util.Collections;
import java.util.List;

import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;

import scenariosui.gui.action.ScenariosUIActions;
import scenariosui.gui.query.EnvironmentSearchCriteria;
import scenariosui.gui.util.purpose.ScenariosUIPurpose;
import scenariosui.properties.UniqueTableIdentifier;
import scenariosui.service.ScenariosUIController;
import ar.uba.dc.thesis.atam.scenario.model.Environment;

import commons.gui.action.OpenDialogWithPurposeAction;
import commons.gui.util.PageHelper;
import commons.properties.EnumProperty;
import commons.query.BaseCriteria;

public class EnvironmentQueryComposite extends ScenariosUIQueryComposite<Environment> {

	public EnvironmentQueryComposite() {
		this(PageHelper.getMainWindow().mainTabFolder, UniqueTableIdentifier.ENVIRONMENTS,
				new EnvironmentSearchCriteria());
	}

	public EnvironmentQueryComposite(Composite parent, EnumProperty tableName, BaseCriteria<Environment> searchCriteria) {
		super(parent, tableName, Environment.class, searchCriteria);
	}

	@Override
	protected void addSpecificFilters(Group groupOfFilters) {
		// TODO Implement me!
	}

	@Override
	protected List<Environment> executeQuery() {
		return ScenariosUIController.getInstance().getEnvironments(this.getCriteria());
	}

	@Override
	@SuppressWarnings("unchecked")
	protected OpenDialogWithPurposeAction<Environment, ScenariosUIPurpose> getActionForEdit() {
		return ScenariosUIActions.EDIT_ENVIRONMENT;
	}

	@Override
	protected List<Control> getFilterControls() {
		// TODO Implement me!
		return Collections.emptyList();
	}

	@Override
	protected ISelectionChangedListener getTableSelectionChangedListener() {
		return null;
	}

	@Override
	protected void addButtons() {
		super.addButtons();
		super.addButton(ScenariosUIActions.NEW_ENVIRONMENT);
	}
}
