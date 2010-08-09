package scenariosui.gui.widget.composite;

import scenariosui.gui.query.ArtifactSearchCriteria;
import scenariosui.gui.widget.dialog.ArtifactSelectionDialog;
import ar.uba.dc.thesis.atam.scenario.model.Artifact;

import commons.gui.widget.composite.ObjectSelectionMetainfo;
import commons.gui.widget.composite.SimpleObjectSelectionComposite;
import commons.utils.Clonator;

public class ArtifactSelectionComposite extends SimpleObjectSelectionComposite<Artifact> {

	private ArtifactSearchCriteria criteria;

	public ArtifactSelectionComposite(ObjectSelectionMetainfo info) {
		this(info, new ArtifactSearchCriteria());
	}

	public ArtifactSelectionComposite(ObjectSelectionMetainfo info, ArtifactSearchCriteria criteria) {
		super(info);
		this.criteria = criteria;
	}

	@Override
	protected Artifact selectObject() {
		ArtifactSearchCriteria criteria = Clonator.clone(this.criteria);
		ArtifactSelectionDialog artifactSelectionDialog = new ArtifactSelectionDialog(criteria);
		artifactSelectionDialog.open();

		return artifactSelectionDialog.getSelectedArtifact();
	}

	@Override
	protected void viewObject(Artifact environments) {
		selectObject();
	}
}
