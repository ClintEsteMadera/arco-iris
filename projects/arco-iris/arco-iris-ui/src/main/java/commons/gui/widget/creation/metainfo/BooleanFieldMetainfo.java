package commons.gui.widget.creation.metainfo;

import org.eclipse.swt.widgets.Composite;

import commons.gui.widget.Alignment;
import commons.gui.widget.creation.binding.Binding;
import commons.properties.EnumProperty;

public class BooleanFieldMetainfo extends ControlMetainfo {

	public Alignment alignment = Alignment.LEFT;

	private static BooleanFieldMetainfo instance = new BooleanFieldMetainfo();

	/**
	 * This class is intended to be a singleton
	 */
	private BooleanFieldMetainfo() {
		super();
		this.applyDefaults();
	}

	public static BooleanFieldMetainfo create(Composite composite, EnumProperty label, Binding binding, boolean readOnly) {
		ControlMetainfo.setValues(instance, composite, label, binding, readOnly);
		return instance;
	}

	@Override
	public void restoreDefaults() {
		super.restoreDefaults();
		applyDefaults();
	}
}