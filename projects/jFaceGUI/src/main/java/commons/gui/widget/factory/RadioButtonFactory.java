package commons.gui.widget.factory;

import org.eclipse.swt.widgets.Control;

import commons.gui.model.types.EditConfiguration;
import commons.gui.model.types.EditConfigurationManager;
import commons.gui.model.types.EditType;
import commons.gui.model.types.EnumConfiguration;
import commons.gui.widget.RadioButtonGroup;
import commons.gui.widget.creation.binding.Binding;
import commons.gui.widget.creation.metainfo.RadioButtonMetainfo;
import commons.utils.ClassUtils;

public abstract class RadioButtonFactory {

	public static Control createRadioButtonGroup(RadioButtonMetainfo metainfo) {
		String groupName = "";
		Binding binding = metainfo.binding;

		if (!metainfo.emptyGroupName) {
			groupName = metainfo.label.toString();
		}

		Object[][] items = null;

		if (metainfo.useCodedItems) {
			items = metainfo.codedItems;
		} else if (metainfo.enumClass != null) {
			items = createLabelsAndValuesFrom(metainfo.enumClass);
		} else {
			@SuppressWarnings("rawtypes")
			final EditType eType = binding.getCompositeModel().getValueType(binding.getPropertyName());
			items = createLabelsAndValuesFrom(eType);
		}

		int selectedIndex = getSelectedIndex(metainfo, items);

		RadioButtonGroup radiogroup = new RadioButtonGroup(groupName, groupName, metainfo.numColumns, items,
				metainfo.composite, metainfo.useGroup, selectedIndex);

		if (!metainfo.readOnly && binding != null && !binding.isFakeBinding()) {
			binding.bind(radiogroup);
		}

		radiogroup.setReadOnly(metainfo.readOnly);

		return radiogroup.getRadioBox();
	}

	private static int getSelectedIndex(RadioButtonMetainfo metainfo, Object[][] items) {
		int selectedIndex = metainfo.selectedIndex;
		if (metainfo.selectedValue != null) {
			selectedIndex = getSelectedIndexFrom(metainfo.selectedValue, items);
		} else if (!metainfo.binding.isFakeBinding()) {
			Object currentValue = ClassUtils.getObject(metainfo.binding.getModel(), metainfo.binding.getPropertyName());
			if (currentValue != null) {
				selectedIndex = getSelectedIndexFrom(currentValue, items);
			}
		}
		return selectedIndex;
	}

	private static int getSelectedIndexFrom(Object selectedValue, Object[][] items) {
		Object currentValue;
		int selectedIndex = 0;
		for (int i = 0; i < items.length; i++) {
			currentValue = items[i][1];
			if (currentValue.equals(selectedValue)) {
				selectedIndex = i;
				break;
			}
		}
		return selectedIndex;
	}

	@SuppressWarnings("rawtypes")
	private static Object[][] createLabelsAndValuesFrom(Class<? extends Enum> enumType) {
		Enum[] enumConstants = enumType.getEnumConstants();
		Object[][] lblsAndValues = new Object[enumConstants.length][2];
		for (int i = 0; i < enumConstants.length; i++) {
			lblsAndValues[i] = new Object[] { enumConstants[i].toString(), enumConstants[i] };
		}
		return lblsAndValues;
	}

	@SuppressWarnings("rawtypes")
	private static Object[][] createLabelsAndValuesFrom(EditType eType) {
		@SuppressWarnings("unchecked")
		final EditConfiguration config = EditConfigurationManager.getInstance().getConfiguration(eType);

		if (config == null || !(config instanceof EnumConfiguration)) {
			return null;
		}

		EnumConfiguration eConfig = (EnumConfiguration) config;

		final Object[] instances = eConfig.getInstances();
		final Object[][] lblsAndValues = new Object[instances.length][2];

		for (int i = 0; i < instances.length; i++) {
			lblsAndValues[i] = new Object[] { eConfig.getFormat().format(instances[i]), instances[i] };
		}
		return lblsAndValues;
	}
}