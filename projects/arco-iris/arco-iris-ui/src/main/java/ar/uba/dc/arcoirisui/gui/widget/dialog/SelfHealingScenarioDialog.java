package ar.uba.dc.arcoirisui.gui.widget.dialog;

import org.eclipse.swt.widgets.Composite;

import ar.uba.dc.arcoirisui.gui.widget.composite.SelfHealingScenarioComposite;
import ar.uba.dc.arcoirisui.properties.ArcoIrisUIMessages;
import ar.uba.dc.arcoirisui.service.ArcoIrisUIManager;
import ar.uba.dc.arcoiris.selfhealing.SelfHealingScenario;

import commons.gui.util.purpose.Purpose;
import commons.properties.EnumProperty;

public class SelfHealingScenarioDialog extends BaseArcoIrisUIMultiPurposeDialog<SelfHealingScenario> {

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
		ArcoIrisUIManager.getInstance().getCurrentSelfHealingConfiguration().addScenario(this.getModel());
	}

	@Override
	public void removeModelFromCurrentSHConfiguration() {
		ArcoIrisUIManager.getInstance().getCurrentSelfHealingConfiguration().removeScenario(this.getModel());
	}

	@Override
	public String getSuccessfulOperationMessage(String operation) {
		String scenarioName = super.getModel().getName() != null ? super.getModel().getName() : "";
		return ArcoIrisUIMessages.SUCCESSFUL_SCENARIO.toString(scenarioName, operation);
	}

	@Override
	protected boolean performOK() {
		this.selfHealingScenarioComposite.okPressed();
		return super.performOK();
	}
}
