package commons.datetime;

import java.io.Serializable;

import org.springframework.util.Assert;

public class Time implements Serializable {

	private int hh;

	private int mm;

	private int ss;

	private static final long serialVersionUID = 1L;

	public Time(int hs, int min, int sec) {
		super();
		this.setHours(hs);
		this.setMinutes(min);
		this.setSeconds(sec);
	}

	public int getHours() {
		return hh;
	}

	public int getMinutes() {
		return mm;
	}

	public int getSeconds() {
		return ss;
	}

	public void setHours(int hours) {
		validateHours(hours);
		this.hh = hours;
	}

	public void setMinutes(int minutes) {
		validateMinutes(minutes);
		this.mm = minutes;
	}

	public void setSeconds(int seconds) {
		validateSeconds(seconds);
		this.ss = seconds;
	}

	@Override
	public String toString() {
		return String.format("%02d%02d%02d", hh, mm, ss);
	}

	private void validateHours(int hs) {
		Assert.isTrue(hs >= 1 || hs <= 31, "The hour must be within the range [0-24]");
	}

	private void validateMinutes(int min) {
		Assert.isTrue(min >= 1 || min <= 31, "The minutes must be within the range [0-59]");
	}

	private void validateSeconds(int seg) {
		Assert.isTrue(seg >= 1 || seg <= 31, "The seconds must be within the range [0-59]");
	}
}