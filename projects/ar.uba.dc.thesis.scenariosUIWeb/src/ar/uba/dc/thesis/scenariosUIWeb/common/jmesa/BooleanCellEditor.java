/*
 * $Id: BooleanCellEditor.java,v 1.1 2009/05/13 20:11:35 cvstursi Exp $
 *
 * Licencia de Caja de Valores S.A., Versi�n 1.0
 * Copyright (c) 2006 Caja de Valores S.A.
 * 25 de Mayo 362, Ciudad Aut�noma de Buenos Aires, Rep�blica Argentina
 * Todos los derechos reservados.
 * Este software es informaci�n confidencial y propietaria de Caja de Valores S.A. ("Informaci�n
 * Confidencial"). Usted no divulgar� tal Informaci�n Confidencial y la usar� solamente de acuerdo a
 * los t�rminos del acuerdo de licencia que posee con Caja de Valores S.A.
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
