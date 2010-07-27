package scenariosui.gui.widget.dialog;

import org.eclipse.swt.widgets.Composite;

import scenariosui.gui.util.purpose.ScenariosUIPurpose;
import scenariosui.gui.widget.composite.EnvironmentComposite;
import scenariosui.service.SelfHealingConfigurationManager;
import ar.uba.dc.thesis.atam.scenario.model.Environment;

import commons.properties.EnumProperty;
import commons.properties.FakeEnumProperty;
import commons.properties.Messages;

public class EnvironmentDialog extends BaseScenariosUIMultiPurposeDialog<Environment> {

	public EnvironmentDialog(ScenariosUIPurpose purpose) {
		super(null, new FakeEnumProperty(""), purpose);
	}

	public EnvironmentDialog(Environment model, EnumProperty title, ScenariosUIPurpose purpose) {
		super(model, title, purpose);
	}

	@Override
	protected Environment newModel() {
		return new Environment();
	}

	@Override
	protected void addWidgetsToDialogArea(Composite parent) {
		new EnvironmentComposite(parent, this.purpose, this.getCompositeModel());
	}

	@Override
	public void addModelToCurrentSHConfiguration() {
		SelfHealingConfigurationManager.getInstance().getCurrentSelfHealingConfiguration().addEnvironment(
				this.getModel());
	}

	@Override
	public void removeModelFromCurrentSHConfiguration() {
		SelfHealingConfigurationManager.getInstance().getCurrentSelfHealingConfiguration().removeEnvironment(
				this.getModel());
	}

	@Override
	public String getSuccessfulOperationMessage(String operation) {
		String environmentName = super.getModel().getName() != null ? super.getModel().getName() : "";
		return Messages.SUCCESSFUL_ENVIRONMENT.toString(environmentName, operation);
	}
}