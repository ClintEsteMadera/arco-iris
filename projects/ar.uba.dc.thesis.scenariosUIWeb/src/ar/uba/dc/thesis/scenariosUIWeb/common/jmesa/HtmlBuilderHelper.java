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

import org.jmesa.view.html.HtmlBuilder;

/**
 * @author pc
 * @version $Revision: 1.8 $ $Date: 2009/04/27 15:53:27 $
 */
public final class HtmlBuilderHelper {

	public static HtmlBuilder addSelectOption(HtmlBuilder html, String optionValue,
	        String optionLabel, String filterValue) {
		html.option().value(optionValue);
		if (optionValue.equals(filterValue)) {
			html.selected();
		}
		html.close().append(optionLabel).optionEnd();
		return html;
	}

	private HtmlBuilderHelper() {
		super();
	}
}
