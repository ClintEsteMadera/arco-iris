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
 * $Id: ColumnInfoConverter.java,v 1.2 2008/02/13 18:43:38 cvschioc Exp $
 */

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
 * Converter para el objeto ColumnInfo. Su responsabilidad doble: dada una representación XML del
 * objeto, crear una instancia de ColumnInfo y viceversa.
 * @author Jonathan Chiocchio
 * @version $Revision: 1.2 $ $Date: 2008/02/13 18:43:38 $
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