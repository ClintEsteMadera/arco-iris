package ar.uba.dc.arcoirisui.gui.action;

import ar.uba.dc.arcoiris.atam.scenario.model.Artifact;
import ar.uba.dc.arcoirisui.gui.widget.dialog.ArtifactDialog;
import ar.uba.dc.arcoirisui.properties.ArcoIrisUILabels;

import commons.gui.action.OpenDialogWithPurposeAction;
import commons.gui.util.purpose.Purpose;
import commons.gui.widget.dialog.BaseCompositeModelBoundedDialog;

public class ArtifactOpenDialogWithPurposeAction extends OpenDialogWithPurposeAction<Artifact, Purpose> {

	protected ArtifactOpenDialogWithPurposeAction(String id, String shortcut, Purpose purpose) {
		super(id, shortcut, purpose);
	}

	@Override
	protected BaseCompositeModelBoundedDialog<Artifact> getDialogFor(Artifact model) {
		// in this case, we want to ensure we use a new instance
		if (this.getPurpose().isCreation()) {
			model = null;
		}
		return new ArtifactDialog(model, ArcoIrisUILabels.ARTIFACT, this.getPurpose());
	}

}
