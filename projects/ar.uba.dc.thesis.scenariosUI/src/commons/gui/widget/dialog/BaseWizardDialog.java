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
 * $Id: BaseWizardDialog.java,v 1.1 2007/09/21 19:04:04 cvschioc Exp $
 */

package commons.gui.widget.dialog;

import org.eclipse.jface.wizard.IWizard;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Shell;

import commons.gui.util.PageHelper;

/**
 * @author Jonathan Chiocchio
 * @version $Revision: 1.1 $ $Date: 2007/09/21 19:04:04 $
 */

public class BaseWizardDialog extends WizardDialog {

	public BaseWizardDialog(IWizard wizard) {
		super(PageHelper.getMainShell(), wizard);
		super.setBlockOnOpen(true);
	}
	
	@Override
	protected void configureShell(Shell shell) {
		super.configureShell(shell);
		Rectangle centerLocation = PageHelper.getCenterLocation(700, 600);
		shell.setBounds(centerLocation);
	}
}