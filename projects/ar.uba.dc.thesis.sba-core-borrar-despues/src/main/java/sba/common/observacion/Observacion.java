 /*
 * $Id: Observacion.java,v 1.1 2007/10/02 20:56:02 cvschioc Exp $
 *
 * Copyright (c) 2003 Caja de Valores S.A.
 * 25 de Mayo 362, Buenos Aires, Rep�blica Argentina.
 * Todos los derechos reservados.
 *
 * Este software es informaci�n confidencial y propietaria de Caja de Valores
 * S.A. ("Informaci�n Confidencial"). Usted no divulgar� tal Informaci�n
 * Confidencial y solamente la usar� conforme a los terminos del Acuerdo que Ud.
 * posee con Caja de Valores S.A.
 */
package sba.common.observacion;

import java.util.Calendar;

import sba.common.persistence.BasePersistentObject;

/**
 * Modela una observaci�n sobre un concepto. Estos pueden ser Cliente, Cuenta o Tr�mite
 *
 * @author Miguel D�az
 * @version $Revision: 1.1 $ - $Date: 2007/10/02 20:56:02 $
 */
public class Observacion extends BasePersistentObject{

	public Observacion(String descripcion) {
		super();
		if(descripcion == null){
			this.descripcion = "";
		} else {
			this.descripcion = descripcion;
		}
		//Es seteado en el backend
		this.fecha = null;
		this.usuario = null;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public Calendar getFecha() {
		return fecha;
	}

	public void setFecha(Calendar fecha) {
		this.fecha = fecha;
	}
	
	public String getUsuario() {
		return usuario;
	}

	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}

	protected Observacion() {
		super();
	}

	private static final long serialVersionUID = -3975881158611233246L;
	
	private Calendar fecha;
	private String descripcion;
	private String usuario;
}

