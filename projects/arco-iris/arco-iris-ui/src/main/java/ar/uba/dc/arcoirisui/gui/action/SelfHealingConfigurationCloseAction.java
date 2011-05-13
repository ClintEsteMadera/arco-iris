package ar.uba.dc.arcoirisui.gui.action;

import org.eclipse.jface.action.Action;
import org.eclipse.swt.custom.CTabItem;

import ar.uba.dc.arcoirisui.service.ArcoIrisUIManager;
import ar.uba.dc.arcoiris.selfhealing.config.SelfHealingConfiguration;

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
			ArcoIrisUIManager.getInstance().close();
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
