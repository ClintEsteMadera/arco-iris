package scenariosui.gui.widget.composite;

import java.util.Collections;
import java.util.List;

import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;

import sba.common.query.BaseCriterio;
import scenariosui.gui.action.ScenariosUIActions;
import scenariosui.gui.query.SelfHealingScenarioSearchCriteria;
import scenariosui.gui.widget.ScenariosUIWindow;
import scenariosui.properties.TableConstants;
import scenariosui.service.ScenariosUIController;
import ar.uba.dc.thesis.selfhealing.SelfHealingScenario;

import commons.gui.action.OpenDialogWithPropositoAction;
import commons.gui.widget.composite.QueryComposite;

public class SelfHealingScenarioQueryComposite extends QueryComposite<SelfHealingScenario> {

	private Button newButton;

	public SelfHealingScenarioQueryComposite() {
		this(ScenariosUIWindow.getInstance().mainTabFolder, new SelfHealingScenarioSearchCriteria());
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
		return ScenariosUIController.getCurrentSelfHealingConfiguration().getScenarios();
	}

	@Override
	protected OpenDialogWithPropositoAction getEdicionAction() {
		// TODO Implement me!
		return null;
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
	protected OpenDialogWithPropositoAction getVerAction() {
		// TODO Implement me!
		return null;
	}

	@Override
	protected boolean permiteBotonCerrar() {
		return false;
	}

	@Override
	protected boolean permiteBotonEdicion() {
		return false;
	}

	@Override
	protected boolean permiteBotonVer() {
		return false;
	}

	@Override
	protected void agregarBotones() {
		super.agregarBotones();
		super.agregarBoton(ScenariosUIActions.NEW_SELF_HEALING_SCENARIO);
	}
}
