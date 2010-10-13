package scenariosui.gui.widget.composite;

import scenariosui.gui.action.ScenariosUIActions;
import scenariosui.gui.query.ArtifactSearchCriteria;
import scenariosui.gui.widget.dialog.ArtifactSelectionDialog;
import scenariosui.properties.UniqueTableIdentifier;
import ar.uba.dc.thesis.atam.scenario.model.Artifact;

import commons.gui.widget.composite.ObjectSelectionMetainfo;
import commons.gui.widget.composite.SingleObjectSelectionComposite;
import commons.gui.widget.composite.helper.QueryCompositeObjectCreator;
import commons.utils.Clonator;

public class ArtifactSelectionComposite extends SingleObjectSelectionComposite<Artifact> {

	private ArtifactSearchCriteria criteria;

	private QueryCompositeObjectCreator<Artifact> objectCreator;

	public ArtifactSelectionComposite(ObjectSelectionMetainfo info) {
		this(info, new ArtifactSearchCriteria());
	}

	public ArtifactSelectionComposite(ObjectSelectionMetainfo info, ArtifactSearchCriteria criteria) {
		super(info);
		this.criteria = criteria;
		this.objectCreator = new QueryCompositeObjectCreator<Artifact>(UniqueTableIdentifier.ARTIFACTS,
				ScenariosUIActions.NEW_ARTIFACT);
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

	@Override
	protected Artifact createNew(Object createOption) {
		return this.objectCreator.createNew(createOption);
	}
}