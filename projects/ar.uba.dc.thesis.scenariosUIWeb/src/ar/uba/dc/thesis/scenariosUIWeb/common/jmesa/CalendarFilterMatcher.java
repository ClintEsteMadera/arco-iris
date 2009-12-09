/*
 * $Id: CalendarFilterMatcher.java,v 1.5 2009/04/28 22:05:54 cvsuribe Exp $
 *
 * Licencia de Caja de Valores S.A., Versi�n 1.0
 * Copyright (c) 2006 Caja de Valores S.A.
 * 25 de Mayo 362, Ciudad Aut�noma de Buenos Aires, Rep�blica Argentina
 * Todos los derechos reservados.
 * Este software es informaci�n confidencial y propietaria de Caja de Valores S.A. ("Informaci�n
 * Confidencial"). Usted no divulgar� tal Informaci�n Confidencial y la usar� solamente de acuerdo a
 * los t�rminos del acuerdo de licencia que posee con Caja de Valores S.A.
 */
package ar.uba.dc.thesis.scenariosUIWeb.common.jmesa;

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.jmesa.core.filter.FilterMatcher;
import org.jmesa.view.editor.PatternSupport;
import org.jmesa.web.WebContext;
import org.jmesa.web.WebContextSupport;

/**
 * @author Rodrigo A. Peinado
 * @version $Revision: 1.5 $ $Date: 2009/04/28 22:05:54 $
 */
public class CalendarFilterMatcher implements FilterMatcher, PatternSupport, WebContextSupport {

	/**
	 *
	 *
	 */
	public CalendarFilterMatcher() {
		// default constructor
	}

	/**
	 * @param pattern The pattern to use.
	 */
	public CalendarFilterMatcher(String pattern) {
		this.pattern = pattern;
	}

	/**
	 * @param pattern
	 * @param webContext
	 */
	public CalendarFilterMatcher(String pattern, WebContext webContext) {
		this.pattern = pattern;
		this.webContext = webContext;
	}

	/**
	 * @see org.jmesa.web.WebContextSupport#getWebContext()
	 */
	public WebContext getWebContext() {
		return webContext;
	}

	/**
	 * @see org.jmesa.web.WebContextSupport#setWebContext(org.jmesa.web.WebContext)
	 */
	public void setWebContext(WebContext webContext) {
		this.webContext = webContext;
	}

	/**
	 * @see org.jmesa.view.editor.PatternSupport#getPattern()
	 */
	public String getPattern() {
		return pattern;
	}

	/**
	 * @see org.jmesa.view.editor.PatternSupport#setPattern(java.lang.String)
	 */
	public void setPattern(String pattern) {
		this.pattern = pattern;
	}

	/**
	 * @see org.jmesa.core.filter.FilterMatcher#evaluate(java.lang.Object, java.lang.String)
	 */
	public boolean evaluate(Object value, String filterValue) {
		if (value == null) {
			return false;
		}
		Object itemValue = ((Calendar) value).getTime();
		if (pattern == null) {
			log
			        .debug("The filter (value "
			                + filterValue
			                + ") is trying to match against a date column using the DateFilterMatcher, "
			                + "but there is no pattern defined. You need to register a CalendarFilterMatcher "
			                + "to be able to filter against this column.");
			return false;
		}
		Locale locale = null;
		if (webContext != null) {
			locale = webContext.getLocale();
		}
		if (locale != null) {
			itemValue = DateFormatUtils.format((Date) itemValue, pattern, locale);
		} else {
			itemValue = DateFormatUtils.format((Date) itemValue, pattern);
		}
		String item = String.valueOf(itemValue);
		log.info(item);
		String filter = String.valueOf(filterValue);
		log.info(filter);
		if (StringUtils.contains(item, filter)) {
			return true;
		}
		return false;
	}

	private org.apache.commons.logging.Log log =
	        org.apache.commons.logging.LogFactory.getLog(CalendarFilterMatcher.class);

	private String pattern;

	private WebContext webContext;
}
