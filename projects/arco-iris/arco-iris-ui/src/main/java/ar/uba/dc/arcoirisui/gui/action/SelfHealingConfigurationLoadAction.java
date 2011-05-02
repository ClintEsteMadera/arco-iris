package ar.uba.dc.arcoirisui.gui.action;

import org.eclipse.jface.action.Action;
import org.eclipse.swt.widgets.FileDialog;

import ar.uba.dc.arcoirisui.service.ArcoIrisUIManager;
import ar.uba.dc.arcoiris.atam.scenario.persist.SelfHealingConfiguration;

public class SelfHealingConfigurationLoadAction extends SelfHealingConfigurationBaseFileAction {

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
		public void run() {
			FileDialog dialog = createFileOpenDialog();
			String filePathToTheXML = dialog.open();
			if (filePathToTheXML == null) {
				return;
			}
			ArcoIrisUIManager.getInstance().openExistingSelfHealingConfiguration(filePathToTheXML);

			showAllQueries();
			setCloseActionEnabled(true);
		}
	}
}
