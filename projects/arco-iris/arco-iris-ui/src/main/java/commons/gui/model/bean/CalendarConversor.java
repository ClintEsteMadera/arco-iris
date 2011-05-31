package commons.gui.model.bean;

import java.util.Calendar;
import java.util.Date;

import commons.gui.util.ValueConversor;

class CalendarConversor implements ValueConversor {

	/**
	 * Calendar to Date
	 */
	public Object convertTo(Object value) {
		if (value != null && value instanceof Calendar) {
			return ((Calendar) value).getTime();
		}
		return value;
	}

	/**
	 * Date to Calendar
	 */
	public Object convertFrom(Object value) {
		if (value != null && value instanceof Date) {
			Calendar calendar = Calendar.getInstance();
			calendar.setTime((Date) value);
			return calendar;
		}
		return value;
	}
}
