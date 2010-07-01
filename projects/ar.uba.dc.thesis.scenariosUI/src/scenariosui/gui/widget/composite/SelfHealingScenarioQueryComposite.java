package scenariosui.gui.widget.composite;

import java.util.Collections;
import java.util.List;

import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;

import sba.common.query.BaseCriterio;
import scenariosui.gui.action.ScenariosUIActions;
import scenariosui.gui.query.SelfHealingScenarioSearchCriteria;
import scenariosui.gui.util.purpose.ScenariosUIPurpose;
import scenariosui.properties.TableConstants;
import scenariosui.service.ScenariosUIController;
import ar.uba.dc.thesis.selfhealing.SelfHealingScenario;

import commons.gui.action.OpenDialogWithPurposeAction;
import commons.gui.util.PageHelper;
import commons.gui.widget.composite.QueryComposite;

public class SelfHealingScenarioQueryComposite extends QueryComposite<SelfHealingScenario> {

	public SelfHealingScenarioQueryComposite() {
		this(PageHelper.getMainWindow().mainTabFolder, new SelfHealingScenarioSearchCriteria());
	}

	public SelfHealingScenarioQueryComposite(Composite parent, BaseCriterio criterioBusqueda) {
		super(parent, TableConstants.SCENARIOS, SelfHealingScenario.class, criterioBusqueda);
	}

	@Override
	protected void addSpecificFilters(Group groupOfFilters) {
		// TODO Implement me!
	}

	@Override
	protected List<SelfHealingScenario> executeQuery() {
		return ScenariosUIController.getInstance().getCurrentSelfHealingConfiguration().getScenarios();
	}

	@Override
	protected OpenDialogWithPurposeAction<SelfHealingScenario, ScenariosUIPurpose> getActionForEdit() {
		return ScenariosUIActions.EDIT_SELF_HEALING_SCENARIO;
	}

	@Override
	protected OpenDialogWithPurposeAction<SelfHealingScenario, ScenariosUIPurpose> getActionForView() {
		return ScenariosUIActions.VIEW_SELF_HEALING_SCENARIO;
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
	protected boolean closeButtonAllowed() {
		return true;
	}

	@Override
	protected boolean editionAllowed() {
		return true;
	}

	@Override
	protected boolean viewButtonAllowed() {
		return true;
	}

	@Override
	protected void addButtons() {
		super.addButtons();
		super.addButton(ScenariosUIActions.NEW_SELF_HEALING_SCENARIO);
	}
}
