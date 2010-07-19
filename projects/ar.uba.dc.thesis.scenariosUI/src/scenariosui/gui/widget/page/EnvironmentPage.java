package scenariosui.gui.widget.page;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;

import scenariosui.gui.util.purpose.ScenariosUIPurpose;
import scenariosui.properties.ScenariosUILabels;
import scenariosui.service.ScenariosUIController;
import ar.uba.dc.thesis.atam.scenario.model.Environment;

import commons.gui.model.CompositeModel;
import commons.gui.widget.creation.binding.BindingInfo;
import commons.gui.widget.creation.metainfo.TextFieldMetainfo;
import commons.gui.widget.factory.TextFactory;
import commons.gui.widget.group.SimpleGroup;
import commons.gui.widget.page.BasePreferencesPage;
import commons.properties.EnumProperty;

public class EnvironmentPage extends BasePreferencesPage<Environment> {

	private final ScenariosUIPurpose purpose;

	protected SimpleGroup datosBasicosGroup;

	public EnvironmentPage(CompositeModel<Environment> model, EnumProperty title, boolean readOnly,
			ScenariosUIPurpose purpose) {
		super(model, title, readOnly);
		this.purpose = purpose;
	}

	@Override
	protected void addFields(Composite parent) {
		datosBasicosGroup = new SimpleGroup(parent, ScenariosUILabels.BASIC_DATA, this.readOnly);
		Group swtGroup = datosBasicosGroup.getSwtGroup();

		CompositeModel<Environment> underlyingScenario = super.getCompositeModel();

		if (this.purpose.isCreation()) {
			super.getModel().setId(ScenariosUIController.getInstance().getNextId());
		}

		TextFieldMetainfo textMetainfo = TextFieldMetainfo.create(swtGroup, ScenariosUILabels.ID, new BindingInfo(
				underlyingScenario, "id"), true);
		TextFactory.createText(textMetainfo);

		textMetainfo = TextFieldMetainfo.create(swtGroup, ScenariosUILabels.NAME, new BindingInfo(underlyingScenario,
				"name"), this.readOnly);
		TextFactory.createText(textMetainfo);

		/*
		 * TODO private List<? extends Constraint> conditions;
		 * 
		 * private Map<Concern, Double> weights;
		 * 
		 * private Heuristic heuristic;
		 * 
		 */
	}
}