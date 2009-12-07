/*
 * Licencia de Caja de Valores S.A., Versión 1.0
 *
 * Copyright (c) 2006 Caja de Valores S.A.
 * 25 de Mayo 362, Ciudad Autónoma de Buenos Aires, República Argentina
 * Todos los derechos reservados.
 *
 * Este software es información confidencial y propietaria de Caja de Valores S.A. ("Información
 * Confidencial"). Usted no divulgará tal Información Confidencial y la usará solamente de acuerdo a
 * los términos del acuerdo de licencia que posee con Caja de Valores S.A.
 */

/*
 * $Id: ValidationException.java,v 1.2 2007/12/21 20:07:09 cvschioc Exp $
 */
package sba.common.exception;

import java.util.List;

import org.springframework.util.Assert;

import sba.common.properties.FakeEnumProperty;
import sba.common.validation.ValidationError;

/**
 * Una excepción que encapsula una lista de errores de validación.<br>
 * @author Jonathan Chiocchio
 * @version $Revision: 1.2 $ - $Date: 2007/12/21 20:07:09 $
 */
public class ValidationException extends ServiceException {

	public ValidationException(List<ValidationError> validationErrors) {
		super(null);
		Assert.notNull(validationErrors, "La lista de errores de validación no puede ser nula");
		this.validationErrors = validationErrors.toArray(new ValidationError[validationErrors.size()]);
	}

	public ValidationException(ValidationError... validationErrors) {
		super(null);
		Assert.notNull(validationErrors, "La lista de errores de validación no puede ser nula");
		this.validationErrors = validationErrors;
	}

	public ValidationException(Throwable cause) {
		super(cause.getMessage(), cause);
		FakeEnumProperty message = new FakeEnumProperty(cause.getMessage());
		this.validationErrors = new ValidationError[]{new ValidationError(message)};
	}
	
	public String[] getValidationMessages() {
		String[] messages = new String[this.validationErrors.length];
		for (int i = 0; i < messages.length; i++) {
			messages[i] = this.validationErrors[i].getMessage();
		}
		return messages;
	}

	/**
	 * Devuelve todos los mensajes de validaciones, uno por cada renglón.
	 * @return lista de los mensajes de validaciones, separados por un <code>Enter</code>
	 */
	@Override
	public String getMessage() {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < this.validationErrors.length; i++) {
			sb.append(this.validationErrors[i].getMessage());
			if (i + 1 != this.validationErrors.length) {
				sb.append(LINE_SEPARATOR);
			}
		}
		return sb.toString();
	}

	public ValidationError[] getErrors() {
		return validationErrors;
	}

	private ValidationError[] validationErrors;

	private static final long serialVersionUID = 1L;

	private static final String LINE_SEPARATOR = System.getProperty("line.separator", "\n");
}