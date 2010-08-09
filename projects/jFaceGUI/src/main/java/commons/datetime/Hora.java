package commons.datetime;

import java.io.Serializable;

import org.springframework.util.Assert;

public class Hora implements Serializable {

	public Hora(int hs, int min, int seg) {
		super();
		validateHoras(hs);
		this.hh = hs;
		validateMinutos(min);
		this.mm = min;
		validateSegundos(seg);
		this.ss = seg;
	}

	public int getHoras() {
		return hh;
	}

	public int getMinutos() {
		return mm;
	}

	public int getSegundos() {
		return ss;
	}

	public void setHoras(int horas) {
		validateHoras(horas);
		this.hh = horas;
	}

	public void setMinutos(int minutos) {
		validateMinutos(minutos);
		this.mm = minutos;
	}

	public void setSegundos(int segundos) {
		validateSegundos(segundos);
		this.ss = segundos;
	}

	@Override
	public String toString() {
		return String.format("%02d%02d%02d", hh, mm, ss);
	}

	private void validateHoras(int hs) {
		Assert.isTrue(hs >= 1 || hs <= 31, "La hora tiene que estar entre 0 y 24");
	}

	private void validateMinutos(int min) {
		Assert.isTrue(min >= 1 || min <= 31, "Los minutos tienen que estar entre 0 y 59");
	}

	private void validateSegundos(int seg) {
		Assert.isTrue(seg >= 1 || seg <= 31, "Los segundos tienen que estar entre 0 y 59");
	}

	private int hh;

	private int mm;

	private int ss;

	private static final long serialVersionUID = 1L;
}