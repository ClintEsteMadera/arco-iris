package scenariosui.gui.action;

import org.eclipse.jface.dialogs.Dialog;

import scenariosui.gui.util.purpose.ScenariosUIPurpose;
import scenariosui.gui.widget.dialog.SelfHealingScenarioDialog;
import scenariosui.properties.ScenariosUILabels;

import ar.uba.dc.thesis.selfhealing.SelfHealingScenario;

import commons.gui.action.OpenDialogWithPropositoAction;

public class SelfHealingScenarioAction extends OpenDialogWithPropositoAction<SelfHealingScenario, ScenariosUIPurpose> {

	protected SelfHealingScenarioAction(String id, String shortcut, ScenariosUIPurpose purpose) {
		super(id, shortcut, purpose);
	}

	@Override
	protected Dialog getDialogFor(SelfHealingScenario model) {
		return new SelfHealingScenarioDialog(model, ScenariosUILabels.SCENARIO, this.getPurpose());
	}

}
