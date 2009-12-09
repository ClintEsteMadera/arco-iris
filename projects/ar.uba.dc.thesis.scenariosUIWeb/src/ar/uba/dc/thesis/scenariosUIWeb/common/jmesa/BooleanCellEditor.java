/*
 * $Id: BooleanCellEditor.java,v 1.1 2009/05/13 20:11:35 cvstursi Exp $
 *
 * Licencia de Caja de Valores S.A., Versión 1.0
 * Copyright (c) 2006 Caja de Valores S.A.
 * 25 de Mayo 362, Ciudad Autónoma de Buenos Aires, República Argentina
 * Todos los derechos reservados.
 * Este software es información confidencial y propietaria de Caja de Valores S.A. ("Información
 * Confidencial"). Usted no divulgará tal Información Confidencial y la usará solamente de acuerdo a
 * los términos del acuerdo de licencia que posee con Caja de Valores S.A.
 */
package ar.uba.dc.thesis.scenariosUIWeb.common.jmesa;

import org.jmesa.util.ItemUtils;
import org.jmesa.view.editor.CellEditor;
import org.jmesa.view.html.HtmlBuilder;

/**
 * @author gtursi
 * @version $Revision: 1.1 $ $Date: 2009/05/13 20:11:35 $
 */
public class BooleanCellEditor implements CellEditor {

	public Object getValue(Object item, String property, int rowcount) {
		Object itemValue = null;
		itemValue = ItemUtils.getItemValue(item, property);
		HtmlBuilder html = new HtmlBuilder().input().disabled().type("checkbox");
		if (itemValue != null && Boolean.TRUE.equals(itemValue)) {
			html.checked();
		}
		html.close();
		return html.toString();
	}

}
