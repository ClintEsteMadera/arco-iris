package ar.uba.dc.arcoirisui.gui.widget.composite;

import java.util.ArrayList;
import java.util.List;

import ar.uba.dc.arcoirisui.gui.action.ArcoIrisUIActions;
import ar.uba.dc.arcoirisui.gui.widget.dialog.EnvironmentsSelectionDialog;
import ar.uba.dc.arcoirisui.properties.UniqueTableIdentifier;
import ar.uba.dc.arcoiris.atam.scenario.model.AnyEnvironment;
import ar.uba.dc.arcoiris.atam.scenario.model.Environment;
import ar.uba.dc.arcoiris.util.Collections;

import commons.gui.widget.composite.MultipleObjectsSelectionComposite;
import commons.gui.widget.composite.ObjectSelectionMetainfo;
import commons.gui.widget.composite.helper.QueryCompositeObjectCreator;
import commons.query.BaseSearchCriteria;
import commons.query.SearchCriteria;
import commons.utils.Clonator;

public class EnvironmentsSelectionComposite extends MultipleObjectsSelectionComposite<Environment> {

	private SearchCriteria<Environment> criteria;

	private QueryCompositeObjectCreator<Environment> newObjectCreator;

	public EnvironmentsSelectionComposite(ObjectSelectionMetainfo info) {
		this(info, new BaseSearchCriteria<Environment>());
	}

	public EnvironmentsSelectionComposite(ObjectSelectionMetainfo info, SearchCriteria<Environment> criteria) {
		super(info);
		this.criteria = criteria;
		this.newObjectCreator = new QueryCompositeObjectCreator<Environment>(UniqueTableIdentifier.ENVIRONMENTS,
				ArcoIrisUIActions.NEW_ENVIRONMENT);
	}

	@Override
	protected List<Environment> selectObjects() {
		SearchCriteria<Environment> criteria = Clonator.clone(this.criteria);
		EnvironmentsSelectionDialog environmentsSelectionDialog = new EnvironmentsSelectionDialog(criteria);
		environmentsSelectionDialog.open();

		return environmentsSelectionDialog.getSelectedEnvironments();
	}

	@Override
	protected List<Environment> getValueToSetWhenClearing() {
		Environment anyEnvironment = AnyEnvironment.getInstance();
		return Collections.createList(anyEnvironment);
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
