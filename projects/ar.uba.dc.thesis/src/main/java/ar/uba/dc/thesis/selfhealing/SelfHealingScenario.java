package ar.uba.dc.thesis.selfhealing;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import ar.uba.dc.thesis.atam.ArchitecturalDecision;
import ar.uba.dc.thesis.atam.Artifact;
import ar.uba.dc.thesis.atam.AtamScenario;
import ar.uba.dc.thesis.atam.ResponseMeasure;
import ar.uba.dc.thesis.qa.Concern;

public class SelfHealingScenario extends AtamScenario {

	private boolean enabled;

	private int priority;

	private final List<RepairStrategySpecification> strategySpecs = new ArrayList<RepairStrategySpecification>();

	public SelfHealingScenario() {
		super();
	}

	public SelfHealingScenario(Long id, String name, Concern concern, String stimulusSource, String stimulus,
			String environment, Artifact artifact, String response, ResponseMeasure responseMeasure,
			Collection<ArchitecturalDecision> architecturalDecisions, boolean enabled, int priority) {
		super(id, name, concern, stimulusSource, stimulus, environment, artifact, response, responseMeasure,
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
		StringBuilder sb = new StringBuilder();
		super.toString(sb);
		this.append(sb, "enabled", String.valueOf(this.enabled));
		this.append(sb, "priority", String.valueOf(this.priority));

		return sb.toString();
	}
}