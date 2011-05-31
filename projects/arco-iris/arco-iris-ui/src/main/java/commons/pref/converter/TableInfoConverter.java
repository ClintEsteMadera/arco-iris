package commons.pref.converter;

import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import commons.pref.domain.ColumnInfo;
import commons.pref.domain.TableInfo;

/**
 * Converts between a TableInfo and its representation in XML.
 */
public class TableInfoConverter extends BaseConverter implements Converter {

	private static TableInfoConverter instance = new TableInfoConverter();

	private static final String NAME = "name";

	private static final String ORDER = "order";

	public static TableInfoConverter getInstance() {
		return instance;
	}

	private TableInfoConverter() {
		super();
	}

	public boolean canConvert(Class type) {
		return type.equals(TableInfo.class);
	}

	public void marshal(Object obj, HierarchicalStreamWriter writer, MarshallingContext context) {
		TableInfo tableInfo = (TableInfo) obj;
		writer.addAttribute(NAME, tableInfo.getName().name());
		if (tableInfo.getOrder() != null) {
			writer.addAttribute(ORDER, tableInfo.getOrder());
		}
		context.convertAnother(tableInfo.getColumnInfos());
	}

	public Object unmarshal(HierarchicalStreamReader reader, UnmarshallingContext context) {
		TableInfo tableInfo = new TableInfo();
		tableInfo.setName(this.getEnumPropertyAttribute(reader, NAME));
		tableInfo.setOrder(reader.getAttribute(ORDER));
		ColumnInfo[] columnInfos = (ColumnInfo[]) context.convertAnother(tableInfo, ColumnInfo[].class);
		tableInfo.setColumnInfos(columnInfos);

		return tableInfo;
	}
}