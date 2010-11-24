package commons.gui.binding;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Scale;
import org.eclipse.swt.widgets.Spinner;
import org.eclipse.swt.widgets.Widget;

import commons.gui.model.ValueChangeEvent;
import commons.gui.model.ValueChangeListener;
import commons.gui.model.ValueModel;
import commons.gui.model.binding.WidgetContainer;
import commons.gui.model.types.EditType;
import commons.gui.widget.adapter.IntegerModifierControl;

/**
 * Represents a wrapper around an {@link IntegerModifierControl} (e.g. {@link Spinner} or {@link Scale}.
 * <p>
 * This value model provides an abstraction layer that deals internally with an integer value, converting it to a double
 * value, according to the digits set in the underlying widget (see {@link IntegerModifierControl#getDigits()}.
 * 
 */
public class IntegerModifierControlValueModel implements ValueModel<Double>, WidgetContainer {

	private List<ValueChangeListener<Double>> listeners;

	private SelectionListener selectionListener;

	private IntegerModifierControl control;

	private static final EditType<Double> EDIT_TYPE = new EditType<Double>(Double.class);

	private static final String ZERO = "0";

	private static final String DECIMAL_SEPARATOR = ".";

	public <T> IntegerModifierControlValueModel(IntegerModifierControl control) {
		super();

		this.control = control;

		this.listeners = new ArrayList<ValueChangeListener<Double>>();

		this.selectionListener = new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				notifyChange();
			}
		};
	}

	public final void notifyChange() {
		final ValueChangeEvent<Double> changeEvent = new ValueChangeEvent<Double>(this, null, this.getValue());

		for (ValueChangeListener<Double> listener : listeners) {
			listener.valueChange(changeEvent);
		}
	}

	public void addValueChangeListener(ValueChangeListener<Double> listener) {
		listeners.add(listener);
		if (listeners.size() == 1) {
			this.control.addSelectionListener(this.selectionListener);
		}
	}

	public void removeValueChangeListener(ValueChangeListener<Double> listener) {
		listeners.remove(listener);
		if (listeners.isEmpty()) {
			this.control.removeSelectionListener(this.selectionListener);
		}
	}

	public EditType<Double> getValueType() {
		return EDIT_TYPE;
	}

	public Double getValue() {
		int intValue = this.control.getSelection();
		int digits = this.control.getDigits();

		String newValueAsString = String.valueOf(intValue);
		if (digits > 0) {
			int index = newValueAsString.length() - digits;
			StringBuffer buffer = new StringBuffer();
			if (index > 0) {
				buffer.append(newValueAsString.substring(0, index));
				buffer.append(DECIMAL_SEPARATOR);
				buffer.append(newValueAsString.substring(index));
			} else {
				buffer.append(ZERO);
				buffer.append(DECIMAL_SEPARATOR);
				while (index++ < 0) {
					buffer.append(ZERO);
				}
				buffer.append(newValueAsString);
			}
			newValueAsString = buffer.toString();
		}
		return Double.valueOf(newValueAsString);
	}

	/**
	 * Removes the decimal separator, parseInt the integer and decimal parts concatenated and sets that value to the
	 * control as the 'current selection'.
	 */
	public void setValue(Double value) {
		if (value != null) {
			BigDecimal normalizedDecimalPart = BigDecimal.valueOf(value).setScale(this.control.getDigits(),
					BigDecimal.ROUND_HALF_UP);
			String[] intAndDecimalParts = normalizedDecimalPart.toString().split("\\" + DECIMAL_SEPARATOR);
			this.control.setSelection(Integer.valueOf(intAndDecimalParts[0] + intAndDecimalParts[1]));
		}
	}

	public Widget getWidget() {
		return (Widget) this.control;
	}
}