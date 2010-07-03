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
 * $Id: SimpleValidationRegistry.java,v 1.2 2008/03/18 20:29:24 cvspasto Exp $
 */

package commons.validation;

/**
 * Registry de validadores "simples"
 * @author Pablo Pastorino
 * @version $Revision: 1.2 $ $Date: 2008/03/18 20:29:24 $
 */

public class SimpleValidationRegistry<T> extends ValidationRegistry<Validator<T>> implements
		Validator<T> {

	public boolean validate(T objectToValidate, ValidationResult result) {
		return this.getValidatorFor(objectToValidate.getClass()).validate(objectToValidate, result);
	}

	public ValidationResult validate(T objectToValidate) {
		ValidationResult result = new ValidationResult();
		validate(objectToValidate, result);
		return result;
	}

}
