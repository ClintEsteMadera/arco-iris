package scenariosui.gui.action;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Shell;

import scenariosui.properties.ScenariosUILabels;
import ar.uba.dc.thesis.atam.scenario.persist.SelfHealingConfiguration;
import ar.uba.dc.thesis.common.ThesisPojo;

import commons.gui.action.BaseGuiAction;
import commons.gui.util.PageHelper;
import commons.properties.CommonLabels;

public abstract class SelfHealingScenarioBaseFileAction extends BaseGuiAction<SelfHealingConfiguration> {

	protected SelfHealingScenarioBaseFileAction(String uniqueId, String shortcut) {
		super(uniqueId, shortcut);
	}

	protected void showAllQueries() {
		showQuery(ScenariosUIActions.QUERY_ARTIFACT);
		showQuery(ScenariosUIActions.QUERY_ENVIRONMENT);
		showQuery(ScenariosUIActions.QUERY_SELF_HEALING_SCENARIO);
	}

	protected void setCloseActionEnabled(boolean enabled) {
		PageHelper.getMainWindow().getMenuItem(ScenariosUIActions.CLOSE_SELF_HEALING_CONFIG.getUniqueId(),
				CommonLabels.MENU_FILE).setEnabled(enabled);
	}

	protected FileDialog createNewFileDialog() {
		return this.createFileDialog(CommonLabels.SAVE.toString().toLowerCase(), ScenariosUILabels.TO.toString());
	}

	protected FileDialog createFileOpenDialog() {
		return this.createFileDialog(CommonLabels.LOAD.toString().toLowerCase(), ScenariosUILabels.FROM.toString());
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
		dialog.setFileName(ScenariosUILabels.SELF_HEALING_CONFIG.toString() + ".xml");
		dialog.setFilterNames(filterNames);
		dialog.setFilterExtensions(filterExtensions);
		dialog.setFilterPath(filterPath);

		return dialog;
	}

	private <T extends ThesisPojo> void showQuery(ScenariosUIQueryAction<T> queryActionToBeTriggered) {
		queryActionToBeTriggered.getActionFor(null).run();
	}
}
