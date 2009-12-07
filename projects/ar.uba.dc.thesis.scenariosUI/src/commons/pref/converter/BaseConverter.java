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
 * $Id: BaseConverter.java,v 1.3 2008/05/06 13:02:56 cvspasto Exp $
 */

package commons.pref.converter;

import sba.common.properties.EnumProperty;
import sba.common.properties.EnumPropertyDirectory;
import sba.common.properties.FakeEnumProperty;

import com.thoughtworks.xstream.io.HierarchicalStreamReader;

import commons.gui.util.PageHelper;

/**
 * Provee comportamiento común.
 *
 * @author Jonathan Chiocchio
 * @version $Revision: 1.3 $ $Date: 2008/05/06 13:02:56 $
 */

public abstract class BaseConverter {

	protected EnumProperty getEnumPropertyAttribute(
			HierarchicalStreamReader reader, String attributeName) {
		EnumProperty tableName;
		String attributeValue = reader.getAttribute(attributeName);
		try {
			tableName = ENUM_PROP_DIR.getEnum(attributeValue);
		} catch (Exception e) {
			// si no se encuentra el Enum dentro de los registrados, no falla la
			// aplicación
			tableName = new FakeEnumProperty("???" + attributeValue + "???");
		}
		return tableName;
	}

	private static final EnumPropertyDirectory ENUM_PROP_DIR = PageHelper
			.getMainWindow().getEnumPropertyDirectory();
}