package scenariosui.gui.action;

import org.eclipse.jface.action.Action;
import org.eclipse.swt.custom.CTabItem;

import scenariosui.service.ScenariosUIManager;
import ar.uba.dc.thesis.atam.scenario.persist.SelfHealingConfiguration;

import commons.gui.util.PageHelper;

public class SelfHealingConfigurationCloseAction extends SelfHealingConfigurationBaseFileAction {

	protected SelfHealingConfigurationCloseAction(String uniqueId, String shortcut) {
		super(uniqueId, shortcut);
	}

	public Action getActionFor(SelfHealingConfiguration model) {
		return new CloseAction(this.getUniqueId());
	}

	private class CloseAction extends Action {

		public CloseAction(String id) {
			super();
			this.setId(id);
		}

		@Override
		public void run() {
			ScenariosUIManager.getInstance().close();
			closeQueryComposites();
			setCloseActionEnabled(false);
		}
	}

	private void closeQueryComposites() {
		CTabItem[] items = PageHelper.getMainWindow().mainTabFolder.getItems();
		for (CTabItem tabItem : items) {
			tabItem.dispose();
		}
		PageHelper.getMainWindow().mainTabFolder.setVisible(false);
	}
}
