package commons.gui.widget.factory;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.DateTime;

import commons.datetime.Time;
import commons.gui.model.validation.ValidationManager;
import commons.gui.widget.DefaultLayoutFactory;
import commons.gui.widget.composite.CalendarComposite;
import commons.gui.widget.creation.metainfo.CalendarMetainfo;
import commons.properties.CommonLabels;

public final class CalendarFactory {

	/**
	 * This class is not meant to be instantiated.
	 */
	private CalendarFactory() {
		super();
	}

	public static CalendarComposite createCalendar(CalendarMetainfo metainfo) {
		if (!CommonLabels.NO_LABEL.equals(metainfo.label)) {
			LabelFactory.createLabel(metainfo.composite, metainfo.label, false, true);
		}
		CalendarComposite fechaComposite = (CalendarComposite) createControl(metainfo, CALENDAR_CONTROL);
		metainfo.binding.bind(fechaComposite);

		metainfo.restoreDefaults();
		return fechaComposite;
	}

	public static Control createTimePicker(CalendarMetainfo metainfo) {
		Control dateTime;
		if (metainfo.readOnly) {
			dateTime = LabelFactory.createReadOnlyField(metainfo.composite, metainfo.binding, metainfo.label);
		} else {
			LabelFactory.createLabel(metainfo.composite, metainfo.label, false, true);
			dateTime = createControl(metainfo, DATE_TIME_CONTROL);
			metainfo.binding.bind(dateTime);
		}
		metainfo.restoreDefaults();
		return dateTime;
	}

	private static DateTime createNewDateTime(CalendarMetainfo metainfo) {
		DateTime dateTime = new DateTime(metainfo.composite, SWT.TIME | SWT.SHORT);
		normalizeTime(dateTime, metainfo.normalizedTime);
		return dateTime;

	}

	private static void normalizeTime(DateTime dateTime, Time hora) {
		dateTime.setHours(Integer.valueOf(hora.getHours()));
		dateTime.setMinutes(Integer.valueOf(hora.getMinutes()));
		dateTime.setSeconds(Integer.valueOf(hora.getSeconds()));
	}

	private static Control createControl(CalendarMetainfo metainfo, int controlType) {

		Control layoutControl = null;
		Control control = null;

		if (!metainfo.validate) {
			control = _createControl(metainfo, controlType);
			layoutControl = control;
		} else {
			final Composite parent = metainfo.composite;

			final Composite composite = new Composite(parent, SWT.NONE);
			DefaultLayoutFactory.setDefaultGridLayout(composite, 2);
			composite.setLayoutData(null);
			metainfo.composite = composite;

			control = _createControl(metainfo, controlType);

			metainfo.composite = parent;

			LabelStatusAware status = new LabelStatusAware(composite);
			ValidationManager.setValidationProperty(control, metainfo.binding);
			ValidationManager.setValidationStatus(control, status);

			layoutControl = composite;
		}

		if (metainfo.layoutData != null) {
			layoutControl.setLayoutData(metainfo.layoutData);
		}

		return control;

	}

	private static Control _createControl(CalendarMetainfo metainfo, int controlType) {
		switch (controlType) {
		case DATE_TIME_CONTROL:
			return createNewDateTime(metainfo);
		}
		return new CalendarComposite(metainfo.composite, metainfo.readOnly);
	}

	private static final int CALENDAR_CONTROL = 0;

	private static final int DATE_TIME_CONTROL = 1;

}