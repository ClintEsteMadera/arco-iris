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
* $Id: CheckedTableValueModel.java,v 1.2 2007/10/10 18:00:57 cvspasto Exp $
*/

package commons.gui.binding;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.SelectionChangedEvent;

import commons.gui.model.types.EditType;
import commons.gui.table.GenericTable;

/**
 * Clase utilizada internamente par el binding de tablas con filas "chequeables"
 * El valor que representan es la lista de elementos chequeados.
 * 
 * @author Pablo Pastorino
 * @version $Revision: 1.2 $ $Date: 2007/10/10 18:00:57 $
 */
class CheckedTableValueModel 
extends TableValueModel {

	public CheckedTableValueModel(GenericTable viewer, EditType editType) {
		super(viewer, editType);
		
		final ISelectionChangedListener selectionChangedListerner=new 
		ISelectionChangedListener()
		{
			public void selectionChanged(SelectionChangedEvent event) {
				CheckedTableValueModel.this.fireValueChange();
			}
		};
		viewer.addSelectionChangedListener(selectionChangedListerner);
	}

	@Override
	public Object getValue() {
		return this.getViewer().getCheckedItems();
	}

	@Override
	public void setValue(Object value) {
		List list=null;

		if(value instanceof List)
		{
			list=(List)value;
		}else if(value instanceof Object[])
		{
			list=Arrays.asList((Object[])value);
		}else if(value instanceof Set){
			list=Arrays.asList(((Set) value).toArray());
		}else{
			list=Arrays.asList(new Object[]{value});
		}
		
		this.getViewer().setCheckedItems(list);
	}
}
