package scenariosui.gui.action;

import scenariosui.gui.util.purpose.ScenariosUIPurpose;
import scenariosui.gui.widget.composite.SelfHealingScenarioQueryComposite;
import scenariosui.properties.ScenariosUILabels;
import scenariosui.properties.TableConstants;

import commons.auth.AuthorizationHelper;

/**
 * Holds the GUI actions related to "Scenarios UI" and specifies the required grants to execute those actions.
 */

public final class ScenariosUIActions {

	/**
	 * This class is not intended to be instantiated.
	 */
	private ScenariosUIActions() {
		super();
	}

	private static final String NO_SHORTCUT = "";

	public static final String ID_NEW_SELF_HEALING_CONFIG = "scenarioUI.scenario.config.new";

	public static final String ID_OPEN_SELF_HEALING_CONFIG = "scenarioUI.scenario.config.open";

	public static final String ID_CLOSE_SELF_HEALING_CONFIG = "scenarioUI.scenario.config.close";

	public static final String ID_QUERY_SELF_HEALING_SCENARIO = "scenarioUI.scenario.query";

	public static final String ID_NEW_SELF_HEALING_SCENARIO = "scenarioUI.scenario.new";

	public static final String ID_EDIT_SELF_HEALING_SCENARIO = "scenarioUI.scenario.edit";

	public static final String ID_VIEW_SELF_HEALING_SCENARIO = "scenarioUI.scenario.view";

	public static SelfHealingConfigurationLoadAction NEW_SELF_HEALING_CONFIG = new SelfHealingConfigurationLoadAction(
			ID_NEW_SELF_HEALING_CONFIG, NO_SHORTCUT, true);

	public static final SelfHealingConfigurationLoadAction OPEN_SELF_HEALING_CONFIG = new SelfHealingConfigurationLoadAction(
			ID_OPEN_SELF_HEALING_CONFIG, NO_SHORTCUT, false);

	public static final SelfHealingConfigurationCloseAction CLOSE_SELF_HEALING_CONFIG = new SelfHealingConfigurationCloseAction(
			ID_CLOSE_SELF_HEALING_CONFIG, NO_SHORTCUT);

	public static final SelfHealingScenarioOpenDialogWithPurposeAction NEW_SELF_HEALING_SCENARIO = new SelfHealingScenarioOpenDialogWithPurposeAction(
			ID_NEW_SELF_HEALING_SCENARIO, NO_SHORTCUT, ScenariosUIPurpose.CREATION);

	public static final SelfHealingScenarioOpenDialogWithPurposeAction EDIT_SELF_HEALING_SCENARIO = new SelfHealingScenarioOpenDialogWithPurposeAction(
			ID_EDIT_SELF_HEALING_SCENARIO, NO_SHORTCUT, ScenariosUIPurpose.EDIT);

	public static final SelfHealingScenarioOpenDialogWithPurposeAction VIEW_SELF_HEALING_SCENARIO = new SelfHealingScenarioOpenDialogWithPurposeAction(
			ID_VIEW_SELF_HEALING_SCENARIO, NO_SHORTCUT, ScenariosUIPurpose.VIEW);

	public static final ScenariosUIQueryAction SCENARIOS_QUERY = new ScenariosUIQueryAction(
			ID_QUERY_SELF_HEALING_SCENARIO, ScenariosUILabels.SCENARIOS, TableConstants.SCENARIOS,
			SelfHealingScenarioQueryComposite.class, NO_SHORTCUT);

	/**
	 * Configures the permissions for executing the actions contained in this class
	 * 
	 * @param authHelper
	 *            the authorization helper to use
	 */
	public static void initialize(AuthorizationHelper authHelper) {
		authHelper.addGrantedPermissions(ID_NEW_SELF_HEALING_CONFIG, "EVERYONE");
		authHelper.addGrantedPermissions(ID_OPEN_SELF_HEALING_CONFIG, "EVERYONE");
		authHelper.addGrantedPermissions(ID_CLOSE_SELF_HEALING_CONFIG, "EVERYONE");
		authHelper.addGrantedPermissions(ID_QUERY_SELF_HEALING_SCENARIO, "EVERYONE");
		authHelper.addGrantedPermissions(ID_NEW_SELF_HEALING_SCENARIO, "EVERYONE");
	}
}