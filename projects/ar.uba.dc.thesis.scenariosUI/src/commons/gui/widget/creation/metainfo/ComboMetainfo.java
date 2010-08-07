package commons.gui.widget.creation.metainfo;

import java.text.Format;

import org.eclipse.swt.widgets.Composite;

import commons.gui.widget.creation.binding.Binding;
import commons.properties.EnumProperty;

/**
 * 
 */
public class ComboMetainfo extends ControlMetainfo {

	private static final ComboMetainfo instance = new ComboMetainfo();

	public static ComboMetainfo create(Composite composite, EnumProperty label, Binding binding, boolean readOnly) {
		ControlMetainfo.setValues(instance, composite, label, binding, readOnly);
		return instance;
	}

	@Override
	public void restoreDefaults() {
		super.restoreDefaults();
		this.items = null;
		this.nullText = "";
		this.itemFormat = null;
		this.addEmptyOption = true;
	}

	public static final int DEFAULT_VISIBLE_ITEM_COUNT = 25;

	/**
	 * Indica la cantidad de ítems a mostrar al desplegar el combo.
	 */
	public int visibleItemCount = DEFAULT_VISIBLE_ITEM_COUNT;

	/**
	 * Indica si debe agregarse una opción para el valor 'nulo'
	 */
	public boolean addEmptyOption = true;

	/**
	 * En caso de agregarse la opción para valor nulo, indica el texto utilizado
	 */
	public String nullText = "";

	/**
	 * Format utilziado para obtener el texto asociado a los items
	 */
	public Format itemFormat;

	/**
	 * Lista de items para poblar el combo
	 */
	public Object[] items;

	/**
	 * Nombre de la propiedad que se utiliza para extraer del item seleccionado el valor a bindear. Se utiliza por
	 * ejemplo para setear un 'id' en un objeto con el 'id' del objeto seleccionado en el combo.
	 */
	public String valueProperty;
}