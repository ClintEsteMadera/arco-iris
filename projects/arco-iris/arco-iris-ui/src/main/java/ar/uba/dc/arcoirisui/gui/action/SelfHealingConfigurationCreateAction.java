package ar.uba.dc.arcoirisui.gui.action;

import org.eclipse.jface.action.Action;
import org.eclipse.swt.widgets.FileDialog;

import ar.uba.dc.arcoirisui.service.ArcoIrisUIManager;
import ar.uba.dc.arcoiris.selfhealing.config.SelfHealingConfiguration;

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
			ArcoIrisUIManager.getInstance().createNewSelfHealingConfiguration(filePathWhereToWriteTheXMLTo);

			showAllQueries();
			setCloseActionEnabled(true);
		}
	}
}
