package ar.uba.dc.arcoirisui.gui.action;

import ar.uba.dc.arcoiris.atam.scenario.model.Artifact;
import ar.uba.dc.arcoiris.atam.scenario.model.Environment;
import ar.uba.dc.arcoiris.selfhealing.SelfHealingScenario;
import ar.uba.dc.arcoirisui.gui.widget.composite.query.ArtifactQueryComposite;
import ar.uba.dc.arcoirisui.gui.widget.composite.query.EnvironmentQueryComposite;
import ar.uba.dc.arcoirisui.gui.widget.composite.query.SelfHealingScenarioQueryComposite;
import ar.uba.dc.arcoirisui.properties.ArcoIrisUILabels;
import ar.uba.dc.arcoirisui.properties.UniqueTableIdentifier;

import commons.auth.AuthorizationManager;
import commons.gui.action.OpenDialogWithPurposeAction;
import commons.gui.util.purpose.BasePurpose;
import commons.gui.util.purpose.Purpose;

/**
 * Holds the GUI actions related to "Arco Iris UI" and specifies the required grants to execute those actions.
 */
public final class ArcoIrisUIActions {

	/**
	 * This class is not intended to be instantiated.
	 */
	private ArcoIrisUIActions() {
		super();
	}

	private static final String NO_SHORTCUT = "";

	// Self Healing Configuration

	public static SelfHealingConfigurationCreateAction NEW_SELF_HEALING_CONFIG = new SelfHealingConfigurationCreateAction(
			"arcoIrisUI.scenario.config.new", "CTRL+N");

	public static final SelfHealingConfigurationLoadAction OPEN_SELF_HEALING_CONFIG = new SelfHealingConfigurationLoadAction(
			"arcoIrisUI.scenario.config.open", "CTRL+O");

	public static final SelfHealingConfigurationCloseAction CLOSE_SELF_HEALING_CONFIG = new SelfHealingConfigurationCloseAction(
			"arcoIrisUI.scenario.config.close", "CTRL+W");

	// Self Healing Scenario

	public static final SelfHealingScenarioOpenDialogWithPurposeAction NEW_SELF_HEALING_SCENARIO = new SelfHealingScenarioOpenDialogWithPurposeAction(
			"arcoIrisUI.scenario.new", NO_SHORTCUT, BasePurpose.CREATION);

	public static final SelfHealingScenarioOpenDialogWithPurposeAction EDIT_SELF_HEALING_SCENARIO = new SelfHealingScenarioOpenDialogWithPurposeAction(
			"arcoIrisUI.scenario.edit", NO_SHORTCUT, BasePurpose.EDIT);

	public static final SelfHealingScenarioOpenDialogWithPurposeAction DELETE_SELF_HEALING_SCENARIO = new SelfHealingScenarioOpenDialogWithPurposeAction(
			"arcoIrisUI.scenario.delete", NO_SHORTCUT, BasePurpose.DELETE);

	public static final ArcoIrisUIQueryAction<SelfHealingScenario> QUERY_SELF_HEALING_SCENARIO = new ArcoIrisUIQueryAction<SelfHealingScenario>(
			"arcoIrisUI.scenario.query", NO_SHORTCUT, ArcoIrisUILabels.SCENARIOS, UniqueTableIdentifier.SCENARIOS,
			SelfHealingScenarioQueryComposite.class);

	// Environment

	public static final OpenDialogWithPurposeAction<Environment, Purpose> NEW_ENVIRONMENT = new EnvironmentOpenDialogWithPurposeAction(
			"arcoIrisUI.environment.new", NO_SHORTCUT, BasePurpose.CREATION);

	public static final OpenDialogWithPurposeAction<Environment, Purpose> EDIT_ENVIRONMENT = new EnvironmentOpenDialogWithPurposeAction(
			"arcoIrisUI.environment.edit", NO_SHORTCUT, BasePurpose.EDIT);

	public static final OpenDialogWithPurposeAction<Environment, Purpose> DELETE_ENVIRONMENT = new EnvironmentOpenDialogWithPurposeAction(
			"arcoIrisUI.environment.delete", NO_SHORTCUT, BasePurpose.DELETE);

	public static final ArcoIrisUIQueryAction<Environment> QUERY_ENVIRONMENT = new ArcoIrisUIQueryAction<Environment>(
			"arcoIrisUI.environment.query", NO_SHORTCUT, ArcoIrisUILabels.ENVIRONMENTS,
			UniqueTableIdentifier.ENVIRONMENTS, EnvironmentQueryComposite.class);

	// Artifact

	public static final OpenDialogWithPurposeAction<Artifact, Purpose> NEW_ARTIFACT = new ArtifactOpenDialogWithPurposeAction(
			"arcoIrisUI.artifact.new", NO_SHORTCUT, BasePurpose.CREATION);

	public static final OpenDialogWithPurposeAction<Artifact, Purpose> EDIT_ARTIFACT = new ArtifactOpenDialogWithPurposeAction(
			"arcoIrisUI.artifact.edit", NO_SHORTCUT, BasePurpose.EDIT);

	public static final OpenDialogWithPurposeAction<Artifact, Purpose> DELETE_ARTIFACT = new ArtifactOpenDialogWithPurposeAction(
			"arcoIrisUI.artifact.delete", NO_SHORTCUT, BasePurpose.DELETE);

	public static final ArcoIrisUIQueryAction<Artifact> QUERY_ARTIFACT = new ArcoIrisUIQueryAction<Artifact>(
			"arcoIrisUI.artifact.query", NO_SHORTCUT, ArcoIrisUILabels.ARTIFACTS, UniqueTableIdentifier.ARTIFACTS,
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