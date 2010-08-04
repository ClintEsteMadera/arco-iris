package scenariosui.gui.widget.composite.query;

import java.util.Collections;
import java.util.List;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;

import ar.uba.dc.thesis.atam.scenario.model.Environment;

import commons.gui.action.OpenDialogWithPurposeAction;
import commons.gui.util.purpose.Purpose;
import commons.gui.widget.composite.QueryComposite;
import commons.properties.EnumProperty;
import commons.query.BaseSearchCriteria;

/**
 * This class provides the default guidelines for any Query Composite within the application "Scenarios UI".
 * 
 * @param <T>
 *            the type of the items contained in the table within this Query Composite.
 */
public abstract class ScenariosUIQueryComposite<T> extends QueryComposite<T> {

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
	protected boolean showFilters() {
		return false;
	}

	/**
	 * We specifically make this method final so that subclasses don't have to overwrite it (since it is not used at all
	 * because this application doesn't support search filters)
	 */
	@Override
	protected final void addSpecificFilters(Group grupoFiltros) {
		// do nothing
	}

	/**
	 * We specifically make this method final so that subclasses don't have to overwrite it (since it is not used at all
	 * because this application doesn't support search filters)
	 */
	@Override
	protected final List<Control> getFilterControls() {
		return Collections.emptyList();
	}

	/**
	 * We specifically make this method final so that subclasses don't have to overwrite it (since it is not used at all
	 * because this application doesn't support viewing the elements within Query Composites.)
	 */
	@Override
	@SuppressWarnings("unchecked")
	protected final OpenDialogWithPurposeAction<Environment, Purpose> getActionForView() {
		throw new RuntimeException(
				"This method should not be called since Scenarios UI does not allow viewing on its QueryComposites");
	}

	/**
	 * For every Query Composite of this application we want them to show the up-to-date information on the tables.
	 */
	@Override
	protected boolean resetAfterAnyActionFinishedExecution() {
		return true;
	}
}
