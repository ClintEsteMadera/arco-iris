package scenariosui.gui.action;

import org.eclipse.jface.action.Action;
import org.eclipse.swt.custom.CTabFolder;

import scenariosui.properties.TableConstants;
import scenariosui.service.ScenariosUIController;
import ar.uba.dc.thesis.atam.scenario.persist.SelfHealingConfiguration;

import commons.gui.util.PageHelper;

public class SelfHealingConfigurationCloseAction extends SelfHealingScenarioBaseFileAction {

	protected SelfHealingConfigurationCloseAction(String uniqueId, String shortcut) {
		super(uniqueId, shortcut);
	}

	public Action getActionFor(final SelfHealingConfiguration model) {
		return new CloseAction(this.getUniqueId());
	}

	private class CloseAction extends Action {

		public CloseAction(String id) {
			super();
			this.setId(id);
		}

		@Override
		@SuppressWarnings("unchecked")
		public void run() {
			ScenariosUIController scenariosUIController = ScenariosUIController.getInstance();

			scenariosUIController.closeSelfHealingConfiguration();

			this.closeScenariosList();

			setCloseActionEnabled(false);
		}

		private void closeScenariosList() {
			CTabFolder mainTabFolder = PageHelper.getMainWindow().mainTabFolder;
			if (mainTabFolder.getItemCount() == 1) {
				mainTabFolder.setVisible(false);
			}
			PageHelper.getMainWindow().getTabItem(TableConstants.SCENARIOS).dispose();
		}
	}
}
