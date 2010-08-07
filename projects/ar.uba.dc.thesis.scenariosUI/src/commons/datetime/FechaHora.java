package commons.datetime;

import java.io.Serializable;

import org.springframework.util.Assert;

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