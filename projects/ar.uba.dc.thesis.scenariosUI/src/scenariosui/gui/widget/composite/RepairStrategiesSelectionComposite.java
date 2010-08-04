package scenariosui.gui.widget.composite;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.DirectoryDialog;

import scenariosui.gui.query.RepairStrategySearchCriteria;
import scenariosui.gui.widget.dialog.RepairStrategiesSelectionDialog;
import scenariosui.properties.ScenariosUILabels;

import commons.exception.ValidationException;
import commons.gui.widget.composite.ObjectSelectionMetainfo;
import commons.gui.widget.composite.SimpleObjectSelectionComposite;
import commons.utils.Clonator;
import commons.validation.ValidationError;

public class RepairStrategiesSelectionComposite extends SimpleObjectSelectionComposite<List<String>> {

	private RepairStrategySearchCriteria criteria;

	// saves the latest used path in order to improve the usability
	private String stitchDir;

	public RepairStrategiesSelectionComposite(ObjectSelectionMetainfo info) {
		super(info);
		this.criteria = new RepairStrategySearchCriteria();
	}

	@Override
	protected List<String> selectObject() {
		DirectoryDialog dirDialog = new DirectoryDialog(this.getShell(), SWT.OPEN);
		dirDialog.setText(ScenariosUILabels.SELECT_STITCH_DIRECTORY.toString());
		dirDialog.setFilterPath(this.stitchDir);

		this.stitchDir = dirDialog.open();

		if (StringUtils.isBlank(this.stitchDir)) {
			throw new ValidationException(new ValidationError(ScenariosUILabels.NO_REPAIR_STRATEGIES_SELECTED));
		}

		this.criteria.setStitchDirectory(stitchDir);

		RepairStrategySearchCriteria criteria = Clonator.clone(this.criteria);
		RepairStrategiesSelectionDialog repairStrategiesSelectionDialog = new RepairStrategiesSelectionDialog(criteria);
		repairStrategiesSelectionDialog.open();

		return repairStrategiesSelectionDialog.getSelectedRepairStrategyNames();
	}

	@Override
	protected void viewObject(List<String> object) {
		// we don't support viewing this kind of object
	}
}
