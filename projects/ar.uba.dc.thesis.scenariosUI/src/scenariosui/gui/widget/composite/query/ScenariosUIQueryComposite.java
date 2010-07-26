package scenariosui.gui.widget.composite.query;

import org.eclipse.swt.widgets.Composite;

import ar.uba.dc.thesis.atam.scenario.model.Environment;
import ar.uba.dc.thesis.common.ThesisPojo;

import commons.gui.action.OpenDialogWithPurposeAction;
import commons.gui.util.purpose.Purpose;
import commons.gui.widget.composite.QueryComposite;
import commons.properties.EnumProperty;
import commons.query.BaseSearchCriteria;

public abstract class ScenariosUIQueryComposite<T extends ThesisPojo> extends QueryComposite<T> {

	public ScenariosUIQueryComposite(Composite parent, EnumProperty tableName, Class<T> tableElementsClassName,
			BaseSearchCriteria<T> searchCriteria) {
		super(parent, tableName, tableElementsClassName, searchCriteria);
	}

	@Override
	protected boolean newButtonAllowed() {
		return true;
	}

	@Override
	protected boolean editButtonAllowed() {
		return true;
	}

	@Override
	protected boolean viewButtonAllowed() {
		return false;
	}

	@Override
	protected boolean deleteButtonAllowed() {
		return true;
	}

	@Override
	protected boolean closeButtonAllowed() {
		return false;
	}

	@Override
	protected boolean showMessageAfterQuery() {
		return false;
	}

	@Override
	@SuppressWarnings("unchecked")
	protected final OpenDialogWithPurposeAction<Environment, Purpose> getActionForView() {
		throw new RuntimeException(
				"This method should not be called since Scenarios UI does not allow viewing in its QueryComposites");
	}

	/**
	 * For every Query Composite of this application we want them to show the up-to-date information on the tables.
	 */
	@Override
	protected boolean resetAfterAnyActionFinishedExecution() {
		return true;
	}
}
