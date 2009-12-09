/*
 * $Id: MenuController.java,v 1.1 2009/06/23 15:09:06 cvstursi Exp $
 *
 * Licencia de Caja de Valores S.A., Versi�n 1.0
 * Copyright (c) 2006 Caja de Valores S.A.
 * 25 de Mayo 362, Ciudad Aut�noma de Buenos Aires, Rep�blica Argentina
 * Todos los derechos reservados.
 * Este software es informaci�n confidencial y propietaria de Caja de Valores S.A. ("Informaci�n
 * Confidencial"). Usted no divulgar� tal Informaci�n Confidencial y la usar� solamente de acuerdo a
 * los t�rminos del acuerdo de licencia que posee con Caja de Valores S.A.
 */
package ar.uba.dc.thesis.scenariosUIWeb.common.ui.web.view;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import ar.uba.dc.thesis.scenariosUIWeb.common.ui.web.ApplicationMenu;


/**
 * @author H. Adri�n Uribe
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
