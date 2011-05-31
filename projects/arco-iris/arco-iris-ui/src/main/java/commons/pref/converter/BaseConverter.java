package commons.pref.converter;

import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import commons.gui.util.PageHelper;
import commons.properties.EnumProperty;
import commons.properties.EnumPropertyDirectory;
import commons.properties.FakeEnumProperty;

public abstract class BaseConverter {

	private static final EnumPropertyDirectory ENUM_PROP_DIR = PageHelper.getMainWindow().getEnumPropertyDirectory();

	protected EnumProperty getEnumPropertyAttribute(HierarchicalStreamReader reader, String attributeName) {
		EnumProperty tableName;
		String attributeValue = reader.getAttribute(attributeName);
		try {
			tableName = ENUM_PROP_DIR.getEnum(attributeValue);
		} catch (Exception e) {
			tableName = new FakeEnumProperty("???" + attributeValue + "???");
		}
		return tableName;
	}
}