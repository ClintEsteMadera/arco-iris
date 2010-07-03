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