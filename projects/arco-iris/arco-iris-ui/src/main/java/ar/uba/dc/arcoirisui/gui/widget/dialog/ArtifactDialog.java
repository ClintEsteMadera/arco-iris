package ar.uba.dc.arcoirisui.gui.widget.dialog;

import org.eclipse.swt.widgets.Composite;

import ar.uba.dc.arcoirisui.gui.widget.composite.ArtifactComposite;
import ar.uba.dc.arcoirisui.properties.ArcoIrisUIMessages;
import ar.uba.dc.arcoirisui.service.ArcoIrisUIManager;
import ar.uba.dc.arcoiris.atam.scenario.model.Artifact;

import commons.gui.util.purpose.Purpose;
import commons.properties.EnumProperty;
import commons.properties.FakeEnumProperty;

public class ArtifactDialog extends BaseArcoIrisUIMultiPurposeDialog<Artifact> {

	public ArtifactDialog(Purpose purpose) {
		super(null, new FakeEnumProperty(""), purpose);
	}

	public ArtifactDialog(Artifact model, EnumProperty title, Purpose purpose) {
		super(model, title, purpose);
	}

	@Override
	protected Artifact newModel() {
		return new Artifact();
	}

	@Override
	protected void addWidgetsToDialogArea(Composite parent) {
		new ArtifactComposite(parent, this.purpose, this.getCompositeModel());
	}

	@Override
	public void addModelToCurrentSHConfiguration() {
		ArcoIrisUIManager.getInstance().getCurrentSelfHealingConfiguration().addArtifact(this.getModel());
	}

	@Override
	public void removeModelFromCurrentSHConfiguration() {
		ArcoIrisUIManager.getInstance().getCurrentSelfHealingConfiguration().removeArtifact(this.getModel());
	}

	@Override
	public String getSuccessfulOperationMessage(String operation) {
		String artifactName = super.getModel().getName() != null ? super.getModel().getName() : "";
		return ArcoIrisUIMessages.SUCCESSFUL_ARTIFACT.toString(artifactName, operation);
	}
}