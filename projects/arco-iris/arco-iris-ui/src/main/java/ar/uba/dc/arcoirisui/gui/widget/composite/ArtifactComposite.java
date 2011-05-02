package ar.uba.dc.arcoirisui.gui.widget.composite;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;

import ar.uba.dc.arcoirisui.properties.ArcoIrisUILabels;
import ar.uba.dc.arcoirisui.service.ArcoIrisUIManager;
import ar.uba.dc.arcoiris.atam.scenario.model.Artifact;

import commons.gui.model.CompositeModel;
import commons.gui.util.purpose.Purpose;
import commons.gui.widget.composite.SimpleComposite;
import commons.gui.widget.creation.binding.BindingInfo;
import commons.gui.widget.creation.metainfo.TextFieldMetainfo;
import commons.gui.widget.factory.TextFactory;
import commons.gui.widget.group.SimpleGroup;
import commons.properties.CommonLabels;

public class ArtifactComposite extends SimpleComposite {

	public ArtifactComposite(Composite parent, Purpose purpose, CompositeModel<Artifact> underlyingArtifact) {
		super(parent, purpose.isReadOnly());

		if (purpose.isCreation()) {
			underlyingArtifact.getValue().setId(ArcoIrisUIManager.getInstance().getNextId(Artifact.class));
		}

		Group swtGroup = new SimpleGroup(parent, CommonLabels.BASIC_INFORMATION, this.readOnly).getSwtGroup();

		TextFieldMetainfo textMetainfo = TextFieldMetainfo.create(swtGroup, ArcoIrisUILabels.ID, new BindingInfo(
				underlyingArtifact, "id"), true);
		TextFactory.createText(textMetainfo);

		textMetainfo = TextFieldMetainfo.create(swtGroup, ArcoIrisUILabels.NAME, new BindingInfo(underlyingArtifact,
				"name"), this.readOnly);
		textMetainfo.visibleSize = 30;
		TextFactory.createText(textMetainfo);

		textMetainfo = TextFieldMetainfo.create(swtGroup, ArcoIrisUILabels.SYSTEM_NAME, new BindingInfo(
				underlyingArtifact, "systemName"), this.readOnly);
		textMetainfo.visibleSize = 30;
		TextFactory.createText(textMetainfo);
	}

}
