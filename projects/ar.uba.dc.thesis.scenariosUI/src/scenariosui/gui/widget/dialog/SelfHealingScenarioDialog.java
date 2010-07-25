package scenariosui.gui.widget.dialog;

import org.eclipse.swt.widgets.Composite;

import scenariosui.gui.util.purpose.ScenariosUIPurpose;
import scenariosui.gui.widget.composite.SelfHealingScenarioComposite;
import scenariosui.properties.UniqueTableIdentifier;
import scenariosui.service.ScenariosUIController;
import ar.uba.dc.thesis.selfhealing.SelfHealingScenario;

import commons.properties.EnumProperty;
import commons.properties.Messages;

public class SelfHealingScenarioDialog extends BaseScenariosUIMultiPurposeDialog<SelfHealingScenario> {

	public SelfHealingScenarioDialog(SelfHealingScenario model, EnumProperty title, ScenariosUIPurpose purpose) {
		super(model, title, purpose);
	}

	@Override
	protected SelfHealingScenario newModel() {
		return new SelfHealingScenario();
	}

	@Override
	protected void addWidgetsToDialogArea(Composite parent) {
		new SelfHealingScenarioComposite(parent, this.purpose, this.getCompositeModel());
	}

	@Override
	public void addModelToCurrentSHConfiguration() {
		ScenariosUIController.getInstance().getCurrentSelfHealingConfiguration().addScenario(this.getModel());
	}

	@Override
	public String getSuccessfulOperationMessage(String operation) {
		String scenarioName = super.getModel().getName() != null ? super.getModel().getName() : "";
		return Messages.SUCCESSFUL_SCENARIO.toString(scenarioName, operation);
	}

	@Override
	public UniqueTableIdentifier getUniqueTableIdentifier() {
		return UniqueTableIdentifier.SCENARIOS;
	}
}