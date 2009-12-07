/*
 * $Id: FakeBinding.java,v 1.14 2008/01/28 17:00:03 cvschioc Exp $
 *
 * Copyright (c) 2003 Caja de Valores S.A.
 * 25 de Mayo 362, Buenos Aires, República Argentina.
 * Todos los derechos reservados.
 *
 * Este software es información confidencial y propietaria de Caja de Valores
 * S.A. ("Información Confidencial"). Usted no divulgará tal Información
 * Confidencial y solamente la usará conforme a los terminos del Acuerdo que Ud.
 * posee con Caja de Valores S.A.
 */

package commons.gui.widget.creation.binding;

import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import sba.common.properties.EnumProperty;

import commons.gui.model.bean.BeanModel;
import commons.gui.model.binding.ValueBinding;

/**
 * @author Jonathan Chiocchio
 * @version $Revision: 1.14 $ $Date: 2008/01/28 17:00:03 $
 */

public class FakeBinding implements Binding {
	
	/**
	 * Constructor que tomar un EnumProperty e invoca al toString() de dicho objeto para obtener el
	 * valor que luego retornará el método <code>getValue()</code>
	 * @param label
	 *            una propiedad enumerada. No puede ser nula.
	 * @throws IllegalArgumentException
	 *             si el label es nulo.
	 */
	public FakeBinding(EnumProperty label) {
		super();
		if(label == null) {
			throw new IllegalArgumentException("El parámetro label no puede ser nulo");
		}
		this.value = label.toString();
	}

	/**
	 * Constructor que recibe un texto libre. En los casos donde se pueda elegir, se prefiere usar
	 * el constructor que recibe un EnumProperty por cuestiones de elegancia y mantenibilidad.
	 * @param freeText
	 *            texto libre, no puede ser nulo.
	 * @throws IllegalArgumentException
	 *             si el freeText es nulo.
	 */
	public FakeBinding(String freeText) {
		super();
		if(freeText == null) {
			throw new IllegalArgumentException("El parámetro freeText no puede ser nulo");
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
		if(component instanceof Text) {
			((Text)component).setText(this.value);
		}
		
		if(component instanceof Label) {
			((Label)component).setText(this.value);
		}
		return null;
	}

	public BeanModel getCompositeModel() {
		throw new UnsupportedOperationException(
				"No se debería invocar al método getCompositeModel() de un FakeBinding!");
	}

	public Object getModel() {
		throw new UnsupportedOperationException(
				"No se debería invocar al método getModel() de un FakeBinding!");
	}

	public String getPropertyName() {
		throw new UnsupportedOperationException(
				"No se debería invocar al método getPropertyName() de un FakeBinding!");
	}
	
	private final String value;
}