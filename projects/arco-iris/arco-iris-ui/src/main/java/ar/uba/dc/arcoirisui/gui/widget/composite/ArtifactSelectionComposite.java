package ar.uba.dc.arcoirisui.gui.widget.composite;

import ar.uba.dc.arcoirisui.gui.action.ArcoIrisUIActions;
import ar.uba.dc.arcoirisui.gui.widget.dialog.ArtifactSelectionDialog;
import ar.uba.dc.arcoirisui.properties.UniqueTableIdentifier;
import ar.uba.dc.arcoiris.atam.scenario.model.Artifact;

import commons.gui.widget.composite.ObjectSelectionMetainfo;
import commons.gui.widget.composite.SingleObjectSelectionComposite;
import commons.gui.widget.composite.helper.QueryCompositeObjectCreator;
import commons.query.BaseSearchCriteria;
import commons.query.SearchCriteria;
import commons.utils.Clonator;

public class ArtifactSelectionComposite extends SingleObjectSelectionComposite<Artifact> {

	private SearchCriteria<Artifact> criteria;

	private QueryCompositeObjectCreator<Artifact> objectCreator;

	public ArtifactSelectionComposite(ObjectSelectionMetainfo info) {
		this(info, new BaseSearchCriteria<Artifact>());
	}

	public ArtifactSelectionComposite(ObjectSelectionMetainfo info, SearchCriteria<Artifact> criteria) {
		super(info);
		this.criteria = criteria;
		this.objectCreator = new QueryCompositeObjectCreator<Artifact>(UniqueTableIdentifier.ARTIFACTS,
				ArcoIrisUIActions.NEW_ARTIFACT);
	}

	@Override
	protected Artifact selectObject() {
		SearchCriteria<Artifact> criteria = Clonator.clone(this.criteria);
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