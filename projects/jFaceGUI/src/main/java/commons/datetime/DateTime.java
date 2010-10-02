package commons.datetime;

import java.io.Serializable;

import org.springframework.util.Assert;

public class DateTime implements Serializable {

	private SimpleDate date;

	private Time time;

	private static final long serialVersionUID = 1L;

	public DateTime(SimpleDate date, Time time) {
		super();
		Assert.notNull(date);
		Assert.notNull(time);
		this.date = date;
		this.time = time;
	}

	public SimpleDate getDate() {
		return date;
	}

	public void setDate(SimpleDate date) {
		this.date = date;
	}

	public Time getTime() {
		return time;
	}

	public void setTime(Time time) {
		this.time = time;
	}

	@Override
	public String toString() {
		return date.toString() + time.toString();
	}
}