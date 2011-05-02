package ar.uba.dc.arcoirisui.gui.widget.composite.query;

import java.util.List;

import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.swt.widgets.Composite;

import ar.uba.dc.arcoirisui.gui.action.ArcoIrisUIActions;
import ar.uba.dc.arcoirisui.properties.UniqueTableIdentifier;
import ar.uba.dc.arcoirisui.service.ArcoIrisUIManager;
import ar.uba.dc.arcoiris.atam.scenario.model.Environment;

import commons.gui.action.OpenDialogWithPurposeAction;
import commons.gui.util.PageHelper;
import commons.gui.util.purpose.Purpose;
import commons.properties.EnumProperty;
import commons.query.BaseSearchCriteria;
import commons.query.SearchCriteria;

public class EnvironmentQueryComposite extends ArcoIrisUIQueryComposite<Environment> {

	public EnvironmentQueryComposite() {
		this(PageHelper.getMainWindow().mainTabFolder, UniqueTableIdentifier.ENVIRONMENTS,
				new BaseSearchCriteria<Environment>());
	}

	public EnvironmentQueryComposite(Composite parent, EnumProperty tableName,
			SearchCriteria<Environment> searchCriteria) {
		super(parent, tableName, Environment.class, searchCriteria);
	}

	@Override
	protected List<Environment> executeQuery() {
		return ArcoIrisUIManager.getInstance().getEnvironments(this.getCriteria());
	}

	@Override
	protected OpenDialogWithPurposeAction<Environment, Purpose> getActionForCreate() {
		return ArcoIrisUIActions.NEW_ENVIRONMENT;
	}

	@Override
	protected OpenDialogWithPurposeAction<Environment, Purpose> getActionForEdit() {
		return ArcoIrisUIActions.EDIT_ENVIRONMENT;
	}

	@Override
	protected OpenDialogWithPurposeAction<Environment, Purpose> getActionForDelete() {
		return ArcoIrisUIActions.DELETE_ENVIRONMENT;
	}

	@Override
	protected ISelectionChangedListener getTableSelectionChangedListener() {
		return null;
	}
}
