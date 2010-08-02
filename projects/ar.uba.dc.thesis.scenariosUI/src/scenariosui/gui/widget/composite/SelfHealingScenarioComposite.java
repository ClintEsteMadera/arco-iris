package scenariosui.gui.widget.composite;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;

import scenariosui.gui.widget.group.ResponseMeasureGroup;
import scenariosui.properties.ScenariosUILabels;
import scenariosui.service.SelfHealingConfigurationManager;
import ar.uba.dc.thesis.atam.scenario.model.ResponseMeasure;
import ar.uba.dc.thesis.selfhealing.SelfHealingScenario;

import commons.gui.model.CompositeModel;
import commons.gui.util.purpose.Purpose;
import commons.gui.widget.Alignment;
import commons.gui.widget.composite.ObjectSelectionMetainfo;
import commons.gui.widget.composite.SimpleComposite;
import commons.gui.widget.creation.binding.BindingInfo;
import commons.gui.widget.creation.metainfo.BooleanFieldMetainfo;
import commons.gui.widget.creation.metainfo.ComboMetainfo;
import commons.gui.widget.creation.metainfo.ControlMetainfo;
import commons.gui.widget.creation.metainfo.TextFieldMetainfo;
import commons.gui.widget.factory.BooleanFactory;
import commons.gui.widget.factory.ComboFactory;
import commons.gui.widget.factory.LabelFactory;
import commons.gui.widget.factory.TextFactory;
import commons.gui.widget.group.SimpleGroup;
import commons.properties.CommonLabels;

public class SelfHealingScenarioComposite extends SimpleComposite {

	private ResponseMeasureGroup responseMeasureGroup;

	public SelfHealingScenarioComposite(Composite parent, Purpose purpose,
			CompositeModel<SelfHealingScenario> underlyingScenario) {
		super(parent, purpose.isReadOnly());

		Group swtGroup = new SimpleGroup(parent, ScenariosUILabels.SCENARIO, this.readOnly).getSwtGroup();

		BooleanFieldMetainfo metainfo = BooleanFieldMetainfo.create(swtGroup, ScenariosUILabels.ENABLED,
				new BindingInfo(underlyingScenario, "enabled"), readOnly);
		metainfo.horizontalSpan = 2;
		metainfo.alignment = Alignment.RIGHT;
		BooleanFactory.createBoolean(metainfo);

		if (purpose.isCreation()) {
			underlyingScenario.getValue().setId(
					SelfHealingConfigurationManager.getInstance().getNextId(SelfHealingScenario.class));
		}

		SimpleComposite fourColumnsComposite = new SimpleComposite(swtGroup, super.readOnly, 4, 2);

		TextFieldMetainfo textMetainfo = TextFieldMetainfo.create(fourColumnsComposite, ScenariosUILabels.ID,
				new BindingInfo(underlyingScenario, "id"), true);
		TextFactory.createText(textMetainfo);

		textMetainfo = TextFieldMetainfo.create(fourColumnsComposite, ScenariosUILabels.PRIORITY, new BindingInfo(
				underlyingScenario, "priority"), this.readOnly);
		textMetainfo.visibleSize = 5;
		TextFactory.createText(textMetainfo);

		textMetainfo = TextFieldMetainfo.create(fourColumnsComposite, ScenariosUILabels.NAME, new BindingInfo(
				underlyingScenario, "name"), this.readOnly);
		textMetainfo.visibleSize = 35;
		TextFactory.createText(textMetainfo);

		ComboMetainfo comboMetainfo = ComboMetainfo.create(fourColumnsComposite, ScenariosUILabels.CONCERN,
				new BindingInfo(underlyingScenario, "concern"), this.readOnly);
		ComboFactory.createCombo(comboMetainfo);

		textMetainfo = TextFieldMetainfo.create(fourColumnsComposite, ScenariosUILabels.STIMULUS_SOURCE,
				new BindingInfo(underlyingScenario, "stimulusSource"), this.readOnly);
		textMetainfo.visibleSize = 35;
		TextFactory.createText(textMetainfo);

		textMetainfo = TextFieldMetainfo.create(fourColumnsComposite, ScenariosUILabels.STIMULUS, new BindingInfo(
				underlyingScenario, "stimulus"), this.readOnly);
		textMetainfo.visibleSize = 30;
		TextFactory.createText(textMetainfo);

		LabelFactory.createLabel(fourColumnsComposite, ScenariosUILabels.ENVIRONMENTS, false, true);

		ObjectSelectionMetainfo objectSelectionMetainfo = new ObjectSelectionMetainfo(fourColumnsComposite,
				CommonLabels.NO_LABEL, new BindingInfo(underlyingScenario, "environments"), this.readOnly);
		objectSelectionMetainfo.nullable = true; // to be used for the "Default" environment
		objectSelectionMetainfo.canView = false;
		objectSelectionMetainfo.columnsToSpan = 3;

		new EnvironmentsSelectionComposite(objectSelectionMetainfo);

		LabelFactory.createLabel(fourColumnsComposite, ScenariosUILabels.ARTIFACT, false, true);

		objectSelectionMetainfo = new ObjectSelectionMetainfo(fourColumnsComposite, CommonLabels.NO_LABEL,
				new BindingInfo(underlyingScenario, "artifact"), this.readOnly);
		objectSelectionMetainfo.canView = false;
		objectSelectionMetainfo.columnsToSpan = 3;

		new ArtifactSelectionComposite(objectSelectionMetainfo);

		textMetainfo = TextFieldMetainfo.create(fourColumnsComposite, ScenariosUILabels.RESPONSE, new BindingInfo(
				underlyingScenario, "response"), this.readOnly);
		textMetainfo.visibleSize = 35;
		TextFactory.createText(textMetainfo);

		ControlMetainfo controlMetainfo = new ControlMetainfo(swtGroup, ScenariosUILabels.RESPONSE_MEASURE,
				new BindingInfo(underlyingScenario.getNestedModel("responseMeasure", ResponseMeasure.class)),
				this.readOnly);
		this.responseMeasureGroup = new ResponseMeasureGroup(controlMetainfo);

		// TODO Architectural Decisions

		// TODO List<String> repairStrategies;
	}

	public void okPressed() {
		this.responseMeasureGroup.okPressed();
	}
}
