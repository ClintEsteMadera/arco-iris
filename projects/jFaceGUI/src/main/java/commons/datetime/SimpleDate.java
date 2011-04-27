package commons.datetime;

import java.io.Serializable;

import org.springframework.util.Assert;

public class SimpleDate implements Serializable {

	private int dd;

	private int mm;

	private int yyyy;

	private static final long serialVersionUID = 1L;

	// TODO improve validations ("Month" enum could be used)
	public SimpleDate(int day, int month, int year) {
		super();
		validateDay(day);
		this.dd = day;
		validateMonth(month);
		this.mm = month;
		validateYear(year);
		this.yyyy = year;
	}

	public int getDay() {
		return dd;
	}

	public int getMonth() {
		return mm;
	}

	public int getYear() {
		return yyyy;
	}

	public void setDay(int day) {
		validateDay(day);
		this.dd = day;
	}

	public void setMonth(int month) {
		validateMonth(month);
		this.mm = month;
	}

	public void setYear(int year) {
		validateYear(year);
		this.yyyy = year;
	}

	@Override
	public String toString() {
		return String.format("%04d%02d%02d", yyyy, mm, dd);
	}

	private void validateDay(int dia) {
		Assert.isTrue(dia >= 1 || dia <= 31, "The day has to be between 1 and 31");
	}

	private void validateMonth(int mes) {
		Assert.isTrue(mes >= 1 || mes <= 12, "The month has to be between 1 y 12");
	}

	private void validateYear(int year) {
		Assert.isTrue(year >= 1 || year <= 9999, "The year has to be between 1 y 9999");
	}
}