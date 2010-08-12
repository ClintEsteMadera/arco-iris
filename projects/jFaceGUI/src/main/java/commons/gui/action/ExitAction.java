package commons.gui.action;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Shell;

import commons.gui.util.PageHelper;
import commons.properties.CommonConstants;
import commons.properties.CommonLabels;
import commons.properties.CommonMessages;

public class ExitAction extends Action {

	public ExitAction() {
		super(CommonLabels.MENU_FILE_EXIT.toString(), ImageDescriptor.createFromFile(ExitAction.class,
				"/images/exit.png"));
		setToolTipText("¿Desea salir de la aplicación?");
	}

	@Override
	public void run() {
		PageHelper.getMainShell().notifyListeners(SWT.Close, null);
	}

	public static boolean confirmExit() {
		boolean confirm = true;
		if (!PageHelper.getMainWindow().getSystemConfiguration().developmentEnvironment()) {
			Shell shell = PageHelper.getMainShell();
			confirm = MessageDialog.openConfirm(shell, CommonLabels.EXIT_DIALOG_TITLE.toString(),
					CommonMessages.EXIT_DIALOG_MESSAGE.toString(CommonConstants.APP_NAME));
		}
		return confirm;
	}
}