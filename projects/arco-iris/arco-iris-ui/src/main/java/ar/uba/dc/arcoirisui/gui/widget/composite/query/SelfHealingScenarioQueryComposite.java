package ar.uba.dc.arcoirisui.gui.widget.composite.query;

import java.util.List;

import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.swt.widgets.Composite;

import ar.uba.dc.arcoirisui.gui.action.ArcoIrisUIActions;
import ar.uba.dc.arcoirisui.properties.UniqueTableIdentifier;
import ar.uba.dc.arcoirisui.service.ArcoIrisUIManager;
import ar.uba.dc.arcoiris.selfhealing.SelfHealingScenario;

import commons.gui.action.OpenDialogWithPurposeAction;
import commons.gui.util.PageHelper;
import commons.gui.util.purpose.Purpose;
import commons.query.BaseSearchCriteria;
import commons.query.SearchCriteria;

public class SelfHealingScenarioQueryComposite extends ArcoIrisUIQueryComposite<SelfHealingScenario> {

	public SelfHealingScenarioQueryComposite() {
		this(PageHelper.getMainWindow().mainTabFolder, new BaseSearchCriteria<SelfHealingScenario>());
	}

	public SelfHealingScenarioQueryComposite(Composite parent, SearchCriteria<SelfHealingScenario> criterioBusqueda) {
		super(parent, UniqueTableIdentifier.SCENARIOS, SelfHealingScenario.class, criterioBusqueda);
	}

	@Override
	protected List<SelfHealingScenario> executeQuery() {
		return ArcoIrisUIManager.getInstance().getScenarios(this.getCriteria());
	}

	@Override
	protected OpenDialogWithPurposeAction<SelfHealingScenario, Purpose> getActionForCreate() {
		return ArcoIrisUIActions.NEW_SELF_HEALING_SCENARIO;
	}

	@Override
	protected OpenDialogWithPurposeAction<SelfHealingScenario, Purpose> getActionForEdit() {
		return ArcoIrisUIActions.EDIT_SELF_HEALING_SCENARIO;
	}

	@Override
	protected OpenDialogWithPurposeAction<SelfHealingScenario, Purpose> getActionForDelete() {
		return ArcoIrisUIActions.DELETE_SELF_HEALING_SCENARIO;
	}

	@Override
	protected ISelectionChangedListener getTableSelectionChangedListener() {
		return null;
	}
}
