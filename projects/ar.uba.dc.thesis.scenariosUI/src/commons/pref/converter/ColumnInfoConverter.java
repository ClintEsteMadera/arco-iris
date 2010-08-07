package commons.pref.converter;

import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import commons.gui.widget.Alignment;
import commons.pref.domain.ColumnInfo;
import commons.properties.EnumProperty;

/**
 * Converter para el objeto ColumnInfo. Su responsabilidad doble: dada una representación XML del objeto, crear una
 * instancia de ColumnInfo y viceversa.
 * 
 */

public class ColumnInfoConverter extends BaseConverter implements Converter {

	public static ColumnInfoConverter getInstance() {
		return instance;
	}

	public boolean canConvert(Class type) {
		return type.equals(ColumnInfo.class);
	}

	public void marshal(Object obj, HierarchicalStreamWriter writer, MarshallingContext context) {
		ColumnInfo columnInfo = (ColumnInfo) obj;
		writer.addAttribute(LABEL, columnInfo.getLabel().name());
		writer.addAttribute(NAME, columnInfo.getFieldName());
		if (columnInfo.getAlignment() != null) {
			writer.addAttribute(ALIGNMENT, columnInfo.getAlignment().toString());
		}
		if (columnInfo.getWidth() != null) {
			writer.addAttribute(WIDTH, columnInfo.getWidth().toString());
		}
	}

	public Object unmarshal(HierarchicalStreamReader reader, UnmarshallingContext context) {
		EnumProperty label = this.getEnumPropertyAttribute(reader, LABEL);
		String name = reader.getAttribute(NAME);
		String sAlign = reader.getAttribute(ALIGNMENT);
		Alignment alignment = sAlign == null ? null : Alignment.valueOf(sAlign);

		String sWidth = reader.getAttribute(WIDTH);
		Integer width = sWidth == null ? null : Integer.valueOf(sWidth);

		return new ColumnInfo(label, name, alignment, width);
	}

	private ColumnInfoConverter() {
		super();
	}

	private static ColumnInfoConverter instance = new ColumnInfoConverter();

	private static final String LABEL = "label";

	private static final String NAME = "name";

	private static final String ALIGNMENT = "alignment";

	private static final String WIDTH = "width";

}