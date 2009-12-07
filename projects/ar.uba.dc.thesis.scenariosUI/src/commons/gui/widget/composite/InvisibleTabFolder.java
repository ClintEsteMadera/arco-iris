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
 * $Id: InvisibleTabFolder.java,v 1.4 2007/11/30 20:31:07 cvsmarco Exp $
 */
package commons.gui.widget.composite;

import org.eclipse.swt.custom.StackLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Layout;

/**
 * 
 * @author Gabriel Tursi
 * @version $Revision: 1.4 $ $Date: 2007/11/30 20:31:07 $
 */
public class InvisibleTabFolder extends Composite {
	
	public InvisibleTabFolder(Composite parent, int style) {
		super(parent, style);
		StackLayout stackLayout = new StackLayout();
		stackLayout.marginWidth = -5;
		super.setLayout(stackLayout);
	}

	@Override
	public void setLayout(Layout layout) {
		// no se puede establecer un nuevo Layout
	}

	public void setSelection(int index) {
		Control[] children = getChildren();
		if (index >= 0 && index < children.length) {
			((StackLayout) getLayout()).topControl = children[index];
			layout();
		}
	}
	
}
