package commons.gui.widget.creation.metainfo;

import org.eclipse.swt.widgets.Composite;

import commons.gui.widget.creation.binding.Binding;
import commons.properties.CommonLabels;
import commons.properties.EnumProperty;

/**
 * Modela la meta información inherente a un Control visual.
 * 
 */
public class ControlMetainfo {

	public Composite composite;

	public EnumProperty label;

	public boolean readOnly = false;

	public Binding binding;

	public int horizontalSpan = -1;

	public boolean validate = DEFAULT_VALIDATE;

	public Object layoutData;

	public static final boolean DEFAULT_VALIDATE = true;

	public ControlMetainfo(Composite composite, EnumProperty label, Binding binding, boolean readOnly) {
		super();
		this.composite = composite;
		this.label = label;
		this.binding = binding;
		this.readOnly = readOnly;
	}

	public void restoreDefaults() {
		this.label = CommonLabels.NO_LABEL;
	}

	protected ControlMetainfo() {
		super();
	}

	protected static void setValues(ControlMetainfo instance, Composite composite, EnumProperty label, Binding binding,
			boolean readOnly) {
		instance.composite = composite;
		instance.label = label;
		instance.binding = binding;
		instance.readOnly = readOnly;
	}

	public void applyDefaults() {
		this.binding = null;
		this.label = CommonLabels.NO_LABEL;
		this.layoutData = null;
		this.validate = DEFAULT_VALIDATE;
	}
}
