package ar.uba.dc.thesis.selfhealing;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.Stack;

import org.acmestudio.acme.core.exception.AcmeException;
import org.acmestudio.acme.environment.error.AcmeError;
import org.acmestudio.acme.model.IAcmeModel;
import org.acmestudio.acme.type.verification.NodeScopeLookup;
import org.acmestudio.acme.type.verification.RuleTypeChecker;
import org.acmestudio.basicmodel.element.AcmeDesignRule;

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

	public boolean isBroken(IAcmeModel acmeModel) {
		AcmeDesignRule rule = getResponseMeasure().getConstraint().getAcmeDesignRule();
		try {
			return RuleTypeChecker.evaluateAsBoolean(rule, rule, rule.getDesignRuleExpression(),
					new Stack<AcmeError>(), new NodeScopeLookup());
		} catch (AcmeException ae) {
			ae.printStackTrace();
			throw new RuntimeException("Error while checking for scenario state");
		}
	}

	public boolean applyFor(Environment environment) {
		return getEnvironments().contains(environment);
	}
}