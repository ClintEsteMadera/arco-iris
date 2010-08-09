package commons.gui.widget.composite;

import java.util.ArrayList;

import org.apache.commons.lang.StringUtils;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.FocusAdapter;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.RowData;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;

import commons.gui.GuiStyle;
import commons.gui.model.ValueChangeEvent;
import commons.gui.model.ValueChangeListener;
import commons.gui.model.ValueModel;
import commons.gui.model.types.EditType;
import commons.gui.util.ListenerHelper;
import commons.gui.util.PageHelper;
import commons.gui.widget.creation.binding.Binding;
import commons.utils.SbaStringUtils;

/**
 * Modela un campo de CUIT / CUIL con las restricciones numéricas necesarias y el layout usual para este tipo de campos.
 * 
 */
public class CuitComposite extends SimpleComposite implements ValueModel {

	public CuitComposite(Composite parent, Binding binding, boolean readOnly) {
		super(parent, readOnly, 1);

		this.valueListeners = new ArrayList<ValueChangeListener>();

		this.prefixTextBox = this.createTextBox(2, readOnly);
		createSeparatorLabel();
		this.dniTextBox = this.createTextBox(8, readOnly);
		createSeparatorLabel();
		this.digitTextBox = this.createTextBox(1, readOnly);

		binding.bind(this);
	}

	public Object getValue() {
		final String s = SbaStringUtils.concat(prefixTextBox.getText(), dniTextBox.getText(), digitTextBox.getText());
		return StringUtils.isEmpty(s) ? null : Long.parseLong(s);
	}

	public void setValue(Object value) {
		if (value instanceof Number) {
			value = ((Number) value).toString();
		}
		String s = value != null ? value.toString() : NULL_STRING;

		this.clear();

		for (int i = 0; i < s.length(); i++) {
			if (i < 2) {
				this.prefixTextBox.setText(this.prefixTextBox.getText() + s.charAt(i));
			} else if (i < 10) {
				this.dniTextBox.setText(this.dniTextBox.getText() + s.charAt(i));
			} else {
				this.digitTextBox.setText(this.digitTextBox.getText() + s.charAt(i));
			}
		}
	}

	public EditType getValueType() {
		return editType;
	}

	@SuppressWarnings("unchecked")
	public void notifyChange() {
		ValueChangeEvent ev = new ValueChangeEvent(this, null, null);

		for (ValueChangeListener l : this.valueListeners) {
			l.valueChange(ev);
		}
	}

	public void addValueChangeListener(ValueChangeListener listener) {
		valueListeners.add(listener);
	}

	public void removeValueChangeListener(ValueChangeListener listener) {
		valueListeners.remove(listener);
	}

	/**
	 * Modifica el layout por defecto para poder ubicar los 3 campos en una misma línea.
	 */
	@Override
	protected void applyLayout() {
		RowLayout layout = new RowLayout(SWT.HORIZONTAL);
		layout.marginLeft = 0;
		this.setLayout(layout);
	}

	private void clear() {
		this.prefixTextBox.setText(NULL_STRING);
		this.dniTextBox.setText(NULL_STRING);
		this.digitTextBox.setText(NULL_STRING);
	}

	private Text createTextBox(int maxLength, boolean readOnly) {
		// dynamic calculus of textbox size
		final int cantPixels = PageHelper.getCantidadDePixels(maxLength);
		final int style = readOnly ? SWT.READ_ONLY : GuiStyle.DEFAULT_TEXTBOX_STYLE;

		Text textBox = new Text(this, style);
		textBox.setLayoutData(new RowData(cantPixels, SWT.DEFAULT));
		textBox.setTextLimit(maxLength);

		if (this.readOnly) {
			textBox.setFont(PageHelper.getValueLabelsFont());
		} else {
			ListenerHelper.addIntegerFieldKeyListener(textBox);
			ListenerHelper.addIntegerFieldModifyListener(textBox);
			this.addModifyListener(textBox);
			this.addFocusListener(textBox);
		}
		return textBox;
	}

	private void addModifyListener(final Text textBox) {
		ModifyListener modifyListener = new ModifyListener() {
			public void modifyText(ModifyEvent event) {
				notifyChange();

				// changes the focus automatically
				if (textBox.getCharCount() == textBox.getTextLimit()) {
					this.getNextTextBox(textBox).setFocus();
				}
			}

			private Text getNextTextBox(Text textbox) {
				Text nextTextBox = digitTextBox;
				if (textbox.equals(prefixTextBox)) {
					nextTextBox = dniTextBox;
				}
				return nextTextBox;
			}
		};
		textBox.addModifyListener(modifyListener);
	}

	private void addFocusListener(final Text textBox) {
		textBox.addFocusListener(new FocusAdapter() {
			@Override
			public void focusGained(FocusEvent e) {
				textBox.selectAll();
			}
		});
	}

	private Text prefixTextBox;

	private Text dniTextBox;

	private Text digitTextBox;

	private ArrayList<ValueChangeListener> valueListeners;

	private static final EditType editType = new EditType<Long>(Long.class);

	private static final String NULL_STRING = "";
}