package commons.gui.binding;

import java.text.Format;
import java.text.ParseException;
import java.util.ArrayList;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Widget;

import commons.gui.model.ValueChangeEvent;
import commons.gui.model.ValueChangeListener;
import commons.gui.model.ValueModel;
import commons.gui.model.binding.WidgetContainer;
import commons.gui.model.types.EditConfigurationManager;
import commons.gui.model.types.EditType;
import commons.gui.swt.text.TextFormatter;
import commons.gui.swt.text.TextFormatterFactory;
import commons.gui.swt.text.TextFormatterFactoryManager;

/**
 * Clase utilizada internamnete para el binding de Text y Label
 * 
 * 
 */
public class TextValueModel implements ValueModel, WidgetContainer {

	public TextValueModel(Control control, Format format) {
		init(control);

		this.format = format;
	}

	@SuppressWarnings("unchecked")
	public <T> TextValueModel(Control control, EditType editType) {
		init(control);

		if (editType != null) {
			this.format = EditConfigurationManager.getInstance().getFormat(editType);
			this.displayFormat = format;

			if (control instanceof Text) {
				TextFormatterFactory formatterFactory = TextFormatterFactoryManager.getInstance().getFactory(editType);

				if (formatterFactory != null) {
					formatter = formatterFactory.createFormatter(editType);
					if (formatter != null) {
						formatter.install((Text) control);
					}
				}
			}
		}
	}

	public void addValueChangeListener(ValueChangeListener listener) {
		listeners.add(listener);
		if (listeners.size() == 1) {
			this.textControl.addModifyListener(this.modifyListener);
		}
	}

	public static boolean supportsControl(Control control) {
		return control instanceof Text || control instanceof Label;
	}

	public Object getValue() {

		if (format == null) {
			return textControl.getText();
		}

		try {
			return format.parseObject(textControl.getText());
		} catch (Exception e) {
			// TODO: hacer algo mejor en algunos casos si el texto es vacío
			// debería retornarse null pero si es un error de formato, no.
			return null;
		}
	}

	public EditType getValueType() {
		return editType;
	}

	public boolean isCheckFormat() {
		return checkFormat;
	}

	public void setCheckFormat(boolean checkFormat) {
		this.checkFormat = checkFormat;
	}

	public void removeValueChangeListener(ValueChangeListener listener) {
		listeners.remove(listener);
		if (listeners.size() == 0) {
			this.textControl.removeModifyListener(this.modifyListener);
		}
	}

	public void setValue(Object value) {
		String s = "";

		if (value != null) {
			if (format != null) {
				s = format.format(value);
			} else {
				s = value.toString();
			}
		}
		textControl.setText(s);
		this.fallbackValue = s;
	}

	public Widget getWidget() {
		return textControl.getWidget();
	}

	public void notifyChange() {
		final ValueChangeEvent<String> changeEvent = new ValueChangeEvent<String>(this, null, this.textControl
				.getText());

		for (ValueChangeListener listener : listeners) {
			listener.valueChange(changeEvent);
		}
	}

	public Format getDisplayFormat() {
		return displayFormat;
	}

	public void setDisplayFormat(Format displayFormat) {
		this.displayFormat = displayFormat;
	}

	private void onModifyText(ModifyEvent modifyEv) {
		if (!this.checkFormat || this.checkFormat()) {
			this.notifyChange();
		}
	}

	private boolean checkFormat() {
		if (this.format != null) {
			try {
				this.format.parseObject(textControl.getText());
				textControl.setInputError(false);
				fallbackValue = textControl.getText();
			} catch (ParseException e) {
				textControl.setInputError(true);
				return false;
			}
		}
		return true;
	}

	private void applyFormat() {
		if (this.displayFormat != null) {
			final Object value = this.getValue();

			if (value != null) {
				final String prevText = textControl.getText();
				final String newText = this.displayFormat.format(value);

				if (!prevText.equals(newText)) {
					textControl.setText(newText);
				}
			}
		}
	}

	private void init(Control control) {
		this.textControl = this.createTextControl(control);

		listeners = new ArrayList<ValueChangeListener>();

		modifyListener = new ModifyListener() {
			public void modifyText(ModifyEvent ev) {
				onModifyText(ev);
			}
		};

		this.textControl.getWidget().addFocusListener(new FocusListener() {
			public void focusGained(FocusEvent e) {
			}

			public void focusLost(FocusEvent e) {
				if (textControl.getInputError()) {
					textControl.setText(fallbackValue);
				}
				applyFormat();
			}
		});
	}

	private TextControl createTextControl(Control control) {
		if (control instanceof Text) {
			return new TextControlImpl((Text) control);
		}

		if (control instanceof Label) {
			return new LabelTextControl((Label) control);
		}

		throw new IllegalArgumentException("Tipio de control no soportado");
	}

	private static interface TextControl {
		void setText(String text);

		void setInputError(boolean b);

		boolean getInputError();

		String getText();

		void addModifyListener(ModifyListener listener);

		void removeModifyListener(ModifyListener listener);

		Control getWidget();
	}

	private static class TextControlImpl implements TextControl {
		public TextControlImpl(Text textWidget) {
			this.widget = textWidget;
		}

		public void addModifyListener(ModifyListener listener) {
			widget.addModifyListener(listener);
		}

		public String getText() {
			return widget.getText();
		}

		public void removeModifyListener(ModifyListener listener) {
			widget.removeModifyListener(listener);
		}

		public void setText(String text) {
			if (text == null) {
				text = "";
			}
			widget.setText(text);
		}

		public Control getWidget() {
			return widget;
		}

		public void setInputError(boolean b) {
			this.inputError = b;

			final int color = b ? SWT.COLOR_RED : SWT.COLOR_WIDGET_FOREGROUND;
			widget.setForeground(widget.getDisplay().getSystemColor(color));
		}

		public boolean getInputError() {
			return this.inputError;
		}

		private Text widget;

		private boolean inputError;
	}

	private static class LabelTextControl implements TextControl {
		public LabelTextControl(Label widget) {
			this.widget = widget;
		}

		public void addModifyListener(ModifyListener listener) {
		}

		public String getText() {
			return widget.getText();
		}

		public void removeModifyListener(ModifyListener listener) {
		}

		public void setText(String text) {
			if (text == null) {
				text = "";
			}
			widget.setText(text);
		}

		public Control getWidget() {
			return widget;
		}

		public void setInputError(boolean b) {
		}

		public boolean getInputError() {
			return false;
		}

		private Label widget;
	}

	private ArrayList<ValueChangeListener> listeners;

	private ModifyListener modifyListener;

	private TextControl textControl;

	private Format format;

	private TextFormatter formatter;

	private boolean checkFormat;

	private Format displayFormat;

	private String fallbackValue = "";

	private static EditType<String> editType = new EditType<String>(String.class);

}