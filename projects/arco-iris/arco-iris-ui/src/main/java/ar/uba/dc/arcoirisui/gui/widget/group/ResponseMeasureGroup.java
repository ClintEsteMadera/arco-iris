package ar.uba.dc.arcoirisui.gui.widget.group;

import ar.uba.dc.arcoirisui.gui.widget.composite.ConstraintComposite;
import ar.uba.dc.arcoirisui.properties.ArcoIrisUILabels;
import ar.uba.dc.arcoiris.rainbow.constraint.Constraint;

import commons.gui.model.CompositeModel;
import commons.gui.widget.composite.SimpleComposite;
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
		super(ctrlMetainfo.composite, ArcoIrisUILabels.RESPONSE_MEASURE, ctrlMetainfo.readOnly, 1, 2);

		CompositeModel<Constraint> compositeModel = ctrlMetainfo.binding.getCompositeModel().getNestedModel(
				"constraint", Constraint.class);

		this.constraintComposite = new ConstraintComposite(this.getSwtGroup(), compositeModel, true,
				ctrlMetainfo.readOnly);

		TextFieldMetainfo textMetainfo = TextFieldMetainfo.create(new SimpleComposite(this.constraintComposite,
				super.readOnly, 2), CommonLabels.DESCRIPTION, new BindingInfo(ctrlMetainfo.binding.getCompositeModel(),
				"description"), ctrlMetainfo.readOnly);
		TextFactory.createText(textMetainfo);

	}

	public void okPressed() {
		this.constraintComposite.okPressed();
	}
}
