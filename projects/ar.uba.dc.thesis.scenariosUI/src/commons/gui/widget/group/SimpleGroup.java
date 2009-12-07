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
 * $Id: SimpleGroup.java,v 1.4 2007/11/30 20:31:08 cvsmarco Exp $
 */

package commons.gui.widget.group;

import org.apache.commons.lang.StringUtils;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;

import sba.common.properties.EnumProperty;

import commons.gui.widget.DefaultLayoutFactory;

/**
 * Grupo base del que heredan todos los grupos.
 * 
 * @author Jonathan Chiocchio
 * @version $Revision: 1.4 $ $Date: 2007/11/30 20:31:08 $
 */
public class SimpleGroup {

	public SimpleGroup(Composite composite, EnumProperty title, boolean readOnly) {
		this(composite, title, readOnly, DEFAULT_NUM_COLUMNS);
	}

	public SimpleGroup(Composite composite, EnumProperty title, boolean readOnly, int numColumns) {
		this.swtGroup = new Group(composite, SWT.NONE);
		this.numColumns = numColumns;
		String groupTitle = title.toString();
		if (StringUtils.isNotBlank(groupTitle)) {
			this.swtGroup.setText(groupTitle);
		}
		applyLayout();
		this.readOnly = readOnly;
	}

	public void setLayoutData(GridData gridData) {
		swtGroup.setLayoutData(gridData);
	}

	/**
	 * Provee un layout por default, sobreescribir este método si se desea otro
	 * layout.
	 */
	protected void applyLayout() {
		DefaultLayoutFactory.setDefaultGridLayout(this.getSwtGroup(), numColumns);
		GridLayout layout = (GridLayout) this.getSwtGroup().getLayout();
		if(!StringUtils.isEmpty(this.getSwtGroup().getText())){
			layout.marginTop = 5;
		}
		layout.marginHeight = 5;
		layout.marginWidth = 10;		
	}

	public Group getSwtGroup() {
		return this.swtGroup;
	}

	public int getNumColumns() {
		return numColumns;
	}
	
	private final Group swtGroup;
	
	protected boolean readOnly;

	private int numColumns;

	private static final int DEFAULT_NUM_COLUMNS = 2;

}