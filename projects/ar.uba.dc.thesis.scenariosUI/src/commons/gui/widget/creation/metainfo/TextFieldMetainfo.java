package commons.gui.widget.creation.metainfo;

import java.util.HashSet;
import java.util.Set;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;

import sba.common.properties.EnumProperty;

import commons.gui.util.PageHelper;
import commons.gui.widget.creation.TextFieldListenerType;
import commons.gui.widget.creation.binding.Binding;

/**
 * Modela la meta-información necesaria para crear un Campo de Texto.<br>
 * Nota: gracias al uso del objeto "FakeBinding", notar que es posible crear un Text field sin
 * necesidad de atarlo a ningún modelo.
 * 
 * @author Gabriel Tursi
 */
public class TextFieldMetainfo extends ControlMetainfo {

	public static TextFieldMetainfo create(Composite composite, EnumProperty label,
			Binding binding, boolean readOnly) {
		instance.applyDefaults();
		ControlMetainfo.setValues(instance, composite, label, binding, readOnly);
		return instance;
	}

	public static TextFieldMetainfo create(Composite composite, EnumProperty label,
			Binding binding, int readOnlyStyle) {
		instance.applyDefaults();
		ControlMetainfo.setValues(instance, composite, label, binding,
				readOnlyStyle == READONLY_STYLE_LABEL);
		instance.readOnlyStyle = readOnlyStyle;
		instance.readOnly = true;
		return instance;
	}

	@Override
	public void restoreDefaults() {
		super.restoreDefaults();
		applyDefaults();
	}

	/**
	 * Agrega los listenes especificados a la caja de texto. Estos listeners son reseteados una vez
	 * creado el campo.
	 * 
	 * @param newListeners
	 */
	public TextFieldMetainfo addListeners(TextFieldListenerType... newListeners) {
		for (TextFieldListenerType listener : newListeners) {
			this.listeners.add(listener);
		}
		return this;
	}

	@Override
	public void applyDefaults() {
		super.applyDefaults();
		this.maxLength = null;
		this.visibleSize = null;
		this.multiline = false;
		this.multiline_lines = 3;
		this.password = false;
		this.listeners = new HashSet<TextFieldListenerType>();
		this.suffix = null;
		this.readOnlyStyle = READONLY_STYLE_LABEL;
	}

	private TextFieldMetainfo() {
		super();
		this.applyDefaults();
	}

	private static TextFieldMetainfo instance = new TextFieldMetainfo();

	/**
	 * Cantidad máxima de carácteres que permite este campo.
	 * 
	 * @see Text#setTextLimit(int)
	 */
	public Integer maxLength;

	/**
	 * Ancho del campo de texto en cantidad de letras.
	 * 
	 * @see PageHelper#getCantidadDePixels(int)
	 */
	public Integer visibleSize;

	public boolean multiline;

	public int readOnlyStyle;

	/**
	 * Especifica la cantidad de líneas que debe abarcar un campo multilínea.
	 */
	public int multiline_lines;

	// Se usa un conjunto para evitar que se apliquen listeners repetidos
	public Set<TextFieldListenerType> listeners;

	public boolean password;

	/**
	 * Permite agregar un Label como sufijo
	 */
	public EnumProperty suffix;

	public static final Integer DEFAULT_VISIBLE_SIZE = 60;

	public static final int READONLY_STYLE_EDITABLE = 0;

	public static final int READONLY_STYLE_LABEL = 2;

	public static final int READONLY_STYLE_TEXT = 3;

	public static final int READONLY_STYLE_DISABLED_TEXT = 4;

}