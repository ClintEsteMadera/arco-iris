package commons.gui.action;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eclipse.jface.action.Action;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;


import commons.gui.widget.composite.QueryComposite;
import commons.properties.EnumProperty;

/**
 * @author Jonathan Chiocchio
 * @version $Revision: 1.1 $ $Date: 2008/04/18 20:55:06 $
 */

public abstract class QueryAction implements GuiAction {

	protected QueryAction(String identificadorUnico, EnumProperty menuText, EnumProperty tabItemText,
			Class<? extends QueryComposite> queryCompositeClass, String shortcut) {

		this.identificadorUnico = identificadorUnico;
		this.menuText = menuText;
		this.tabItemText = tabItemText;
		this.queryCompositeClass = queryCompositeClass;
		this.shortcut = shortcut;
	}

	public Action getActionFor(Object model) {
		return new Action(this.getMenuText().toString()) {
			@Override
			public void run() {
				CTabFolder mainTabFolder = getMainTabFolder();
				CTabItem tabItem = getTabItem(getTabItemText());
				QueryComposite queryComposite;
				if (tabItem == null) {
					tabItem = new CTabItem(mainTabFolder, SWT.CLOSE);
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

	public String getUniqueId() {
		return this.identificadorUnico;
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

	public String getShortcut() {
		return shortcut;
	}

	protected abstract CTabFolder getMainTabFolder();

	protected abstract CTabItem getTabItem(EnumProperty tabItemText);

	private EnumProperty menuText;

	private EnumProperty tabItemText;

	private Class<? extends QueryComposite> queryCompositeClass;

	private String shortcut;

	private String identificadorUnico;

	private static final Log log = LogFactory.getLog(QueryAction.class);
}