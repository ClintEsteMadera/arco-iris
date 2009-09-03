package ar.uba.dc.thesis.selfhealing;

import java.util.Collection;
import java.util.Map;
import java.util.TreeMap;

import ar.uba.dc.thesis.acme.Operation;
import ar.uba.dc.thesis.atam.ArchitecturalDecision;
import ar.uba.dc.thesis.atam.Artifact;
import ar.uba.dc.thesis.atam.AtamScenario;
import ar.uba.dc.thesis.atam.ResponseMeasure;
import ar.uba.dc.thesis.qa.Concern;

public class SelfHealingScenario extends AtamScenario {

	private boolean enabled;

	private int priority;

	private Map<RepairStrategy, SelfHealingTradeoff> strategyTradeoffMap = new TreeMap<RepairStrategy, SelfHealingTradeoff>();

	public SelfHealingScenario(String name, Concern concern, String stimulusSource,
			Operation stimulus, String environment, Collection<Artifact> components,
			String response, ResponseMeasure responseMeasure,
			Collection<ArchitecturalDecision> architecturalDecisions, boolean enabled, int priority) {
		super(name, concern, stimulusSource, stimulus, environment, components, response,
				responseMeasure, architecturalDecisions);
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

	public Map<RepairStrategy, SelfHealingTradeoff> getRepairStrategies() {
		return this.strategyTradeoffMap;
	}

	public void addRepairStrategy(RepairStrategy repairStrategy, SelfHealingTradeoff selfHealingTradeoff) {
		if (repairStrategy == null || selfHealingTradeoff == null) {
			throw new RuntimeException("Neither the repair strategy or the tradeoff can be null");
		}
		this.strategyTradeoffMap.put(repairStrategy, selfHealingTradeoff);
	}

	@Override
	public String toString(){
		return super.toString()
        + this.toString("enabled", String.valueOf(this.enabled))
        + this.toString("priority", String.valueOf(this.priority))
        ;
	}
}