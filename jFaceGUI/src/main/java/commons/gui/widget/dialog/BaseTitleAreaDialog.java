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
 * Clase Base de la que extienden todos los diálogos. Esto permite implementar métodos comunes a todos los diálogos,
 * manteniendo toda la funcionalidad de un {@link TitleAreaDialog}
 * 
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