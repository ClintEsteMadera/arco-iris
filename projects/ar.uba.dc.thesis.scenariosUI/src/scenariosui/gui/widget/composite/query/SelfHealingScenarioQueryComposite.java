package scenariosui.gui.widget.composite.query;

import java.util.List;

import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.swt.widgets.Composite;

import scenariosui.gui.action.ScenariosUIActions;
import scenariosui.gui.query.SelfHealingScenarioSearchCriteria;
import scenariosui.gui.util.purpose.ScenariosUIPurpose;
import scenariosui.properties.UniqueTableIdentifier;
import scenariosui.service.ScenariosUIManager;
import ar.uba.dc.thesis.selfhealing.SelfHealingScenario;

import commons.gui.action.OpenDialogWithPurposeAction;
import commons.gui.util.PageHelper;
import commons.query.BaseSearchCriteria;

public class SelfHealingScenarioQueryComposite extends ScenariosUIQueryComposite<SelfHealingScenario> {

	public SelfHealingScenarioQueryComposite() {
		this(PageHelper.getMainWindow().mainTabFolder, new SelfHealingScenarioSearchCriteria());
	}

	public SelfHealingScenarioQueryComposite(Composite parent, BaseSearchCriteria<SelfHealingScenario> criterioBusqueda) {
		super(parent, UniqueTableIdentifier.SCENARIOS, SelfHealingScenario.class, criterioBusqueda);
	}

	@Override
	protected List<SelfHealingScenario> executeQuery() {
		return ScenariosUIManager.getInstance().getScenarios(this.getCriteria());
	}

	@Override
	@SuppressWarnings("unchecked")
	protected OpenDialogWithPurposeAction<SelfHealingScenario, ScenariosUIPurpose> getActionForNew() {
		return ScenariosUIActions.NEW_SELF_HEALING_SCENARIO;
	}

	@Override
	@SuppressWarnings("unchecked")
	protected OpenDialogWithPurposeAction<SelfHealingScenario, ScenariosUIPurpose> getActionForEdit() {
		return ScenariosUIActions.EDIT_SELF_HEALING_SCENARIO;
	}

	@Override
	@SuppressWarnings("unchecked")
	protected OpenDialogWithPurposeAction<SelfHealingScenario, ScenariosUIPurpose> getActionForDelete() {
		return ScenariosUIActions.DELETE_SELF_HEALING_SCENARIO;
	}

	@Override
	protected ISelectionChangedListener getTableSelectionChangedListener() {
		return null;
	}
}
