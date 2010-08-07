package commons.gui.widget.composite;

import java.util.ArrayList;

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
import commons.gui.model.CompositeModel;
import commons.gui.model.ValueChangeEvent;
import commons.gui.model.ValueChangeListener;
import commons.gui.model.ValueModel;
import commons.gui.model.types.EditType;
import commons.gui.util.PageHelper;
import commons.gui.widget.creation.binding.BindingInfo;
import commons.utils.SbaStringUtils;

/**
 * Modela los campos de RPC con las restricciones numéricas necesarias y el layout usual para este tipo de campos.
 * 
 * 
 */
public class RPCComposite extends SimpleComposite implements ValueModel<String> {

	public RPCComposite(Composite parent, CompositeModel model, String propertyName, boolean readOnly) {
		super(parent, readOnly, 1);

		this.valueListeners = new ArrayList<ValueChangeListener>();

		this.binding = new BindingInfo(model, propertyName);

		this.nro = this.createTextBox(6);
		createSeparatorLabel();
		this.folio = this.createTextBox(3);
		createSeparatorLabel();
		this.libro = this.createTextBox(3);
		createSeparatorLabel();
		this.tomo = this.createTextBox(1);

		this.binding.bind(this);
	}

	public String getValue() {
		return SbaStringUtils.concat(nro.getText(), folio.getText(), libro.getText(), tomo.getText());
	}

	public void setValue(String value) {
		this.clear();

		if (value != null) {
			for (int i = 0; i < value.length(); i++) {
				if (i < 6) {
					this.nro.setText(this.nro.getText() + value.charAt(i));
				} else if (i < 9) {
					this.folio.setText(this.folio.getText() + value.charAt(i));
				} else if (i < 12) {
					this.libro.setText(this.libro.getText() + value.charAt(i));
				} else {
					this.tomo.setText(this.tomo.getText() + value.charAt(i));
				}
			}
		}
	}

	public EditType<String> getValueType() {
		return editType;
	}

	public void notifyChange() {
		ValueChangeEvent ev = new ValueChangeEvent<String>(this, null, null);

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
	 * Modifica el layout por defecto para poder ubicar los 4 campos en una misma línea.
	 */
	@Override
	protected void applyLayout() {
		RowLayout layout = new RowLayout(SWT.HORIZONTAL);
		layout.marginLeft = 0;
		this.setLayout(layout);
	}

	/**
	 * Limpia los campos de texto, asignandole a cada uno el string vacío (<code>""</code>).
	 */
	private void clear() {
		this.nro.setText(NULL_STRING);
		this.folio.setText(NULL_STRING);
		this.libro.setText(NULL_STRING);
		this.tomo.setText(NULL_STRING);
	}

	private Text createTextBox(int maxLength) {
		// dynamic calculus of textbox size
		final int cantPixels = PageHelper.getCantidadDePixels(Math.max(2 * maxLength - 1, maxLength + 1));
		final int style = readOnly ? SWT.READ_ONLY : GuiStyle.DEFAULT_TEXTBOX_STYLE;

		Text textBox = new Text(this, style);
		textBox.setLayoutData(new RowData(cantPixels, SWT.DEFAULT));
		textBox.setTextLimit(maxLength);

		if (this.readOnly) {
			textBox.setFont(PageHelper.getValueLabelsFont());
		} else {
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
				Text nextTextBox = tomo;
				if (textbox.equals(nro)) {
					nextTextBox = folio;
				} else if (textbox.equals(folio)) {
					nextTextBox = libro;
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

	private Text nro;

	private Text folio;

	private Text libro;

	private Text tomo;

	private BindingInfo binding;

	private ArrayList<ValueChangeListener> valueListeners;

	private static final EditType<String> editType = new EditType<String>(String.class);

	private static final String NULL_STRING = "";
}