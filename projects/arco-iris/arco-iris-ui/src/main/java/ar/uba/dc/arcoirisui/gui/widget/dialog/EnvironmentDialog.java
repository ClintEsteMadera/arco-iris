package ar.uba.dc.arcoirisui.gui.widget.dialog;

import org.eclipse.swt.widgets.Composite;

import ar.uba.dc.arcoiris.atam.scenario.model.Environment;
import ar.uba.dc.arcoirisui.gui.widget.composite.EnvironmentComposite;
import ar.uba.dc.arcoirisui.properties.ArcoIrisUIMessages;
import ar.uba.dc.arcoirisui.service.ArcoIrisUIManager;

import commons.gui.util.purpose.Purpose;
import commons.properties.EnumProperty;
import commons.properties.FakeEnumProperty;

public class EnvironmentDialog extends BaseArcoIrisUIMultiPurposeDialog<Environment> {

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
		ArcoIrisUIManager.getInstance().getCurrentSelfHealingConfiguration().addEnvironment(this.getModel());
	}

	@Override
	public void removeModelFromCurrentSHConfiguration() {
		ArcoIrisUIManager.getInstance().getCurrentSelfHealingConfiguration().removeEnvironment(this.getModel());
	}

	@Override
	public String getSuccessfulOperationMessage(String operation) {
		String environmentName = super.getModel().getName() != null ? super.getModel().getName() : "";
		return ArcoIrisUIMessages.SUCCESSFUL_ENVIRONMENT.toString(environmentName, operation);
	}
}