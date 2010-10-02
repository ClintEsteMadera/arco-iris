package scenariosui.gui.widget.group;

import java.util.Map.Entry;
import java.util.SortedMap;
import java.util.TreeMap;

import org.eclipse.swt.widgets.Composite;

import ar.uba.dc.thesis.qa.Concern;

import commons.gui.model.CompositeModel;
import commons.gui.model.ValueChangeEvent;
import commons.gui.model.ValueChangeListener;
import commons.gui.model.ValueModel;
import commons.gui.model.bean.BeanModel;
import commons.gui.widget.creation.metainfo.SpinnerWithScaleMetainfo;
import commons.gui.widget.factory.SpinnerWithScaleFactory;
import commons.gui.widget.group.SimpleGroup;
import commons.properties.EnumProperty;
import commons.properties.FakeEnumProperty;

public class ConcernWeightsGroup extends SimpleGroup {

	private SortedMap<Concern, Double> model;

	public ConcernWeightsGroup(CompositeModel<SortedMap<Concern, Double>> weightsMap, Composite parent,
			EnumProperty title, boolean readOnly, int columnsToSpan) {
		super(parent, title, readOnly, 1, columnsToSpan);

		this.model = weightsMap.getValue();

		SortedMap<EnumProperty, ValueModel<Double>> labelsAndModels = new TreeMap<EnumProperty, ValueModel<Double>>();

		for (Entry<Concern, Double> current : this.model.entrySet()) {
			final Concern concern = current.getKey();
			final FakeEnumProperty label = new FakeEnumProperty(concern.toString());
			final BeanModel<Double> beanModel = new BeanModel<Double>(current.getValue());

			labelsAndModels.put(label, beanModel);

			beanModel.addValueChangeListener(new ValueChangeListener<Double>() {
				public void valueChange(ValueChangeEvent<Double> ev) {
					// update the model received as parameter
					ConcernWeightsGroup.this.model.put(concern, beanModel.getValue());
				}
			});
		}
		SpinnerWithScaleFactory.create(SpinnerWithScaleMetainfo.multipleWidgets(this.getSwtGroup(), this.readOnly,
				labelsAndModels));
	}
}
