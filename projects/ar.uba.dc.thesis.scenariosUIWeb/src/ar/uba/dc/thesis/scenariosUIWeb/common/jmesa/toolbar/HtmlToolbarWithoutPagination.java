/*
 * $Id: HtmlToolbarWithoutPagination.java,v 1.4 2009/04/30 18:19:03 cvsuribe Exp $
 *
 * Licencia de Caja de Valores S.A., Versi�n 1.0
 * Copyright (c) 2006 Caja de Valores S.A.
 * 25 de Mayo 362, Ciudad Aut�noma de Buenos Aires, Rep�blica Argentina
 * Todos los derechos reservados.
 * Este software es informaci�n confidencial y propietaria de Caja de Valores S.A. ("Informaci�n
 * Confidencial"). Usted no divulgar� tal Informaci�n Confidencial y la usar� solamente de acuerdo a
 * los t�rminos del acuerdo de licencia que posee con Caja de Valores S.A.
 */
package ar.uba.dc.thesis.scenariosUIWeb.common.jmesa.toolbar;

import java.util.List;
import org.jmesa.view.ViewUtils;
import org.jmesa.view.component.Row;
import org.jmesa.view.html.toolbar.HtmlToolbar;
import org.jmesa.view.html.toolbar.MaxRowsItem;
import org.jmesa.view.html.toolbar.ToolbarItemType;

/**
 * A diferencia de HtmlToolbar, no muestra los iconos relacionados con paginado.
 * @author Gabriel Tursi
 * @version $Revision: 1.4 $ $Date: 2009/04/30 18:19:03 $
 */
public class HtmlToolbarWithoutPagination extends HtmlToolbar {
	@SuppressWarnings("unchecked")
	@Override
	public String render() {
		if (hasToolbarItems()) { // already has items
			return super.render();
		}

		if (enablePageNumbers) {
			addToolbarItem(ToolbarItemType.PAGE_NUMBER_ITEMS);
		}

		if (enableSeparators) {
			addToolbarItem(ToolbarItemType.SEPARATOR);
		}

		MaxRowsItem maxRowsItem = (MaxRowsItem) addToolbarItem(ToolbarItemType.MAX_ROWS_ITEM);
		if (getMaxRowsIncrements() != null) {
			maxRowsItem.setIncrements(getMaxRowsIncrements());
		}

		boolean exportable = ViewUtils.isExportable(getExportTypes());

		if (exportable && enableSeparators) {
			addToolbarItem(ToolbarItemType.SEPARATOR);
		}

		if (exportable) {
			addExportToolbarItems(getExportTypes());
		}

		Row row = getTable().getRow();
		List columns = row.getColumns();

		boolean filterable = ViewUtils.isFilterable(columns);

		if (filterable && enableSeparators) {
			addToolbarItem(ToolbarItemType.SEPARATOR);
		}

		if (filterable) {
			addToolbarItem(ToolbarItemType.FILTER_ITEM);
			addToolbarItem(ToolbarItemType.CLEAR_ITEM);
		}

		boolean editable = ViewUtils.isEditable(getCoreContext().getWorksheet());
		if (editable && enableSeparators) {
			addToolbarItem(ToolbarItemType.SEPARATOR);
		}

		if (editable) {
			addToolbarItem(ToolbarItemType.SAVE_WORKSHEET_ITEM);
			addToolbarItem(ToolbarItemType.FILTER_WORKSHEET_ITEM);
		}

		return super.render();
	}
}
