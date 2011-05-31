package commons.gui.model.types;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import commons.utils.DateUtils;

/**
 * Parametros de configuracion de un valor de tipo SimpleDate.
 */
public class DateEditParameters implements Cloneable {

	public static char DEFAULT_DATE_SEPARATOR = '/';

	public static char DEFAULT_HOUR_SEPARATOR = ':';

	/**
	 * Flag para mostrar el año con dos dígitos.
	 */
	public static boolean DEFAULT_SHORT_YEAR = false;

	private static final boolean DEFAULT_SHOW_SECONDS = true;

	public static final int MODE_DATE = 0;

	public static final int MODE_DATE_HOUR = 1;

	public static final int MODE_HOUR = 2;

	private static final int DEFAULT_MODE = MODE_DATE;

	private static Date s_prototype = new Date(0L);// epoch time, namely January 1, 1970, 00:00:00 GMT.

	public int mode = DEFAULT_MODE;

	public boolean shortYear = DEFAULT_SHORT_YEAR;

	public boolean showSeconds = DEFAULT_SHOW_SECONDS;

	private Calendar m_defaultTime;

	private Calendar m_defaultDate;

	public DateEditParameters() {
		super();
	}

	public DateEditParameters(int mode) {
		this.mode = mode;
	}

	public DateEditParameters(int mode, boolean shortYear) {
		this.mode = mode;
		this.shortYear = shortYear;
	}

	@Override
	public Object clone() {
		try {
			return super.clone();
		} catch (CloneNotSupportedException e) {
			throw new UnsupportedOperationException(e.getClass().getName());
		}
	}

	public SimpleDateFormat getDateFormat() {
		String datePattern;
		String hourPattern;
		String pattern;

		if (shortYear) {
			datePattern = "dd" + DEFAULT_DATE_SEPARATOR + "MM" + DEFAULT_DATE_SEPARATOR + "yy";
		} else {
			datePattern = "dd" + DEFAULT_DATE_SEPARATOR + "MM" + DEFAULT_DATE_SEPARATOR + "yyyy";
		}

		if (showSeconds) {
			hourPattern = "HH" + DEFAULT_HOUR_SEPARATOR + "mm" + DEFAULT_HOUR_SEPARATOR + "ss";
		} else {
			hourPattern = "HH" + DEFAULT_HOUR_SEPARATOR + "mm";
		}

		if (mode == MODE_DATE) {
			pattern = datePattern;
		} else if (mode == MODE_DATE_HOUR) {
			pattern = datePattern + "  " + hourPattern;
		} else if (mode == MODE_HOUR) {
			pattern = hourPattern;
		} else {
			pattern = datePattern;
		}

		ExDateFormat format = new ExDateFormat(pattern);
		format.setLenient(true);

		if (mode == MODE_HOUR && m_defaultDate != null) {
			format.setBaseDate(Calendar.getInstance());
		}

		if (mode == MODE_DATE && m_defaultTime != null) {
			format.setBaseTime(m_defaultTime);
		}

		return format;
	}

	public Date getPrototype() {
		return s_prototype;
	}

	public SimpleDateFormat getExportableFormat() {
		if (mode == MODE_DATE) {
			return new SimpleDateFormat("yyyy-MM-dd");
		} else if (mode == MODE_HOUR) {
			return showSeconds ? new SimpleDateFormat("hh:mm:ss") : new SimpleDateFormat("hh:mm");
		}
		return showSeconds ? new SimpleDateFormat("yyyy-MM-dd hh:mm:ss") : new SimpleDateFormat("yyyy-MM-dd hh:mm");
	}

	/**
	 * Returns the baseTime.
	 * 
	 * @return Calendar
	 */
	public Calendar getBaseTime() {
		return m_defaultTime;
	}

	public void setDefaultTime(Calendar defaultTime) {
		m_defaultTime = defaultTime;
	}

	public void setMinDefaultTime() {
		m_defaultTime = DateUtils.today();
	}

	public void setMaxDefaultTime() {
		m_defaultTime = DateUtils.endOfToday();
	}

	/**
	 * Returns the defaultDate.
	 * 
	 * @return Calendar
	 */
	public Calendar getDefaultDate() {
		return m_defaultDate;
	}

	/**
	 * Sets the defaultDate.
	 * 
	 * @param defaultDate
	 *            The defaultDate to set
	 */
	public void setDefaultDate(Calendar defaultDate) {
		m_defaultDate = defaultDate;
	}

	public void setTodayDefaultDate() {
		m_defaultDate = DateUtils.today();
	}
}