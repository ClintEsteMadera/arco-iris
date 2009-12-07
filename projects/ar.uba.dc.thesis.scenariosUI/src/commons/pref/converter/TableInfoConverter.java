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
 * $Id: TableInfoConverter.java,v 1.3 2008/02/13 18:43:39 cvschioc Exp $
 */

package commons.pref.converter;

import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import commons.pref.domain.ColumnInfo;
import commons.pref.domain.TableInfo;

/**
 * Converter para el objeto TableInfo. Su responsabilidad doble: dada una representaci�n XML del
 * objeto, crear una instancia de TableInfo y viceversa.
 * @author Jonathan Chiocchio
 * @version $Revision: 1.3 $ $Date: 2008/02/13 18:43:39 $
 */

public class TableInfoConverter extends BaseConverter implements Converter {

	public static TableInfoConverter getInstance() {
		return instance;
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
		ColumnInfo[] columnInfos = (ColumnInfo[]) context.convertAnother(tableInfo,
				ColumnInfo[].class);
		tableInfo.setColumnInfos(columnInfos);

		return tableInfo;
	}

	private TableInfoConverter() {
		super();
	}

	private static TableInfoConverter instance = new TableInfoConverter();

	private static final String NAME = "name";

	private static final String ORDER = "order";
}