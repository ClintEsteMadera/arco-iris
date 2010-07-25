package scenariosui.gui.widget.composite;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;

import scenariosui.properties.ScenariosUILabels;
import scenariosui.service.ScenariosUIController;
import ar.uba.dc.thesis.atam.scenario.model.Environment;

import commons.gui.model.CompositeModel;
import commons.gui.util.purpose.Purpose;
import commons.gui.widget.composite.SimpleComposite;
import commons.gui.widget.creation.binding.BindingInfo;
import commons.gui.widget.creation.metainfo.TextFieldMetainfo;
import commons.gui.widget.factory.TextFactory;
import commons.gui.widget.group.SimpleGroup;

public class EnvironmentComposite extends SimpleComposite {

	public EnvironmentComposite(Composite parent, Purpose purpose, CompositeModel<Environment> underlyingEnvironment) {
		super(parent, purpose.isReadOnly());

		if (purpose.isCreation()) {
			underlyingEnvironment.getValue().setId(ScenariosUIController.getInstance().getNextId());
		}

		Group swtGroup = new SimpleGroup(parent, ScenariosUILabels.BASIC_DATA, this.readOnly).getSwtGroup();

		TextFieldMetainfo textMetainfo = TextFieldMetainfo.create(swtGroup, ScenariosUILabels.ID, new BindingInfo(
				underlyingEnvironment, "id"), true);
		TextFactory.createText(textMetainfo);

		textMetainfo = TextFieldMetainfo.create(swtGroup, ScenariosUILabels.NAME, new BindingInfo(
				underlyingEnvironment, "name"), this.readOnly);
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
