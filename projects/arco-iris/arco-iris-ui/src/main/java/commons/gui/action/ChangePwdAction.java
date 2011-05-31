package commons.gui.action;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.resource.ImageDescriptor;

import commons.auth.AuthenticationManager;
import commons.gui.util.PageHelper;
import commons.gui.widget.dialog.ChangePwdDialog;
import commons.properties.CommonLabels;

public class ChangePwdAction extends Action {
	
	private AuthenticationManager authManager;

	public ChangePwdAction(AuthenticationManager authManager) {
		super(CommonLabels.MENU_FILE_PASSWORD_CHANGE.toString(), ImageDescriptor.createFromFile(ChangePwdAction.class,
				"/images/chpwd.gif"));
		this.authManager = authManager;
	}

	@Override
	public void run() {
		ChangePwdDialog.openWithUserLoggedIn(PageHelper.getMainShell(), false, this.authManager);
	}
}