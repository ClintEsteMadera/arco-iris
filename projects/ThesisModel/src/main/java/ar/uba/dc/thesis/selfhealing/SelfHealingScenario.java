package ar.uba.dc.thesis.selfhealing;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import ar.uba.dc.thesis.acme.Operation;
import ar.uba.dc.thesis.atam.ArchitecturalDecision;
import ar.uba.dc.thesis.atam.Artifact;
import ar.uba.dc.thesis.atam.AtamScenario;
import ar.uba.dc.thesis.atam.ResponseMeasure;
import ar.uba.dc.thesis.qa.Concern;

public class SelfHealingScenario extends AtamScenario {

	private boolean enabled;

	private int priority;

	private final List<RepairStrategySpecification> strategySpecs = new ArrayList<RepairStrategySpecification>();

	public SelfHealingScenario(String name, Concern concern, String stimulusSource, Operation stimulus,
			String environment, Collection<Artifact> components, String response, ResponseMeasure responseMeasure,
			Collection<ArchitecturalDecision> architecturalDecisions, boolean enabled, int priority) {
		super(name, concern, stimulusSource, stimulus, environment, components, response, responseMeasure,
				architecturalDecisions);
		this.enabled = enabled;
		this.priority = priority;
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

	public List<RepairStrategySpecification> getRepairStrategySpecs() {
		return this.strategySpecs;
	}

	public void addRepairStrategySpec(RepairStrategySpecification spec) {
		this.strategySpecs.add(spec);
	}

	@Override
	public String toString() {
		return super.toString() + this.toString("enabled", String.valueOf(this.enabled))
				+ this.toString("priority", String.valueOf(this.priority));
	}
}