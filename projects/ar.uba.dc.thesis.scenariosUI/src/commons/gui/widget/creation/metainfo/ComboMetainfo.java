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
 * $Id: ComboMetainfo.java,v 1.11 2008/04/01 12:49:42 cvspasto Exp $
 */
package commons.gui.widget.creation.metainfo;

import java.text.Format;

import org.eclipse.swt.widgets.Composite;

import sba.common.properties.EnumProperty;

import commons.gui.widget.creation.binding.Binding;

/**
 * @author Gabriel Tursi
 * @version $Revision: 1.11 $ $Date: 2008/04/01 12:49:42 $
 */
public class ComboMetainfo extends ControlMetainfo {

	private static final ComboMetainfo instance = new ComboMetainfo();

	public static ComboMetainfo create(Composite composite, EnumProperty label, Binding binding,
			boolean readOnly) {
		ControlMetainfo.setValues(instance, composite, label, binding, readOnly);
		return instance;
	}

	@Override
	public void restoreDefaults() {
		super.restoreDefaults();
		this.items = null;
		this.nullText = "";
		this.itemFormat = null;
		this.addEmptyOption = true;
	}

	public static final int DEFAULT_VISIBLE_ITEM_COUNT = 25;

	/**
	 * Indica la cantidad de �tems a mostrar al desplegar el combo.
	 */
	public int visibleItemCount = DEFAULT_VISIBLE_ITEM_COUNT;

	/**
	 * Indica si debe agregarse una opci�n para el valor 'nulo'
	 */
	public boolean addEmptyOption = true;

	/**
	 * En caso de agregarse la opci�n para valor nulo, indica el texto utilizado
	 */
	public String nullText = "";

	/**
	 * Format utilziado para obtener el texto asociado a los items
	 */
	public Format itemFormat;

	/**
	 * Lista de items para poblar el combo
	 */
	public Object[] items;

	/**
	 * Nombre de la propiedad que se utiliza para extraer del item seleccionado el valor a bindear.
	 * Se utiliza por ejemplo para setear un 'id' en un objeto con el 'id' del objeto seleccionado
	 * en el combo.
	 */
	public String valueProperty;
}