package scenariosui.gui.action;

import org.eclipse.jface.action.Action;
import org.eclipse.swt.widgets.FileDialog;

import scenariosui.service.SelfHealingConfigurationManager;
import ar.uba.dc.thesis.atam.scenario.persist.SelfHealingConfiguration;

public class SelfHealingConfigurationLoadAction extends SelfHealingScenarioBaseFileAction {

	protected SelfHealingConfigurationLoadAction(String uniqueId, String shortcut) {
		super(uniqueId, shortcut);
	}

	public Action getActionFor(final SelfHealingConfiguration model) {
		return new LoadAction(this.getUniqueId());
	}

	private class LoadAction extends Action {
		public LoadAction(String id) {
			super();
			this.setId(id);
		}

		@Override
		@SuppressWarnings("unchecked")
		public void run() {
			FileDialog dialog = createFileOpenDialog();
			String filePathToTheXML = dialog.open();
			if (filePathToTheXML == null) {
				return;
			}
			SelfHealingConfigurationManager.getInstance().openExistingSelfHealingConfiguration(filePathToTheXML);

			showAllQueries();
			setCloseActionEnabled(true);
		}
	}
}
