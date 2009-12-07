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
 * $Id: RangoFechas.java,v 1.3 2008/04/23 17:39:14 cvsmarco Exp $
 */
package sba.common.datetime;

import java.io.Serializable;
import java.util.Calendar;

import sba.common.utils.DateUtils;
import sba.common.validation.CommonValidationMessages;
import sba.common.validation.ValidationResult;
import sba.common.validation.ValidationUtils;

/**
 * @author Jonathan Chiocchio
 * @version $Revision: 1.3 $ $Date: 2008/04/23 17:39:14 $
 */
public class RangoFechas implements Serializable {

	/**
	 * Crea un rango de fechas a partir de dos objetos Calendar. La cantidad de par�metros n�los es
	 * a lo sumo uno.
	 * 
	 * @param fechaHoraDesde
	 *            un calendar que determina el l�mite inferior del rango.
	 * @param fechaHoraHasta
	 *            un calendar que determina el l�mite superior del rango.
	 * @param considerTime
	 *            especifica si debe considerarse o no la hora del calendario.
	 */
	public RangoFechas(Calendar fechaHoraDesde, Calendar fechaHoraHasta, boolean considerTime) {
		super();
		this.considerTime = considerTime;
		this.setFechaHoraDesde(fechaHoraDesde);
		this.setFechaHoraHasta(fechaHoraHasta);
	}

	/**
	 * @return el l�mite inferior del rango de fechas.
	 */
	public Calendar getFechaHoraDesde() {
		return fechaHoraDesde;
	}

	/**
	 * @return el l�mite superior del rango de fechas.
	 */
	public Calendar getFechaHoraHasta() {
		return fechaHoraHasta;
	}

	/**
	 * @param fechaHoraDesde
	 *            el l�mite inferior del rango de fechas.
	 */
	public void setFechaHoraDesde(Calendar fechaHoraDesde) {
		if (!this.considerTime) {
			clearTimeIfNotNull(fechaHoraDesde);
		}
		this.fechaHoraDesde = fechaHoraDesde;
	}

	/**
	 * @param fechaHoraHasta
	 *            el l�mite superior del rango de fechas.
	 */
	public void setFechaHoraHasta(Calendar fechaHoraHasta) {
		if (!this.considerTime) {
			endOfDayIfNotNull(fechaHoraHasta);
		}
		this.fechaHoraHasta = fechaHoraHasta;
	}

	/**
	 * Decide si la fecha pasado por par�metro se encuentra en rango con la subyacente.
	 * 
	 * @param fechaHora
	 *            la fecha a chequear
	 * @return <code>true</code> si se encuentra en rango, <code>false</code> en caso contrario.
	 */
	public boolean estaEnRango(Calendar fechaHora) {
		boolean estaEnRango = true;
		if (this.fechaHoraDesde != null) {
			if (considerTime) {
				estaEnRango = estaEnRango && this.fechaHoraDesde.compareTo(fechaHora) <= 0;
			} else {
				estaEnRango = estaEnRango
						&& DateUtils.compareDateOnly(this.fechaHoraDesde, fechaHora) <= 0;
			}
		}

		if (this.fechaHoraHasta != null) {
			if (considerTime) {
				estaEnRango = estaEnRango && fechaHora.compareTo(this.fechaHoraHasta) >= 0;
			} else {
				estaEnRango = estaEnRango
						&& DateUtils.compareDateOnly(fechaHora, this.fechaHoraHasta) >= 0;
			}
		}
		return estaEnRango;
	}

	private static void clearTimeIfNotNull(Calendar fechaHora) {
		if (fechaHora != null) {
			DateUtils.clearTime(fechaHora);
		}
	}

	private static void endOfDayIfNotNull(Calendar fhHasta) {
		if (fhHasta != null) {
			DateUtils.endOfDay(fhHasta);
		}
	}

	public boolean validate(ValidationResult errors, String location) {
		boolean valid = true;
		if ((getFechaHoraDesde() != null) && (getFechaHoraHasta() != null)) {
			valid &= ValidationUtils.isValidCondition(errors, location, (getFechaHoraDesde()
					.compareTo(getFechaHoraHasta()) <= 0),
					CommonValidationMessages.BEFORE_THAN_OR_EQUALS_FOR_CONCEPT, FECHA_DESDE,
					location, FECHA_HASTA);
		}
		return valid;
	}

	private static final String FECHA_DESDE = "Fecha Desde";

	private static final String FECHA_HASTA = "Fecha Hasta";

	private static final long serialVersionUID = 1L;

	private Calendar fechaHoraDesde;

	private Calendar fechaHoraHasta;

	private boolean considerTime;

}