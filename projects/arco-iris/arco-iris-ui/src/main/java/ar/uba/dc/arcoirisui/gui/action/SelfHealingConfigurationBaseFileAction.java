package ar.uba.dc.arcoirisui.gui.action;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.FileDialog;

import ar.uba.dc.arcoirisui.properties.ArcoIrisUILabels;
import ar.uba.dc.arcoirisui.properties.ArcoIrisUIMessages;
import ar.uba.dc.arcoiris.common.ArcoIrisDomainObject;
import ar.uba.dc.arcoiris.selfhealing.config.SelfHealingConfiguration;

import commons.gui.action.BaseGuiAction;
import commons.gui.util.PageHelper;
import commons.properties.CommonLabels;

public abstract class SelfHealingConfigurationBaseFileAction extends BaseGuiAction<SelfHealingConfiguration> {

	protected SelfHealingConfigurationBaseFileAction(String uniqueId, String shortcut) {
		super(uniqueId, shortcut);
	}

	protected void showAllQueries() {
		showQuery(ArcoIrisUIActions.QUERY_ARTIFACT);
		showQuery(ArcoIrisUIActions.QUERY_ENVIRONMENT);
		showQuery(ArcoIrisUIActions.QUERY_SELF_HEALING_SCENARIO);
	}

	protected void setCloseActionEnabled(boolean enabled) {
		PageHelper.getMainWindow()
				.getMenuItem(ArcoIrisUIActions.CLOSE_SELF_HEALING_CONFIG.getUniqueId(), CommonLabels.MENU_FILE)
				.setEnabled(enabled);
	}

	protected FileDialog createNewFileDialog() {
		return this.createFileDialog(FileDialogPurpose.SAVE);
	}

	protected FileDialog createFileOpenDialog() {
		return this.createFileDialog(FileDialogPurpose.OPEN);
	}

	private FileDialog createFileDialog(FileDialogPurpose fileDialogPurpose) {
		String[] filterNames = new String[] { "XML Files" };
		String[] filterExtensions = new String[] { "*.xml" };
		String filterPath = "/";
		String platform = SWT.getPlatform();
		if (platform.equals("win32") || platform.equals("wpf")) {
			filterPath = System.getProperty("env.CSIDL_DESKTOP");
		}

		FileDialog dialog = new FileDialog(PageHelper.getMainShell(), fileDialogPurpose.getSWTStyle());
		dialog.setText(ArcoIrisUIMessages.FILE_DIALOG_MESSAGE.toString(fileDialogPurpose.getReplacements()));
		dialog.setFileName(ArcoIrisUILabels.SELF_HEALING_CONFIG.toString() + ".xml");
		dialog.setFilterNames(filterNames);
		dialog.setFilterExtensions(filterExtensions);
		dialog.setFilterPath(filterPath);

		return dialog;
	}

	private <T extends ArcoIrisDomainObject> void showQuery(ArcoIrisUIQueryAction<T> queryActionToBeTriggered) {
		queryActionToBeTriggered.getActionFor(null).run();
	}

	enum FileDialogPurpose {
		OPEN(SWT.OPEN, CommonLabels.LOAD.toString().toLowerCase(), ArcoIrisUILabels.FROM.toString()), SAVE(SWT.SAVE,
				CommonLabels.SAVE.toString().toLowerCase(), ArcoIrisUILabels.TO.toString());

		public int getSWTStyle() {
			return this.style;
		}

		public String[] getReplacements() {
			return this.replacements;
		}

		private int style;

		private String[] replacements;

		FileDialogPurpose(int style, String... replacements) {
			this.style = style;
			this.replacements = replacements;
		}
	}
}
