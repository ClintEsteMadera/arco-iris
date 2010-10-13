package scenariosui.gui.action;

import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;

import scenariosui.gui.widget.ScenariosUIWindow;
import ar.uba.dc.thesis.common.ArcoIrisDomainObject;

import commons.gui.action.QueryAction;
import commons.gui.widget.composite.QueryComposite;
import commons.properties.EnumProperty;

public class ScenariosUIQueryAction<T extends ArcoIrisDomainObject> extends QueryAction<T> {

	public ScenariosUIQueryAction(String uniqueId, String shortcut, EnumProperty menuText, EnumProperty tabItemText,
			Class<? extends QueryComposite<T>> queryCompositeClass) {
		super(uniqueId, shortcut, menuText, tabItemText, queryCompositeClass);
	}

	@Override
	protected CTabFolder getMainTabFolder() {
		return ScenariosUIWindow.getInstance().mainTabFolder;
	}

	@Override
	protected CTabItem getTabItem(EnumProperty tabItemText) {
		return ScenariosUIWindow.getInstance().getTabItem(tabItemText);
	}

	/**
	 * For this application, by design, we don't allow tabs to be closed.
	 */
	@Override
	protected boolean tabsAreCloseable() {
		return false;
	}
}
