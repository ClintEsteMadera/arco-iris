package scenariosui.gui.widget.composite;

import commons.gui.widget.composite.SimpleComposite;
import commons.gui.widget.creation.binding.BindingInfo;
import commons.gui.widget.creation.metainfo.ControlMetainfo;
import commons.gui.widget.creation.metainfo.TextFieldMetainfo;
import commons.gui.widget.factory.TextFactory;
import commons.properties.CommonLabels;

public class ResponseMeasureComposite extends SimpleComposite {

	public ResponseMeasureComposite(ControlMetainfo ctrlMetainfo) {
		super(ctrlMetainfo.composite, ctrlMetainfo.readOnly, 2, 2);

		TextFieldMetainfo textMetainfo = TextFieldMetainfo.create(ctrlMetainfo.composite, CommonLabels.DESCRIPTION,
				new BindingInfo(ctrlMetainfo.binding.getCompositeModel(), "responseMeasure.description"),
				ctrlMetainfo.readOnly);

		TextFactory.createText(textMetainfo);

	}
}
