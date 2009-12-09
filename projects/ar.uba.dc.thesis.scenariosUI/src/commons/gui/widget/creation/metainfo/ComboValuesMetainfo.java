/*
 * Licencia de Caja de Valores S.A., Versi�n 1.0
 *
 * Copyright (c) 2006 Caja de Valores S.A.
 * 25 de Mayo 362, Ciudad Aut�noma de Buenos Aires, Rep�blica Argentina
 * Todos los derechos reservados.
 *
 * Este software es informaci�n confidencial y propietaria de Caja de Valores S.A. ("Informaci�n
 * Confidencial"). Usted no divulgar� tal Informaci�n Confidencial y la usar� solamente de acuerdo a
 * los t�rminos del acuerdo de licencia que posee con Caja de Valores S.A.
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
	 * Indica la cantidad de �tems a mostrar al desplegar el combo.
	 */
	public int visibleItemCount = DEFAULT_VISIBLE_ITEM_COUNT;

	public Enum[] enumsMostrables;

	public Class<? extends Enum> enumClass;

	public String[] items;

	public LinkedHashMap<String, Object> codedItems;

}