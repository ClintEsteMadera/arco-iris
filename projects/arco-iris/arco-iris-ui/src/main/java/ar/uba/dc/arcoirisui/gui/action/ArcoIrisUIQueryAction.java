package ar.uba.dc.arcoirisui.gui.action;

import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;

import ar.uba.dc.arcoiris.common.ArcoIrisDomainObject;
import ar.uba.dc.arcoirisui.gui.widget.ArcoIrisUIMainWindow;

import commons.gui.action.QueryAction;
import commons.gui.widget.composite.QueryComposite;
import commons.properties.EnumProperty;

public class ArcoIrisUIQueryAction<T extends ArcoIrisDomainObject> extends QueryAction<T> {

	public ArcoIrisUIQueryAction(String uniqueId, String shortcut, EnumProperty menuText, EnumProperty tabItemText,
			Class<? extends QueryComposite<T>> queryCompositeClass) {
		super(uniqueId, shortcut, menuText, tabItemText, queryCompositeClass);
	}

	@Override
	protected CTabFolder getMainTabFolder() {
		return ArcoIrisUIMainWindow.getInstance().mainTabFolder;
	}

	@Override
	protected CTabItem getTabItem(EnumProperty tabItemText) {
		return ArcoIrisUIMainWindow.getInstance().getTabItem(tabItemText);
	}

	/**
	 * For this application, by design, we don't allow tabs to be closed.
	 */
	@Override
	protected boolean tabsAreCloseable() {
		return false;
	}
}
