package scenariosui.gui.widget.composite;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;

import scenariosui.properties.ScenariosUILabels;
import scenariosui.service.ScenariosUIController;
import ar.uba.dc.thesis.selfhealing.SelfHealingScenario;

import commons.gui.model.CompositeModel;
import commons.gui.util.purpose.Purpose;
import commons.gui.widget.composite.ObjectSelectionMetainfo;
import commons.gui.widget.composite.SimpleComposite;
import commons.gui.widget.creation.binding.BindingInfo;
import commons.gui.widget.creation.metainfo.BooleanFieldMetainfo;
import commons.gui.widget.creation.metainfo.ComboMetainfo;
import commons.gui.widget.creation.metainfo.TextFieldMetainfo;
import commons.gui.widget.factory.BooleanFactory;
import commons.gui.widget.factory.ComboFactory;
import commons.gui.widget.factory.TextFactory;
import commons.gui.widget.group.SimpleGroup;

public class SelfHealingScenarioComposite extends SimpleComposite {

	public SelfHealingScenarioComposite(Composite parent, Purpose purpose,
			CompositeModel<SelfHealingScenario> underlyingScenario) {
		super(parent, purpose.isReadOnly());
		Group swtGroup = new SimpleGroup(parent, ScenariosUILabels.BASIC_DATA, this.readOnly).getSwtGroup();

		if (purpose.isCreation()) {
			underlyingScenario.getValue().setId(ScenariosUIController.getInstance().getNextId());
		}

		TextFieldMetainfo textMetainfo = TextFieldMetainfo.create(swtGroup, ScenariosUILabels.ID, new BindingInfo(
				underlyingScenario, "id"), true);
		TextFactory.createText(textMetainfo);

		textMetainfo = TextFieldMetainfo.create(swtGroup, ScenariosUILabels.NAME, new BindingInfo(underlyingScenario,
				"name"), this.readOnly);
		TextFactory.createText(textMetainfo);

		ComboMetainfo comboMetainfo = ComboMetainfo.create(swtGroup, ScenariosUILabels.CONCERN, new BindingInfo(
				underlyingScenario, "concern"), this.readOnly);
		ComboFactory.createCombo(comboMetainfo);

		textMetainfo = TextFieldMetainfo.create(swtGroup, ScenariosUILabels.STIMULUS_SOURCE, new BindingInfo(
				underlyingScenario, "stimulusSource"), this.readOnly);
		TextFactory.createText(textMetainfo);

		textMetainfo = TextFieldMetainfo.create(swtGroup, ScenariosUILabels.STIMULUS, new BindingInfo(
				underlyingScenario, "stimulus"), this.readOnly);
		TextFactory.createText(textMetainfo);

		ObjectSelectionMetainfo objectSelectionMetainfo = new ObjectSelectionMetainfo(swtGroup,
				ScenariosUILabels.ENVIRONMENTS, new BindingInfo(underlyingScenario, "environments"), this.readOnly);
		objectSelectionMetainfo.nullable = true; // to be used for the "Default" environment
		objectSelectionMetainfo.canView = false;

		new EnvironmentsSelectionComposite(objectSelectionMetainfo);

		// TODO: Artifact

		textMetainfo = TextFieldMetainfo.create(swtGroup, ScenariosUILabels.RESPONSE, new BindingInfo(
				underlyingScenario, "response"), this.readOnly);
		TextFactory.createText(textMetainfo);

		// TODO: Response Measure

		// TODO: Architectural Decisions

		textMetainfo = TextFieldMetainfo.create(swtGroup, ScenariosUILabels.PRIORITY, new BindingInfo(
				underlyingScenario, "priority"), this.readOnly);
		TextFactory.createText(textMetainfo);

		BooleanFieldMetainfo metainfo = BooleanFieldMetainfo.create(swtGroup, ScenariosUILabels.ENABLED,
				new BindingInfo(underlyingScenario, "enabled"), readOnly);
		BooleanFactory.createBoolean(metainfo);

		// TODO: Repair Strategies
	}

}
