/*
 * $Id: MenuController.java,v 1.1 2009/06/23 15:09:06 cvstursi Exp $
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

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import ar.uba.dc.thesis.scenariosUIWeb.common.ui.web.ApplicationMenu;


/**
 * @author H. Adrián Uribe
 * @version $Revision: 1.1 $ $Date: 2009/06/23 15:09:06 $
 */
@Controller
public class MenuController {

	@RequestMapping
	public ModelAndView execute() {
		List<MenuLine> lines = applicationMenu.getMenuLines();

		ModelAndView mav = new ModelAndView("menu");
		mav.addObject("menuLines", lines);
		mav.addObject("username", getUsername());
		if ((lines != null) && (lines.size() != 0)) {
			mav.addObject("firstId", lines.get(0).getId());
			mav.addObject("lastId", lines.get(lines.size() - 1).getId());
		}
		return mav;
	}

	private String getUsername() {
		return "anonymous";
	}

	@Autowired
	private ApplicationMenu applicationMenu;
}
