package commons.gui.widget.creation.metainfo;

import java.util.SortedMap;

import org.eclipse.swt.widgets.Composite;

import commons.gui.model.ValueModel;
import commons.gui.widget.creation.binding.Binding;
import commons.properties.EnumProperty;

public class SpinnerWithScaleMetainfo extends ControlMetainfo {

	private static final SpinnerWithScaleMetainfo instance = new SpinnerWithScaleMetainfo();

	/**
	 * Specifies whether multipleWidgets widgets must be created.
	 */
	public boolean multipleWidgets;

	/**
	 * Specifies the list of <label, model> pairs that are going to be used when creating multipleWidgets widgets
	 */
	public SortedMap<EnumProperty, ValueModel<Double>> labelsAndModels;

	/**
	 * This class is intended to be a singleton
	 */
	private SpinnerWithScaleMetainfo() {
		super();
		this.applyDefaults();
	}

	/**
	 * Meta information for constructing a single instance of a Spinner with an scale.
	 */
	public static SpinnerWithScaleMetainfo singleWidget(Composite composite, EnumProperty label, Binding binding,
			boolean readOnly) {
		instance.applyDefaults();
		ControlMetainfo.setValues(instance, composite, label, binding, readOnly);

		return instance;
	}

	/**
	 * Meta information for constructing a multiple instances of a Spinner with an scale, at the same time.
	 */
	public static SpinnerWithScaleMetainfo multipleWidgets(Composite composite, boolean readOnly,
			SortedMap<EnumProperty, ValueModel<Double>> labelsAndModels) {
		instance.applyDefaults();
		instance.composite = composite;
		instance.readOnly = readOnly;
		instance.multipleWidgets = true;
		instance.labelsAndModels = labelsAndModels;

		return instance;
	}

	@Override
	public void applyDefaults() {
		super.applyDefaults();
		this.multipleWidgets = false;
		this.labelsAndModels = null;
	}

	@Override
	public void restoreDefaults() {
		super.restoreDefaults();
		this.applyDefaults();
	}
}