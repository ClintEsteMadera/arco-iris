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
 * $Id: StringConstraints.java,v 1.7 2008/04/21 18:53:30 cvschioc Exp $
 */

package sba.common.validation;

import sba.common.validation.string.CharacterSet;

/**
 * Restricciones para tipo String
 * 
 * @author Pablo Pastorino
 * @version $Revision: 1.7 $ $Date: 2008/04/21 18:53:30 $
 */

public class StringConstraints implements Validator<String> {

	public StringConstraints(int maxLength, CharacterSet validChars, CharacterSet invalidChars,
			boolean alwaysUppercase) {
		super();
		this.maxLength = maxLength;
		this.validChars = validChars;
		this.invalidChars = invalidChars;
		this.alwaysUppercase = alwaysUppercase;
	}

	public StringConstraints(int maxLength, CharacterSet validChars, CharacterSet invalidChars) {
		this(maxLength, validChars, invalidChars, false);
	}

	public StringConstraints(int minLength, int maxLength, CharacterSet validChars) {
		super();
		this.minLength = minLength;
		this.maxLength = maxLength;
		this.validChars = validChars;
	}

	public StringConstraints(int maxLength, CharacterSet validChars) {
		super();
		this.maxLength = maxLength;
		this.validChars = validChars;
	}

	public StringConstraints(int maxLength) {
		super();
		this.maxLength = maxLength;
	}

	public boolean validate(String s, ValidationResult result) {
		boolean valid = true;

		if (!thereAreValidChars(s)) {
			result.addError(CommonValidationMessages.VALID_CHAR_CONSTRAINT, result
					.getValueDescription(), validChars.getName());
			valid = false;
		} else if (thereAreInvalidChars(s)) {
			result.addError(CommonValidationMessages.INVALID_CHAR_CONSTRAINT, result
					.getValueDescription(), invalidChars.getName());
			valid = false;
		} else {
			// Si están ambos límites definidos
			if (estaDefinidoTamanioMinimo() && estaDefinidoTamanioMaximo()
					&& (s.length() < this.minLength || s.length() > this.maxLength)) {
				if (this.minLength == this.maxLength) {
					result.addError(CommonValidationMessages.STRING_SIZE_EXACTLY, result
							.getValueDescription(), this.minLength);
					valid = false;
				} else {
					result.addError(CommonValidationMessages.STRING_RANGE_SIZE_CONSTRAINT, result
							.getValueDescription(), this.minLength, this.maxLength);
					valid = false;
				}
			} else {
				if (estaDefinidoTamanioMinimo() && s.length() < this.minLength) {
					result.addError(CommonValidationMessages.STRING_MIN_SIZE_CONSTRAINT, result
							.getValueDescription(), this.minLength);
					valid = false;
				}
				if (estaDefinidoTamanioMaximo() && s.length() > this.maxLength) {
					result.addError(CommonValidationMessages.STRING_MAX_SIZE_CONSTRAINT, result
							.getValueDescription(), this.maxLength);
					valid = false;
				}
			}
		}

		return valid;
	}

	public CharacterSet getInvalidChars() {
		return invalidChars;
	}

	public int getMaxLength() {
		return maxLength;
	}

	public CharacterSet getValidChars() {
		return validChars;
	}

	public boolean isAlwaysUppercase() {
		return alwaysUppercase;
	}

	public void setAlwaysUppercase(boolean alwaysUppercase) {
		this.alwaysUppercase = alwaysUppercase;
	}

	public boolean isValidChar(char c) {
		return validChars == null || validChars.isMember(c);
	}

	public boolean isInvalidChar(char c) {
		return invalidChars != null && invalidChars.isMember(c);
	}

	public boolean thereAreValidChars(String s) {
		if (validChars == null) {
			return true;
		}
		for (int i = 0; i < s.length(); i++) {
			if (!validChars.isMember(s.charAt(i))) {
				return false;
			}
		}
		return true;
	}

	public boolean thereAreInvalidChars(String s) {
		if (invalidChars == null) {
			return false;
		}

		for (int i = 0; i < s.length(); i++) {
			if (invalidChars.isMember(s.charAt(i))) {
				return true;
			}
		}
		return false;
	}

	private boolean estaDefinidoTamanioMaximo() {
		return this.maxLength >= 0;
	}

	private boolean estaDefinidoTamanioMinimo() {
		return this.minLength >= 0;
	}

	private int minLength = -1;

	private int maxLength = -1;

	private CharacterSet validChars;

	private CharacterSet invalidChars;

	private boolean alwaysUppercase;
}