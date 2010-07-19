package scenariosui.gui.action;

import scenariosui.gui.util.purpose.ScenariosUIPurpose;
import scenariosui.gui.widget.composite.EnvironmentQueryComposite;
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

	public static SelfHealingConfigurationLoadAction NEW_SELF_HEALING_CONFIG = new SelfHealingConfigurationLoadAction(
			"scenarioUI.scenario.config.new", NO_SHORTCUT, true);

	public static final SelfHealingConfigurationLoadAction OPEN_SELF_HEALING_CONFIG = new SelfHealingConfigurationLoadAction(
			"scenarioUI.scenario.config.open", NO_SHORTCUT, false);

	public static final SelfHealingConfigurationCloseAction CLOSE_SELF_HEALING_CONFIG = new SelfHealingConfigurationCloseAction(
			"scenarioUI.scenario.config.close", NO_SHORTCUT);

	public static final SelfHealingScenarioOpenDialogWithPurposeAction NEW_SELF_HEALING_SCENARIO = new SelfHealingScenarioOpenDialogWithPurposeAction(
			"scenarioUI.scenario.new", NO_SHORTCUT, ScenariosUIPurpose.CREATION);

	public static final SelfHealingScenarioOpenDialogWithPurposeAction EDIT_SELF_HEALING_SCENARIO = new SelfHealingScenarioOpenDialogWithPurposeAction(
			"scenarioUI.scenario.edit", NO_SHORTCUT, ScenariosUIPurpose.EDIT);

	public static final SelfHealingScenarioOpenDialogWithPurposeAction VIEW_SELF_HEALING_SCENARIO = new SelfHealingScenarioOpenDialogWithPurposeAction(
			"scenarioUI.scenario.view", NO_SHORTCUT, ScenariosUIPurpose.VIEW);

	public static final ScenariosUIQueryAction SCENARIOS_QUERY = new ScenariosUIQueryAction(
			"scenarioUI.scenario.query", NO_SHORTCUT, ScenariosUILabels.SCENARIOS, TableConstants.SCENARIOS,
			SelfHealingScenarioQueryComposite.class);

	public static final ScenariosUIQueryAction ENVIRONMENT_QUERY = new ScenariosUIQueryAction(
			"scenarioUI.environment.query", NO_SHORTCUT, ScenariosUILabels.ENVIRONMENTS, TableConstants.ENVIRONMENTS,
			EnvironmentQueryComposite.class);

	public static final EnvironmentOpenDialogWithPurposeAction NEW_ENVIRONMENT = new EnvironmentOpenDialogWithPurposeAction(
			"scenarioUI.environment.new", NO_SHORTCUT, ScenariosUIPurpose.CREATION);

	/**
	 * Configures the permissions for executing the actions contained in this class
	 * 
	 * @param authHelper
	 *            the authorization helper to use
	 */
	public static void initialize(AuthorizationHelper authHelper) {
		authHelper.addGrantedPermissions(NEW_SELF_HEALING_CONFIG.getUniqueId(), "EVERYONE");
		authHelper.addGrantedPermissions(OPEN_SELF_HEALING_CONFIG.getUniqueId(), "EVERYONE");
		authHelper.addGrantedPermissions(CLOSE_SELF_HEALING_CONFIG.getUniqueId(), "EVERYONE");
		authHelper.addGrantedPermissions(NEW_SELF_HEALING_SCENARIO.getUniqueId(), "EVERYONE");
		authHelper.addGrantedPermissions(EDIT_SELF_HEALING_SCENARIO.getUniqueId(), "EVERYONE");
		authHelper.addGrantedPermissions(VIEW_SELF_HEALING_SCENARIO.getUniqueId(), "EVERYONE");
		authHelper.addGrantedPermissions(SCENARIOS_QUERY.getUniqueId(), "EVERYONE");
		authHelper.addGrantedPermissions(ENVIRONMENT_QUERY.getUniqueId(), "EVERYONE");
	}
}