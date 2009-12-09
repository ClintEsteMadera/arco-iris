/*
 * $Id: CustomCalendarEditor.java,v 1.3 2009/05/26 21:00:45 cvstursi Exp $
 *
 * Licencia de Caja de Valores S.A., Versi�n 1.0
 * Copyright (c) 2006 Caja de Valores S.A.
 * 25 de Mayo 362, Ciudad Aut�noma de Buenos Aires, Rep�blica Argentina
 * Todos los derechos reservados.
 * Este software es informaci�n confidencial y propietaria de Caja de Valores S.A. ("Informaci�n
 * Confidencial"). Usted no divulgar� tal Informaci�n Confidencial y la usar� solamente de acuerdo a
 * los t�rminos del acuerdo de licencia que posee con Caja de Valores S.A.
 */
package ar.uba.dc.thesis.scenariosUIWeb.common.propertyeditors;

import java.beans.PropertyEditorSupport;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;
import org.springframework.util.StringUtils;

/**
 * @author gtursi
 * @version $Revision: 1.3 $ $Date: 2009/05/26 21:00:45 $
 */
public class CustomCalendarEditor extends PropertyEditorSupport {

	private final DateFormat dateFormat;

	private final boolean allowEmpty;

	private final int exactDateLength;

	public CustomCalendarEditor() {
		this("dd/MM/yyyy");
	}

	public CustomCalendarEditor(String dateFormat) {
		this.dateFormat = new SimpleDateFormat(dateFormat);
		this.dateFormat.setLenient(false);
		this.allowEmpty = true;
		this.exactDateLength = 10;
	}

	@Override
	public void setAsText(String text) throws IllegalArgumentException {
		if (this.allowEmpty && !StringUtils.hasText(text)) {
			// Treat empty String as null value.
			setValue(null);
		} else if (text != null && this.exactDateLength >= 0
		        && text.length() != this.exactDateLength) {
			throw new IllegalArgumentException("Could not parse date: it is not exactly"
			        + this.exactDateLength + "characters long");
		} else {
			try {
				GregorianCalendar calendar = new GregorianCalendar();
				calendar.setTime(this.dateFormat.parse(text));
				setValue(calendar);
			} catch (ParseException ex) {
				IllegalArgumentException iae =
				        new IllegalArgumentException("Could not parse date: " + ex.getMessage());
				iae.initCause(ex);
				throw iae;
			}
		}
	}

	@Override
	public String getAsText() {
		GregorianCalendar value = (GregorianCalendar) getValue();
		return value != null ? this.dateFormat.format(value.getTime()) : "";
	}

}