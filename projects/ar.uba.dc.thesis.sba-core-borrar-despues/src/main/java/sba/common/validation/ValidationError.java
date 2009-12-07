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
