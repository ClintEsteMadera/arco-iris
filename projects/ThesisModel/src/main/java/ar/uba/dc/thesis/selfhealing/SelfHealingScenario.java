package ar.uba.dc.thesis.selfhealing;

import java.util.Collection;
import java.util.TreeMap;

import ar.uba.dc.thesis.acme.Operation;
import ar.uba.dc.thesis.atam.ArchitecturalDecision;
import ar.uba.dc.thesis.atam.Artifact;
import ar.uba.dc.thesis.atam.AtamScenario;
import ar.uba.dc.thesis.atam.ResponseMeasure;
import ar.uba.dc.thesis.atam.Tradeoff;
import ar.uba.dc.thesis.qa.Concern;

public class SelfHealingScenario extends AtamScenario {

	private boolean enabled;

	private int priority;

	private TreeMap<RepairStrategy, Tradeoff> strategyTradeoffMap = new TreeMap<RepairStrategy, Tradeoff>();

	public SelfHealingScenario(String name, Concern concern, String stimulusSource,
			Operation stimulus, String environment, Collection<Artifact> artifacts,
			String response, ResponseMeasure responseMeasure,
			Collection<ArchitecturalDecision> architecturalDecisions, boolean enabled, int priority) {
		super(name, concern, stimulusSource, stimulus, environment, artifacts, response,
				responseMeasure, architecturalDecisions);
		this.enabled = enabled;
		this.priority = priority;

	}

	public boolean isEnabled() {
		return this.enabled;
	}

	public int getPriority() {
		return this.priority;
	}

	public TreeMap<RepairStrategy, Tradeoff> getRepairStrategies() {
		return this.strategyTradeoffMap;
	}

	public void addRepairStrategy(RepairStrategy repairStrategy, Tradeoff tradeoff) {
		if (repairStrategy == null || tradeoff == null) {
			throw new RuntimeException("Either the repair strategy or the tradeoff is null");
		}
		this.strategyTradeoffMap.put(repairStrategy, tradeoff);
	}
}