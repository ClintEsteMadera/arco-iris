package commons.gui.widget.creation.metainfo;

import java.util.LinkedHashMap;

/**
 * 
 * 
 */
public class ComboValuesMetainfo {

	private static final ComboValuesMetainfo instance = new ComboValuesMetainfo();

	public static <T extends Enum> ComboValuesMetainfo create(T[] enumsMostrables) {
		restoreDefaults();
		instance.enumsMostrables = enumsMostrables;
		return instance;
	}

	public static <T extends Enum> ComboValuesMetainfo create(Class<T> enumClass) {
		restoreDefaults();
		instance.enumClass = enumClass;
		return instance;
	}

	public static <T extends Enum> ComboValuesMetainfo create(String[] items) {
		restoreDefaults();
		instance.items = items;
		return instance;
	}

	public static <T extends Enum> ComboValuesMetainfo create(LinkedHashMap<String, Object> codedItems) {
		restoreDefaults();
		instance.codedItems = codedItems;
		return instance;
	}

	private static void restoreDefaults() {
		instance.items = null;
		instance.enumsMostrables = null;
		instance.enumClass = null;
		instance.codedItems = null;
	}

	private ComboValuesMetainfo() {
		super();
	}

	public boolean useCodedItems() {
		return codedItems != null;
	}

	public boolean useEnums() {
		return enumClass != null || enumsMostrables != null;
	}

	public boolean useStringItems() {
		return items != null;
	}

	public static final int DEFAULT_VISIBLE_ITEM_COUNT = 25;

	/**
	 * Indica la cantidad de ítems a mostrar al desplegar el combo.
	 */
	public int visibleItemCount = DEFAULT_VISIBLE_ITEM_COUNT;

	public Enum[] enumsMostrables;

	public Class<? extends Enum> enumClass;

	public String[] items;

	public LinkedHashMap<String, Object> codedItems;

}
