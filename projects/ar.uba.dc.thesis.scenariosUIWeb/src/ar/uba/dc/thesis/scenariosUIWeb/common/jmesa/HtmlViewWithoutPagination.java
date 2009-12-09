/*
 * $Id: HtmlViewWithoutPagination.java,v 1.5 2009/04/30 18:37:14 cvsuribe Exp $
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

import org.jmesa.view.html.AbstractHtmlView;
import org.jmesa.view.html.HtmlBuilder;
import org.jmesa.view.html.HtmlSnippets;

/**
 * @see <a href="http://code.google.com/p/jmesa/wiki/TableWithoutPagination">TableWithoutPagination
 * </a>
 */
public class HtmlViewWithoutPagination extends AbstractHtmlView {
	public Object render() {
		HtmlSnippets snippets = getHtmlSnippets();
		HtmlBuilder html = new HtmlBuilder();
		html.append(snippets.themeStart());
		html.append(snippets.tableStart());
		html.append(snippets.theadStart());
		html.append(snippets.filter());
		html.append(snippets.header());
		html.append(snippets.theadEnd());
		html.append(snippets.tbodyStart());
		html.append(snippets.body());
		html.append(snippets.tbodyEnd());
		html.append(snippets.footer());
		html.append(snippets.tableEnd());
		html.append(snippets.themeEnd());
		html.append(snippets.initJavascriptLimit());
		return html.toString();
	}
}
