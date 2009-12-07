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
 * $Id: SimpleComposite.java,v 1.9 2007/12/05 19:22:27 cvschioc Exp $
 */

package commons.gui.widget.composite;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

import sba.common.properties.FakeEnumProperty;

import commons.gui.widget.DefaultLayoutFactory;
import commons.gui.widget.factory.LabelFactory;


/**
 * Modela un composite b�sico.
 * @author Jonathan Chiocchio
 * @version $Revision: 1.9 $ $Date: 2007/12/05 19:22:27 $
 */

public class SimpleComposite extends Composite {

	public SimpleComposite(Composite parent, boolean readOnly) {
		this(parent, readOnly, DEFAULT_COLUMN_NUMBER, DEFAULT_COLUMN_SPAN);
	}

	public SimpleComposite(Composite parent, boolean readOnly, int numColumns) {
		this(parent, readOnly, numColumns, DEFAULT_COLUMN_SPAN);
	}

	public SimpleComposite(Composite parent, boolean readOnly, int numColumns, int columnsToSpan) {
		super(parent, SWT.NONE);
		this.readOnly = readOnly;
		this.numColumns = numColumns;
		this.columnsToSpan = columnsToSpan;
		this.applyLayout();
	}
	
	/**
	 * Habilita todos los controles que tienen como padre a �ste Composite.
	 */
	@Override
	public void setEnabled(boolean enabled) {
		Control[] children = getChildren();
		for (int i = 0; i < children.length; i++) {
			children[i].setEnabled(enabled);
		}
		super.setEnabled(enabled);
	}
	
	/**
	 * Provee un layout por default, sobreescribir este m�todo si se desea otro layout.
	 */
	protected void applyLayout() {
		DefaultLayoutFactory.setDefaultGridLayout(this, getNumColumns());
		GridData gridData =	(GridData)this.getLayoutData();
		gridData.horizontalSpan = columnsToSpan;
		GridLayout layout = (GridLayout) this.getLayout();
		layout.marginLeft = 0;
		layout.marginWidth = 0;
		layout.marginHeight = 0;
	}

	protected int getNumColumns(){
		return numColumns;
	}
	
	/**
	 * M�todo Helper de conveniencia para las subclases.
	 */
	protected void createSeparatorLabel() {
		LabelFactory.createLabel(this, new FakeEnumProperty(""), false, false);
	}

	private static final int DEFAULT_COLUMN_NUMBER = 2;
	
	private static final int DEFAULT_COLUMN_SPAN = 1;
	
	protected boolean readOnly;

	private final int numColumns;
	
	private final int columnsToSpan;
}