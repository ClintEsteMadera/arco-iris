package scenariosui.gui.widget.composite;

import org.eclipse.swt.widgets.Composite;

import ar.uba.dc.thesis.atam.scenario.model.Environment;
import ar.uba.dc.thesis.common.ThesisPojo;

import commons.gui.action.OpenDialogWithPurposeAction;
import commons.gui.util.purpose.Purpose;
import commons.gui.widget.composite.QueryComposite;
import commons.properties.EnumProperty;
import commons.query.BaseCriteria;

public abstract class ScenariosUIQueryComposite<T extends ThesisPojo> extends QueryComposite<T> {

	public ScenariosUIQueryComposite(Composite parent, EnumProperty tableName, Class<T> tableElementsClassName,
			BaseCriteria<T> searchCriteria) {
		super(parent, tableName, tableElementsClassName, searchCriteria);
	}

	@Override
	protected boolean closeButtonAllowed() {
		return false;
	}

	@Override
	protected boolean editionAllowed() {
		return true;
	}

	@Override
	protected boolean viewButtonAllowed() {
		return false;
	}

	@Override
	protected boolean showMessageAfterQuery() {
		return false;
	}

	@Override
	@SuppressWarnings("unchecked")
	protected OpenDialogWithPurposeAction<Environment, Purpose> getActionForView() {
		throw new RuntimeException(
				"This method should not be called since Scenarios UI does not allow viewing in its QueryComposites");
	}
}
