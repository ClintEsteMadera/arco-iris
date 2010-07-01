package scenariosui.gui.widget.page;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;

import sba.common.properties.EnumProperty;
import scenariosui.gui.util.purpose.ScenariosUIPurpose;
import scenariosui.properties.ScenariosUILabels;
import scenariosui.service.ScenariosUIController;
import ar.uba.dc.thesis.selfhealing.SelfHealingScenario;

import commons.gui.model.CompositeModel;
import commons.gui.widget.creation.binding.BindingInfo;
import commons.gui.widget.creation.metainfo.BooleanFieldMetainfo;
import commons.gui.widget.creation.metainfo.ComboMetainfo;
import commons.gui.widget.creation.metainfo.TextFieldMetainfo;
import commons.gui.widget.factory.BooleanFactory;
import commons.gui.widget.factory.ComboFactory;
import commons.gui.widget.factory.TextFactory;
import commons.gui.widget.group.SimpleGroup;
import commons.gui.widget.page.BasePreferencesPage;

public class SelfHealingScenarioPage extends BasePreferencesPage<SelfHealingScenario> {

	public SelfHealingScenarioPage(CompositeModel<SelfHealingScenario> model, EnumProperty title, boolean readOnly,
			ScenariosUIPurpose proposito) {
		super(model, title, readOnly);
		this.purpose = proposito;
	}

	@Override
	protected void addFields(Composite parent) {
		datosBasicosGroup = new SimpleGroup(parent, ScenariosUILabels.BASIC_DATA, this.readOnly);
		Group swtGroup = datosBasicosGroup.getSwtGroup();

		CompositeModel<SelfHealingScenario> underlyingScenario = super.getCompositeModel();

		if (this.purpose.isCreation()) {
			super.getModel().setId(ScenariosUIController.getInstance().getNextId());
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

		// TODO: Environments

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

	private final ScenariosUIPurpose purpose;

	protected SimpleGroup datosBasicosGroup;
}