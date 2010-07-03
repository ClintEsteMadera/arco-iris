package scenariosui.gui.action;

import org.eclipse.jface.action.Action;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Shell;

import scenariosui.gui.widget.ScenariosUIWindow;
import scenariosui.properties.ScenariosUILabels;
import scenariosui.properties.TableConstants;
import scenariosui.service.ScenariosUIController;
import ar.uba.dc.thesis.atam.scenario.persist.SelfHealingConfiguration;

import commons.gui.util.PageHelper;
import commons.properties.CommonLabels;

public class SelfHealingConfigurationLoadAction extends SelfHealingScenarioBaseFileAction {

	private final boolean createNewConfig;

	protected SelfHealingConfigurationLoadAction(String uniqueId, String shortcut, boolean createNewConfig) {
		super(uniqueId, shortcut);

		this.createNewConfig = createNewConfig;
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
			ScenariosUIController scenariosUIController = ScenariosUIController.getInstance();
			if (createNewConfig) {
				FileDialog dialog = this.createFileDialog(CommonLabels.LOAD.toString().toLowerCase());
				String xmlFilePath = dialog.open();
				if (xmlFilePath == null) {
					return;
				}
				scenariosUIController.newSelfHealingConfiguration(xmlFilePath);
			} else {
				FileDialog dialog = this.createFileDialog(CommonLabels.SAVE.toString().toLowerCase());
				String xmlFilePath = dialog.open();
				if (xmlFilePath == null) {
					return;
				}
				scenariosUIController.openSelfHealingConfiguration(xmlFilePath);
			}

			this.displayScenariosList(scenariosUIController);

			setCloseActionEnabled(true);
		}

		private void displayScenariosList(ScenariosUIController scenariosUIController) {
			ScenariosUIActions.SCENARIOS_QUERY.getActionFor(scenariosUIController.getCurrentSelfHealingConfiguration())
					.run();
			ScenariosUIWindow.getInstance().resetQuery(TableConstants.SCENARIOS);
		}

		private FileDialog createFileDialog(String... replacements) {
			Shell shell = PageHelper.getMainShell();

			String[] filterNames = new String[] { "XML Files" };
			String[] filterExtensions = new String[] { "*.xml" };
			String filterPath = "/";
			String platform = SWT.getPlatform();
			if (platform.equals("win32") || platform.equals("wpf")) {
				filterPath = System.getProperty("env.CSIDL_DESKTOP");
			}

			FileDialog dialog = new FileDialog(shell, SWT.OPEN);
			dialog.setText(ScenariosUILabels.FILE_DIALOG_MESSAGE.toString(replacements));
			dialog.setFileName(ScenariosUILabels.SCENARIOS.toString() + ".xml");
			dialog.setFilterNames(filterNames);
			dialog.setFilterExtensions(filterExtensions);
			dialog.setFilterPath(filterPath);

			return dialog;
		}
	}
}
