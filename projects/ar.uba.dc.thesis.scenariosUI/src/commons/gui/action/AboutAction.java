package commons.gui.action;

import java.text.MessageFormat;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.MessageDialog;


import commons.properties.CommonConstants;
import commons.properties.CommonLabels;
import commons.properties.CommonTooltips;

public class AboutAction extends Action {

	public AboutAction() {
		super(CommonLabels.MENU_HELP_ABOUT.toString(CommonConstants.APP_NAME));
		setToolTipText(CommonTooltips.ABOUT.toString());
	}

	@Override
	public void run() {
		MessageDialog.openInformation(null, ABOUT_TITLE, ABOUT_TEXT);
	}

	private static final String ABOUT_TITLE = CommonLabels.COPYRIGHT_TITLE.toString(CommonConstants.APP_NAME.toString());

	private static final String ABOUT_TEXT;
	static {
		String msg = CommonConstants.COPYRIGHT_TEXT.toString();
		String appName = CommonConstants.APP_NAME.toString();
		ABOUT_TEXT = MessageFormat.format(msg, appName);
	}
}