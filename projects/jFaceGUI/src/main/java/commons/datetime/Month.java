package commons.datetime;

import java.io.Serializable;

public enum Month implements Serializable {

	JANUARY("January"),
	FEBRUARY("February"),
	MARCH("March"),
	APRIL("April"),
	MAY("May"),
	JUNE("June"),
	JULY("July"),
	AUGUST("August"),
	SEPTEMBER("September"),
	OCTOBER("October"),
	NOVEMBER("November"),
	DECEMBER("December");

	private Month(String description) {
		this.description = description;
	}

	@Override
	public String toString() {
		return this.description;
	}

	private String description;
}
