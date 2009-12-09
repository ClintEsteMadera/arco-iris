/*
 * $Id: SbaConventions.java,v 1.9 2009/07/01 19:42:54 cvstursi Exp $
 *
 * Licencia de Caja de Valores S.A., Versión 1.0
 * Copyright (c) 2006 Caja de Valores S.A.
 * 25 de Mayo 362, Ciudad Autónoma de Buenos Aires, República Argentina
 * Todos los derechos reservados.
 * Este software es información confidencial y propietaria de Caja de Valores S.A. ("Información
 * Confidencial"). Usted no divulgará tal Información Confidencial y la usará solamente de acuerdo a
 * los términos del acuerdo de licencia que posee con Caja de Valores S.A.
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
	 * El redirect evita repetir una acción al refrescar una pagina. Se debe usar este viewName
	 * luego de una acción de alta o modificación.
	 * @param aClass clase de la cual se quiere mostrar el detalle
	 * @param id identifica el objeto a mostrar
	 * @return viewName prefijado con el redirect y como sufijo el id del objeto a mostrar como
	 * parámetro
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
