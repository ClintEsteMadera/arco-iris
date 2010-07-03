package scenariosui.gui.action;

import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;

import scenariosui.gui.widget.ScenariosUIWindow;

import commons.gui.action.QueryAction;
import commons.gui.widget.composite.QueryComposite;
import commons.properties.EnumProperty;

/**
 * @author Jonathan Chiocchio
 * @version $Revision: 1.1 $ $Date: 2008/04/18 20:55:16 $
 */

public class ScenariosUIQueryAction extends QueryAction {

	public ScenariosUIQueryAction(String identificadorUnico, EnumProperty menuText, EnumProperty tabItemText,
			Class<? extends QueryComposite> queryCompositeClass, String shortcut) {
		super(identificadorUnico, menuText, tabItemText, queryCompositeClass, shortcut);
	}

	@Override
	protected CTabFolder getMainTabFolder() {
		return ScenariosUIWindow.getInstance().mainTabFolder;
	}

	@Override
	protected CTabItem getTabItem(EnumProperty tabItemText) {
		return ScenariosUIWindow.getInstance().getTabItem(tabItemText);
	}
}
