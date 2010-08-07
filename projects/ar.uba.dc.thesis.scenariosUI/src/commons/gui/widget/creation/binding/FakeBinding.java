package commons.gui.widget.creation.binding;

import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import commons.gui.model.bean.BeanModel;
import commons.gui.model.binding.ValueBinding;
import commons.properties.EnumProperty;

public class FakeBinding implements Binding {

	/**
	 * Constructor que tomar un EnumProperty e invoca al toString() de dicho objeto para obtener el valor que luego
	 * retornar� el m�todo <code>getValue()</code>
	 * 
	 * @param label
	 *            una propiedad enumerada. No puede ser nula.
	 * @throws IllegalArgumentException
	 *             si el label es nulo.
	 */
	public FakeBinding(EnumProperty label) {
		super();
		if (label == null) {
			throw new IllegalArgumentException("El par�metro label no puede ser nulo");
		}
		this.value = label.toString();
	}

	/**
	 * Constructor que recibe un texto libre. En los casos donde se pueda elegir, se prefiere usar el constructor que
	 * recibe un EnumProperty por cuestiones de elegancia y mantenibilidad.
	 * 
	 * @param freeText
	 *            texto libre, no puede ser nulo.
	 * @throws IllegalArgumentException
	 *             si el freeText es nulo.
	 */
	public FakeBinding(String freeText) {
		super();
		if (freeText == null) {
			throw new IllegalArgumentException("El par�metro freeText no puede ser nulo");
		}
		this.value = freeText;
	}

	public boolean isFakeBinding() {
		return true;
	}

	public String getValue() {
		return value;
	}

	public ValueBinding bind(Object component) {
		if (component instanceof Text) {
			((Text) component).setText(this.value);
		}

		if (component instanceof Label) {
			((Label) component).setText(this.value);
		}
		return null;
	}

	public BeanModel getCompositeModel() {
		throw new UnsupportedOperationException(
				"No se deber�a invocar al m�todo getCompositeModel() de un FakeBinding!");
	}

	public Object getModel() {
		throw new UnsupportedOperationException("No se deber�a invocar al m�todo getModel() de un FakeBinding!");
	}

	public String getPropertyName() {
		throw new UnsupportedOperationException("No se deber�a invocar al m�todo getPropertyName() de un FakeBinding!");
	}

	private final String value;
}