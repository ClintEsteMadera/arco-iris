package scenariosui.gui.widget.composite.query;

import java.util.List;

import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.swt.widgets.Composite;

import scenariosui.gui.action.ScenariosUIActions;
import scenariosui.gui.query.ArtifactSearchCriteria;
import scenariosui.gui.util.purpose.ScenariosUIPurpose;
import scenariosui.properties.UniqueTableIdentifier;
import scenariosui.service.ScenariosUIManager;
import ar.uba.dc.thesis.atam.scenario.model.Artifact;

import commons.gui.action.OpenDialogWithPurposeAction;
import commons.gui.util.PageHelper;
import commons.properties.EnumProperty;
import commons.query.BaseSearchCriteria;

public class ArtifactQueryComposite extends ScenariosUIQueryComposite<Artifact> {

	public ArtifactQueryComposite() {
		this(PageHelper.getMainWindow().mainTabFolder, UniqueTableIdentifier.ARTIFACTS, new ArtifactSearchCriteria());
	}

	public ArtifactQueryComposite(Composite parent, EnumProperty tableName, BaseSearchCriteria<Artifact> searchCriteria) {
		super(parent, tableName, Artifact.class, searchCriteria);
	}

	@Override
	protected List<Artifact> executeQuery() {
		return ScenariosUIManager.getInstance().getArtifacts(this.getCriteria());
	}

	@Override
	@SuppressWarnings("unchecked")
	protected OpenDialogWithPurposeAction<Artifact, ScenariosUIPurpose> getActionForNew() {
		return ScenariosUIActions.NEW_ARTIFACT;
	}

	@Override
	@SuppressWarnings("unchecked")
	protected OpenDialogWithPurposeAction<Artifact, ScenariosUIPurpose> getActionForEdit() {
		return ScenariosUIActions.EDIT_ARTIFACT;
	}

	@Override
	@SuppressWarnings("unchecked")
	protected OpenDialogWithPurposeAction<Artifact, ScenariosUIPurpose> getActionForDelete() {
		return ScenariosUIActions.DELETE_ARTIFACT;
	}

	@Override
	protected ISelectionChangedListener getTableSelectionChangedListener() {
		return null;
	}
}