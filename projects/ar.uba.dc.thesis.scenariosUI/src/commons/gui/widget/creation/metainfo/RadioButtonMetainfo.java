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
 * $Id: RadioButtonMetainfo.java,v 1.5 2008/04/29 20:33:27 cvschioc Exp $
 */

package commons.gui.widget.creation.metainfo;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.swt.widgets.Composite;

import sba.common.properties.EnumProperty;

import commons.gui.widget.creation.binding.Binding;

/**
 * @author Jonathan Chiocchio
 * @version $Revision: 1.5 $ $Date: 2008/04/29 20:33:27 $
 */

public class RadioButtonMetainfo extends ControlMetainfo {

	public static RadioButtonMetainfo create(Composite composite, EnumProperty label,
			Binding binding, boolean readOnly) {
		instance.applyDefaults();
		ControlMetainfo.setValues(instance, composite, label, binding, readOnly);
		return instance;
	}

	@Override
	public void restoreDefaults() {
		super.restoreDefaults();
		applyDefaults();
	}

	@Override
	public void applyDefaults() {
		super.applyDefaults();
		this.emptyGroupName = false;
		this.useGroup = true;
		this.useCodedItems = false;
		this.codedItems = null;
		this.enumClass = null;
		this.numColumns = 2;
		this.selectedIndex = 0;
		this.selectedValue = null;
		this.listeners.clear();
	}

	private RadioButtonMetainfo() {
		super();
		this.listeners = new ArrayList<IPropertyChangeListener>();
		this.applyDefaults();
	}

	private static RadioButtonMetainfo instance = new RadioButtonMetainfo();

	public boolean emptyGroupName;

	public boolean useGroup;

	public boolean useCodedItems;

	public Object[][] codedItems;

	public Class<? extends Enum> enumClass;

	public int numColumns;

	public int selectedIndex;

	public Object selectedValue;

	public List<IPropertyChangeListener> listeners;
}