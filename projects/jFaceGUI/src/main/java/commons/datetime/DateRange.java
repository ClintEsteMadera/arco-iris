package commons.datetime;

import java.io.Serializable;
import java.util.Calendar;

import commons.utils.DateUtils;
import commons.validation.CommonValidationMessages;
import commons.validation.ValidationResult;
import commons.validation.ValidationUtils;

public class DateRange implements Serializable {

	private static final String DATE_FROM = "SimpleDate From";

	private static final String DATE_TO = "SimpleDate To";

	private static final long serialVersionUID = 1L;

	private Calendar dateTimeFrom;

	private Calendar dateTimeTo;

	private boolean considerTime;

	/**
	 * Constructs a DateRange based on two calendars.
	 * 
	 * @param dateTimeFrom
	 *            range's lower bound.
	 * @param dateTimeTo
	 *            range's upper bound.
	 * @param considerTime
	 *            specifies whether calendars' times must be considered,
	 */
	public DateRange(Calendar dateTimeFrom, Calendar dateTimeTo, boolean considerTime) {
		super();
		this.considerTime = considerTime;
		this.setDateTimeFrom(dateTimeFrom);
		this.setDateTimeTo(dateTimeTo);
	}

	public Calendar getDateTimeFrom() {
		return dateTimeFrom;
	}

	public Calendar getDateTimeTo() {
		return dateTimeTo;
	}

	public void setDateTimeFrom(Calendar dateTimeFrom) {
		if (!this.considerTime) {
			clearTimeIfNotNull(dateTimeFrom);
		}
		this.dateTimeFrom = dateTimeFrom;
	}

	public void setDateTimeTo(Calendar dateTimeTo) {
		if (!this.considerTime) {
			endOfDayIfNotNull(dateTimeTo);
		}
		this.dateTimeTo = dateTimeTo;
	}

	public boolean isInRange(Calendar dateTime) {
		boolean isInRange = true;
		if (this.dateTimeFrom != null) {
			if (considerTime) {
				isInRange = isInRange && this.dateTimeFrom.compareTo(dateTime) <= 0;
			} else {
				isInRange = isInRange && DateUtils.compareDateOnly(this.dateTimeFrom, dateTime) <= 0;
			}
		}

		if (this.dateTimeTo != null) {
			if (considerTime) {
				isInRange = isInRange && dateTime.compareTo(this.dateTimeTo) >= 0;
			} else {
				isInRange = isInRange && DateUtils.compareDateOnly(dateTime, this.dateTimeTo) >= 0;
			}
		}
		return isInRange;
	}

	private static void clearTimeIfNotNull(Calendar fechaHora) {
		if (fechaHora != null) {
			DateUtils.clearTime(fechaHora);
		}
	}

	private static void endOfDayIfNotNull(Calendar fhHasta) {
		if (fhHasta != null) {
			DateUtils.endOfDay(fhHasta);
		}
	}

	public boolean validate(ValidationResult errors, String location) {
		boolean valid = true;
		if ((getDateTimeFrom() != null) && (getDateTimeTo() != null)) {
			valid &= ValidationUtils.isValidCondition(errors, location,
					(getDateTimeFrom().compareTo(getDateTimeTo()) <= 0),
					CommonValidationMessages.BEFORE_THAN_OR_EQUALS_FOR_CONCEPT, DATE_FROM, location, DATE_TO);
		}
		return valid;
	}
}