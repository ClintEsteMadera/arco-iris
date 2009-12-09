/*
 * $Id: SbaWebBindingInitializer.java,v 1.1 2009/06/16 18:35:28 cvstursi Exp $
 *
 * Licencia de Caja de Valores S.A., Versi�n 1.0
 * Copyright (c) 2006 Caja de Valores S.A.
 * 25 de Mayo 362, Ciudad Aut�noma de Buenos Aires, Rep�blica Argentina
 * Todos los derechos reservados.
 * Este software es informaci�n confidencial y propietaria de Caja de Valores S.A. ("Informaci�n
 * Confidencial"). Usted no divulgar� tal Informaci�n Confidencial y la usar� solamente de acuerdo a
 * los t�rminos del acuerdo de licencia que posee con Caja de Valores S.A.
 */
package ar.uba.dc.thesis.scenariosUIWeb.common.ui.web.propertyeditor;

import java.util.Calendar;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.support.WebBindingInitializer;
import org.springframework.web.context.request.WebRequest;

import ar.uba.dc.thesis.scenariosUIWeb.common.propertyeditors.CustomCalendarEditor;

/**
 * @author gtursi
 * @version $Revision: 1.1 $ $Date: 2009/06/16 18:35:28 $
 */
public class SbaWebBindingInitializer implements WebBindingInitializer {

	public void initBinder(WebDataBinder dataBinder, WebRequest request) {
		dataBinder.setDisallowedFields(new String[] { "id" });
		dataBinder.registerCustomEditor(Calendar.class, new CustomCalendarEditor());
		dataBinder.registerCustomEditor(String.class, new StringTrimmerEditor(true));
	}

}
