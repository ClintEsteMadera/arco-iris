/*
 * $Id: GarantiasBindingInitializer.java,v 1.5 2009/06/30 21:14:49 cvstursi Exp $
 *
 * Licencia de Caja de Valores S.A., Versi�n 1.0
 * Copyright (c) 2006 Caja de Valores S.A.
 * 25 de Mayo 362, Ciudad Aut�noma de Buenos Aires, Rep�blica Argentina
 * Todos los derechos reservados.
 * Este software es informaci�n confidencial y propietaria de Caja de Valores S.A. ("Informaci�n
 * Confidencial"). Usted no divulgar� tal Informaci�n Confidencial y la usar� solamente de acuerdo a
 * los t�rminos del acuerdo de licencia que posee con Caja de Valores S.A.
 */
package ar.uba.dc.thesis.scenariosUIWeb.ui.web;

import ar.uba.dc.thesis.scenariosUIWeb.common.ui.web.propertyeditor.SbaWebBindingInitializer;

/**
 * Shared WebBindingInitializer for Garantias's custom editors.
 * 
 * @author gtursi
 * @version $Revision: 1.5 $ $Date: 2009/06/30 21:14:49 $
 */
public class GarantiasBindingInitializer extends SbaWebBindingInitializer {

	// @Autowired
	// private AgenteService agentesService;
	//
	// @Override
	// public void initBinder(WebDataBinder dataBinder, final WebRequest request) {
	// dataBinder.setDisallowedFields(new String[] { "id" });
	// dataBinder.registerCustomEditor(Calendar.class, new CustomCalendarEditor());
	// dataBinder.registerCustomEditor(String.class, new StringTrimmerEditor(true));
	// dataBinder.registerCustomEditor(Agente.class, "agente", new PropertyEditorSupport() {
	// @Override
	// public void setAsText(String text) {
	// Agente agente = agentesService.obtener(Long.parseLong(text));
	// setValue(agente);
	// }
	//
	// @Override
	// public String getAsText() {
	// if (getValue() == null) {
	// return "";
	// }
	// Agente agente = (Agente) getValue();
	// return agente.getCodigo().toString();
	// }
	// });
	// }

}
