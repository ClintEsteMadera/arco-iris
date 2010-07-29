package scenariosui.gui.widget.composite;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;

import scenariosui.properties.ScenariosUILabels;
import scenariosui.service.SelfHealingConfigurationManager;
import ar.uba.dc.thesis.selfhealing.SelfHealingScenario;

import commons.gui.model.CompositeModel;
import commons.gui.util.purpose.Purpose;
import commons.gui.widget.composite.ObjectSelectionMetainfo;
import commons.gui.widget.composite.SimpleComposite;
import commons.gui.widget.creation.binding.BindingInfo;
import commons.gui.widget.creation.metainfo.BooleanFieldMetainfo;
import commons.gui.widget.creation.metainfo.ComboMetainfo;
import commons.gui.widget.creation.metainfo.ControlMetainfo;
import commons.gui.widget.creation.metainfo.TextFieldMetainfo;
import commons.gui.widget.factory.BooleanFactory;
import commons.gui.widget.factory.ComboFactory;
import commons.gui.widget.factory.TextFactory;
import commons.gui.widget.group.SimpleGroup;
import commons.properties.CommonLabels;
import commons.properties.FakeEnumProperty;

public class SelfHealingScenarioComposite extends SimpleComposite {

	public SelfHealingScenarioComposite(Composite parent, Purpose purpose,
			CompositeModel<SelfHealingScenario> underlyingScenario) {
		super(parent, purpose.isReadOnly());

		BooleanFieldMetainfo metainfo = BooleanFieldMetainfo.create(parent, ScenariosUILabels.ENABLED, new BindingInfo(
				underlyingScenario, "enabled"), readOnly);
		BooleanFactory.createBoolean(metainfo);

		Group atamScenarioGroup = new SimpleGroup(parent, new FakeEnumProperty("Basic Information"), this.readOnly)
				.getSwtGroup();

		if (purpose.isCreation()) {
			underlyingScenario.getValue().setId(
					SelfHealingConfigurationManager.getInstance().getNextId(SelfHealingScenario.class));
		}

		TextFieldMetainfo textMetainfo = TextFieldMetainfo.create(atamScenarioGroup, ScenariosUILabels.ID,
				new BindingInfo(underlyingScenario, "id"), true);
		TextFactory.createText(textMetainfo);

		textMetainfo = TextFieldMetainfo.create(atamScenarioGroup, ScenariosUILabels.NAME, new BindingInfo(
				underlyingScenario, "name"), this.readOnly);
		TextFactory.createText(textMetainfo);

		ComboMetainfo comboMetainfo = ComboMetainfo.create(atamScenarioGroup, ScenariosUILabels.CONCERN,
				new BindingInfo(underlyingScenario, "concern"), this.readOnly);
		ComboFactory.createCombo(comboMetainfo);

		textMetainfo = TextFieldMetainfo.create(atamScenarioGroup, ScenariosUILabels.PRIORITY, new BindingInfo(
				underlyingScenario, "priority"), this.readOnly);
		TextFactory.createText(textMetainfo);

		Group stimulusGroup = new SimpleGroup(parent, new FakeEnumProperty("Stimulus Information"), this.readOnly)
				.getSwtGroup();

		textMetainfo = TextFieldMetainfo.create(stimulusGroup, ScenariosUILabels.STIMULUS_SOURCE, new BindingInfo(
				underlyingScenario, "stimulusSource"), this.readOnly);
		TextFactory.createText(textMetainfo);

		textMetainfo = TextFieldMetainfo.create(stimulusGroup, ScenariosUILabels.STIMULUS, new BindingInfo(
				underlyingScenario, "stimulus"), this.readOnly);
		TextFactory.createText(textMetainfo);

		Group selectionGroup = new SimpleGroup(parent, CommonLabels.NO_LABEL, this.readOnly).getSwtGroup();

		ObjectSelectionMetainfo objectSelectionMetainfo = new ObjectSelectionMetainfo(selectionGroup,
				ScenariosUILabels.ENVIRONMENTS, new BindingInfo(underlyingScenario, "environments"), this.readOnly);
		objectSelectionMetainfo.nullable = true; // to be used for the "Default" environment
		objectSelectionMetainfo.canView = false;

		new EnvironmentsSelectionComposite(objectSelectionMetainfo);

		objectSelectionMetainfo = new ObjectSelectionMetainfo(selectionGroup, ScenariosUILabels.ARTIFACT,
				new BindingInfo(underlyingScenario, "artifact"), this.readOnly);
		objectSelectionMetainfo.canView = false;

		new ArtifactSelectionComposite(objectSelectionMetainfo);

		Group responseGroup = new SimpleGroup(parent, new FakeEnumProperty("Response Information"), this.readOnly)
				.getSwtGroup();

		textMetainfo = TextFieldMetainfo.create(responseGroup, ScenariosUILabels.RESPONSE, new BindingInfo(
				underlyingScenario, "response"), this.readOnly);
		TextFactory.createText(textMetainfo);

		ControlMetainfo controlMetainfo = new ControlMetainfo(responseGroup, ScenariosUILabels.RESPONSE_MEASURE,
				new BindingInfo(underlyingScenario, "responseMeasure"), this.readOnly);
		new ResponseMeasureComposite(controlMetainfo);

		// Group selfHealingScenarioGroup = new SimpleGroup(parent, ScenariosUILabels.SELF_HEALING_SPECIFIC_INFO,
		// this.readOnly).getSwtGroup();

		// TODO Architectural Decisions

		// TODO List<String> repairStrategies;
	}
}
