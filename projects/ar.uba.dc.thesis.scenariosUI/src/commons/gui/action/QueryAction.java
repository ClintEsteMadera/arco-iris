package commons.gui.action;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eclipse.jface.action.Action;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;

import commons.gui.widget.composite.QueryComposite;
import commons.properties.EnumProperty;

public abstract class QueryAction<T> extends BaseGuiAction<T> {

	private EnumProperty menuText;

	private EnumProperty tabItemText;

	private Class<? extends QueryComposite<T>> queryCompositeClass;

	private static final Log log = LogFactory.getLog(QueryAction.class);

	protected QueryAction(String uniqueId, String shortcut, EnumProperty menuText, EnumProperty tabItemText,
			Class<? extends QueryComposite<T>> queryCompositeClass) {
		super(uniqueId, shortcut);
		this.menuText = menuText;
		this.tabItemText = tabItemText;
		this.queryCompositeClass = queryCompositeClass;
	}

	public Action getActionFor(T model) {
		return new Action(this.getMenuText().toString()) {
			@Override
			@SuppressWarnings("unchecked")
			public void run() {
				CTabFolder mainTabFolder = getMainTabFolder();
				CTabItem tabItem = getTabItem(getTabItemText());
				QueryComposite<T> queryComposite;
				if (tabItem == null) {
					tabItem = new CTabItem(mainTabFolder, tabsAreCloseable() ? SWT.CLOSE : SWT.NONE);
					queryComposite = getQueryCompositeInstance();
					tabItem.setControl(queryComposite);
					tabItem.setText(getTabItemText().toString());
				} else {
					// Limpío los filtros que ya tuviera
					queryComposite = (QueryComposite<T>) tabItem.getControl();
					queryComposite.reset();
				}
				mainTabFolder.setSelection(tabItem);
				mainTabFolder.setVisible(true);
			}

			private QueryComposite<T> getQueryCompositeInstance() {
				QueryComposite<T> queryComposite = null;
				try {
					queryComposite = getQueryCompositeClass().newInstance();
				} catch (Exception ex) {
					log.fatal("No se ha podido instanciar la clase " + getQueryCompositeClass().getSimpleName(), ex);
				}
				return queryComposite;
			}
		};
	}

	public EnumProperty getMenuText() {
		return this.menuText;
	}

	public EnumProperty getTabItemText() {
		return this.tabItemText;
	}

	public Class<? extends QueryComposite<T>> getQueryCompositeClass() {
		return this.queryCompositeClass;
	}

	protected abstract CTabFolder getMainTabFolder();

	protected abstract CTabItem getTabItem(EnumProperty tabItemText);

	protected abstract boolean tabsAreCloseable();
}