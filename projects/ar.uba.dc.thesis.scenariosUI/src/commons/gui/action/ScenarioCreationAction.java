package commons.gui.action;

import org.eclipse.jface.dialogs.Dialog;

import ar.uba.dc.thesis.selfhealing.SelfHealingScenario;

import commons.gui.util.proposito.ScenariosUIProposito;
import commons.gui.widget.dialog.ScenarioDialog;
import commons.properties.ScenariosUILabels;

public class ScenarioCreationAction extends OpenDialogWithPropositoAction<SelfHealingScenario, ScenariosUIProposito> {

	protected ScenarioCreationAction(String identificadorUnico, String shortcut, ScenariosUIProposito proposito) {
		super(identificadorUnico, shortcut, proposito);
	}

	@Override
	protected Dialog getDialogFor(SelfHealingScenario model) {
		return new ScenarioDialog(model, ScenariosUILabels.CREATE_SCENARIO, this.getProposito());
	}

}
