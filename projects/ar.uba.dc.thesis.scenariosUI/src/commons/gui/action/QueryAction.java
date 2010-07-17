package commons.gui.action;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eclipse.jface.action.Action;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;

import commons.gui.widget.composite.QueryComposite;
import commons.properties.EnumProperty;

public abstract class QueryAction extends BaseGuiAction {

	private EnumProperty menuText;

	private EnumProperty tabItemText;

	private Class<? extends QueryComposite> queryCompositeClass;

	private static final Log log = LogFactory.getLog(QueryAction.class);

	protected QueryAction(String uniqueId, String shortcut, EnumProperty menuText, EnumProperty tabItemText,
			Class<? extends QueryComposite> queryCompositeClass) {
		super(uniqueId, shortcut);
		this.menuText = menuText;
		this.tabItemText = tabItemText;
		this.queryCompositeClass = queryCompositeClass;
	}

	public Action getActionFor(Object model) {
		return new Action(this.getMenuText().toString()) {
			@Override
			public void run() {
				CTabFolder mainTabFolder = getMainTabFolder();
				CTabItem tabItem = getTabItem(getTabItemText());
				QueryComposite queryComposite;
				if (tabItem == null) {
					tabItem = new CTabItem(mainTabFolder, tabsAreCloseable() ? SWT.CLOSE : SWT.NONE);
					queryComposite = getQueryCompositeInstance();
					tabItem.setControl(queryComposite);
					tabItem.setText(getTabItemText().toString());
				} else {
					// Limpío los filtros que ya tuviera
					queryComposite = (QueryComposite) tabItem.getControl();
					queryComposite.reset();
				}
				mainTabFolder.setSelection(tabItem);
				mainTabFolder.setVisible(true);
			}

			private QueryComposite getQueryCompositeInstance() {
				QueryComposite queryComposite = null;
				try {
					queryComposite = getQueryCompositeClass().newInstance();
				} catch (Exception ex) {
					log.fatal("No se ha podido instanciar la  clase " + getQueryCompositeClass().getSimpleName(), ex);
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

	public Class<? extends QueryComposite> getQueryCompositeClass() {
		return this.queryCompositeClass;
	}

	protected abstract CTabFolder getMainTabFolder();

	protected abstract CTabItem getTabItem(EnumProperty tabItemText);

	protected abstract boolean tabsAreCloseable();
}