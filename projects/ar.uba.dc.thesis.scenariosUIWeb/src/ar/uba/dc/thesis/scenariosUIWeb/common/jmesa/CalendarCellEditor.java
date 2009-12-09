/*
 * $Id: CalendarCellEditor.java,v 1.6 2009/04/30 20:36:15 cvstursi Exp $
 *
 * Licencia de Caja de Valores S.A., Versión 1.0
 * Copyright (c) 2006 Caja de Valores S.A.
 * 25 de Mayo 362, Ciudad Autónoma de Buenos Aires, República Argentina
 * Todos los derechos reservados.
 * Este software es información confidencial y propietaria de Caja de Valores S.A. ("Información
 * Confidencial"). Usted no divulgará tal Información Confidencial y la usará solamente de acuerdo a
 * los términos del acuerdo de licencia que posee con Caja de Valores S.A.
 */
package ar.uba.dc.thesis.scenariosUIWeb.common.jmesa;

import java.util.Calendar;
import java.util.Locale;
import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jmesa.util.ItemUtils;
import org.jmesa.view.editor.AbstractPatternSupport;

import ar.uba.dc.thesis.scenariosUIWeb.common.util.ClassUtils;

/**
 * @author Rodrigo A. Peinado
 * @version $Revision: 1.6 $ $Date: 2009/04/30 20:36:15 $
 */
public class CalendarCellEditor extends AbstractPatternSupport {

	/**
	 * @param pattern
	 */
	public CalendarCellEditor(String pattern) {
		super();
		this.setPattern(pattern);
	}

	/**
	 * @see org.jmesa.view.editor.CellEditor#getValue(java.lang.Object, java.lang.String, int)
	 */
	public Object getValue(Object item, String property, int rowcount) {
		Object itemValue = null;
		itemValue = ItemUtils.getItemValue(item, property);
		if (itemValue == null) {
			return null;
		}
		try {
			Locale locale = getWebContext().getLocale();
			itemValue =
			        DateFormatUtils.format(((Calendar) itemValue).getTime(), getPattern(), locale);
		} catch (Exception e) {
			log.warn(
			        (new StringBuilder())
			                .append("Could not process Calendar editor with property ").append(
			                        property).toString(), e);
		}
		return itemValue;
	}

	private final Log log = LogFactory.getLog(ClassUtils.getCurrentClass());
}
