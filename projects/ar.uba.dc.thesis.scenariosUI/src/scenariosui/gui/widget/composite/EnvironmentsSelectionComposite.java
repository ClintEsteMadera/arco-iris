package scenariosui.gui.widget.composite;

import java.util.List;

import scenariosui.gui.query.EnvironmentSearchCriteria;
import scenariosui.gui.widget.dialog.EnvironmentsSelectionDialog;
import scenariosui.gui.widget.dialog.MultipleEnvironmentsDialog;
import ar.uba.dc.thesis.atam.scenario.model.DefaultEnvironment;
import ar.uba.dc.thesis.atam.scenario.model.Environment;
import ar.uba.dc.thesis.util.Collections;

import commons.gui.widget.composite.ObjectSelectionMetainfo;
import commons.gui.widget.composite.SimpleObjectSelectionComposite;
import commons.utils.Clonator;

public class EnvironmentsSelectionComposite extends SimpleObjectSelectionComposite<List<Environment>> {

	private EnvironmentSearchCriteria criteria;

	public EnvironmentsSelectionComposite(ObjectSelectionMetainfo info) {
		this(info, new EnvironmentSearchCriteria());
	}

	public EnvironmentsSelectionComposite(ObjectSelectionMetainfo info, EnvironmentSearchCriteria criteria) {
		super(info);
		this.criteria = criteria;
	}

	@Override
	protected List<Environment> selectObject() {
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
		new MultipleEnvironmentsDialog(environments).open();
	}
}
