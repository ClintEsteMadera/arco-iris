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
 * $Id: FilterButtonsGroup.java,v 1.4 2007/10/30 15:33:18 cvschioc Exp $
 */

package commons.gui.widget.group.query;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;

import commons.gui.widget.group.SimpleGroup;
import commons.properties.CommonLabels;

/**
 * Modela un grupo con botones "Buscar" y "Limpiar"
 * @author Jonathan Chiocchio
 * @version $Revision: 1.4 $ $Date: 2007/10/30 15:33:18 $
 */

public class FilterButtonsGroup extends SimpleGroup {

	public FilterButtonsGroup(Composite composite, SelectionListener filterListener,
			SelectionListener cleanUpListener) {
		super(composite, CommonLabels.EMPTY, false);
		super.getSwtGroup().setLayout(new GridLayout(1, true));
		super.getSwtGroup().setLayoutData(new GridData(GridData.FILL_BOTH));

		this.filterButton = new Button(super.getSwtGroup(), SWT.CENTER);
		this.filterButton.setText(CommonLabels.SEARCH.toString());
		this.filterButton.setLayoutData(new GridData(GridData.FILL_HORIZONTAL
				| GridData.GRAB_VERTICAL));
		this.filterButton.addSelectionListener(filterListener);

		this.cleanUpButton = new Button(super.getSwtGroup(), SWT.CENTER);
		this.cleanUpButton.setText(CommonLabels.CLEAN_FILTERS.toString());
		this.cleanUpButton.setLayoutData(new GridData(GridData.FILL_HORIZONTAL
				| GridData.GRAB_VERTICAL));
		this.cleanUpButton.addSelectionListener(cleanUpListener);
	}

	public Button getFilterButton() {
		return this.filterButton;
	}
	
	public Button getCleanUpButton() {
		return this.cleanUpButton;
	}
	
	private final Button filterButton;

	private final Button cleanUpButton;
}