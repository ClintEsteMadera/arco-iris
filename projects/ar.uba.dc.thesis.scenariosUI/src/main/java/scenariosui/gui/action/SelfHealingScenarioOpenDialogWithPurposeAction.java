package scenariosui.gui.action;

import scenariosui.gui.widget.dialog.SelfHealingScenarioDialog;
import scenariosui.properties.ScenariosUILabels;
import ar.uba.dc.thesis.selfhealing.SelfHealingScenario;

import commons.gui.action.OpenDialogWithPurposeAction;
import commons.gui.util.purpose.Purpose;
import commons.gui.widget.dialog.BaseCompositeModelBoundedDialog;

public class SelfHealingScenarioOpenDialogWithPurposeAction extends
		OpenDialogWithPurposeAction<SelfHealingScenario, Purpose> {

	protected SelfHealingScenarioOpenDialogWithPurposeAction(String id, String shortcut, Purpose purpose) {
		super(id, shortcut, purpose);
	}

	@Override
	protected BaseCompositeModelBoundedDialog<SelfHealingScenario> getDialogFor(SelfHealingScenario model) {
		// in this case, we want to ensure we use a new instance
		if (this.getPurpose().isCreation()) {
			model = null;
		}
		return new SelfHealingScenarioDialog(model, ScenariosUILabels.SELF_HEALING_SCENARIO, this.getPurpose());
	}

}
