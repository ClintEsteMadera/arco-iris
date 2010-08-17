package scenariosui.gui.widget.dialog;

import org.eclipse.swt.widgets.Composite;

import scenariosui.gui.widget.composite.SelfHealingScenarioComposite;
import scenariosui.properties.ScenariosUIMessages;
import scenariosui.service.ScenariosUIManager;
import ar.uba.dc.thesis.selfhealing.SelfHealingScenario;

import commons.gui.util.purpose.Purpose;
import commons.properties.EnumProperty;

public class SelfHealingScenarioDialog extends BaseScenariosUIMultiPurposeDialog<SelfHealingScenario> {

	private SelfHealingScenarioComposite selfHealingScenarioComposite;

	public SelfHealingScenarioDialog(SelfHealingScenario model, EnumProperty title, Purpose purpose) {
		super(model, title, purpose);
	}

	@Override
	protected SelfHealingScenario newModel() {
		return new SelfHealingScenario();
	}

	@Override
	protected void addWidgetsToDialogArea(Composite parent) {
		this.selfHealingScenarioComposite = new SelfHealingScenarioComposite(parent, this.purpose, this
				.getCompositeModel());
	}

	@Override
	public void addModelToCurrentSHConfiguration() {
		ScenariosUIManager.getInstance().getCurrentSelfHealingConfiguration().addScenario(this.getModel());
	}

	@Override
	public void removeModelFromCurrentSHConfiguration() {
		ScenariosUIManager.getInstance().getCurrentSelfHealingConfiguration().removeScenario(this.getModel());
	}

	@Override
	public String getSuccessfulOperationMessage(String operation) {
		String scenarioName = super.getModel().getName() != null ? super.getModel().getName() : "";
		return ScenariosUIMessages.SUCCESSFUL_SCENARIO.toString(scenarioName, operation);
	}

	@Override
	protected boolean performOK() {
		this.selfHealingScenarioComposite.okPressed();
		return super.performOK();
	}
}