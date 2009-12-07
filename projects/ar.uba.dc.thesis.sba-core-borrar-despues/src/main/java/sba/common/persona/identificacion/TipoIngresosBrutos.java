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