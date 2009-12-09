/*
 * $Id: AddRenderer.java,v 1.4 2009/04/30 18:37:14 cvsuribe Exp $
 *
 * Licencia de Caja de Valores S.A., Versión 1.0
 * Copyright (c) 2006 Caja de Valores S.A.
 * 25 de Mayo 362, Ciudad Autónoma de Buenos Aires, República Argentina
 * Todos los derechos reservados.
 * Este software es información confidencial y propietaria de Caja de Valores S.A. ("Información
 * Confidencial"). Usted no divulgará tal Información Confidencial y la usará solamente de acuerdo a
 * los términos del acuerdo de licencia que posee con Caja de Valores S.A.
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
