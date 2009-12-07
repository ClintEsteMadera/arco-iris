package commons.gui.action;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.resource.ImageDescriptor;

import commons.auth.AuthenticationHelper;
import commons.gui.util.PageHelper;
import commons.gui.widget.dialog.ChangePwdDialog;
import commons.properties.CommonLabels;

public class ChangePwdAction extends Action {

	public ChangePwdAction(AuthenticationHelper authHelper) {
		super(CommonLabels.MENU_FILE_PASSWORD_CHANGE.toString(), ImageDescriptor.createFromFile(
				ChangePwdAction.class, "/images/chpwd.gif"));
		this.authHelper = authHelper;
	}

	@Override
	public void run() {
		ChangePwdDialog.openWithUserLoggedIn(PageHelper.getMainShell(), false, authHelper);
	}

	private AuthenticationHelper authHelper;
}