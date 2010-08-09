package scenariosui.gui.widget.composite.query;

import java.util.List;

import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.swt.widgets.Composite;

import scenariosui.gui.action.ScenariosUIActions;
import scenariosui.gui.query.EnvironmentSearchCriteria;
import scenariosui.properties.UniqueTableIdentifier;
import scenariosui.service.ScenariosUIManager;
import ar.uba.dc.thesis.atam.scenario.model.Environment;

import commons.gui.action.OpenDialogWithPurposeAction;
import commons.gui.util.PageHelper;
import commons.gui.util.purpose.Purpose;
import commons.properties.EnumProperty;
import commons.query.BaseSearchCriteria;

public class EnvironmentQueryComposite extends ScenariosUIQueryComposite<Environment> {

	public EnvironmentQueryComposite() {
		this(PageHelper.getMainWindow().mainTabFolder, UniqueTableIdentifier.ENVIRONMENTS,
				new EnvironmentSearchCriteria());
	}

	public EnvironmentQueryComposite(Composite parent, EnumProperty tableName,
			BaseSearchCriteria<Environment> searchCriteria) {
		super(parent, tableName, Environment.class, searchCriteria);
	}

	@Override
	protected List<Environment> executeQuery() {
		return ScenariosUIManager.getInstance().getEnvironments(this.getCriteria());
	}

	@Override
	@SuppressWarnings("unchecked")
	protected OpenDialogWithPurposeAction<Environment, Purpose> getActionForNew() {
		return ScenariosUIActions.NEW_ENVIRONMENT;
	}

	@Override
	@SuppressWarnings("unchecked")
	protected OpenDialogWithPurposeAction<Environment, Purpose> getActionForEdit() {
		return ScenariosUIActions.EDIT_ENVIRONMENT;
	}

	@Override
	@SuppressWarnings("unchecked")
	protected OpenDialogWithPurposeAction<Environment, Purpose> getActionForDelete() {
		return ScenariosUIActions.DELETE_ENVIRONMENT;
	}

	@Override
	protected ISelectionChangedListener getTableSelectionChangedListener() {
		return null;
	}
}
