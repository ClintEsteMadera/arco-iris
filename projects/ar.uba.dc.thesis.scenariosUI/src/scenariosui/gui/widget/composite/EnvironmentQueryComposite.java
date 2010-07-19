package scenariosui.gui.widget.composite;

import java.util.Collections;
import java.util.List;

import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;

import scenariosui.gui.action.ScenariosUIActions;
import scenariosui.gui.query.EnvironmentSearchCriteria;
import scenariosui.properties.TableConstants;
import scenariosui.service.ScenariosUIController;
import ar.uba.dc.thesis.atam.scenario.model.Environment;

import commons.gui.action.OpenDialogWithPurposeAction;
import commons.gui.util.PageHelper;
import commons.gui.util.purpose.Purpose;
import commons.gui.widget.composite.QueryComposite;
import commons.properties.EnumProperty;
import commons.query.BaseCriteria;

public class EnvironmentQueryComposite extends QueryComposite<Environment> {

	public EnvironmentQueryComposite() {
		this(PageHelper.getMainWindow().mainTabFolder, TableConstants.ENVIRONMENTS, new EnvironmentSearchCriteria());
	}

	public EnvironmentQueryComposite(Composite parent, EnumProperty tableName, BaseCriteria searchCriteria) {
		super(parent, tableName, Environment.class, searchCriteria);
	}

	@Override
	protected void addSpecificFilters(Group groupOfFilters) {
		// TODO Implement me!
	}

	@Override
	// FIXME workaround para que el update de environments se refleje correctamente en la tabla
	protected List<Environment> executeQuery() {
		ScenariosUIController scenariosUIController = ScenariosUIController.getInstance();

		if (this.getCriterio().getId() == null) {
			return scenariosUIController.getCurrentSelfHealingConfiguration().getEnvironments();
		} else {
			Environment environment = scenariosUIController.findEnvironment(this.getCriterio().getId());
			this.getCriterio().setId(null);
			return Collections.singletonList(environment);
		}
	}

	@Override
	protected OpenDialogWithPurposeAction<Environment, Purpose> getActionForEdit() {
		throw new RuntimeException("Functionality not implemented yet");
	}

	@Override
	protected OpenDialogWithPurposeAction<Environment, Purpose> getActionForView() {
		throw new RuntimeException("Functionality not implemented yet");
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
		return false;
	}

	@Override
	protected boolean editionAllowed() {
		return false;
	}

	@Override
	protected boolean viewButtonAllowed() {
		return false;
	}

	@Override
	protected void addButtons() {
		super.addButtons();
		super.addButton(ScenariosUIActions.NEW_ENVIRONMENT);
	}
}
