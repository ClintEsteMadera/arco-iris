package commons.gui.widget.factory;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

import commons.gui.model.CompositeModel;
import commons.gui.model.types.EditConfiguration;
import commons.gui.model.types.EditConfigurationManager;
import commons.gui.model.types.EditType;
import commons.gui.model.validation.ValidationManager;
import commons.gui.widget.ComboEditor;
import commons.gui.widget.DefaultLayoutFactory;
import commons.gui.widget.creation.metainfo.ComboMetainfo;
import commons.properties.CommonLabels;

public abstract class ComboFactory {

	public static final String COMBO_EDITOR_KEY = ComboEditor.class.getName();

	public static final ComboEditor getEditor(Control control) {
		return (ComboEditor) control.getData(COMBO_EDITOR_KEY);
	}

	@SuppressWarnings("unchecked")
	public static <T extends Enum> Control createCombo(ComboMetainfo comboMetainfo) {
		Control combo = createControl(comboMetainfo);

		if (combo instanceof Combo) {
			final ComboEditor comboEditor = createComboEditor((Combo) combo, comboMetainfo);

			if (comboMetainfo.binding != null) {
				comboMetainfo.binding.bind(comboEditor);
			}
			combo.setData(COMBO_EDITOR_KEY, comboEditor);

			Combo comboBox = (Combo) combo;
			if (comboBox.getItemCount() == 1) {
				comboBox.select(0);
			}
		}

		comboMetainfo.restoreDefaults();
		return combo;
	}

	@SuppressWarnings("unchecked")
	private static ComboEditor createComboEditor(Combo combo, ComboMetainfo comboMetainfo) {

		final ComboEditor editor = new ComboEditor(combo);

		if (comboMetainfo.addEmptyOption) {
			editor.setNullText(comboMetainfo.nullText);
		}

		if (comboMetainfo.itemFormat != null) {
			editor.setItemFormat(comboMetainfo.itemFormat);
		}

		if (comboMetainfo.valueProperty != null) {
			editor.setValueProperty(comboMetainfo.valueProperty);
		}

		EditType valueType = null;

		if (comboMetainfo.binding != null && !comboMetainfo.binding.isFakeBinding()) {
			final CompositeModel model = comboMetainfo.binding.getCompositeModel();
			final Object propertyName = comboMetainfo.binding.getPropertyName();

			if (model != null && propertyName != null) {
				valueType = model.getValueType(propertyName);
			}
		}

		if (comboMetainfo.items != null) {
			editor.setItemList(comboMetainfo.items);

			// si no hay formato especificado lo deduzco del tipo bindeado binding
			if (valueType != null && comboMetainfo.itemFormat == null) {
				final EditConfiguration config = EditConfigurationManager.getInstance()
						.getConfiguration(valueType);

				if (config != null && config.getFormat() != null) {
					editor.setItemFormat(config.getFormat());
				}
			}
		} else if (valueType != null) {
			// deduzco los items y el formato del tipo bindeado
			editor.setItemsFromMetadata(valueType);
		}

		comboMetainfo.restoreDefaults();
		return editor;
	}

	/**
	 * Crea el control Combo sin ítems.
	 * @param comboInfo
	 *            la metainformación necesaria para poder crear el control.
	 * @return un Label en caso de que <code>comboInfo.readOnly</code> sea verdadero, un Combo en
	 *         otro caso.
	 */
	private static Control createControl(ComboMetainfo comboInfo) {
		final Control combo;
		if (comboInfo.readOnly) {
			combo = LabelFactory.createReadOnlyField(comboInfo.composite, comboInfo.binding,
					comboInfo.label);
		} else {
			if (!comboInfo.label.equals(CommonLabels.NO_LABEL)) {
				LabelFactory.createLabel(comboInfo.composite, comboInfo.label, false, true);
			}
			combo = createComboControl(comboInfo);

			((Combo) combo).setVisibleItemCount(comboInfo.visibleItemCount);
		}
		return combo;
	}

	private static Combo createComboControl(ComboMetainfo metainfo) {
		
		if(!metainfo.validate){
			return _createComboControl(metainfo);
		}

		final Composite parent=metainfo.composite;
		final Composite composite=new Composite(parent,SWT.NONE);
		
		DefaultLayoutFactory.setDefaultGridLayout(composite, 2);
		composite.setLayoutData(null);
		metainfo.composite=composite;
		
		final Combo combo =_createComboControl(metainfo);
		
		metainfo.composite=parent;
		
		LabelStatusAware status=new LabelStatusAware(composite);
		ValidationManager.setValidationProperty(combo, metainfo.binding);
		ValidationManager.setValidationStatus(combo, status);

		return combo;
	}

	private static Combo _createComboControl(ComboMetainfo comboInfo) {
		return new Combo(comboInfo.composite, SWT.READ_ONLY);
	}
	
	public static final String EMPTY_ITEM = "";
}
