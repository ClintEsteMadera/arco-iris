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
 * $Id: FechaHora.java,v 1.4 2008/04/08 15:04:14 cvschioc Exp $
 */

package commons.datetime;

import java.io.Serializable;

import org.springframework.util.Assert;

/**
 * @author Pablo Pastorino
 * @version $Revision: 1.4 $ $Date: 2008/04/08 15:04:14 $
 */

public class FechaHora implements Serializable {

	public FechaHora(Fecha fecha, Hora hora) {
		super();
		Assert.notNull(fecha, "La Fecha no puede ser nula");
		this.fecha = fecha;
		Assert.notNull(fecha, "La Hora no puede ser nula");
		this.hora = hora;
	}

	public Fecha getFecha() {
		return fecha;
	}

	public void setFecha(Fecha fecha) {
		this.fecha = fecha;
	}

	public Hora getHora() {
		return hora;
	}

	public void setHora(Hora hora) {
		this.hora = hora;
	}

	@Override
	public String toString() {
		return fecha.toString() + hora.toString();
	}

	private Fecha fecha;

	private Hora hora;

	private static final long serialVersionUID = 1L;
}