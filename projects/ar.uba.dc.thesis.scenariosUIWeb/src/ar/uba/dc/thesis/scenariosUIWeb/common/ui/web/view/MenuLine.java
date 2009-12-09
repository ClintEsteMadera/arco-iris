/*
 * $Id: MenuLine.java,v 1.4 2009/04/30 18:37:14 cvsuribe Exp $
 *
 * Licencia de Caja de Valores S.A., Versión 1.0
 * Copyright (c) 2006 Caja de Valores S.A.
 * 25 de Mayo 362, Ciudad Autónoma de Buenos Aires, República Argentina
 * Todos los derechos reservados.
 * Este software es información confidencial y propietaria de Caja de Valores S.A. ("Información
 * Confidencial"). Usted no divulgará tal Información Confidencial y la usará solamente de acuerdo a
 * los términos del acuerdo de licencia que posee con Caja de Valores S.A.
 */
package ar.uba.dc.thesis.scenariosUIWeb.common.ui.web.view;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import ar.uba.dc.thesis.scenariosUIWeb.common.ui.web.Item;
import ar.uba.dc.thesis.scenariosUIWeb.common.ui.web.Menu;
import ar.uba.dc.thesis.scenariosUIWeb.common.ui.web.MenuComponent;
import ar.uba.dc.thesis.scenariosUIWeb.common.ui.web.RootMenu;
import ar.uba.dc.thesis.scenariosUIWeb.common.util.StringUtils;


/**
 * @author H. Adrián Uribe
 * @version $Revision: 1.4 $ $Date: 2009/04/30 18:37:14 $
 */
public class MenuLine {
	public MenuLine(final byte level, short id, final String title, final String url) {
		this.level = level;
		this.id = id;
		this.title = title;
		this.url = url;
	}

	public byte getLevel() {
		return level;
	}

	public short getId() {
		return id;
	}

	public String getTitle() {
		return title;
	}

	public String getUrl() {
		return url;
	}

	@Override
	public String toString() {
		return StringUtils.toString(getClass(), "level", level, "id", id, "title", title, "url", url);
	}

	public static List<MenuLine> toMenuLines(MenuComponent component) {
		ArrayList<MenuLine> menuLines = new ArrayList<MenuLine>();
		add(menuLines, (byte) 1, component);
		return Collections.unmodifiableList(menuLines);
	}

	private static void add(List<MenuLine> menuLines, byte level, MenuComponent component) {
		if (!(component instanceof RootMenu)) {
			String url = null;
			if (component instanceof Item) {
				url = ((Item) component).getUrl();
			}
			MenuLine menuLine = new MenuLine(level, uniqueID++, component.getTitle(), url);
			menuLines.add(menuLine);
		}
		if (component instanceof Menu) {
			for (MenuComponent children : component.getChildrens()) {
				add(menuLines, (byte) (level + 1), children);
			}
		}
	}

	private final byte level;
	private final short id;
	private final String title;
	private final String url;

	private static short uniqueID = 1;
}
