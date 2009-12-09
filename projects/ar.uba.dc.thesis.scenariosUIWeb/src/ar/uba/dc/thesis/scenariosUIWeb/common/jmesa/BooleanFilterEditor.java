/*
 * $Id: org.eclipse.jdt.ui.prefs,v 1.5 2009/04/23 19:56:32 cvsuribe Exp $
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

import static org.apache.commons.lang.StringEscapeUtils.escapeHtml;
import static org.apache.commons.lang.StringEscapeUtils.escapeJavaScript;
import org.jmesa.limit.Filter;
import org.jmesa.limit.Limit;
import org.jmesa.view.editor.AbstractFilterEditor;
import org.jmesa.view.html.HtmlBuilder;
import org.jmesa.view.html.component.HtmlColumn;

/**
 * @author pc
 * @version $Revision: 1.8 $ $Date: 2009/04/27 15:53:27 $
 */
public class BooleanFilterEditor extends AbstractFilterEditor {

	// java.lang.NoSuchMethodError:
	// org.jmesa.view.html.editor.DroplistFilterEditor.addOption(Ljava/lang/String;Ljava/lang/String;)V
	// public BooleanFilterEditor() {
	// super();
	// this.addOption(new Option("", "Todos"));
	// this.addOption(new Option("true", "Sí"));
	// this.addOption(new Option("false", "No"));
	// }

	public Object getValue() {
		HtmlBuilder html = new HtmlBuilder();

		Limit limit = getCoreContext().getLimit();
		HtmlColumn column = (HtmlColumn) getColumn();
		String property = column.getProperty();
		Filter filter = limit.getFilterSet().getFilter(property);

		StringBuilder array = new StringBuilder();
		array.append("{");

		addOption(array, "", "Todos");
		array.append(",");
		addOption(array, "true", "Sí");
		array.append(",");
		addOption(array, "false", "No");

		array.append("}");

		html.div().styleClass("dynFilter");
		html.onclick("jQuery.jmesa.createDroplistDynFilter(this,'" + limit.getId() + "','"
		        + column.getProperty() + "'," + array + ")");
		html.close();
		showSelected(html, filter);
		html.divEnd();

		return html.toString();
	}

	private void addOption(StringBuilder array, String value, String label) {
		String escapedValue = escapeJavaScript(escapeHtml(value));
		String escapedLabel = escapeJavaScript(escapeHtml(label));
		array.append("'").append(escapedValue).append("':'").append(escapedLabel).append("'");
	}

	private void showSelected(HtmlBuilder html, Filter filter) {
		String filterValue = "";
		if (filter != null) {
			filterValue = filter.getValue();
			if (filterValue.equals("true")) {
				html.append("Sí");
			} else if (filterValue.equals("false")) {
				html.append("No");
			} else {
				html.append("Todos");
			}
		}
	}
}
