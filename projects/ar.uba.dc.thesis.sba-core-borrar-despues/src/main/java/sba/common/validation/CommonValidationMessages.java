/*
* Licencia de Caja de Valores S.A., Versi�n 1.0
*
* Copyright (c) 2006 Caja de Valores S.A.
* 25 de Mayo 362, Ciudad Aut�noma de Buenos Aires, Rep�blica Argentina
* Todos los derechos reservados.
*
* Este software es informaci�n confidencial y propietaria de Caja de Valores S.A. ("Informaci�n
* Confidencial"). Usted no divulgar� tal Informaci�n Confidencial y la usar� solamente de acuerdo a
* los t�rminos del acuerdo de licencia que posee con Caja de Valores S.A.
*/

/*
* $Id: CommonValidationMessages.java,v 1.10 2008/05/15 19:25:40 cvsmvera Exp $
*/

package sba.common.validation;

import java.util.Properties;

import sba.common.properties.EnumPropertiesHelper;
import sba.common.properties.EnumProperty;

/**
 * @author Jonathan Chiocchio
 * @version $Revision: 1.10 $ $Date: 2008/05/15 19:25:40 $
 */

public enum CommonValidationMessages implements EnumProperty {
	
	NOT_NULL,
	NOT_ALPHA,
	NOT_ALPHANUMERIC,
	NOT_ALPHANUMERIC_SPACE,
	NOT_NUMERIC,
	NOT_CUIT,
	BETWEEN,
	STRING_SIZE,
	STRING_SIZE_EXACTLY,
	LESS_THAN,
	LESS_THAN_OR_EQUAL,
	BEFORE_THAN,
	BEFORE_THAN_OR_EQUALS,
	BEFORE_THAN_OR_EQUALS_FOR_CONCEPT,
	AFTER_THAN,
	AFTER_THAN_OR_EQUALS,
	PREEXISTENT_CUIT,
	INVALID_CUIT,
	INVALID_CUIL,
	INVALID_EMAIL,
	IS_NOT_ARGENTINA,
	COULD_NOT_BE_ARGENTINA,
	GREATER_THAN,
	GREATER_THAN_OR_EQUAL,
	VALID_CHAR_CONSTRAINT,
	INVALID_CHAR_CONSTRAINT,
	STRING_MIN_SIZE_CONSTRAINT,
	STRING_MAX_SIZE_CONSTRAINT,
	STRING_RANGE_SIZE_CONSTRAINT,
	NOT_ZERO;
	
	@Override
	public String toString() {
		return EnumPropertiesHelper.getString(props, this.name());
	}

	public String toString(Object... reemplazos) {
		return EnumPropertiesHelper.getString(props, this.name(), reemplazos);
	}
	
	private static Properties props = EnumPropertiesHelper.load("/CommonValidationMessages.properties");
}