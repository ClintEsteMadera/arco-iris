package scenariosui.gui.action;

import org.eclipse.jface.dialogs.Dialog;

import scenariosui.gui.util.purpose.ScenariosUIPurpose;
import scenariosui.gui.widget.dialog.SelfHealingScenarioDialog;
import scenariosui.properties.ScenariosUILabels;
import ar.uba.dc.thesis.selfhealing.SelfHealingScenario;

import commons.gui.action.OpenDialogWithPurposeAction;

public class SelfHealingScenarioOpenDialogWithPurposeAction extends
		OpenDialogWithPurposeAction<SelfHealingScenario, ScenariosUIPurpose> {

	protected SelfHealingScenarioOpenDialogWithPurposeAction(String id, String shortcut, ScenariosUIPurpose purpose) {
		super(id, shortcut, purpose);
	}

	@Override
	protected Dialog getDialogFor(SelfHealingScenario model) {
		// in this case, we want to ensure we use a new instance
		if (this.getPurpose().isCreation()) {
			model = null;
		}
		return new SelfHealingScenarioDialog(model, ScenariosUILabels.SELF_HEALING_SCENARIO, this.getPurpose());
	}

}
