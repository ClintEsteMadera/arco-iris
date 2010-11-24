package commons.pref.converter;

import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import commons.gui.util.PageHelper;
import commons.properties.EnumProperty;
import commons.properties.EnumPropertyDirectory;
import commons.properties.FakeEnumProperty;

/**
 * Provee comportamiento común.
 * 
 * 
 */

public abstract class BaseConverter {

	protected EnumProperty getEnumPropertyAttribute(HierarchicalStreamReader reader, String attributeName) {
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

	private static final EnumPropertyDirectory ENUM_PROP_DIR = PageHelper.getMainWindow().getEnumPropertyDirectory();
}