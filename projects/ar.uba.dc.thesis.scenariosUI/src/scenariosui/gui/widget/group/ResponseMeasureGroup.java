package scenariosui.gui.widget.group;

import scenariosui.gui.widget.composite.ConstraintComposite;
import scenariosui.properties.ScenariosUILabels;
import ar.uba.dc.thesis.rainbow.constraint.Constraint;

import commons.gui.model.CompositeModel;
import commons.gui.widget.creation.binding.BindingInfo;
import commons.gui.widget.creation.metainfo.ControlMetainfo;
import commons.gui.widget.creation.metainfo.TextFieldMetainfo;
import commons.gui.widget.factory.TextFactory;
import commons.gui.widget.group.SimpleGroup;
import commons.properties.CommonLabels;

public class ResponseMeasureGroup extends SimpleGroup {

	private ConstraintComposite constraintComposite;

	@SuppressWarnings("unchecked")
	public ResponseMeasureGroup(ControlMetainfo ctrlMetainfo) {
		super(ctrlMetainfo.composite, ScenariosUILabels.RESPONSE_MEASURE, ctrlMetainfo.readOnly, 1, 2);

		CompositeModel<Constraint> compositeModel = ctrlMetainfo.binding.getCompositeModel().getNestedModel(
				"constraint", Constraint.class);

		this.constraintComposite = new ConstraintComposite(this.getSwtGroup(), compositeModel, ctrlMetainfo.readOnly);

		TextFieldMetainfo textMetainfo = TextFieldMetainfo.create(this.constraintComposite, CommonLabels.DESCRIPTION,
				new BindingInfo(ctrlMetainfo.binding.getCompositeModel(), "description"), ctrlMetainfo.readOnly);
		textMetainfo.visibleSize = 35;
		TextFactory.createText(textMetainfo);

	}

	public void okPressed() {
		this.constraintComposite.okPressed();
	}
}
