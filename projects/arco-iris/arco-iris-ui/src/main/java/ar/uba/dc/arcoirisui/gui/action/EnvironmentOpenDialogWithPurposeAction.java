package ar.uba.dc.arcoirisui.gui.action;

import ar.uba.dc.arcoiris.atam.scenario.model.Environment;
import ar.uba.dc.arcoirisui.gui.widget.dialog.EnvironmentDialog;
import ar.uba.dc.arcoirisui.properties.ArcoIrisUILabels;

import commons.gui.action.OpenDialogWithPurposeAction;
import commons.gui.util.purpose.Purpose;
import commons.gui.widget.dialog.BaseCompositeModelBoundedDialog;

public class EnvironmentOpenDialogWithPurposeAction extends OpenDialogWithPurposeAction<Environment, Purpose> {

	protected EnvironmentOpenDialogWithPurposeAction(String id, String shortcut, Purpose purpose) {
		super(id, shortcut, purpose);
	}

	@Override
	protected BaseCompositeModelBoundedDialog<Environment> getDialogFor(Environment model) {
		// in this case, we want to ensure we use a new instance
		if (this.getPurpose().isCreation()) {
			model = null;
		}
		return new EnvironmentDialog(model, ArcoIrisUILabels.ENVIRONMENT, this.getPurpose());
	}

}
