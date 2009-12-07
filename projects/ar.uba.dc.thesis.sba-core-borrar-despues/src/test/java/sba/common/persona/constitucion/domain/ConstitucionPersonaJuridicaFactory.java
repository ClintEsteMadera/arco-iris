/*
 * $Id: ConstitucionPersonaJuridicaFactory.java,v 1.2 2007/11/30 17:53:42 cvsmarco Exp $
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
package sba.common.persona.constitucion.domain;

import java.util.Calendar;
import java.util.GregorianCalendar;

import sba.common.persona.core.ConstitucionPersonaJuridica;

/**
 * @version $Revision: 1.2 $ - $Date: 2007/11/30 17:53:42 $
 * @author Fernando Scanavino
 */
public abstract class ConstitucionPersonaJuridicaFactory {

	public static ConstitucionPersonaJuridica construirConstitucionPersonaJuridica() {
		Calendar fechaDeConstitucion = fechaDeConstitucionValida();
		Integer duracion = duracionValida();
		return new ConstitucionPersonaJuridica(fechaDeConstitucion, null,duracion);
	}

	private static Calendar fechaDeConstitucionValida() {
		return new GregorianCalendar(2006, 0, 2);
	}

	private static Integer duracionValida() {
		return new Integer(99);
	}
}