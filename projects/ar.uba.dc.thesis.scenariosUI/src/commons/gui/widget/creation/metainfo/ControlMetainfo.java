/*
 * Licencia de Caja de Valores S.A., Versión 1.0
 *
 * Copyright (c) 2006 Caja de Valores S.A.
 * 25 de Mayo 362, Ciudad Autónoma de Buenos Aires, República Argentina
 * Todos los derechos reservados.
 *
 * Este software es información confidencial y propietaria de Caja de Valores S.A. ("Información
 * Confidencial"). Usted no divulgará tal Información Confidencial y la usará solamente de acuerdo a
 * los términos del acuerdo de licencia que posee con Caja de Valores S.A.
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
 * Modela la meta información inherente a un Control visual.
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
