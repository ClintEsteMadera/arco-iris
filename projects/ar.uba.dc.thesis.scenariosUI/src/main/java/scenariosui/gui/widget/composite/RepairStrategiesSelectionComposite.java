package scenariosui.gui.widget.composite;

import org.apache.commons.lang.StringUtils;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.DirectoryDialog;

import scenariosui.gui.query.RepairStrategySearchCriteria;
import scenariosui.gui.widget.dialog.RepairStrategiesSelectionDialog;
import scenariosui.properties.ScenariosUIMessages;
import ar.uba.dc.thesis.selfhealing.repair.AllRepairStrategies;
import ar.uba.dc.thesis.selfhealing.repair.RepairStrategies;
import ar.uba.dc.thesis.selfhealing.repair.SpecificRepairStrategies;

import commons.exception.ValidationException;
import commons.gui.widget.composite.ObjectSelectionMetainfo;
import commons.gui.widget.composite.SimpleObjectSelectionComposite;
import commons.utils.Clonator;
import commons.validation.ValidationError;

public class RepairStrategiesSelectionComposite extends SimpleObjectSelectionComposite<RepairStrategies> {

	private RepairStrategySearchCriteria criteria;

	// saves the latest used path in order to improve the usability
	private String stitchDir;

	public RepairStrategiesSelectionComposite(ObjectSelectionMetainfo info) {
		super(info);
		this.criteria = new RepairStrategySearchCriteria();
	}

	@Override
	protected RepairStrategies selectObject() {
		DirectoryDialog dirDialog = new DirectoryDialog(this.getShell(), SWT.OPEN);
		dirDialog.setText(ScenariosUIMessages.SELECT_STITCH_DIRECTORY.toString());
		dirDialog.setFilterPath(this.stitchDir);

		this.stitchDir = dirDialog.open();

		if (StringUtils.isBlank(this.stitchDir)) {
			throw new ValidationException(new ValidationError(ScenariosUIMessages.NO_REPAIR_STRATEGIES_SELECTED));
		}

		this.criteria.setStitchDirectory(stitchDir);

		RepairStrategySearchCriteria criteria = Clonator.clone(this.criteria);
		RepairStrategiesSelectionDialog repairStrategiesSelectionDialog = new RepairStrategiesSelectionDialog(criteria);
		repairStrategiesSelectionDialog.open();

		return new SpecificRepairStrategies(repairStrategiesSelectionDialog.getSelectedRepairStrategyNames());
	}

	@Override
	protected void viewObject(RepairStrategies object) {
		// we don't support viewing this kind of object
	}

	/**
	 * We use the clear button to specify that all available Repair Strategies should be considered by Rainbow.
	 * 
	 * @return the value to be set when clearing.
	 */
	@Override
	protected RepairStrategies getValueToSetWhenClearing() {
		return AllRepairStrategies.getInstance();
	}
}
