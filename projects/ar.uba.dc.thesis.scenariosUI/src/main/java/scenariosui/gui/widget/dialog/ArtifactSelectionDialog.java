package scenariosui.gui.widget.dialog;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;

import scenariosui.gui.query.ArtifactSearchCriteria;
import scenariosui.gui.widget.composite.query.ArtifactSelectionQueryComposite;
import scenariosui.properties.ScenariosUILabels;
import ar.uba.dc.thesis.atam.scenario.model.Artifact;

import commons.gui.Advised;
import commons.gui.util.PageHelper;

public class ArtifactSelectionDialog extends Dialog implements Advised<ArtifactSelectionQueryComposite> {

	private Artifact artifact;

	private ArtifactSelectionQueryComposite artifactSelectionQueryComposite;

	private ArtifactSearchCriteria criteria;

	public ArtifactSelectionDialog() {
		this(new ArtifactSearchCriteria());
	}

	public ArtifactSelectionDialog(ArtifactSearchCriteria criteria) {
		super(PageHelper.getMainShell());
		this.criteria = criteria;
	}

	@Override
	protected void configureShell(Shell shell) {
		super.configureShell(shell);
		shell.setText(ScenariosUILabels.SELECT_ARTIFACT.toString());
		Rectangle centerLocation = PageHelper.getCenterLocation(800, 500);
		shell.setBounds(centerLocation);
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

	@Override
	protected void cancelPressed() {
		this.close();
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