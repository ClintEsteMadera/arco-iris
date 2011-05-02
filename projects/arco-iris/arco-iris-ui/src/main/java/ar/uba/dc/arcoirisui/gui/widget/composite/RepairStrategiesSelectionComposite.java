package ar.uba.dc.arcoirisui.gui.widget.composite;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.DirectoryDialog;

import ar.uba.dc.arcoirisui.gui.query.RepairStrategySearchCriteria;
import ar.uba.dc.arcoirisui.gui.widget.dialog.RepairStrategiesSelectionDialog;
import ar.uba.dc.arcoirisui.properties.ArcoIrisUIMessages;
import ar.uba.dc.arcoiris.common.validation.ValidationError;
import ar.uba.dc.arcoiris.common.validation.ValidationException;
import ar.uba.dc.arcoiris.selfhealing.repair.AllRepairStrategies;
import ar.uba.dc.arcoiris.selfhealing.repair.RepairStrategies;
import ar.uba.dc.arcoiris.selfhealing.repair.SpecificRepairStrategies;

import commons.gui.widget.composite.ObjectSelectionMetainfo;
import commons.gui.widget.composite.SingleObjectSelectionComposite;
import commons.utils.Clonator;

public class RepairStrategiesSelectionComposite extends SingleObjectSelectionComposite<RepairStrategies> {

	private static final String TOOLTIP_ALL_AVAILABLE = "All Available";

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
		dirDialog.setText(ArcoIrisUIMessages.SELECT_STITCH_DIRECTORY.toString());
		dirDialog.setFilterPath(this.stitchDir);

		this.stitchDir = dirDialog.open();

		if (StringUtils.isBlank(this.stitchDir)) {
			throw new ValidationException(new ValidationError(ArcoIrisUIMessages.INVALID_DIRECTORY.toString()));
		}

		this.criteria.setStitchDirectory(stitchDir);

		RepairStrategySearchCriteria criteria = Clonator.clone(this.criteria);
		RepairStrategiesSelectionDialog repairStrategiesSelectionDialog = new RepairStrategiesSelectionDialog(criteria);
		repairStrategiesSelectionDialog.open();

		RepairStrategies result = null;
		List<String> selectedRepairStrategyNames = repairStrategiesSelectionDialog.getSelectedRepairStrategyNames();
		if (CollectionUtils.isNotEmpty(selectedRepairStrategyNames)) {
			result = new SpecificRepairStrategies(selectedRepairStrategyNames);
		}
		return result;
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

	/**
	 * We want the tooltip text over the "Clear" button to display "All Available"
	 */
	@Override
	protected String getToolTip4ClearButton() {
		return TOOLTIP_ALL_AVAILABLE;
	}
}
