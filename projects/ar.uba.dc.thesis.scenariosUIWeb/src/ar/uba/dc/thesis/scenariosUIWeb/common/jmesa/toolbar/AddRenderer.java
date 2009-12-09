/*
 * $Id: AddRenderer.java,v 1.4 2009/04/30 18:37:14 cvsuribe Exp $
 *
 * Licencia de Caja de Valores S.A., Versi�n 1.0
 * Copyright (c) 2006 Caja de Valores S.A.
 * 25 de Mayo 362, Ciudad Aut�noma de Buenos Aires, Rep�blica Argentina
 * Todos los derechos reservados.
 * Este software es informaci�n confidencial y propietaria de Caja de Valores S.A. ("Informaci�n
 * Confidencial"). Usted no divulgar� tal Informaci�n Confidencial y la usar� solamente de acuerdo a
 * los t�rminos del acuerdo de licencia que posee con Caja de Valores S.A.
 */
package ar.uba.dc.thesis.scenariosUIWeb.common.jmesa.toolbar;

import org.jmesa.core.CoreContext;
import org.jmesa.view.html.toolbar.AbstractItemRenderer;
import org.jmesa.view.html.toolbar.ToolbarItem;

public class AddRenderer extends AbstractItemRenderer {
	public AddRenderer(ToolbarItem item, CoreContext coreContext, String addUrl) {
		setToolbarItem(item);
		setCoreContext(coreContext);
		this.addUrl = addUrl;
	}

	public String render() {
		ToolbarItem item = getToolbarItem();
		StringBuilder action = new StringBuilder("javascript:");
		action.append("document.location='" + addUrl + "'");
		item.setAction(action.toString());
		return item.enabled();
	}

	public String getAddUrl() {
		return addUrl;
	}

	private final String addUrl;
}
