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
 * $Id: ComboValuesMetainfo.java,v 1.6 2008/03/26 15:57:28 cvsmarco Exp $
 */
package commons.gui.widget.creation.metainfo;

import java.util.LinkedHashMap;

/**
 * 
 * @author Gabriel Tursi
 * @version $Revision: 1.6 $ $Date: 2008/03/26 15:57:28 $
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

	public static <T extends Enum> ComboValuesMetainfo create(
			LinkedHashMap<String, Object> codedItems) {
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
