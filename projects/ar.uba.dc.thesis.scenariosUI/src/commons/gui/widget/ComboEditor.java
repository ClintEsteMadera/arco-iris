package commons.gui.widget;

import java.text.Format;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;

import commons.gui.binding.ComboModel;
import commons.gui.model.Attribute;
import commons.gui.model.ValueChangeEvent;
import commons.gui.model.ValueChangeListener;
import commons.gui.model.ValueHolder;
import commons.gui.model.ValueModel;
import commons.gui.model.bean.BeanAttribute;
import commons.gui.model.types.DefaultFormat;
import commons.gui.model.types.EditConfiguration;
import commons.gui.model.types.EditConfigurationManager;
import commons.gui.model.types.EditType;
import commons.gui.model.types.EnumConfiguration;

/**
 * Wrapper de Combo<br>
 * TODO: OPTIMIZACIONES - Cachear los textos, Agregar métodos para setear un valor, obtener el valor seleccionado, etc.
 * 
 * @author Pablo Pastorino
 */
public class ComboEditor {

	public ComboEditor(Composite parent, int style) {
		combo = new Combo(parent, style);
		init();
	}

	public ComboEditor(Combo combo) {
		this.combo = combo;
		init();
	}

	public String getNullText() {
		return nullText;
	}

	public Combo getCombo() {
		return combo;
	}

	public void setNullText(String nullText) {
		this.nullText = nullText;
	}

	public String getValueProperty() {
		return valueProperty;
	}

	public void setValueProperty(String valueProperty) {
		this.valueProperty = valueProperty;
	}

	/**
	 * Obtiene el ValueModel que contiene la lista de items.
	 * 
	 * @return
	 */
	public ValueModel getItemListModel() {
		return listModel;
	}

	/**
	 * Asigna la lista de items
	 * 
	 * @param items
	 */
	@SuppressWarnings("unchecked")
	public void setItemList(List items) {
		if (this.listModel == null) {
			this.setItemListModel(new ValueHolder<Object>(Object.class));
		}
		this.listModel.setValue(items);

	}

	/**
	 * Asigna la lista de items
	 * 
	 * @param array
	 */
	public void setItemList(Object[] array) {
		setItemList(Arrays.asList(array));
	}

	/**
	 * Setea la lista de items tomandolo de la meta información.
	 * 
	 * @param eType
	 */
	@SuppressWarnings("unchecked")
	public void setItemsFromMetadata(EditType eType) {

		final EditConfiguration config = EditConfigurationManager.getInstance().getConfiguration(eType);

		EnumConfiguration eConfig = null;

		if (config != null && config instanceof EnumConfiguration) {
			eConfig = (EnumConfiguration) config;
		}

		if (eConfig != null) {
			this.setItemList(eConfig.getInstances());
			this.setItemFormat(eConfig.getFormat());
		}
	}

	/**
	 * Asigna el ValueModel que contiene la lista de items. Este ValueModel debe contener objetos de tipo List o Array.
	 * 
	 * @param listModel
	 */
	public void setItemListModel(ValueModel listModel) {
		if (listModel == this.listModel) {
			return;
		}

		if (this.listModel != null) {
			this.listModel.removeValueChangeListener(listListener);
		}

		this.listModel = listModel;

		if (this.listModel != null) {
			this.listModel.addValueChangeListener(listListener);
		}
	}

	/**
	 * Obtiene la lista de items
	 * 
	 * @return
	 */
	public List getItemList() {
		final Object value = listModel != null ? listModel.getValue() : Collections.EMPTY_LIST;

		if (value instanceof List) {
			return (List) value;
		}

		if (value instanceof Object[]) {
			return Arrays.asList((Object[]) value);
		}

		throw new IllegalArgumentException("El valor de los items debe ser de tipo lista");
	}

	public Format getItemFormat() {
		return itemFormat;
	}

	public void setItemFormat(Format itemFormat) {
		this.itemFormat = itemFormat;
		updateItems();
	}

	/**
	 * Obtiene el modelo utilizado por el framework de binding.
	 * 
	 * @return
	 */
	public ComboModelImpl getComboModel() {
		return comboModel;
	}

	private void updateItems() {
		this.combo.removeAll();

		if (this.nullText != null) {
			this.combo.add(this.nullText);
		}

		final List items = getItemList();

		if (items == null) {
			return;
		}

		for (Iterator it = items.iterator(); it.hasNext();) {
			this.combo.add(formatValue(it.next()));
		}
	}

	private String formatValue(Object o) {
		return this.itemFormat != null ? this.itemFormat.format(o) : DefaultFormat.getInstance().format(o);
	}

	private void init() {
		// listener para mantener actualizada la lista de items
		this.listListener = new ValueChangeListener() {
			public void valueChange(ValueChangeEvent ev) {
				updateItems();
			}
		};

		// modelo para binding
		this.comboModel = new ComboModelImpl();
	}

	/**
	 * Implementación de ComboModel para binding
	 * 
	 */
	private class ComboModelImpl implements ComboModel {
		public Object getItem(int index) {
			final List items = getItemList();
			return items.get(index);
		}

		public Object getItemValue(int index) {
			return getItemValue(getItem(index));
		}

		public int getItemCount() {
			final List items = getItemList();
			return items.size();
		}

		public String getItemText(int index) {
			final List items = getItemList();
			return format(items.get(index));
		}

		public String getNullText() {
			return ComboEditor.this.nullText;
		}

		private String format(Object o) {
			return ComboEditor.this.formatValue(o);
		}

		private Object getItemValue(Object item) {
			if (ComboEditor.this.valueProperty == null) {
				return item;
			}

			if (item == null) {
				return null;
			}

			return getPropertyAttribute(item.getClass(), ComboEditor.this.valueProperty).getValue(item);
		}

		private Attribute getPropertyAttribute(Class itemClass, String valueProp) {
			if (this.valueAttribute == null || !this.valueAttribute.getAttributeClass().equals(itemClass)
					|| !this.valueAttribute.getPropertyName().equals(valueProp)) {
				this.valueAttribute = new BeanAttribute(itemClass, valueProp, null);
			}
			return this.valueAttribute;
		}

		private BeanAttribute valueAttribute;
	}

	private Combo combo;

	private String nullText;

	private ValueModel listModel;

	private Format itemFormat;

	private String valueProperty;

	private ValueChangeListener listListener;

	private ComboModelImpl comboModel;
}
