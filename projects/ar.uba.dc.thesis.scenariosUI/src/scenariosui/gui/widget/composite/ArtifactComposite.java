package scenariosui.gui.widget.composite;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;

import scenariosui.properties.ScenariosUILabels;
import scenariosui.service.SelfHealingConfigurationManager;
import ar.uba.dc.thesis.atam.scenario.model.Artifact;

import commons.gui.model.CompositeModel;
import commons.gui.util.purpose.Purpose;
import commons.gui.widget.composite.SimpleComposite;
import commons.gui.widget.creation.binding.BindingInfo;
import commons.gui.widget.creation.metainfo.TextFieldMetainfo;
import commons.gui.widget.factory.TextFactory;
import commons.gui.widget.group.SimpleGroup;

public class ArtifactComposite extends SimpleComposite {

	public ArtifactComposite(Composite parent, Purpose purpose, CompositeModel<Artifact> underlyingArtifact) {
		super(parent, purpose.isReadOnly());

		if (purpose.isCreation()) {
			underlyingArtifact.getValue().setId(SelfHealingConfigurationManager.getInstance().getNextId(Artifact.class));
		}

		Group swtGroup = new SimpleGroup(parent, ScenariosUILabels.ATAM_SCENARIO_INFO, this.readOnly).getSwtGroup();

		TextFieldMetainfo textMetainfo = TextFieldMetainfo.create(swtGroup, ScenariosUILabels.ID, new BindingInfo(
				underlyingArtifact, "id"), true);
		TextFactory.createText(textMetainfo);

		textMetainfo = TextFieldMetainfo.create(swtGroup, ScenariosUILabels.NAME, new BindingInfo(underlyingArtifact,
				"name"), this.readOnly);
		TextFactory.createText(textMetainfo);

		textMetainfo = TextFieldMetainfo.create(swtGroup, ScenariosUILabels.SYSTEM_NAME, new BindingInfo(
				underlyingArtifact, "systemName"), this.readOnly);
		TextFactory.createText(textMetainfo);
	}

}