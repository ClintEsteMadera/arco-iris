package scenariosui.gui.widget.dialog;

import org.eclipse.swt.widgets.Composite;

import scenariosui.gui.widget.composite.ArtifactComposite;
import scenariosui.properties.ScenariosUIMessages;
import scenariosui.service.ScenariosUIManager;
import ar.uba.dc.thesis.atam.scenario.model.Artifact;

import commons.gui.util.purpose.Purpose;
import commons.properties.EnumProperty;
import commons.properties.FakeEnumProperty;

public class ArtifactDialog extends BaseScenariosUIMultiPurposeDialog<Artifact> {

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
		ScenariosUIManager.getInstance().getCurrentSelfHealingConfiguration().addArtifact(this.getModel());
	}

	@Override
	public void removeModelFromCurrentSHConfiguration() {
		ScenariosUIManager.getInstance().getCurrentSelfHealingConfiguration().removeArtifact(this.getModel());
	}

	@Override
	public String getSuccessfulOperationMessage(String operation) {
		String artifactName = super.getModel().getName() != null ? super.getModel().getName() : "";
		return ScenariosUIMessages.SUCCESSFUL_ARTIFACT.toString(artifactName, operation);
	}
}