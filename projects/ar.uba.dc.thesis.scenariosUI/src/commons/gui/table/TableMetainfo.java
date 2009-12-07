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
 * $Id: TableMetainfo.java,v 1.3 2008/02/05 16:05:16 cvschioc Exp $
 */

package commons.gui.table;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;

import sba.common.properties.EnumProperty;

import commons.gui.widget.creation.binding.BindingInfo;

/**
 * Metainformacion para la creacion de tablas
 * @author Pablo Pastorino
 * @version $Revision: 1.3 $ $Date: 2008/02/05 16:05:16 $
 */
public class TableMetainfo {

	public TableMetainfo(Composite parent, EnumProperty tableName, Class itemClass,
			BindingInfo bindingInfo, boolean readOnly) {

		this.parent = parent;
		this.tableName = tableName;
		this.bindingInfo = bindingInfo;
		this.readOnly = readOnly;
		this.itemClass = itemClass;
	}

	public Composite parent;

	public BindingInfo bindingInfo;

	public boolean readOnly;

	public boolean sorteable = true;

	public EnumProperty tableName;

	public Class itemClass;

	public int tableStyle = DEFAULT_TABLE_STYLE;

	public static final int DEFAULT_TABLE_STYLE = SWT.FULL_SELECTION | SWT.BORDER;
}
