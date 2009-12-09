/*
 * $Id: SbaWebBindingInitializer.java,v 1.1 2009/06/16 18:35:28 cvstursi Exp $
 *
 * Licencia de Caja de Valores S.A., Versión 1.0
 * Copyright (c) 2006 Caja de Valores S.A.
 * 25 de Mayo 362, Ciudad Autónoma de Buenos Aires, República Argentina
 * Todos los derechos reservados.
 * Este software es información confidencial y propietaria de Caja de Valores S.A. ("Información
 * Confidencial"). Usted no divulgará tal Información Confidencial y la usará solamente de acuerdo a
 * los términos del acuerdo de licencia que posee con Caja de Valores S.A.
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
