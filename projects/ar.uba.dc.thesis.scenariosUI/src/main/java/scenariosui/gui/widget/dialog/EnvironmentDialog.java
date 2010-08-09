package scenariosui.gui.widget.dialog;

import org.eclipse.swt.widgets.Composite;

import scenariosui.gui.widget.composite.EnvironmentComposite;
import scenariosui.properties.ScenariosUIMessages;
import scenariosui.service.ScenariosUIManager;
import ar.uba.dc.thesis.atam.scenario.model.Environment;

import commons.gui.util.purpose.Purpose;
import commons.properties.EnumProperty;
import commons.properties.FakeEnumProperty;

public class EnvironmentDialog extends BaseScenariosUIMultiPurposeDialog<Environment> {

	public EnvironmentDialog(Purpose purpose) {
		super(null, new FakeEnumProperty(""), purpose);
	}

	public EnvironmentDialog(Environment model, EnumProperty title, Purpose purpose) {
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
		ScenariosUIManager.getInstance().getCurrentSelfHealingConfiguration().addEnvironment(this.getModel());
	}

	@Override
	public void removeModelFromCurrentSHConfiguration() {
		ScenariosUIManager.getInstance().getCurrentSelfHealingConfiguration().removeEnvironment(this.getModel());
	}

	@Override
	public String getSuccessfulOperationMessage(String operation) {
		String environmentName = super.getModel().getName() != null ? super.getModel().getName() : "";
		return ScenariosUIMessages.SUCCESSFUL_ENVIRONMENT.toString(environmentName, operation);
	}
}