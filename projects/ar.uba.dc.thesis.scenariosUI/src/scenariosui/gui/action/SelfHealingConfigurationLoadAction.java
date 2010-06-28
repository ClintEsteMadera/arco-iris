package scenariosui.gui.action;

import org.eclipse.jface.action.Action;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Shell;
import org.springframework.util.Assert;

import scenariosui.properties.ScenariosUILabels;
import scenariosui.service.ScenariosUIController;
import ar.uba.dc.thesis.atam.scenario.persist.SelfHealingConfiguration;

import commons.gui.action.GuiAction;
import commons.gui.util.PageHelper;

public class SelfHealingConfigurationLoadAction implements GuiAction<SelfHealingConfiguration> {

	private final String shortcut;

	private String uniqueId;

	private final boolean createNewConfig;

	protected SelfHealingConfigurationLoadAction(String uniqueId, String shortcut, boolean createNewConfig) {
		super();
		Assert.notNull(uniqueId, "The unique id cannot be null");

		this.uniqueId = uniqueId;
		this.shortcut = shortcut == null ? "" : shortcut;
		this.createNewConfig = createNewConfig;
	}

	public Action getActionFor(final SelfHealingConfiguration model) {
		return new Action() {
			@Override
			@SuppressWarnings("unchecked")
			public void run() {
				FileDialog dialog = this.createFileDialog();

				String xmlFilePath = dialog.open();
				if (createNewConfig) {
					ScenariosUIController.newSelfHealingConfiguration(xmlFilePath);
				} else {
					ScenariosUIController.openSelfHealingConfiguration(xmlFilePath);
				}

				ScenariosUIActions.SCENARIOS_QUERY.getActionFor(model).run();
			}

			private FileDialog createFileDialog() {
				Shell shell = PageHelper.getMainShell();

				String[] filterNames = new String[] { "XML Files" };
				String[] filterExtensions = new String[] { "*.xml" };
				String filterPath = "/";
				String platform = SWT.getPlatform();
				if (platform.equals("win32") || platform.equals("wpf")) {
					filterPath = System.getProperty("env.CSIDL_DESKTOP");
				}

				FileDialog dialog = new FileDialog(shell, SWT.OPEN);
				dialog.setText("Please select XML File to save the new configuration to");
				dialog.setFileName(ScenariosUILabels.SCENARIOS.toString() + ".xml");
				dialog.setFilterNames(filterNames);
				dialog.setFilterExtensions(filterExtensions);
				dialog.setFilterPath(filterPath);

				return dialog;
			}
		};
	}

	public String getUniqueId() {
		return this.uniqueId;
	}

	public String getShortcut() {
		return this.shortcut;
	}
}
