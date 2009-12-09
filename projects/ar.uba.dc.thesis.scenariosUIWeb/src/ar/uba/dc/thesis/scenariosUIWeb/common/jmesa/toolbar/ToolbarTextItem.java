/*
 * $Id: ToolbarTextItem.java,v 1.5 2009/04/30 18:37:14 cvsuribe Exp $
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

import org.jmesa.view.html.HtmlBuilder;
import org.jmesa.view.html.toolbar.AbstractItem;

/**
 * Permite agregar un item solo con texto en la Toolbar de Jmesa. Por ejemplo, se usa para el alta
 * de nuevo item.
 * @author Gabriel Tursi
 * @version $Revision: 1.5 $ $Date: 2009/04/30 18:37:14 $
 */
public class ToolbarTextItem extends AbstractItem {

	String text;

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	@Override
	public String disabled() {
		return this.enabled();
	}

	@Override
	public String enabled() {
		HtmlBuilder html = new HtmlBuilder();
		html.a().href();
		html.quote();
		html.append(getAction());
		html.quote().close();
		html.append(getText());
		html.aEnd();
		return html.toString();
	}
}
