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
 * $Id: BaseTitleAreaDialog.java,v 1.2 2007/08/09 20:27:29 cvschioc Exp $
 */
package commons.gui.widget.dialog;

import org.apache.commons.lang.StringUtils;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

/**
 * Clase Base de la que extienden todos los diálogos. Esto permite implementar métodos comunes 
 * a todos los diálogos desarrollados en Caja de Valores, manteniendo toda la funcionalidad de 
 * un {@link TitleAreaDialog}
 * 
 * 
 * @author Gabriel Tursi
 * @version $Revision: 1.2 $ $Date: 2007/08/09 20:27:29 $
 */
public class BaseTitleAreaDialog extends TitleAreaDialog {

	public BaseTitleAreaDialog(Shell parentShell) {
		super(parentShell);
	}
	
	protected void setNotNullValidationListener(final Text control) {
		control.addFocusListener(new FocusListener() {

			public void focusGained(FocusEvent event) {
				boolean estaEnBlanco = StringUtils.isBlank(control.getText()); 
				if (estaEnBlanco) {
					Button botonAceptar = BaseTitleAreaDialog.super.getButton(0); 
					botonAceptar.setEnabled(false);
					BaseTitleAreaDialog.this.setErrorMessage("El campo no puede ser nulo");
				}
			}

			public void focusLost(FocusEvent event) {
				BaseTitleAreaDialog.this.setErrorMessage(null);
			}
		});

		control.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent event) {
				boolean estaEnBlanco = StringUtils.isBlank(control.getText());
				if (estaEnBlanco) {
					BaseTitleAreaDialog.super.getButton(0).setEnabled(false);
					BaseTitleAreaDialog.this.setErrorMessage("El campo no puede ser nulo");
				} else {
					BaseTitleAreaDialog.super.getButton(0).setEnabled(true);
					BaseTitleAreaDialog.this.setErrorMessage(null);
				}
			}
		});
	}
}