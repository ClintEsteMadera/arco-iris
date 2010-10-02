package commons.gui.widget.creation.metainfo;

import org.eclipse.swt.widgets.Composite;

import commons.datetime.Time;
import commons.gui.widget.creation.binding.Binding;
import commons.properties.EnumProperty;

public class CalendarMetainfo extends ControlMetainfo {

	public Time normalizedTime;

	private static CalendarMetainfo instance = new CalendarMetainfo();

	private CalendarMetainfo() {
		super();
		this.applyDefaults();
	}

	public static CalendarMetainfo create(Composite composite, EnumProperty label, Binding binding, boolean readOnly) {
		instance.applyDefaults();
		ControlMetainfo.setValues(instance, composite, label, binding, readOnly);
		return instance;
	}

	@Override
	public void applyDefaults() {
		super.applyDefaults();
		this.normalizedTime = new Time(9, 0, 0); // by default, 9 AM.
	}

	@Override
	public void restoreDefaults() {
		super.restoreDefaults();
		this.applyDefaults();
	}
}