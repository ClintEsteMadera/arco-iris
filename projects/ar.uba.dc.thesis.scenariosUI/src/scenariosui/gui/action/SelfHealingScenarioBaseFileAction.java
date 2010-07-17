package scenariosui.gui.action;

import ar.uba.dc.thesis.atam.scenario.persist.SelfHealingConfiguration;

import commons.gui.action.BaseGuiAction;
import commons.gui.util.PageHelper;
import commons.properties.CommonLabels;

public abstract class SelfHealingScenarioBaseFileAction extends BaseGuiAction<SelfHealingConfiguration> {

	protected SelfHealingScenarioBaseFileAction(String uniqueId, String shortcut) {
		super(uniqueId, shortcut);
	}

	protected void setCloseActionEnabled(boolean enabled) {
		PageHelper.getMainWindow().getMenuItem(ScenariosUIActions.CLOSE_SELF_HEALING_CONFIG.getUniqueId(),
				CommonLabels.MENU_FILE).setEnabled(enabled);
	}

}
