package scenariosui.gui.widget.composite.query;

import java.util.List;

import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;

import scenariosui.gui.query.RepairStrategySearchCriteria;
import scenariosui.properties.ScenariosUIMessages;
import scenariosui.properties.UniqueTableIdentifier;
import scenariosui.service.ScenariosUIManager;
import ar.uba.dc.thesis.selfhealing.StrategyTO;

import commons.exception.ValidationException;
import commons.gui.Advised;
import commons.gui.action.OpenDialogWithPurposeAction;
import commons.gui.table.GenericTable;
import commons.gui.util.purpose.Purpose;
import commons.validation.ValidationError;

public class RepairStrategiesSelectionQueryComposite extends ScenariosUIQueryComposite<StrategyTO> {

	private final Advised<RepairStrategiesSelectionQueryComposite> advised;

	public RepairStrategiesSelectionQueryComposite(Advised<RepairStrategiesSelectionQueryComposite> advised,
			Composite parent) {
		this(advised, parent, new RepairStrategySearchCriteria());
	}

	public RepairStrategiesSelectionQueryComposite(Advised<RepairStrategiesSelectionQueryComposite> advised,
			Composite parent, RepairStrategySearchCriteria criteria) {
		super(parent, UniqueTableIdentifier.REPAIR_STRATEGY_SELECTION, StrategyTO.class, criteria);
		this.advised = advised;
	}

	@Override
	protected ISelectionChangedListener getTableSelectionChangedListener() {
		return new ISelectionChangedListener() {
			public void selectionChanged(SelectionChangedEvent event) {
				if (advised != null) {
					advised.rowSelected(RepairStrategiesSelectionQueryComposite.this);
				}
			}
		};
	}

	@Override
	protected IDoubleClickListener getTableDoubleClickListener() {
		return new IDoubleClickListener() {
			public void doubleClick(DoubleClickEvent event) {
				advised.rowDoubleClicked(RepairStrategiesSelectionQueryComposite.this);
			}
		};
	}

	/**
	 * This kind of selection query composite is designed to select more than one item.
	 */
	@Override
	protected int getTableStyle() {
		return GenericTable.DEFAULT_TABLE_STYLE | SWT.MULTI;
	}

	@Override
	protected List<StrategyTO> executeQuery() {
		List<StrategyTO> allStrategies = ScenariosUIManager.getInstance().getAllStrategies(this.getCriteria());

		if (allStrategies.isEmpty()) {
			throw new ValidationException(new ValidationError(ScenariosUIMessages.NO_REPAIR_STRATEGIES_FOUND, this
					.getCriteria().getStitchDirectory()));
		}
		return allStrategies;

	}

	@Override
	public RepairStrategySearchCriteria getCriteria() {
		return (RepairStrategySearchCriteria) super.getCriteria();
	}

	@Override
	protected boolean newButtonAllowed() {
		return false;
	}

	@Override
	protected boolean editButtonAllowed() {
		return false;
	}

	@Override
	protected boolean deleteButtonAllowed() {
		return false;
	}

	@Override
	protected <P extends Purpose> OpenDialogWithPurposeAction<StrategyTO, P> getActionForNew() {
		throw new RuntimeException(
				"This method should not be invoked since this composite does not allow creating new items");
	}

	@Override
	protected <P extends Purpose> OpenDialogWithPurposeAction<StrategyTO, P> getActionForEdit() {
		throw new RuntimeException(
				"This method should not be invoked since this composite does not allow editing items");
	}

	@Override
	protected <P extends Purpose> OpenDialogWithPurposeAction<StrategyTO, P> getActionForDelete() {
		throw new RuntimeException(
				"This method should not be invoked since this composite does not allow deleting items");
	}
}