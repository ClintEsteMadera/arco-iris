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
 * $Id: Empleado.java,v 1.3 2008/05/14 17:34:59 cvspasto Exp $
 */

package sba.common.anotaciones.domain;

import sba.common.annotations.PropertyDescription;
import sba.common.annotations.ValidateRequired;
import sba.common.anotaciones.main.ValidateTipoEmpleado;

/**
 * 
 * @author Pablo Pastorino
 * @version $Revision: 1.3 $ $Date: 2008/05/14 17:34:59 $
 */

public class Empleado {

    @PropertyDescription(value="APELLIDO")
    @ValidateRequired
	public String getApellido() {
		return apellido;
	}

	public void setApellido(String apellido) {
		this.apellido = apellido;
	}

    @PropertyDescription(value="NOMBRE")
    @ValidateRequired
	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	
	public boolean isEmpty(){
		return (this.apellido == null || this.apellido.length() == 0) && 
			(this.nombre == null || this.nombre.length() == 0);
	}
	
	@ValidateTipoEmpleado
	public String getTipoEmpleado() {
		return tipoEmpleado;
	}

	public void setTipoEmpleado(String tipoEmpleado) {
		this.tipoEmpleado = tipoEmpleado;
	}

	@PropertyDescription(value="DOCUMENTO")
	public String getDocumento() {
		return documento;
	}

	public void setDocumento(String documento) {
		this.documento = documento;
	}
	
	private String apellido;

	private String nombre;
	
	private String tipoEmpleado;

	private String documento;
}
