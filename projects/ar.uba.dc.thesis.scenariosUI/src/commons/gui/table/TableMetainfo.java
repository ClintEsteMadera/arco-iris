package commons.gui.table;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;


import commons.gui.widget.creation.binding.BindingInfo;
import commons.properties.EnumProperty;

/**
 * Metainformacion para la creacion de tablas
 * 
 * @author Pablo Pastorino
 * @version $Revision: 1.3 $ $Date: 2008/02/05 16:05:16 $
 */
public class TableMetainfo<T> {

	public TableMetainfo(Composite parent, EnumProperty tableName, Class<T> itemClass, BindingInfo bindingInfo,
			boolean readOnly) {

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

	public Class<T> itemClass;

	public int tableStyle = DEFAULT_TABLE_STYLE;

	public static final int DEFAULT_TABLE_STYLE = SWT.FULL_SELECTION | SWT.BORDER;
}
