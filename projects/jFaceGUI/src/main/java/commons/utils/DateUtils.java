/*
 * $Id: DateUtils.java,v 1.5 2008/04/24 19:06:29 cvschioc Exp $
 */
package commons.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import commons.datetime.DateTime;
import commons.datetime.SimpleDate;

public abstract class DateUtils {

	private static DateFormat dateTimeFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");

	private static DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

	private static DateFormat timeFormat = new SimpleDateFormat("HH:mm");

	private static DateFormat dateTimeDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");

	private static DateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd");

	private static final int[] DATE_FIELDS = { Calendar.YEAR, Calendar.MONTH, Calendar.DAY_OF_MONTH, };

	private static final int[] TIME_FIELDS = { Calendar.HOUR_OF_DAY, Calendar.MINUTE, Calendar.SECOND,
			Calendar.MILLISECOND, };

	public static String formatCalendarAsDateTime(Calendar cal) {
		synchronized (dateTimeFormat) {
			return dateTimeFormat.format(cal.getTime());
		}
	}

	public static String formatCalendarAsTime(Calendar cal) {
		synchronized (timeFormat) {
			return timeFormat.format(cal.getTime());
		}
	}

	public static String formatDateAsDateTime(Date date) {
		synchronized (dateTimeFormat) {
			return dateTimeFormat.format(date);
		}
	}

	public static String formatDate(Date date) {
		synchronized (dateFormat) {
			return dateFormat.format(date);
		}
	}

	public static String formatCalendar(Calendar cal) {
		String result = "";
		if (cal != null) {
			synchronized (dateFormat) {
				result = dateFormat.format(cal.getTime());
			}
		}
		return result;
	}

	public static Calendar parseStringDateAsCalendar(String date) {
		try {
			synchronized (dateTimeFormat) {
				Date time = dateTimeFormat.parse(date);
				Calendar calendar = Calendar.getInstance();
				calendar.setTime(time);
				return calendar;
			}
		} catch (ParseException e) {
			throw new RuntimeException("Could not parse " + date + " as a date");
		}
	}

	/**
	 * Converts to format "dd/MM/yyyy"
	 * 
	 * @param stringDate
	 *            a string in the form dd/MM/yyyy
	 * @return a Calendar in the form dd/MM/yyyy
	 */
	public static Calendar parseDMY(String stringDate) {
		try {
			synchronized (dateFormat) {
				Date date = dateFormat.parse(stringDate);
				Calendar calendar = Calendar.getInstance();
				calendar.setTime(date);
				return calendar;
			}
		} catch (ParseException e) {
			throw new RuntimeException("Could not parse " + stringDate + " as a date");
		}
	}

	public static String getCurrentTimeAsString() {
		return formatCalendarAsTime(getCurrentDatetimeAsCalendar());
	}

	public static String getCurrentDatetimeAsString() {
		return formatCalendarAsDateTime(getCurrentDatetimeAsCalendar());
	}

	public static Calendar getCurrentDatetimeAsCalendar() {
		return Calendar.getInstance();
	}

	/**
	 * @return a Calendar instance set to date January 1st, 1970, time: 00:00:00.000 GMT (Gregorian Calendar).
	 */
	public static Calendar getEpochTime() {
		Calendar epoch = Calendar.getInstance();
		epoch.setTime(new Date(0L));
		return epoch;
	}

	/**
	 * Tries to convert the object taken as parameter to an instance of Calendar:<br>
	 * <li>
	 * <ul>
	 * If the object is already a Calendar, object is <code>returned</code>.
	 * <ul>
	 * If the object is an instance of Date, a new Calendar with that Date is returned.
	 * <ul>
	 * If the object is a String, try to parse it using methods {@link #parseStringDateAsCalendar(String)} and
	 * {@link #parseDMY(String)}, in that order.</li>
	 * 
	 * @param object
	 *            the object to convert to a Calendar. <b>must be not null</b>
	 * @return Calendar an instance of Calendar or null if conversion wasn't possible.
	 */
	public static Calendar try2GetObjectAsCalendar(Object object) {
		Calendar calendar = null;

		if (object instanceof Calendar) {
			calendar = (Calendar) object;
		} else if (object instanceof Date) {
			calendar = getDateAsCalendar((Date) object);
		} else if (object instanceof String) {
			try {
				calendar = parseStringDateAsCalendar((String) object);
			} catch (Exception e) {
				calendar = parseDMY((String) object);
			}
		} else if (object != null) {
			throw new IllegalArgumentException("the object " + object + " of type " + object.getClass().getName()
					+ " cannot be converted to a Calendar.");
		}
		return calendar;
	}

	public static Calendar getDateAsCalendar(Date date) {
		Calendar result = Calendar.getInstance();
		result.setTime(date);
		return result;
	}

	public static Calendar getDateTimeAsCalendar(DateTime fechaHora) {
		try {
			return getDateAsCalendar(dateTimeDateFormat.parse(fechaHora.getDate().toString()
					+ fechaHora.getTime().toString()));
		} catch (ParseException e) {
			throw new RuntimeException(e.getMessage());
		}
	}

	public static Calendar getSimpleDateAsCalendar(SimpleDate fecha) {
		try {
			return getDateAsCalendar(simpleDateFormat.parse(fecha.toString()));
		} catch (ParseException e) {
			throw new RuntimeException(e.getMessage());
		}
	}

	public static int compareDateOnly(Calendar date1, Calendar date2) {
		return compare(date1, date2, DATE_FIELDS);
	}

	public static int compareTimeOnly(Calendar time1, Calendar time2) {
		return compare(time1, time2, TIME_FIELDS);
	}

	private static final int compare(Calendar c1, Calendar c2, int[] fields) {
		int order = 0;
		for (int i = 0; i < fields.length; i++) {
			order = c1.get(fields[i]) - c2.get(fields[i]);
			if (order != 0) {
				break;
			}
		}
		return order;
	}

	/**
	 * Copies the fields: day, month and year.
	 */
	public static Calendar copyDate(Calendar dest, Calendar source) {
		dest.set(Calendar.DAY_OF_MONTH, source.get(Calendar.DAY_OF_MONTH));
		dest.set(Calendar.MONTH, source.get(Calendar.MONTH));
		dest.set(Calendar.YEAR, source.get(Calendar.YEAR));
		return dest;
	}

	/**
	 * Copies the fields: hour, minutes, seconds and milliseconds.
	 */
	public static Calendar copyTime(Calendar dest, Calendar source) {
		dest.set(Calendar.HOUR_OF_DAY, source.get(Calendar.HOUR_OF_DAY));
		dest.set(Calendar.MINUTE, source.get(Calendar.MINUTE));
		dest.set(Calendar.SECOND, source.get(Calendar.SECOND));
		dest.set(Calendar.MILLISECOND, source.get(Calendar.MILLISECOND));
		return dest;
	}

	public static Calendar clearTime(Calendar calendar) {
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		return calendar;
	}

	public static Calendar clearDate(Calendar calendar) {
		calendar.clear(Calendar.DAY_OF_MONTH);
		calendar.clear(Calendar.MONTH);
		calendar.clear(Calendar.YEAR);
		return calendar;
	}

	public static Calendar today() {
		Calendar ret = Calendar.getInstance();
		return clearTime(ret);
	}

	public static Calendar endOfToday() {
		return endOfDay(Calendar.getInstance());
	}

	public static Calendar endOfDay(Calendar calendar) {
		clearTime(calendar);
		calendar.add(Calendar.DATE, 1);
		calendar.add(Calendar.SECOND, -1);
		return calendar;
	}

	public static Calendar addDays(Calendar calendar, int days) {
		calendar.add(Calendar.DATE, days);
		return calendar;
	}

	public static boolean isSameDay(Calendar c1, Calendar c2) {
		return c1.get(Calendar.DAY_OF_MONTH) == c2.get(Calendar.DAY_OF_MONTH)
				&& c1.get(Calendar.MONTH) == c2.get(Calendar.MONTH) && c1.get(Calendar.YEAR) == c2.get(Calendar.YEAR);
	}
}