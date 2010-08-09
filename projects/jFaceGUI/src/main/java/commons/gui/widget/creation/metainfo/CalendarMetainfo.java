package commons.gui.widget.creation.metainfo;

import org.eclipse.swt.widgets.Composite;

import commons.datetime.Hora;
import commons.gui.widget.creation.binding.Binding;
import commons.properties.EnumProperty;

/**
 * 
 */

public class CalendarMetainfo extends ControlMetainfo {

	public static CalendarMetainfo create(Composite composite, EnumProperty label, Binding binding, boolean readOnly) {
		instance.applyDefaults();
		ControlMetainfo.setValues(instance, composite, label, binding, readOnly);
		return instance;
	}

	@Override
	public void restoreDefaults() {
		super.restoreDefaults();
		this.applyDefaults();
	}

	private CalendarMetainfo() {
		super();
		this.applyDefaults();
	}

	@Override
	public void applyDefaults() {
		super.applyDefaults();
		this.normalizedTime = new Hora(9, 0, 0); // por defecto, las 9 AM.
	}

	public Hora normalizedTime;

	private static CalendarMetainfo instance = new CalendarMetainfo();
}