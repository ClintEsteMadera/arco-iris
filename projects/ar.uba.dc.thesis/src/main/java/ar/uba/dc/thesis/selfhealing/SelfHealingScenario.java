package ar.uba.dc.thesis.selfhealing;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.acmestudio.acme.model.IAcmeModel;

import ar.uba.dc.thesis.atam.ArchitecturalDecision;
import ar.uba.dc.thesis.atam.Artifact;
import ar.uba.dc.thesis.atam.AtamScenario;
import ar.uba.dc.thesis.atam.Environment;
import ar.uba.dc.thesis.atam.ResponseMeasure;
import ar.uba.dc.thesis.qa.Concern;

public class SelfHealingScenario extends AtamScenario {

	private boolean enabled;

	private int priority;

	private final List<String> repairStrategies = new ArrayList<String>();

	public SelfHealingScenario() {
		super();
	}

	public SelfHealingScenario(Long id, String name, Concern concern, String stimulusSource, String stimulus,
			Set<Environment> environments, Artifact artifact, String response, ResponseMeasure responseMeasure,
			Collection<ArchitecturalDecision> architecturalDecisions, boolean enabled, int priority) {
		super(id, name, concern, stimulusSource, stimulus, environments, artifact, response, responseMeasure,
				architecturalDecisions);
		this.enabled = enabled;
		this.priority = priority;

		this.validate();
	}

	public boolean isEnabled() {
		return this.enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public int getPriority() {
		return this.priority;
	}

	public void setPriority(int priority) {
		this.priority = priority;
	}

	public List<String> getRepairStrategies() {
		return this.repairStrategies;
	}

	public void addRepairStrategy(String repairStrategy) {
		this.repairStrategies.add(repairStrategy);
	}

	/**
	 * Checks whether this scenario is broken or not, based on the current state of the model.
	 * 
	 * @param acmeModel
	 * @return
	 */
	public boolean isBroken(final IAcmeModel acmeModel, final String involvedArtifactName) {
		boolean isBroken = false;
		if (anyEnvironmentApplies(acmeModel)) {
			isBroken = !getResponseMeasure().holds(acmeModel, involvedArtifactName);
		}
		return isBroken;
	}

	public boolean isBroken(final IAcmeModel acmeModel) {
		boolean isBroken = false;
		if (anyEnvironmentApplies(acmeModel)) {
			isBroken = !getResponseMeasure().holds(acmeModel);
		}
		return isBroken;
	}

	private boolean anyEnvironmentApplies(final IAcmeModel acmeModel) {
		boolean anyEnvironmentApplies = false;

		for (Environment environment : this.getEnvironments()) {
			anyEnvironmentApplies = anyEnvironmentApplies || environment.holds(acmeModel);
		}
		return anyEnvironmentApplies;
	}

	// /**
	// * Versión usando type checkers
	// *
	// * @param acmeModel
	// * @return
	// */
	// public boolean isBroken(IAcmeModel acmeModel) {
	// SimpleModelTypeChecker typeChecker = new SimpleModelTypeChecker();
	// typeChecker.registerModel(acmeModel);
	// AcmeDesignRule rule = getResponseMeasure().getConstraint().getAcmeDesignRule();
	// TypeCheckingState typeCheckingState = typeChecker.evaluateExpressionInContext(null, rule
	// .getDesignRuleExpression(), new Stack<AcmeError>());
	//
	// return !typeCheckingState.typechecks();
	// }

	public boolean applyFor(Environment environment) {
		return getEnvironments().contains(environment);
	}
}