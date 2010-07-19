package scenariosui.gui.action;

import org.eclipse.jface.dialogs.Dialog;

import scenariosui.gui.util.purpose.ScenariosUIPurpose;
import scenariosui.gui.widget.dialog.EnvironmentDialog;
import scenariosui.properties.ScenariosUILabels;
import ar.uba.dc.thesis.atam.scenario.model.Environment;

import commons.gui.action.OpenDialogWithPurposeAction;

public class EnvironmentOpenDialogWithPurposeAction extends
		OpenDialogWithPurposeAction<Environment, ScenariosUIPurpose> {

	protected EnvironmentOpenDialogWithPurposeAction(String id, String shortcut, ScenariosUIPurpose purpose) {
		super(id, shortcut, purpose);
	}

	@Override
	protected Dialog getDialogFor(Environment model) {
		// in this case, we want to ensure we use a new instance
		if (this.getPurpose().isCreation()) {
			model = null;
		}
		return new EnvironmentDialog(model, ScenariosUILabels.ENVIRONMENT, this.getPurpose());
	}

}
