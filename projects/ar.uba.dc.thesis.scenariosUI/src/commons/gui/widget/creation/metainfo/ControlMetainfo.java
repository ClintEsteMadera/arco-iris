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
 * $Id: ControlMetainfo.java,v 1.10 2008/05/15 20:53:29 cvspasto Exp $
 */

package commons.gui.widget.creation.metainfo;

import org.eclipse.swt.widgets.Composite;

import sba.common.properties.EnumProperty;

import commons.gui.widget.creation.binding.Binding;
import commons.properties.CommonLabels;

/**
 * Modela la meta informaci�n inherente a un Control visual.
 * 
 * @author Jonathan Chiocchio
 * @version $Revision: 1.10 $ $Date: 2008/05/15 20:53:29 $
 */

public class ControlMetainfo {

	public ControlMetainfo(Composite composite, EnumProperty label, Binding binding,
			boolean readOnly) {
		super();
		this.composite = composite;
		this.label = label;
		this.binding = binding;
		this.readOnly = readOnly;
	}

	public void restoreDefaults() {
		this.label = CommonLabels.NO_LABEL;
	}

	protected ControlMetainfo() {
		super();
	}

	protected static void setValues(ControlMetainfo instance, Composite composite,
			EnumProperty label, Binding binding, boolean readOnly) {
		instance.composite = composite;
		instance.label = label;
		instance.binding = binding;
		instance.readOnly = readOnly;
	}

	public void applyDefaults() {
		this.binding = null;
		this.label = CommonLabels.NO_LABEL;
		this.layoutData=null;
		this.validate=DEFAULT_VALIDATE;
	}

	public Composite composite;

	public EnumProperty label;

	public boolean readOnly = false;

	public Binding binding;
	
	public int horizontalSpan=-1;
	
	public boolean validate=DEFAULT_VALIDATE;
	
	public Object layoutData;
	
	public static final boolean DEFAULT_VALIDATE=true;
}
