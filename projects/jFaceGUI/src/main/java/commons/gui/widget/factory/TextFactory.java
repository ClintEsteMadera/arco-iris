package commons.gui.widget.factory;

import java.text.Format;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Text;

import commons.gui.GuiStyle;
import commons.gui.model.ComplexModel;
import commons.gui.model.types.DefaultFormat;
import commons.gui.model.types.EditConfiguration;
import commons.gui.model.types.EditConfigurationManager;
import commons.gui.model.types.EditType;
import commons.gui.model.validation.ValidationManager;
import commons.gui.util.ListenerHelper;
import commons.gui.util.PageHelper;
import commons.gui.widget.DefaultLayoutFactory;
import commons.gui.widget.composite.SimpleComposite;
import commons.gui.widget.creation.TextFieldListenerType;
import commons.gui.widget.creation.binding.Binding;
import commons.gui.widget.creation.binding.FakeBinding;
import commons.gui.widget.creation.metainfo.TextFieldMetainfo;
import commons.properties.CommonLabels;
import commons.properties.EnumProperty;
import commons.utils.SbaStringUtils;

public final class TextFactory {

	/**
	 * This class is not meant to be instantiated.
	 */
	private TextFactory() {
		super();
	}

	public static Control createText(TextFieldMetainfo textMetainfo) {
		Control text = createTextField(textMetainfo);
		textMetainfo.restoreDefaults();
		return text;
	}

	public static <T> Control createNumeroDocumentoField(Composite composite, EnumProperty enumProperty,
			Binding binding, boolean readOnly) {
		Control result;
		if (readOnly) {
			String value = SbaStringUtils.formatDniValue(binding.getValue());
			result = LabelFactory.createReadOnlyField(composite, new FakeBinding(value), enumProperty);
		} else {
			TextFieldMetainfo metainfo = TextFieldMetainfo.create(composite, enumProperty, binding, readOnly);
			metainfo.maxLength = 8;
			result = TextFactory.createTextField(metainfo);
		}
		return result;
	}

	/**
	 * Crea un campo de texto a partir de la meta informción brindada.
	 * 
	 * @param metainfo
	 *            : meta información de un campo de texto
	 * @return el campo de texto creado, puede ser un <code>Label</code> si es de solo lectura o un <code>Text</code> si
	 *         es de lectura escritura.
	 */
	private static <T> Control createTextField(final TextFieldMetainfo metainfo) {
		Control result = null;
		if (metainfo.readOnly && metainfo.readOnlyStyle == TextFieldMetainfo.READONLY_STYLE_LABEL) {
			if (metainfo.suffix != null) {
				SimpleComposite comp = new SimpleComposite(metainfo.composite, metainfo.readOnly, 3, 2);
				result = LabelFactory.createReadOnlyField(comp, metainfo.binding, metainfo.label);
				LabelFactory.createLabel(comp, metainfo.suffix, false, false); // suffix
			} else {
				result = LabelFactory.createReadOnlyField(metainfo.composite, metainfo.binding, metainfo.label);
			}
		} else {
			Text textBox;
			// permite crear caja de texto sin label
			if (!metainfo.label.equals(CommonLabels.NO_LABEL)) {
				LabelFactory.createLabel(metainfo.composite, metainfo.label, false, true);

				if (metainfo.suffix != null) {
					Composite originalComposite = metainfo.composite;

					metainfo.composite = new SimpleComposite(metainfo.composite, metainfo.readOnly, 2);

					textBox = createTextBox(metainfo);

					GridData gridTextBox = (GridData) textBox.getLayoutData();
					int gridFlags = GridData.VERTICAL_ALIGN_BEGINNING;

					if (gridTextBox.horizontalAlignment == SWT.FILL) {
						gridFlags |= GridData.FILL_HORIZONTAL;
					}
					if (gridTextBox.grabExcessHorizontalSpace) {
						gridFlags |= GridData.GRAB_HORIZONTAL;
					}
					metainfo.composite.setLayoutData(new GridData(gridFlags));

					LabelFactory.createLabel(metainfo.composite, metainfo.suffix, false, false);
					// restituyo el composite original para posteriores usos
					metainfo.composite = originalComposite;
				} else {
					textBox = createTextBox(metainfo);
				}

			} else {
				textBox = createTextBox(metainfo);
				if (metainfo.suffix != null) {
					LabelFactory.createLabel(metainfo.composite, metainfo.suffix, false, false);
				}
			}

			metainfo.binding.bind(textBox);

			addListeners(textBox, metainfo);
			result = textBox;
		}
		return result;
	}

	@SuppressWarnings("unchecked")
	private static int getVisibleSize(final Text textField, final TextFieldMetainfo metainfo) {
		int visibleSize = -1;

		if (metainfo.binding == null || metainfo.binding.isFakeBinding()) {
			return visibleSize;
		}

		final ComplexModel model = metainfo.binding.getCompositeModel();
		final Object propertyName = metainfo.binding.getPropertyName();
		final EditType editType = model.getValueType(propertyName);

		Format format = EditConfigurationManager.getInstance().getFormat(editType);

		if (format == null) {
			format = DefaultFormat.getInstance();
		}

		if (format != null) {
			final Object proto = EditConfigurationManager.getInstance().getPrototype(editType);

			if (proto != null) {
				final String protoText = format.format(proto);
				visibleSize = protoText.length();
			}
		}

		return visibleSize;
	}

	@SuppressWarnings("unchecked")
	private static int getAlignment(final TextFieldMetainfo metainfo) {

		if (metainfo.binding != null && !metainfo.binding.isFakeBinding()) {
			final ComplexModel model = metainfo.binding.getCompositeModel();
			final Object propertyName = metainfo.binding.getPropertyName();
			final EditType editType = model.getValueType(propertyName);

			EditConfiguration cfg = EditConfigurationManager.getInstance().getConfiguration(editType);

			if (cfg != null && cfg.isRightAligned()) {
				return SWT.RIGHT;
			}
		}
		return SWT.LEFT;
	}

	private static Text createTextBox(final TextFieldMetainfo metainfo) {

		Control control = null;
		Text text = null;

		if (!metainfo.validate) {
			text = _createTextBox(metainfo);
			control = text;
		} else {

			final Composite parent = metainfo.composite;

			final Composite composite = new Composite(parent, SWT.NONE);
			DefaultLayoutFactory.setDefaultGridLayout(composite, 2);
			composite.setLayoutData(null);
			metainfo.composite = composite;

			text = _createTextBox(metainfo);

			metainfo.composite = parent;

			LabelStatusAware status = new LabelStatusAware(composite);
			ValidationManager.setValidationProperty(text, metainfo.binding);
			ValidationManager.setValidationStatus(text, status);

			control = composite;
		}

		if (metainfo.layoutData != null) {
			control.setLayoutData(metainfo.layoutData);
		}

		return text;
	}

	private static Text _createTextBox(final TextFieldMetainfo metainfo) {
		int style = GuiStyle.DEFAULT_TEXTBOX_STYLE;
		int horizontalAlignment = SWT.BEGINNING;
		int verticalAlignment = SWT.BEGINNING;
		boolean grabExcessHorizontalSpace = true;
		boolean grabExcessVerticalSpace = false;
		int widthHint = SWT.DEFAULT;
		int heightHint = SWT.DEFAULT;

		if (metainfo.password) {
			style = GuiStyle.PASSWORD_TEXTBOX_STYLE;
		}

		if (metainfo.multiline) {
			style = GuiStyle.DEFAULT_MULTILINE_TEXTBOX_STYLE;
			horizontalAlignment = SWT.FILL;
			verticalAlignment = SWT.FILL;
			heightHint = PageHelper.getMinimunCharHeight() * metainfo.multiline_lines;
		}

		if (metainfo.readOnly && metainfo.readOnlyStyle == TextFieldMetainfo.READONLY_STYLE_TEXT) {
			style |= (SWT.READ_ONLY);
		}

		style |= getAlignment(metainfo);

		final Text textBox = new Text(metainfo.composite, style);

		if (metainfo.readOnly && metainfo.readOnlyStyle == TextFieldMetainfo.READONLY_STYLE_DISABLED_TEXT) {
			textBox.setEnabled(false);
		}

		if (metainfo.maxLength == null) {
			if (metainfo.visibleSize != null) {
				widthHint = PageHelper.getCantidadDePixels(Math.min(metainfo.visibleSize,
						TextFieldMetainfo.DEFAULT_VISIBLE_SIZE));
			} else {
				// me fijo si puedo inferir el tamaño visible del tipo de dato
				int typeVisibleSize = getVisibleSize(textBox, metainfo);
				if (typeVisibleSize >= 0) {
					widthHint = PageHelper.getCantidadDePixels(Math.min(typeVisibleSize,
							TextFieldMetainfo.DEFAULT_VISIBLE_SIZE));
					grabExcessHorizontalSpace = false;
				} else {
					horizontalAlignment = SWT.FILL;
					widthHint = PageHelper.getCantidadDePixels(TextFieldMetainfo.DEFAULT_VISIBLE_SIZE);
				}
			}
		} else {
			textBox.setTextLimit(metainfo.maxLength);
			if (metainfo.visibleSize != null) {
				widthHint = PageHelper.getCantidadDePixels(Math.min(metainfo.visibleSize,
						TextFieldMetainfo.DEFAULT_VISIBLE_SIZE));
			} else {
				// se acota el ancho para que no supere a MAX_FIXED_LENGTH
				widthHint = PageHelper.getCantidadDePixels(Math.min(metainfo.maxLength, MAX_FIXED_LENGTH));
			}
		}

		// NOTA: incremento la cantidad de pixeles porque la cantidad original es un "promedio"
		if (widthHint != SWT.DEFAULT) {
			widthHint = Math.max((int) (widthHint * 1.1), widthHint + PageHelper.getCantidadDePixels(3));
		}

		if (metainfo.composite.getLayout() instanceof GridLayout) {
			GridData gridData = new GridData(horizontalAlignment, verticalAlignment, grabExcessHorizontalSpace,
					grabExcessVerticalSpace);
			gridData.widthHint = widthHint;
			gridData.heightHint = heightHint;
			gridData.minimumHeight = PageHelper.getMinimunCharHeight();
			textBox.setLayoutData(gridData);
		} else {
			log.warn("No se ajustará el tamaño del campo para el atributo " + metainfo.label
					+ " ya que solo esta implementado para layouts del tipo GridData");
		}
		return textBox;
	}

	private static void addListeners(Text textBox, TextFieldMetainfo metainfo) {
		addCommonListeners(textBox, metainfo);
		if (metainfo.listeners != null) {
			for (TextFieldListenerType listener : metainfo.listeners) {
				listener.addListener(textBox);
			}
		}
	}

	private static void addCommonListeners(Text textBox, TextFieldMetainfo metainfo) {
		if (metainfo.multiline) {
			// Proceso los eventos que genera la tecla "Tab" en los campos de texto multilíneas
			ListenerHelper.addMultilineFieldTabListener(textBox);
		}
		ListenerHelper.addFocusListener(textBox);
		ListenerHelper.addNotAsciiPrintableCharacterListener(textBox);
	}

	private static final int MAX_FIXED_LENGTH = 60;

	private static final Log log = LogFactory.getLog(TextFactory.class);
}