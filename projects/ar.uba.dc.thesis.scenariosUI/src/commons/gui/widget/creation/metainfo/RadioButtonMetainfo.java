package commons.gui.widget.creation.metainfo;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.swt.widgets.Composite;

import commons.gui.widget.creation.binding.Binding;
import commons.properties.EnumProperty;

/**
 * 
 */

public class RadioButtonMetainfo extends ControlMetainfo {

	public static RadioButtonMetainfo create(Composite composite, EnumProperty label, Binding binding, boolean readOnly) {
		instance.applyDefaults();
		ControlMetainfo.setValues(instance, composite, label, binding, readOnly);
		return instance;
	}

	@Override
	public void restoreDefaults() {
		super.restoreDefaults();
		applyDefaults();
	}

	@Override
	public void applyDefaults() {
		super.applyDefaults();
		this.emptyGroupName = false;
		this.useGroup = true;
		this.useCodedItems = false;
		this.codedItems = null;
		this.enumClass = null;
		this.numColumns = 2;
		this.selectedIndex = 0;
		this.selectedValue = null;
		this.listeners.clear();
	}

	private RadioButtonMetainfo() {
		super();
		this.listeners = new ArrayList<IPropertyChangeListener>();
		this.applyDefaults();
	}

	private static RadioButtonMetainfo instance = new RadioButtonMetainfo();

	public boolean emptyGroupName;

	public boolean useGroup;

	public boolean useCodedItems;

	public Object[][] codedItems;

	public Class<? extends Enum> enumClass;

	public int numColumns;

	public int selectedIndex;

	public Object selectedValue;

	public List<IPropertyChangeListener> listeners;
}