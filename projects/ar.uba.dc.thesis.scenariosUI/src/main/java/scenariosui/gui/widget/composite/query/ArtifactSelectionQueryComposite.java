package scenariosui.gui.widget.composite.query;

import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.swt.widgets.Composite;

import scenariosui.gui.query.ArtifactSearchCriteria;
import scenariosui.properties.UniqueTableIdentifier;

import commons.gui.Advised;

public class ArtifactSelectionQueryComposite extends ArtifactQueryComposite {

	private final Advised<ArtifactSelectionQueryComposite> advised;

	public ArtifactSelectionQueryComposite(Advised<ArtifactSelectionQueryComposite> advised, Composite parent) {
		this(advised, parent, new ArtifactSearchCriteria());
	}

	public ArtifactSelectionQueryComposite(Advised<ArtifactSelectionQueryComposite> advised, Composite parent,
			ArtifactSearchCriteria criteria) {
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