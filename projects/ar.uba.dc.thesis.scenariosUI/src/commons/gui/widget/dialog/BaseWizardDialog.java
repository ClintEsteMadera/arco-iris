package commons.gui.widget.dialog;

import org.eclipse.jface.wizard.IWizard;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Shell;

import commons.gui.util.PageHelper;

/**
 * 
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