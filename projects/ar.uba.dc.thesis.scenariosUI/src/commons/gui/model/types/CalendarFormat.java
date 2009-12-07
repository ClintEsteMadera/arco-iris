package commons.gui.model.types;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;

/**
 * Extension de SimpleDateFormat para soportar "fecha base"
 * @author P.Pastorino
 */
class CalendarFormat extends AbstractFormat {

	public CalendarFormat(String pattern) {
		this.dateFormat = new ExDateFormat(pattern);
	}

	@Override
	public Object stringToValue(String str) {
		Date d=null;
		
		try {
			d = this.dateFormat.parse(str);
		} catch (ParseException e) {
			throw new IllegalArgumentException("Error parseando la fecha '" + 
					str + "': " + e.getMessage(), e);
		}
		
		if(d == null){
			return null;
		}
		final Calendar cal=Calendar.getInstance();
		cal.setTime(d);
		return cal;
	}

	@Override
	public String valueToString(Object obj) {
		Calendar cal=(Calendar)obj;
		if(cal == null){
			return null;
		}
		return dateFormat.format(cal.getTime());
	}

	private ExDateFormat dateFormat;

	private static final long serialVersionUID = 1L;
}
