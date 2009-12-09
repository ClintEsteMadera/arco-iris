/*
 * $Id: HtmlToolbarWithAdd.java,v 1.3 2009/04/27 18:02:07 cvsuribe Exp $
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

import org.jmesa.view.html.toolbar.HtmlToolbar;
import org.jmesa.view.html.toolbar.ToolbarItemRenderer;
import org.jmesa.view.html.toolbar.ToolbarItemType;

/**
 * @author Gabriel Tursi
 * @version $Revision: 1.3 $ $Date: 2009/04/27 18:02:07 $
 */
public class HtmlToolbarWithAdd extends HtmlToolbar {

	public HtmlToolbarWithAdd(String addUrl) {
		super();
		this.addUrl = addUrl;
	}

	@Override
	public String render() {
		super.render();
		ToolbarTextItem item = new ToolbarTextItem();
		item.setTooltip("Agregar nuevo");
		item.setText("Alta");

		if (this.getAddUrl() != null) {
			ToolbarItemRenderer renderer = new AddRenderer(item, getCoreContext(), addUrl);
			renderer.setOnInvokeAction("onInvokeAction");
			item.setToolbarItemRenderer(renderer);
			addToolbarItem(ToolbarItemType.SEPARATOR);
			addToolbarItem(item);
		}

		return super.render();
	}

	public String getAddUrl() {
		return addUrl;
	}

	private String addUrl;
}
