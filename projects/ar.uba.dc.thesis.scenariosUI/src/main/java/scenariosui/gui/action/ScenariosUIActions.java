package scenariosui.gui.action;

import scenariosui.gui.widget.composite.query.ArtifactQueryComposite;
import scenariosui.gui.widget.composite.query.EnvironmentQueryComposite;
import scenariosui.gui.widget.composite.query.SelfHealingScenarioQueryComposite;
import scenariosui.properties.ScenariosUILabels;
import scenariosui.properties.UniqueTableIdentifier;
import ar.uba.dc.thesis.atam.scenario.model.Artifact;
import ar.uba.dc.thesis.atam.scenario.model.Environment;
import ar.uba.dc.thesis.selfhealing.SelfHealingScenario;

import commons.auth.AuthorizationManager;
import commons.gui.action.OpenDialogWithPurposeAction;
import commons.gui.util.purpose.BasePurpose;
import commons.gui.util.purpose.Purpose;

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

	// Self Healing Configuration

	public static SelfHealingConfigurationCreateAction NEW_SELF_HEALING_CONFIG = new SelfHealingConfigurationCreateAction(
			"scenarioUI.scenario.config.new", "CTRL+N");

	public static final SelfHealingConfigurationLoadAction OPEN_SELF_HEALING_CONFIG = new SelfHealingConfigurationLoadAction(
			"scenarioUI.scenario.config.open", "CTRL+O");

	public static final SelfHealingConfigurationCloseAction CLOSE_SELF_HEALING_CONFIG = new SelfHealingConfigurationCloseAction(
			"scenarioUI.scenario.config.close", "CTRL+W");

	// Self Healing Scenario

	public static final SelfHealingScenarioOpenDialogWithPurposeAction NEW_SELF_HEALING_SCENARIO = new SelfHealingScenarioOpenDialogWithPurposeAction(
			"scenarioUI.scenario.new", NO_SHORTCUT, BasePurpose.CREATION);

	public static final SelfHealingScenarioOpenDialogWithPurposeAction EDIT_SELF_HEALING_SCENARIO = new SelfHealingScenarioOpenDialogWithPurposeAction(
			"scenarioUI.scenario.edit", NO_SHORTCUT, BasePurpose.EDIT);

	public static final SelfHealingScenarioOpenDialogWithPurposeAction DELETE_SELF_HEALING_SCENARIO = new SelfHealingScenarioOpenDialogWithPurposeAction(
			"scenarioUI.scenario.delete", NO_SHORTCUT, BasePurpose.DELETE);

	public static final ScenariosUIQueryAction<SelfHealingScenario> QUERY_SELF_HEALING_SCENARIO = new ScenariosUIQueryAction<SelfHealingScenario>(
			"scenarioUI.scenario.query", NO_SHORTCUT, ScenariosUILabels.SCENARIOS, UniqueTableIdentifier.SCENARIOS,
			SelfHealingScenarioQueryComposite.class);

	// Environment

	public static final EnvironmentOpenDialogWithPurposeAction NEW_ENVIRONMENT = new EnvironmentOpenDialogWithPurposeAction(
			"scenarioUI.environment.new", NO_SHORTCUT, BasePurpose.CREATION);

	public static final OpenDialogWithPurposeAction<Environment, Purpose> EDIT_ENVIRONMENT = new EnvironmentOpenDialogWithPurposeAction(
			"scenarioUI.environment.edit", NO_SHORTCUT, BasePurpose.EDIT);

	public static final OpenDialogWithPurposeAction<Environment, Purpose> DELETE_ENVIRONMENT = new EnvironmentOpenDialogWithPurposeAction(
			"scenarioUI.environment.delete", NO_SHORTCUT, BasePurpose.DELETE);

	public static final ScenariosUIQueryAction<Environment> QUERY_ENVIRONMENT = new ScenariosUIQueryAction<Environment>(
			"scenarioUI.environment.query", NO_SHORTCUT, ScenariosUILabels.ENVIRONMENTS,
			UniqueTableIdentifier.ENVIRONMENTS, EnvironmentQueryComposite.class);

	// Artifact

	public static final OpenDialogWithPurposeAction<Artifact, Purpose> NEW_ARTIFACT = new ArtifactOpenDialogWithPurposeAction(
			"scenarioUI.artifact.new", NO_SHORTCUT, BasePurpose.CREATION);

	public static final OpenDialogWithPurposeAction<Artifact, Purpose> EDIT_ARTIFACT = new ArtifactOpenDialogWithPurposeAction(
			"scenarioUI.artifact.edit", NO_SHORTCUT, BasePurpose.EDIT);

	public static final OpenDialogWithPurposeAction<Artifact, Purpose> DELETE_ARTIFACT = new ArtifactOpenDialogWithPurposeAction(
			"scenarioUI.artifact.delete", NO_SHORTCUT, BasePurpose.DELETE);

	public static final ScenariosUIQueryAction<Artifact> QUERY_ARTIFACT = new ScenariosUIQueryAction<Artifact>(
			"scenarioUI.artifact.query", NO_SHORTCUT, ScenariosUILabels.ARTIFACTS, UniqueTableIdentifier.ARTIFACTS,
			ArtifactQueryComposite.class);

	/**
	 * Configures the permissions for executing the actions contained in this class
	 * 
	 * @param authHelper
	 *            the authorization helper to use
	 */
	public static void initialize(AuthorizationManager authHelper) {
		authHelper.addGrantedPermissions(NEW_SELF_HEALING_CONFIG.getUniqueId(), "EVERYONE");
		authHelper.addGrantedPermissions(OPEN_SELF_HEALING_CONFIG.getUniqueId(), "EVERYONE");
		authHelper.addGrantedPermissions(CLOSE_SELF_HEALING_CONFIG.getUniqueId(), "EVERYONE");

		authHelper.addGrantedPermissions(NEW_SELF_HEALING_SCENARIO.getUniqueId(), "EVERYONE");
		authHelper.addGrantedPermissions(EDIT_SELF_HEALING_SCENARIO.getUniqueId(), "EVERYONE");
		authHelper.addGrantedPermissions(QUERY_SELF_HEALING_SCENARIO.getUniqueId(), "EVERYONE");

		authHelper.addGrantedPermissions(NEW_ENVIRONMENT.getUniqueId(), "EVERYONE");
		authHelper.addGrantedPermissions(EDIT_ENVIRONMENT.getUniqueId(), "EVERYONE");
		authHelper.addGrantedPermissions(QUERY_ENVIRONMENT.getUniqueId(), "EVERYONE");

		authHelper.addGrantedPermissions(NEW_ARTIFACT.getUniqueId(), "EVERYONE");
		authHelper.addGrantedPermissions(EDIT_ARTIFACT.getUniqueId(), "EVERYONE");
		authHelper.addGrantedPermissions(QUERY_ARTIFACT.getUniqueId(), "EVERYONE");
	}
}