/*
 * Licencia de Caja de Valores S.A., Versi�n 1.0
 *
 * Copyright (c) 2006 Caja de Valores S.A.
 * 25 de Mayo 362, Ciudad Aut�noma de Buenos Aires, Rep�blica Argentina
 * Todos los derechos reservados.
 *
 * Este software es informaci�n confidencial y propietaria de Caja de Valores S.A. ("Informaci�n
 * Confidencial"). Usted no divulgar� tal Informaci�n Confidencial y la usar� solamente de acuerdo a
 * los t�rminos del acuerdo de licencia que posee con Caja de Valores S.A.
 */

/*
 * $Id: CalendarMetainfo.java,v 1.5 2008/03/26 15:57:28 cvsmarco Exp $
 */

package commons.gui.widget.creation.metainfo;

import org.eclipse.swt.widgets.Composite;

import sba.common.datetime.Hora;
import sba.common.properties.EnumProperty;

import commons.gui.widget.creation.binding.Binding;

/**
 * @author Jonathan Chiocchio
 * @version $Revision: 1.5 $ $Date: 2008/03/26 15:57:28 $
 */

public class CalendarMetainfo extends ControlMetainfo {

	public static CalendarMetainfo create(Composite composite, EnumProperty label, Binding binding,
			boolean readOnly) {
		instance.applyDefaults();
		ControlMetainfo.setValues(instance, composite, label, binding, readOnly);
		return instance;
	}

	@Override
	public void restoreDefaults() {
		super.restoreDefaults();
		this.applyDefaults();
	}

	private CalendarMetainfo() {
		super();
		this.applyDefaults();
	}

	@Override
	public void applyDefaults() {
		super.applyDefaults();
		this.normalizedTime = new Hora(9, 0, 0); // por defecto, las 9 AM.
	}

	public Hora normalizedTime;

	private static CalendarMetainfo instance = new CalendarMetainfo();
}