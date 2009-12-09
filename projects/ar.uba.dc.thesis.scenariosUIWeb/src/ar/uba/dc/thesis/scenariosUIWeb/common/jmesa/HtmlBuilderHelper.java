/*
 * $Id: org.eclipse.jdt.ui.prefs,v 1.5 2009/04/23 19:56:32 cvsuribe Exp $
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
