package commons.gui.action;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.resource.ImageDescriptor;

import commons.gui.browser.BaseBrowser;
import commons.properties.CommonConstants;
import commons.properties.CommonLabels;
import commons.properties.CommonTooltips;

public class HelpAction extends Action {
	public HelpAction() {
		super(CommonLabels.MENU_HELP_HELP.toString(CommonConstants.APP_NAME), ImageDescriptor.createFromFile(
				HelpAction.class, "/images/help.png"));
		setToolTipText(CommonTooltips.HELP.toString());
	}

	@Override
	public void run() {
		BaseBrowser.show();
	}
}