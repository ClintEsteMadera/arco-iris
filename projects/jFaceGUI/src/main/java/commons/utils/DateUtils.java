/*
 * $Id: DateUtils.java,v 1.5 2008/04/24 19:06:29 cvschioc Exp $
 */
package commons.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import commons.datetime.SimpleDate;
import commons.datetime.DateTime;

public abstract class DateUtils {

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
			throw new RuntimeException("No se pudo parsear la fecha " + date);
		}
	}

	/**
	 * Convierte a formato "dd/MM/yyyy"
	 * 
	 * @param stringDate
	 *            un string en la forma dd/MM/yyyy
	 * @return un Calendar en la forma dd/MM/yyyy
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
			throw new RuntimeException("No se pudo parsear la fecha " + stringDate);
		}
	}

	public static String getCurrentTimeAsString() {
		return formatCalendarAsTime(getCurrentDatetimeAsCalendar());
	}

	public static String getCurrentDatetimeAsString() {
		return formatCalendarAsDateTime(getCurrentDatetimeAsCalendar());
	}

	// TODO Tomar hora del sistema del servidor
	public static Calendar getCurrentDatetimeAsCalendar() {
		return Calendar.getInstance();
	}

	/**
	 * @return un Calendar con fecha del 1 de Enero de 1970 y hora 00:00:00.000 GMT (Calendario Gregoriano).
	 */
	public static Calendar getEpochTime() {
		Calendar epoch = Calendar.getInstance();
		epoch.setTime(new Date(0L));
		return epoch;
	}

	/**
	 * Intenta convertir el objeto tomado como parámetro a un Calendar:<br>
	 * <li>
	 * <ul>
	 * Si el objeto es un Calendar, devuelve el mismo objeto.
	 * <ul>
	 * Si el objeto es un Date, crea un Calendar y setea dicho Date.
	 * <ul>
	 * Si el objeto es un String, intenta parsearlo a Calendar utilizando los métodos
	 * {@link #parseStringDateAsCalendar(String)} y {@link #parseDMY(String)} de esta clase, en ese orden. </li>
	 * 
	 * @param object
	 *            objeto a convertir en un Calendar
	 * @return Calendar un Calendar creado a partir del parámetro NO NULO <code>object</code>, o <code>null</code>
	 *         si dicho paramétro fuera nulo.
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
			throw new IllegalArgumentException("el objeto " + object + " de tipo " + object.getClass().getName()
					+ " no es convertible a Calendar:");
		}
		return calendar;
	}

	/**
	 * Dado un <code>Date</code>, retorna un <code>Calendar</code> con una fecha equivalente
	 * 
	 * @param date
	 *            Fecha a convertir
	 * @return Calendar con fecha equivalente al Date recibido
	 */
	public static Calendar getDateAsCalendar(Date date) {
		Calendar result = Calendar.getInstance();
		result.setTime(date);
		return result;
	}

	public static Calendar getFechaHoraAsCalendar(DateTime fechaHora) {
		try {
			return getDateAsCalendar(fechaHoraDateFormat.parse(fechaHora.getDate().toString()
					+ fechaHora.getTime().toString()));
		} catch (ParseException e) {
			throw new RuntimeException(e.getMessage());
		}
	}

	public static Calendar getFechaAsCalendar(SimpleDate fecha) {
		try {
			return getDateAsCalendar(fechaDateFormat.parse(fecha.toString()));
		} catch (ParseException e) {
			throw new RuntimeException(e.getMessage());
		}
	}

	/**
	 * Compara el <i>orden</i> de dos objetos {@link Calendar}, utilizando sólo los campos que representan la fecha:
	 * día, mes, y año.
	 * 
	 * @param date1
	 *            Primer objeto {@link Calendar}.
	 * @param date2
	 *            Segundo objeto {@link Calendar}.
	 * @return Un número entero negativo, cero, o un número entero positivo de acuerdo a si el primer argumento es
	 *         menor, igual, o mayor que el segundo.
	 */
	public static int compareDateOnly(Calendar date1, Calendar date2) {
		return compare(date1, date2, DATE_FIELDS);
	}

	/**
	 * Compara el <i>orden</i> de dos objetos {@link Calendar}, utilizando sólo los campos que representan la hora:
	 * hora, minutos, segundos y milisegundos.
	 * 
	 * @param time1
	 *            Primer objeto {@link Calendar}.
	 * @param time2
	 *            Segundo objeto {@link Calendar}.
	 * @return Un número entero negativo, cero, o un número entero positivo de acuerdo a si el primer argumento es
	 *         menor, igual, o mayor que el segundo.
	 */
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
	 * Copia los campos dia, mes y año.
	 * 
	 * @param dest
	 * @param source
	 * @return dest
	 */
	public static Calendar copyDate(Calendar dest, Calendar source) {
		dest.set(Calendar.DAY_OF_MONTH, source.get(Calendar.DAY_OF_MONTH));
		dest.set(Calendar.MONTH, source.get(Calendar.MONTH));
		dest.set(Calendar.YEAR, source.get(Calendar.YEAR));
		return dest;
	}

	/**
	 * Copia los campos hora , minutos ,segundos y milisegundos.
	 * 
	 * @param dest
	 * @param source
	 * @return dest
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

	// TODO Hacer configurables el formato de la fecha usando un preferences
	private static DateFormat dateTimeFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");

	private static DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

	private static DateFormat timeFormat = new SimpleDateFormat("HH:mm");

	private static DateFormat fechaHoraDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");

	private static DateFormat fechaDateFormat = new SimpleDateFormat("yyyyMMdd");

	private static final int[] DATE_FIELDS = { Calendar.YEAR, Calendar.MONTH, Calendar.DAY_OF_MONTH, };

	private static final int[] TIME_FIELDS = { Calendar.HOUR_OF_DAY, Calendar.MINUTE, Calendar.SECOND,
			Calendar.MILLISECOND, };

}