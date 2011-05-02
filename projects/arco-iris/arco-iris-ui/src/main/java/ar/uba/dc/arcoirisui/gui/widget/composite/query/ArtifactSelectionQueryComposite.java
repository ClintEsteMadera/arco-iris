package ar.uba.dc.arcoirisui.gui.widget.composite.query;

import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.swt.widgets.Composite;

import ar.uba.dc.arcoirisui.properties.UniqueTableIdentifier;
import ar.uba.dc.arcoiris.atam.scenario.model.Artifact;

import commons.gui.Advised;
import commons.query.BaseSearchCriteria;
import commons.query.SearchCriteria;

public class ArtifactSelectionQueryComposite extends ArtifactQueryComposite {

	private final Advised<ArtifactSelectionQueryComposite> advised;

	public ArtifactSelectionQueryComposite(Advised<ArtifactSelectionQueryComposite> advised, Composite parent) {
		this(advised, parent, new BaseSearchCriteria<Artifact>());
	}

	public ArtifactSelectionQueryComposite(Advised<ArtifactSelectionQueryComposite> advised, Composite parent,
			SearchCriteria<Artifact> criteria) {
		super(parent, UniqueTableIdentifier.ARTIFACT_SELECTION, criteria);
		this.advised = advised;
	}

	@Override
	protected ISelectionChangedListener getTableSelectionChangedListener() {
		return new ISelectionChangedListener() {
			public void selectionChanged(SelectionChangedEvent event) {
				if (advised != null) {
					advised.rowSelected(ArtifactSelectionQueryComposite.this);
				}
			}
		};
	}

	@Override
	protected IDoubleClickListener getTableDoubleClickListener() {
		return new IDoubleClickListener() {
			public void doubleClick(DoubleClickEvent event) {
				advised.rowDoubleClicked(ArtifactSelectionQueryComposite.this);
			}
		};
	}
}