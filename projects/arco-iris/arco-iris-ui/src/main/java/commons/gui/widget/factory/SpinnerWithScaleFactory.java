package commons.gui.widget.factory;

import java.util.Map.Entry;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Layout;
import org.eclipse.swt.widgets.Scale;
import org.eclipse.swt.widgets.Spinner;

import commons.gui.model.ValueModel;
import commons.gui.widget.adapter.ScaleAdapter;
import commons.gui.widget.adapter.SpinnerAdapter;
import commons.gui.widget.composite.SimpleComposite;
import commons.gui.widget.creation.binding.BindingInfo;
import commons.gui.widget.creation.metainfo.ControlMetainfo;
import commons.gui.widget.creation.metainfo.SpinnerWithScaleMetainfo;
import commons.properties.EnumProperty;

/**
 * This class is a factory of a composite component consisting of a spinner (see {@link Spinner} and a scale (
 * {@link Scale} which are synchronized to each other.
 */
public final class SpinnerWithScaleFactory {

	private static final int DIGITS = 2;

	/**
	 * This class is not meant to be instantiated.
	 */
	private SpinnerWithScaleFactory() {
		super();
	}

	public static Composite create(SpinnerWithScaleMetainfo metainfo) {
		SimpleComposite spinnerWithScaleComposite = createComposite4Widgets(metainfo.composite, metainfo.readOnly);
		metainfo.composite = spinnerWithScaleComposite;

		if (metainfo.multipleWidgets) {
			for (final Entry<EnumProperty, ValueModel<Double>> current : metainfo.labelsAndModels.entrySet()) {
				metainfo.label = current.getKey();
				metainfo.binding = new BindingInfo(current.getValue());
				doCreateWidgets(metainfo);
			}
		} else {
			doCreateWidgets(metainfo);
		}
		return spinnerWithScaleComposite;
	}

	private static void doCreateWidgets(final ControlMetainfo metainfo) {
		LabelFactory.createLabel(metainfo.composite, metainfo.label, false, true);

		// TODO Several settings below can be turned into parameters (via metainfo)

		ScaleAdapter scale = new ScaleAdapter(metainfo.composite, SWT.HORIZONTAL);
		scale.setPageIncrement(10);
		scale.setDigits(DIGITS);
		scale.setMaximum(100);

		final SpinnerAdapter spinner = new SpinnerAdapter(metainfo.composite, SWT.HORIZONTAL);
		spinner.setDigits(DIGITS);

		metainfo.binding.bind(spinner);
		metainfo.binding.bind(scale);
	}

	private static SimpleComposite createComposite4Widgets(Composite parent, boolean readOnly) {
		return new SimpleComposite(parent, readOnly, 3, deduceAmountOfColumnsToSpan(parent));
	}

	private static int deduceAmountOfColumnsToSpan(Composite parent) {
		int columnsToSpan = 2;
		Layout layout = parent.getLayout();
		if (layout instanceof GridLayout) {
			columnsToSpan = ((GridLayout) layout).numColumns;
		}
		return columnsToSpan;
	}
}
