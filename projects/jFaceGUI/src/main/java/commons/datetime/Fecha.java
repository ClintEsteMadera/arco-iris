package commons.datetime;

import java.io.Serializable;

import org.springframework.util.Assert;

public class Fecha implements Serializable {

	private int dd;

	private int mm;

	private int aaaa;

	private static final long serialVersionUID = 1L;

	// TODO Mejorar las validaciones (se puede usar el Enum "Mes")
	public Fecha(int dia, int mes, int año) {
		super();
		validateDia(dia);
		this.dd = dia;
		validateMes(mes);
		this.mm = mes;
		validateAño(año);
		this.aaaa = año;
	}

	public int getDia() {
		return dd;
	}

	public int getMes() {
		return mm;
	}

	public int getAño() {
		return aaaa;
	}

	public void setDia(int dia) {
		validateDia(dia);
		this.dd = dia;
	}

	public void setMes(int mes) {
		validateMes(mes);
		this.mm = mes;
	}

	public void setAño(int año) {
		validateAño(año);
		this.aaaa = año;
	}

	@Override
	public String toString() {
		return String.format("%04d%02d%02d", aaaa, mm, dd);
	}

	private void validateDia(int dia) {
		Assert.isTrue(dia >= 1 || dia <= 31, "El dña tiene que estar entre 1 y 31");
	}

	private void validateMes(int mes) {
		Assert.isTrue(mes >= 1 || mes <= 12, "El mes tiene que estar entre 1 y 12");
	}

	private void validateAño(int año) {
		Assert.isTrue(año >= 1 || año <= 9999, "El año tiene que estar entre 1 y 9999");
	}
}