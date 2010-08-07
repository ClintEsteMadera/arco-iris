package commons.validation;

import java.io.Serializable;

import org.springframework.util.Assert;

import commons.properties.EnumProperty;

/**
 * 
 */

public class ValidationError implements Serializable {

	public ValidationError(EnumProperty message) {
		this(null, message);
	}

	public ValidationError(EnumProperty message, Object... reemplazos) {
		this(null, message, reemplazos);
	}

	public ValidationError(String errorLocation, EnumProperty message) {
		super();
		Assert.notNull(message, "El mensaje del error no puede ser un objeto nulo");
		this.errorLocation = errorLocation;
		this.message = message.toString();
	}

	public ValidationError(String errorLocation, EnumProperty message, Object... reemplazos) {
		super();
		Assert.notNull(message, "El mensaje del error no puede ser un objeto nulo");
		Assert.notNull(reemplazos, "Los reemplazos no pueden ser nulos");
		this.errorLocation = errorLocation;
		this.message = message.toString(reemplazos);
	}

	public String getErrorLocation() {
		return errorLocation;
	}

	public String getMessage() {
		return message;
	}

	@Override
	public String toString() {
		return this.getMessage();
	}

	private String message;

	private String errorLocation;

	private static final long serialVersionUID = 1L;

}
