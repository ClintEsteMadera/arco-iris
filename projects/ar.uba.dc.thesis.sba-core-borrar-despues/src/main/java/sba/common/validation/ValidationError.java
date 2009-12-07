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
 * $Id: ValidationError.java,v 1.1 2007/12/21 19:21:56 cvschioc Exp $
 */

package sba.common.validation;

import java.io.Serializable;

import org.springframework.util.Assert;

import sba.common.properties.EnumProperty;

/**
 * @author Pablo Pastorino
 * @version $Revision: 1.1 $ $Date: 2007/12/21 19:21:56 $
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
