/*
 * $Id: SbaConventions.java,v 1.9 2009/07/01 19:42:54 cvstursi Exp $
 *
 * Licencia de Caja de Valores S.A., Versi�n 1.0
 * Copyright (c) 2006 Caja de Valores S.A.
 * 25 de Mayo 362, Ciudad Aut�noma de Buenos Aires, Rep�blica Argentina
 * Todos los derechos reservados.
 * Este software es informaci�n confidencial y propietaria de Caja de Valores S.A. ("Informaci�n
 * Confidencial"). Usted no divulgar� tal Informaci�n Confidencial y la usar� solamente de acuerdo a
 * los t�rminos del acuerdo de licencia que posee con Caja de Valores S.A.
 */
package ar.uba.dc.thesis.scenariosUIWeb.common.util;

import org.springframework.web.servlet.view.UrlBasedViewResolver;

/**
 * @author Gabriel Tursi
 * @version $Revision: 1.9 $ $Date: 2009/07/01 19:42:54 $
 */
public final class SbaConventions {

	public static String controllerToViewName(Class<?> aClass) {
		String viewName = camelLowerCase(aClass);
		viewName = viewName.replace("Controller", "");
		return viewName;
	}

	public static String getTableViewName(Class<?> aClass) {
		return camelLowerCase(aClass) + "Table";
	}

	public static String getTableViewNameForRedirect(Class<?> aClass) {
		return "redirect:/service/" + getTableViewName(aClass);
	}

	public static String getDetailDeleteViewName(Class<?> aClass) {
		return camelLowerCase(aClass) + "DetailDelete";
	}

	public static String getDetailDeleteViewName(String className) {
		return camelLowerCase(className) + "DetailDelete";
	}

	/**
	 * El redirect evita repetir una acci�n al refrescar una pagina. Se debe usar este viewName
	 * luego de una acci�n de alta o modificaci�n.
	 * @param aClass clase de la cual se quiere mostrar el detalle
	 * @param id identifica el objeto a mostrar
	 * @return viewName prefijado con el redirect y como sufijo el id del objeto a mostrar como
	 * par�metro
	 */
	public static String getDetailDeleteViewNameForRedirect(Class<?> aClass, Long id) {
		return REDIRECT_PREFIX + camelLowerCase(aClass) + "DetailDelete" + "?id=" + id;
	}

	public static String getAddUpdateViewName(Class<?> aClass) {
		return camelLowerCase(aClass) + "AddUpdate";
	}

	public static String getAltaViewName(Class<?> aClass) {
		return camelLowerCase(aClass) + "Alta";
	}

	private static String camelLowerCase(Class<?> aClass) {
		return camelLowerCase(aClass.getSimpleName());
	}

	private static String camelLowerCase(String className) {
		return className.toLowerCase().charAt(0) + className.substring(1);
	}

	private static final String REDIRECT_PREFIX = UrlBasedViewResolver.REDIRECT_URL_PREFIX;

}
