package scenariosui.gui.action;

import org.springframework.util.Assert;

import ar.uba.dc.thesis.atam.scenario.persist.SelfHealingConfiguration;

import commons.gui.action.GuiAction;
import commons.gui.util.PageHelper;
import commons.properties.CommonLabels;

public abstract class SelfHealingScenarioBaseFileAction implements GuiAction<SelfHealingConfiguration> {

	private String shortcut;

	private String uniqueId;

	protected SelfHealingScenarioBaseFileAction(String uniqueId, String shortcut) {
		super();
		Assert.notNull(uniqueId, "The unique id cannot be null");
		this.uniqueId = uniqueId;
		this.shortcut = shortcut;
	}

	@Override
	public String getShortcut() {
		return this.shortcut;
	}

	@Override
	public String getUniqueId() {
		return this.uniqueId;
	}

	protected void setCloseActionEnabled(boolean enabled) {
		PageHelper.getMainWindow().getMenuItem(ScenariosUIActions.ID_CLOSE_SELF_HEALING_CONFIG, CommonLabels.MENU_FILE)
				.setEnabled(enabled);
	}

}
