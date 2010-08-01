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
 * $Id: BooleanFieldMetainfo.java,v 1.4 2007/11/30 20:31:04 cvsmarco Exp $
 */
package commons.gui.widget.creation.metainfo;

import org.eclipse.swt.widgets.Composite;

import commons.gui.widget.Alignment;
import commons.gui.widget.creation.binding.Binding;
import commons.properties.EnumProperty;

/**
 * Meta-informaci�n necesaria para la creaci�n de un campo booleano
 * 
 * @author Gabriel Tursi
 * @version $Revision: 1.4 $ $Date: 2007/11/30 20:31:04 $
 */
public class BooleanFieldMetainfo extends ControlMetainfo {

	public Alignment alignment = Alignment.LEFT;

	private static BooleanFieldMetainfo instance = new BooleanFieldMetainfo();

	public static BooleanFieldMetainfo create(Composite composite, EnumProperty label, Binding binding, boolean readOnly) {
		ControlMetainfo.setValues(instance, composite, label, binding, readOnly);
		return instance;
	}

	@Override
	public void restoreDefaults() {
		super.restoreDefaults();
		applyDefaults();
	}

	public BooleanFieldMetainfo() {
		super();
		this.applyDefaults();
	}

}