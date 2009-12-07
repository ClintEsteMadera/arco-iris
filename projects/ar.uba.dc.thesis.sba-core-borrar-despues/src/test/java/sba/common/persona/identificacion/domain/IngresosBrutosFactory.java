/*
 * $Id: IngresosBrutosFactory.java,v 1.2 2008/04/01 16:41:59 cvschioc Exp $
 *
 * Copyright (c) 2006 Caja de Valores S.A.
 * 25 de Mayo 362 Buenos Aires, República Argentina
 * Todos los derechos reservados.
 *
 * Este software es información confidencial y propietaria de
 * Caja de Valores S.A. ("Información Confidencial"). Usted no
 * divulgará tal Información Confidencial y la usará solamente
 * de acuerdo a los términos del acuerdo de licencia que Ud. posee
 * con Caja de Valores S.A.
 */
package sba.common.persona.identificacion.domain;

import sba.common.pais.CountrySubdivision_AR;
import sba.common.persona.identificacion.IngresosBrutos;
import sba.common.persona.identificacion.TipoIngresosBrutos;

/**
 * @version $Revision: 1.2 $ - $Date: 2008/04/01 16:41:59 $
 * @author Fernando Scanavino
 */
public abstract class IngresosBrutosFactory {

	public static IngresosBrutos construirIngresosBrutos() {
		IngresosBrutos inscripcion = null;
		String numero = IngresosBrutosFactory.numeroValido();
		inscripcion = new IngresosBrutos(TipoIngresosBrutos.INGRESOS_BRUTOS, numero,
				IngresosBrutosFactory.provinciaValida());
		return inscripcion;
	}

	private static String numeroValido() {
		return "123456789";
	}

	private static CountrySubdivision_AR provinciaValida() {
		return CountrySubdivision_AR.CABA;
	}
}