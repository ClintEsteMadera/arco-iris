/*
 * $Id: DateUtils.java,v 1.6 2009/04/30 18:37:13 cvsuribe Exp $
 *
 * Licencia de Caja de Valores S.A., Versión 1.0
 * Copyright (c) 2006 Caja de Valores S.A.
 * 25 de Mayo 362, Ciudad Autónoma de Buenos Aires, República Argentina
 * Todos los derechos reservados.
 * Este software es información confidencial y propietaria de Caja de Valores S.A. ("Información
 * Confidencial"). Usted no divulgará tal Información Confidencial y la usará solamente de acuerdo a
 * los términos del acuerdo de licencia que posee con Caja de Valores S.A.
 */
package ar.uba.dc.thesis.scenariosUIWeb.common.util;

import java.sql.Time;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * Utilidades para el manejo y conversión de objetos de tipo fecha, hora y time-stamp.
 * @author H. Adrián Uribe
 * @version $Revision: 1.6 $ $Date: 2009/04/30 18:37:13 $
 */
public final class DateUtils {

	public static java.sql.Date toSqlDate(Calendar cal) {
		return (cal == null) ? null : new java.sql.Date(cal.getTimeInMillis());
	}

	public static java.sql.Date toSqlDate(java.util.Date date) {
		return (date == null) ? null : new java.sql.Date(date.getTime());
	}

	public static Time toTime(Calendar cal) {
		return (cal == null) ? null : new Time(cal.getTimeInMillis());
	}

	public static Time toTime(java.util.Date date) {
		return (date == null) ? null : new Time(date.getTime());
	}

	public static Timestamp toTimestamp(Calendar cal) {
		return (cal == null) ? null : new Timestamp(cal.getTimeInMillis());
	}

	public static Timestamp toTimestamp(java.util.Date date) {
		return (date == null) ? null : new Timestamp(date.getTime());
	}

	public static Calendar toCalendar(java.util.Date date) {
		Calendar cal;
		if (date == null) {
			cal = null;
		} else {
			cal = newEpochCalendar();
			cal.setTime(date);
		}
		return cal;
	}

	/**
	 * <b>Nota</b>: Puede perder precisión ya que Timestamp puede almacenar nanosegundos y Calendar
	 * sólo puede almacenar milisegundos.
	 * @param tstamp Timestamp.
	 * @return Calendar
	 */
	public static Calendar toCalendar(Timestamp tstamp) {
		Calendar cal;
		if (tstamp == null) {
			cal = null;
		} else {
			cal = newEpochCalendar();
			// NOTA: Timestamp guarda los nanosegundos en un campo aparte
			cal.setTimeInMillis(tstamp.getTime() + tstamp.getNanos() / 1000000);
		}
		return cal;
	}

	public static Calendar toCalendar(long millis) {
		Calendar cal = newEpochCalendar();
		cal.setTimeInMillis(millis);
		return cal;
	}

	/**
	 * Compara el <i>orden</i> de dos objetos {@link Calendar}.
	 * @param cal1 Primer objeto {@link Calendar}.
	 * @param cal2 Segundo objeto {@link Calendar}.
	 * @return Un número entero negativo, cero, o un número entero positivo de acuerdo a si el
	 * primer argumento es menor, igual, o mayor que el segundo.
	 */
	public static int compare(Calendar cal1, Calendar cal2) {
		int order = compare(cal1, cal2, DATE_FIELDS);
		if (order == 0) {
			order = compare(cal1, cal2, TIME_FIELDS);
		}
		return order;
	}

	/**
	 * Compara el <i>orden</i> de dos objetos {@link Calendar}, utilizando sólo los campos que
	 * representan la fecha: día, mes, y año.
	 * @param date1 Primer objeto {@link Calendar}.
	 * @param date2 Segundo objeto {@link Calendar}.
	 * @return Un número entero negativo, cero, o un número entero positivo de acuerdo a si el
	 * primer argumento es menor, igual, o mayor que el segundo.
	 */
	public static int compareDateOnly(Calendar date1, Calendar date2) {
		return compare(date1, date2, DATE_FIELDS);
	}

	/**
	 * Compara el <i>orden</i> de dos objetos {@link Calendar}, utilizando sólo los campos que
	 * representan la hora: hora, minutos, segundos y milisegundos.
	 * @param time1 Primer objeto {@link Calendar}.
	 * @param time2 Segundo objeto {@link Calendar}.
	 * @return Un número entero negativo, cero, o un número entero positivo de acuerdo a si el
	 * primer argumento es menor, igual, o mayor que el segundo.
	 */
	public static int compareTimeOnly(Calendar time1, Calendar time2) {
		return compare(time1, time2, TIME_FIELDS);
	}

	public static int differenceInHours(Calendar calFrom, Calendar calTo, boolean timeOnly) {
		return differenceInSeconds(calFrom, calTo, timeOnly) / 3600;
	}

	public static int differenceInMinutes(Calendar calFrom, Calendar calTo, boolean timeOnly) {
		return differenceInSeconds(calFrom, calTo, timeOnly) / 60;
	}

	public static int differenceInSeconds(Calendar calFrom, Calendar calTo, boolean timeOnly) {
		if (timeOnly) {
			// Copiar todos los campos que no sean de la hora de calTo, desde
			// los de calFrom.
			Calendar originalCalTo = calTo;
			calTo = (Calendar) calFrom.clone();
			calTo.set(Calendar.HOUR_OF_DAY, originalCalTo.get(Calendar.HOUR_OF_DAY));
			calTo.set(Calendar.MINUTE, originalCalTo.get(Calendar.MINUTE));
			calTo.set(Calendar.SECOND, originalCalTo.get(Calendar.SECOND));
			// NOTA: No se hace set de los milisegundos porque se truncan del
			// resultado
		}

		long millisFrom = calFrom.getTimeInMillis();
		long millisTo = calTo.getTimeInMillis();
		return (int) ((millisTo - millisFrom) / 1000);
	}

	public static String fastDateFormat(Calendar cal, int dateFormat) {
		return (cal == null) ? null : fastDateFormat(cal, -1, dateFormat);
	}

	public static String fastDateFormat(java.util.Date date, int dateFormat) {
		return (date == null) ? null : fastDateFormat(toCalendar(date), -1, dateFormat);
	}

	public static String fastDateFormat(long millis, int dateFormat) {
		return fastDateFormat(toCalendar(millis), -1, dateFormat);
	}

	public static String fastDateFormat(Timestamp tstamp) {
		return (tstamp == null) ? null : fastDateFormat(toCalendar(tstamp), tstamp.getNanos(),
		        FORMAT_TIMESTAMP);
	}

	public static String toString(Calendar cal) {
		return fastDateFormat(cal, FORMAT_AUTO);
	}

	public static String toString(java.util.Date date) {
		return fastDateFormat(date, FORMAT_AUTO);
	}

	public static String toString(long millis) {
		return fastDateFormat(millis, FORMAT_AUTO);
	}

	public static String toString(Timestamp tstamp) {
		return fastDateFormat(tstamp);
	}

	public static Calendar newEpochCalendar() {
		// Se evita usar el constructor default por performance, ya que el mismo
		// obtiene la fecha y hora del sistema (que no utilizamos para nada)
		return new GregorianCalendar(EPOCH_YEAR, EPOCH_MONTH, EPOCH_DAY);
	}

	public static final int EPOCH_YEAR = 1970;

	public static final int EPOCH_MONTH = 0;

	public static final int EPOCH_DAY = 1;

	public static final long MINUTE_MILLIS = 60L * 1000L;

	public static final long HOUR_MILLIS = 60L * MINUTE_MILLIS;

	public static final long DAY_MILLIS = 24L * HOUR_MILLIS;

	private static final int MASK_DATE = 1 << 0;

	private static final int MASK_TIME = 1 << 1;

	private static final int MASK_FRAC_SECONDS = 1 << 2;

	public static final int FORMAT_AUTO = 0;

	/** Formato: "YYYY-MM-DD" */
	public static final int FORMAT_DATE = MASK_DATE;

	/** Formato: "hh:mm:ss" */
	public static final int FORMAT_TIME = MASK_TIME;

	/** Formato: "YYYY-MM-DD hh:mm:ss" */
	public static final int FORMAT_DATE_TIME = MASK_DATE | MASK_TIME;

	/** Formato: "YYYY-MM-DD hh:mm:ss.SSS[SSSSSS]" */
	public static final int FORMAT_TIMESTAMP = MASK_DATE | MASK_TIME | MASK_FRAC_SECONDS;

	public static final long MIN_VALID_TIME;

	public static final long MAX_VALID_TIME;
	static {
		Calendar cal = newEpochCalendar();
		MIN_VALID_TIME = cal.getTimeInMillis();
		cal.set(Calendar.HOUR_OF_DAY, 23);
		cal.set(Calendar.MINUTE, 59);
		cal.set(Calendar.SECOND, 59);
		cal.set(Calendar.MILLISECOND, 999);
		MAX_VALID_TIME = cal.getTimeInMillis();
	}

	private DateUtils() {
		super();
	}

	private static String fastDateFormat(Calendar cal, int nanos, int dateFormat) {
		// Formato: "YYYY-MM-DD hh:mm:ss.SSS[SSSSSS]"
		if (dateFormat == FORMAT_AUTO) {
			dateFormat = determineDateFormat(cal);
		}
		int len = -1;
		switch (dateFormat) {
		case FORMAT_DATE:
			len = 10;
			break;
		case FORMAT_TIME:
			len = 8;
			break;
		case FORMAT_DATE_TIME:
			len = 10 + 1 + 8;
			break;
		case FORMAT_TIMESTAMP:
			len = 10 + 1 + 8 + 1 + ((nanos == -1) ? 3 : 9);
			break;
		default:
			assert false : dateFormat;
			break;
		}

		char[] buf = new char[len];
		int bufInd = 0;
		int i;
		String str;

		if ((dateFormat & MASK_DATE) != 0) {
			i = cal.get(Calendar.YEAR);
			if (i == sm_currentYear) {
				for (i = 0; i < sm_currentYearChars.length; i++) {
					buf[bufInd++] = sm_currentYearChars[i];
				}
			} else {
				str = Integer.toString(i);
				len = str.length();
				if (len != 4) {
					if (len < 4) {
						for (i = 4 - len; i > 0; i--) {
							buf[bufInd++] = '0';
						}
					} else {
						str = "****";
						len = 4;
					}
				}
				for (i = 0; i < len; i++) {
					buf[bufInd++] = str.charAt(i);
				}
			}

			buf[bufInd++] = SEPARATOR_DATE;

			i = cal.get(Calendar.MONTH) + 1;
			if (i < 10) {
				buf[bufInd++] = '0';
				buf[bufInd++] = (char) ('0' + i);
			} else {
				buf[bufInd++] = (char) ('0' + i / 10);
				buf[bufInd++] = (char) ('0' + i % 10);
			}

			buf[bufInd++] = SEPARATOR_DATE;

			i = cal.get(Calendar.DAY_OF_MONTH);
			if (i < 10) {
				buf[bufInd++] = '0';
				buf[bufInd++] = (char) ('0' + i);
			} else {
				buf[bufInd++] = (char) ('0' + i / 10);
				buf[bufInd++] = (char) ('0' + i % 10);
			}
		}

		if ((dateFormat & MASK_TIME) != 0) {
			if (bufInd != 0) {
				buf[bufInd++] = ' ';
			}

			i = cal.get(Calendar.HOUR_OF_DAY);
			if (i < 10) {
				buf[bufInd++] = '0';
				buf[bufInd++] = (char) ('0' + i);
			} else {
				buf[bufInd++] = (char) ('0' + i / 10);
				buf[bufInd++] = (char) ('0' + i % 10);
			}

			buf[bufInd++] = SEPARATOR_TIME;

			i = cal.get(Calendar.MINUTE);
			if (i < 10) {
				buf[bufInd++] = '0';
				buf[bufInd++] = (char) ('0' + i);
			} else {
				buf[bufInd++] = (char) ('0' + i / 10);
				buf[bufInd++] = (char) ('0' + i % 10);
			}

			buf[bufInd++] = SEPARATOR_TIME;

			i = cal.get(Calendar.SECOND);
			if (i < 10) {
				buf[bufInd++] = '0';
				buf[bufInd++] = (char) ('0' + i);
			} else {
				buf[bufInd++] = (char) ('0' + i / 10);
				buf[bufInd++] = (char) ('0' + i % 10);
			}

			if ((dateFormat & MASK_FRAC_SECONDS) != 0) {
				buf[bufInd++] = SEPARATOR_FRAC_SECONDS;
				i = (nanos == -1) ? cal.get(Calendar.MILLISECOND) : nanos;
				str = Integer.toString(i);
				len = str.length();
				for (i = ((nanos == -1) ? 3 : 9) - len; i > 0; i--) {
					buf[bufInd++] = '0';
				}
				for (i = 0; i < len; i++) {
					buf[bufInd++] = str.charAt(i);
				}
			}
		}

		return new String(buf);
	}

	private static int determineDateFormat(Calendar cal) {
		int dateFormat = 0;

		if (cal.get(Calendar.MILLISECOND) != 0) {
			dateFormat = FORMAT_TIMESTAMP;
		} else if (cal.get(Calendar.YEAR) == 1970 && cal.get(Calendar.MONTH) == 0
		        && cal.get(Calendar.DAY_OF_MONTH) == 1) {
			dateFormat = FORMAT_TIME;
		} else if (cal.get(Calendar.HOUR_OF_DAY) == 0 && cal.get(Calendar.MINUTE) == 0
		        && cal.get(Calendar.SECOND) == 0) {
			dateFormat = FORMAT_DATE;
		} else {
			dateFormat = FORMAT_DATE_TIME;
		}

		return dateFormat;
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

	private static final char SEPARATOR_DATE = '-';

	private static final char SEPARATOR_TIME = ':';

	private static final char SEPARATOR_FRAC_SECONDS = '.';

	private static final int[] DATE_FIELDS =
	        { Calendar.YEAR, Calendar.MONTH, Calendar.DAY_OF_MONTH, };

	private static final int[] TIME_FIELDS =
	        { Calendar.HOUR_OF_DAY, Calendar.MINUTE, Calendar.SECOND, Calendar.MILLISECOND, };

	private static final int sm_currentYear = new GregorianCalendar().get(Calendar.YEAR);

	private static final char[] sm_currentYearChars =
	        Integer.toString(sm_currentYear).toCharArray();
}
