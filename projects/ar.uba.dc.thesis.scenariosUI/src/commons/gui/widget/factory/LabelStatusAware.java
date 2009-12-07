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
 * $Id: LabelStatusAware.java,v 1.3 2008/05/16 16:08:42 cvsmvera Exp $
 */

package commons.gui.widget.factory;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;

import commons.gui.model.validation.ValidationStatusAware;
import commons.gui.widget.DefaultLayoutFactory;

/**
 * 
 * @author Pablo Pastorino
 * @version $Revision: 1.3 $ $Date: 2008/05/16 16:08:42 $
 */

public class LabelStatusAware implements ValidationStatusAware {

	public LabelStatusAware(Composite composite) {
		this.label=new Label(composite,SWT.CENTER);
		GridData gd=DefaultLayoutFactory.getGridData(label);
		gd.heightHint=12;
		gd.widthHint=12;
		gd.horizontalIndent=0;
		gd.verticalIndent=0;
		gd.grabExcessHorizontalSpace=false;

		this.ok=true;
	}

	public boolean isOkStatus() {
		return this.ok;
	}

	public void setErrorStatus(String message) {
		label.setToolTipText(message);
		
		if(this.image == null){
			this.image=new Image(Display.getCurrent(), getClass().getResourceAsStream(
			"/images/bullet_delete.png"));
		}
		label.setImage(this.image);
	}

	public void setOkStatus() {
		label.setToolTipText("");
		label.setImage(null);
	}

	private boolean ok;

	private Image image;
	private Label label;
}
