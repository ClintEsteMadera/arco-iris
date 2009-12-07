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
 * $Id: SimplePairComposite.java,v 1.3 2008/05/05 16:18:04 cvschioc Exp $
 */

package commons.gui.widget.composite;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;

import sba.common.properties.EnumProperty;

/**
 * Crea un composite en un area de texto, defeiniendo el titulo del grupo y las columnas que
 * componen el layout
 * 
 * @author mvera
 */

public class SimplePairComposite extends SimpleComposite {

	public SimplePairComposite(Composite parent, boolean readOnly) {
		this(parent, readOnly, DEFAULT_COLUMN_NUMBER, DEFAULT_COLUMN_SPAN);
	}

	public SimplePairComposite(Composite parent, boolean readOnly, int numColumns) {
		this(parent, readOnly, numColumns, DEFAULT_COLUMN_SPAN);
	}

	public SimplePairComposite(Composite parent, boolean readOnly, int numColumns, int columnsToSpan) {
		super(parent, readOnly, numColumns, columnsToSpan);
		this.applyLayout();
	}

	@Override
	protected void applyLayout() {
		setLayout(new GridLayout(1, true));
	}

	/**
	 * @param title
	 *            titulo del grupo
	 * @param numColum
	 *            numero de columnas en los cuales se divide el layout
	 * @return
	 */
	public Group createInputArea(String title, int numColum) {
		Group entryGroup = new Group(this, SWT.NONE);
		if (title != null)
			entryGroup.setText(title);
		GridLayout entryLayout = new GridLayout(numColum, false); /* false = no con el mismo ancho */
		entryGroup.setLayout(entryLayout);
		entryGroup.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		return entryGroup;
	}

	/**
	 * @param title
	 *            Titulo del grupo
	 * @param numColum
	 *            numero de columnas en los cuales se divide el layout
	 * @return
	 */
	public Group createInputArea(EnumProperty title, int numColum) {
		return createInputArea(title.name(), numColum);

	}

	private static final int DEFAULT_COLUMN_NUMBER = 2;

	private static final int DEFAULT_COLUMN_SPAN = 1;
}