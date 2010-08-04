package scenariosui.gui.action;

import org.eclipse.jface.action.Action;
import org.eclipse.swt.widgets.FileDialog;

import scenariosui.service.ScenariosUIManager;
import ar.uba.dc.thesis.atam.scenario.persist.SelfHealingConfiguration;

public class SelfHealingConfigurationCreateAction extends SelfHealingConfigurationBaseFileAction {

	protected SelfHealingConfigurationCreateAction(String uniqueId, String shortcut) {
		super(uniqueId, shortcut);
	}

	public Action getActionFor(final SelfHealingConfiguration model) {
		return new CreateAction(this.getUniqueId());
	}

	private class CreateAction extends Action {
		public CreateAction(String id) {
			super();
			this.setId(id);
		}

		@Override
		public void run() {
			FileDialog dialog = createNewFileDialog();
			String filePathWhereToWriteTheXMLTo = dialog.open();
			if (filePathWhereToWriteTheXMLTo == null) {
				return;
			}
			ScenariosUIManager.getInstance().createNewSelfHealingConfiguration(
					filePathWhereToWriteTheXMLTo);

			showAllQueries();
			setCloseActionEnabled(true);
		}
	}
}
