package commons.gui.model.types;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Extension de SimpleDateFormat para soportar "fecha base"
 */
class ExDateFormat extends SimpleDateFormat {

	public ExDateFormat(String pattern) {
		super(pattern);
		this.setLenient(true);
	}

	/**
	 * @see java.text.DateFormat#parse(java.lang.String)
	 */
	@Override
	public Date parse(String str) throws ParseException {
		parseCheck(str);
		return convert(super.parse(str));
	}

	private Date convert(Date d) {
		if (d != null) {

			if (m_baseDate != null) {
				Calendar ret = Calendar.getInstance();
				ret.setTime(d);
				ret.set(Calendar.DAY_OF_MONTH, m_baseDate.get(Calendar.DAY_OF_MONTH));
				ret.set(Calendar.MONTH, m_baseDate.get(Calendar.MONTH));
				ret.set(Calendar.YEAR, m_baseDate.get(Calendar.YEAR));
				return ret.getTime();
			} else if (m_baseTime != null) {
				Calendar ret = Calendar.getInstance();
				ret.setTime(d);
				ret.set(Calendar.HOUR_OF_DAY, m_baseTime.get(Calendar.HOUR_OF_DAY));
				ret.set(Calendar.MINUTE, m_baseTime.get(Calendar.MINUTE));
				ret.set(Calendar.SECOND, m_baseTime.get(Calendar.SECOND));
				return ret.getTime();
			}
		}
		return d;
	}

	public void setBaseDate(Calendar c) {
		m_baseDate = c;
	}

	/**
	 * @see java.text.Format#parseObject(java.lang.String)
	 */
	@Override
	public Object parseObject(String str) throws ParseException {
		if (str.length() == 0) {
			return null;
		}
		return convert((Date) super.parseObject(str));
	}

	/**
	 * Sets the baseTime.
	 * 
	 * @param baseTime
	 *            The baseTime to set
	 */
	public void setBaseTime(Calendar baseTime) {
		m_baseTime = baseTime;
	}

	private void parseCheck(String str) throws ParseException {
		if (str.length() > 0
				&& (str.length() < toPattern().length() || !Character.isDigit(str.charAt(str.length() - 1)))) {
			throw new ParseException("Fecha incompleta", 0);
		}
	}

	private Calendar m_baseDate;

	private Calendar m_baseTime;

	private static final long serialVersionUID = 1L;
}
