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
 * $Id: TipoIngresosBrutos.java,v 1.1 2008/04/01 16:41:59 cvschioc Exp $
 */

package sba.common.persona.identificacion;

/**
 * Modela el tipo de Ingresos Brutos.
 * 
 * @author Jonathan Chiocchio
 * @version $Revision: 1.1 $ $Date: 2008/04/01 16:41:59 $
 */

public enum TipoIngresosBrutos {

	NO_ESPECIFICADO("No Especificado"),

	INGRESOS_BRUTOS("Ingresos Brutos"),

	CONVENIO_MULTILATERAL("Convenio Multilateral");

	private TipoIngresosBrutos(String denominacion) {
		this.denominacion = denominacion;
	}

	@Override
	public String toString() {
		return this.denominacion;
	}

	private String denominacion;
}