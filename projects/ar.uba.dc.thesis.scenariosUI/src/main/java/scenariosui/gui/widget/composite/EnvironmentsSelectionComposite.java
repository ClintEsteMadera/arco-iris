package scenariosui.gui.widget.composite;

import java.util.ArrayList;
import java.util.List;

import scenariosui.gui.action.ScenariosUIActions;
import scenariosui.gui.query.EnvironmentSearchCriteria;
import scenariosui.gui.widget.dialog.EnvironmentsSelectionDialog;
import scenariosui.properties.UniqueTableIdentifier;
import ar.uba.dc.thesis.atam.scenario.model.DefaultEnvironment;
import ar.uba.dc.thesis.atam.scenario.model.Environment;
import ar.uba.dc.thesis.util.Collections;

import commons.gui.widget.composite.MultipleObjectsSelectionComposite;
import commons.gui.widget.composite.ObjectSelectionMetainfo;
import commons.gui.widget.composite.helper.QueryCompositeObjectCreator;
import commons.utils.Clonator;

public class EnvironmentsSelectionComposite extends MultipleObjectsSelectionComposite<Environment> {

	private EnvironmentSearchCriteria criteria;

	private QueryCompositeObjectCreator<Environment> newObjectCreator;

	public EnvironmentsSelectionComposite(ObjectSelectionMetainfo info) {
		this(info, new EnvironmentSearchCriteria());
	}

	public EnvironmentsSelectionComposite(ObjectSelectionMetainfo info, EnvironmentSearchCriteria criteria) {
		super(info);
		this.criteria = criteria;
		this.newObjectCreator = new QueryCompositeObjectCreator<Environment>(UniqueTableIdentifier.ENVIRONMENTS,
				ScenariosUIActions.NEW_ENVIRONMENT);
	}

	@Override
	protected List<Environment> selectObjects() {
		EnvironmentSearchCriteria criteria = Clonator.clone(this.criteria);
		EnvironmentsSelectionDialog environmentsSelectionDialog = new EnvironmentsSelectionDialog(criteria);
		environmentsSelectionDialog.open();

		return environmentsSelectionDialog.getSelectedEnvironments();
	}

	@Override
	protected List<Environment> getValueToSetWhenClearing() {
		Environment defaultEnvironment = DefaultEnvironment.getInstance();
		return Collections.createList(defaultEnvironment);
	}

	@Override
	protected void viewObject(List<Environment> environments) {
		selectObjects();
	}

	@Override
	protected List<Environment> createNew(Object createOption) {
		Environment newEnvironment = this.newObjectCreator.createNew(createOption);

		// We don't use Collections.singletonList() because this list can grow in the near future...
		List<Environment> newEnvironments = new ArrayList<Environment>();
		newEnvironments.add(newEnvironment);

		return newEnvironments;
	}
}
