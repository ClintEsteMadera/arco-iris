/*
 * Licencia de Caja de Valores S.A., Versión 1.0
 *
 * Copyright (c) 2006 Caja de Valores S.A.
 * 25 de Mayo 362, Ciudad Autónoma de Buenos Aires, República Argentina
 * Todos los derechos reservados.
 *
 * Este software es información confidencial y propietaria de Caja de Valores S.A. ("Información
 * Confidencial"). Usted no divulgará tal Información Confidencial y la usará solamente de acuerdo a
 * los términos del acuerdo de licencia que posee con Caja de Valores S.A.
 */

/*
 * $Id: RadioButtonFactory.java,v 1.10 2008/03/13 13:37:17 cvspasto Exp $
 */

package commons.gui.widget.factory;

import org.eclipse.swt.widgets.Control;

import sba.common.utils.ClassUtils;

import commons.gui.model.types.EditConfiguration;
import commons.gui.model.types.EditConfigurationManager;
import commons.gui.model.types.EditType;
import commons.gui.model.types.EnumConfiguration;
import commons.gui.widget.RadioButtonGroup;
import commons.gui.widget.creation.binding.Binding;
import commons.gui.widget.creation.metainfo.RadioButtonMetainfo;

/**
 * @author Jonathan Chiocchio
 * @version $Revision: 1.10 $ $Date: 2008/03/13 13:37:17 $
 */

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
			final EditType eType = binding.getCompositeModel().getValueType(
					binding.getPropertyName());
			items = createLabelsAndValuesFrom(eType);
		}

		int selectedIndex = getSelectedIndex(metainfo, items);

		RadioButtonGroup radiogroup = new RadioButtonGroup(groupName, groupName,
				metainfo.numColumns, items, metainfo.composite, metainfo.useGroup, selectedIndex);

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
			Object currentValue = ClassUtils.getObject(metainfo.binding.getModel(),
					metainfo.binding.getPropertyName());
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

	private static Object[][] createLabelsAndValuesFrom(Class<? extends Enum> enumType) {
		Enum[] enumConstants = enumType.getEnumConstants();
		Object[][] lblsAndValues = new Object[enumConstants.length][2];
		for (int i = 0; i < enumConstants.length; i++) {
			lblsAndValues[i] = new Object[] { enumConstants[i].toString(), enumConstants[i] };
		}
		return lblsAndValues;
	}

	@SuppressWarnings("unchecked")
	private static Object[][] createLabelsAndValuesFrom(EditType eType) {
		final EditConfiguration config = EditConfigurationManager.getInstance().getConfiguration(
				eType);

		if (config == null || !(config instanceof EnumConfiguration)) {
			return null;
		}

		EnumConfiguration eConfig = (EnumConfiguration) config;

		final Object[] instances = eConfig.getInstances();
		final Object[][] lblsAndValues = new Object[instances.length][2];

		for (int i = 0; i < instances.length; i++) {
			lblsAndValues[i] = new Object[] { eConfig.getFormat().format(instances[i]),
					instances[i] };
		}
		return lblsAndValues;
	}
}