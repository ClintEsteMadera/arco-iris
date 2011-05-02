package ar.uba.dc.arcoirisui.gui.widget.dialog;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;

import ar.uba.dc.arcoirisui.gui.widget.composite.query.ArtifactSelectionQueryComposite;
import ar.uba.dc.arcoirisui.properties.ArcoIrisUILabels;
import ar.uba.dc.arcoiris.atam.scenario.model.Artifact;

import commons.gui.Advised;
import commons.gui.util.PageHelper;
import commons.query.BaseSearchCriteria;
import commons.query.SearchCriteria;

public class ArtifactSelectionDialog extends Dialog implements Advised<ArtifactSelectionQueryComposite> {

	private Artifact artifact;

	private ArtifactSelectionQueryComposite artifactSelectionQueryComposite;

	private SearchCriteria<Artifact> criteria;

	public ArtifactSelectionDialog() {
		this(new BaseSearchCriteria<Artifact>());
	}

	public ArtifactSelectionDialog(SearchCriteria<Artifact> criteria) {
		super(PageHelper.getMainShell());
		this.criteria = criteria;
	}

	@Override
	protected void configureShell(Shell shell) {
		super.configureShell(shell);
		shell.setText(ArcoIrisUILabels.SELECT_ARTIFACT.toString());
	}

	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		super.createButtonsForButtonBar(parent);
		getButton(IDialogConstants.OK_ID).setEnabled(false);
	}

	@Override
	protected Control createDialogArea(Composite parent) {
		artifactSelectionQueryComposite = new ArtifactSelectionQueryComposite(this, parent, this.criteria) {
			@Override
			protected ISelectionChangedListener getTableSelectionChangedListener() {
				ISelectionChangedListener listener = new ISelectionChangedListener() {
					public void selectionChanged(SelectionChangedEvent event) {
						Button acceptButton = getButton(IDialogConstants.OK_ID);
						if (acceptButton != null) {
							acceptButton.setEnabled(!getTable().getSelectedElements().isEmpty());
						}
					}
				};
				return listener;
			}
		};
		return parent;
	}

	@Override
	protected void okPressed() {
		rowDoubleClicked(artifactSelectionQueryComposite);
	}

	public Artifact getSelectedArtifact() {
		return artifact;

	}

	public void setSelectedArtifact(Artifact artifact) {
		this.artifact = artifact;
	}

	/**
	 * @see jface.gui.gui.Advised#rowSelected(jface.gui.gui.widget.composite.QueryComposite))
	 */
	public void rowSelected(ArtifactSelectionQueryComposite queryComposite) {
		this.setSelectedArtifact(queryComposite.getModel());
		this.close();
	}

	public void rowDoubleClicked(ArtifactSelectionQueryComposite queryComposite) {
		this.setSelectedArtifact(queryComposite.getModel());
		this.close();
	}
}
