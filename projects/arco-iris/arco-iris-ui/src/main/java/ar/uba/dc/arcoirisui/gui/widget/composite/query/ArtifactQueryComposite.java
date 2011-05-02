package ar.uba.dc.arcoirisui.gui.widget.composite.query;

import java.util.List;

import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.swt.widgets.Composite;

import ar.uba.dc.arcoirisui.gui.action.ArcoIrisUIActions;
import ar.uba.dc.arcoirisui.properties.UniqueTableIdentifier;
import ar.uba.dc.arcoirisui.service.ArcoIrisUIManager;
import ar.uba.dc.arcoiris.atam.scenario.model.Artifact;

import commons.gui.action.OpenDialogWithPurposeAction;
import commons.gui.util.PageHelper;
import commons.gui.util.purpose.Purpose;
import commons.properties.EnumProperty;
import commons.query.BaseSearchCriteria;
import commons.query.SearchCriteria;

public class ArtifactQueryComposite extends ArcoIrisUIQueryComposite<Artifact> {

	public ArtifactQueryComposite() {
		this(PageHelper.getMainWindow().mainTabFolder, UniqueTableIdentifier.ARTIFACTS,
				new BaseSearchCriteria<Artifact>());
	}

	public ArtifactQueryComposite(Composite parent, EnumProperty tableName, SearchCriteria<Artifact> searchCriteria) {
		super(parent, tableName, Artifact.class, searchCriteria);
	}

	@Override
	protected List<Artifact> executeQuery() {
		return ArcoIrisUIManager.getInstance().getArtifacts(this.getCriteria());
	}

	@Override
	protected OpenDialogWithPurposeAction<Artifact, Purpose> getActionForCreate() {
		return ArcoIrisUIActions.NEW_ARTIFACT;
	}

	@Override
	protected OpenDialogWithPurposeAction<Artifact, Purpose> getActionForEdit() {
		return ArcoIrisUIActions.EDIT_ARTIFACT;
	}

	@Override
	protected OpenDialogWithPurposeAction<Artifact, Purpose> getActionForDelete() {
		return ArcoIrisUIActions.DELETE_ARTIFACT;
	}

	@Override
	protected ISelectionChangedListener getTableSelectionChangedListener() {
		return null;
	}
}