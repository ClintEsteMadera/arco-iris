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
 * $Id: BaseDialog.java,v 1.3 2008/03/26 15:57:28 cvsmarco Exp $
 */
package commons.gui.widget.dialog;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;

import commons.gui.util.PageHelper;

/**
 * 
 * @author Gabriel Tursi
 * @version $Revision: 1.3 $ $Date: 2008/03/26 15:57:28 $
 */
public abstract class BaseDialog extends Dialog {

	public BaseDialog(String title, boolean readOnly) {
		super(PageHelper.getMainShell());
		setShellStyle(getShellStyle() | SWT.RESIZE | SWT.MAX);
		this.readOnly = readOnly;
		this.title = title;
	}

	/**
	 * Sobreescribir este m�todo para definir un tama�o espec�fico
	 */
	@Override
	protected Point getInitialSize() {
		return super.getInitialSize();
	}

	@Override
	protected void configureShell(Shell shell) {
		super.configureShell(shell);
		shell.setText(title);
	}

	@Override
	protected Control createDialogArea(Composite parent) {
		applyLayout(parent);
		return addFields(parent);
	}

	protected abstract Control addFields(Composite parent);

	protected void applyLayout(Composite parent) {
		GridLayout gridLayout = new GridLayout();
		gridLayout.marginTop = 10;
		gridLayout.marginBottom = 10;
		gridLayout.marginLeft = 5;
		gridLayout.marginRight = 5;
		gridLayout.verticalSpacing = 10;
		parent.setLayout(gridLayout);

	}

	protected boolean readOnly;

	private final String title;

}
