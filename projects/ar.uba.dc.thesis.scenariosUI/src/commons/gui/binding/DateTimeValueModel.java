package commons.gui.binding;

import java.util.ArrayList;
import java.util.Calendar;

import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.events.TypedEvent;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.DateTime;
import org.eclipse.swt.widgets.Widget;

import sba.common.utils.DateUtils;

import commons.gui.model.ValueChangeEvent;
import commons.gui.model.ValueChangeListener;
import commons.gui.model.ValueModel;
import commons.gui.model.binding.WidgetContainer;
import commons.gui.model.types.EditType;

/**
 * Clase utilizada internamente para el binding de un DateTime
 * @author Jonathan Chiocchio
 */
public class DateTimeValueModel implements ValueModel, WidgetContainer {

	public DateTimeValueModel(DateTime dateTime) {
		this.dateTime = dateTime;
		this.listeners = new ArrayList<ValueChangeListener>();
		this.selectionListener = new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent event) {
				onModifyDateTime(event);
			}
		};
	}

	public void addValueChangeListener(ValueChangeListener listener) {
		listeners.add(listener);
		if (listeners.size() == 1) {
			this.dateTime.addSelectionListener(this.selectionListener);
		}
	}

	public static boolean supportsControl(Control control) {
		return control instanceof DateTime;
	}

	public Object getValue() {
		return this.getCalendarFrom(this.dateTime, true);
	}

	public void setValue(Object value) {
		Calendar calendar = DateUtils.try2GetObjectAsCalendar(value);
		if(calendar != null) {
			this.completeDateTime(calendar, true);
		}
	}

	public void notifyChange() {
		final ValueChangeEvent<Calendar> changeEvent = new ValueChangeEvent<Calendar>(this, null,
				getCalendarFrom(this.dateTime, true));

		for (ValueChangeListener listener : listeners) {
			listener.valueChange(changeEvent);
		}
	}

	public EditType getValueType() {
		return editType;
	}

	public void removeValueChangeListener(ValueChangeListener listener) {
		listeners.remove(listener);
		if (listeners.size() == 0) {
			this.dateTime.removeSelectionListener(this.selectionListener);
		}
	}

	public Widget getWidget() {
		return dateTime;
	}

	private void onModifyDateTime(TypedEvent event) {
		notifyChange();
	}

	private Calendar getCalendarFrom(DateTime aDateTime, boolean onlyTimeFields) {
		Calendar calendar = Calendar.getInstance();
		if(!onlyTimeFields) {
			calendar.set(Calendar.YEAR, aDateTime.getYear());
			calendar.set(Calendar.MONTH, aDateTime.getMonth());
			calendar.set(Calendar.DAY_OF_MONTH, aDateTime.getDay());
		}
		calendar.set(Calendar.HOUR_OF_DAY, aDateTime.getHours());
		calendar.set(Calendar.MINUTE, aDateTime.getMinutes());
		calendar.set(Calendar.SECOND, aDateTime.getSeconds());
		return calendar;
	}

	private void completeDateTime(Calendar calendar, boolean onlyTimeFields) {
		if(!onlyTimeFields) {
			dateTime.setYear(calendar.get(Calendar.YEAR));
			dateTime.setMonth(calendar.get(Calendar.MONTH));
			dateTime.setDay(calendar.get(Calendar.DAY_OF_MONTH));
		}
		dateTime.setHours(calendar.get(Calendar.HOUR_OF_DAY));
		dateTime.setMinutes(calendar.get(Calendar.MINUTE));
		dateTime.setSeconds(calendar.get(Calendar.SECOND));
	}

	private ArrayList<ValueChangeListener> listeners;

	private SelectionListener selectionListener;

	private DateTime dateTime;

	private static EditType<Calendar> editType = new EditType<Calendar>(Calendar.class);
}