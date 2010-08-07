package commons.gui.model.validation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

import commons.validation.ValidationError;

/**
 * Wrapper para mostrar los errores de validación en un control visual
 * 
 * 
 * 
 */
public class ValidableComposite {

	public ValidableComposite(Composite parent) {
		this.parent = parent;
		this.controlMap = new HashMap<String, List<Control>>();
	}

	public void validate(ValidationError[] errors) {
		for (ValidationError e : errors) {
			setErrorStatus(e.getErrorLocation(), e.getMessage());
		}

		for (String p : this.controlMap.keySet()) {
			if (p != null && !isPropertyIncluded(p, errors)) {
				final List<Control> controls = this.controlMap.get(p);
				for (Control c : controls) {
					this.setOkStatus(c);
				}
			}
		}
		this.errors = errors;
	}

	public void setErrorStatus(String property, String message) {
		List<Control> controls = this.getControls(property);

		for (Control c : controls) {
			this.setErrorStatus(c, message);
		}
	}

	private void setErrorStatus(Control c, String message) {
		final ValidationStatusAware status = ValidationManager.getValidationStatus(c);
		if (status != null) {
			status.setErrorStatus(message);
		}
	}

	private void setOkStatus(Control c) {
		final ValidationStatusAware status = ValidationManager.getValidationStatus(c);
		if (status != null) {
			status.setOkStatus();
		}
	}

	private List<Control> getControls(String property) {
		List<Control> controls = this.controlMap.get(property);

		if (controls == null) {
			controls = new ArrayList<Control>();
			discoverControls(parent, property, controls);
			this.controlMap.put(property, controls);
		}
		return controls;
	}

	private void discoverControls(Control parent, String property, List<Control> controls) {
		if (property == null) {
			return;
		}
		String p = ValidationManager.getValidationProperty(parent);
		if (property.equals(p)) {
			controls.add(parent);
		}
		if (parent instanceof Composite) {
			Control[] childs = ((Composite) parent).getChildren();
			for (Control c : childs) {
				discoverControls(c, property, controls);
			}
		}
	}

	private boolean isPropertyIncluded(String property, ValidationError[] errors) {
		for (ValidationError e : errors) {
			if (e.getErrorLocation() != null && property.equals(e.getErrorLocation())) {
				return true;
			}
		}
		return false;
	}

	private HashMap<String, List<Control>> controlMap;

	private Composite parent;

	private ValidationError[] errors;

	public void refresh() {
		if (this.errors != null) {
			this.validate(this.errors);
		}
	}
}
