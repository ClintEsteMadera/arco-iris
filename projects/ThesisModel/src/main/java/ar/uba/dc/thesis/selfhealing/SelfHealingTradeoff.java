package ar.uba.dc.thesis.selfhealing;

import java.util.Collection;

public class SelfHealingTradeoff {

	private Collection<SelfHealingScenario> affects;

	private Collection<SelfHealingScenario> breaks;

	public SelfHealingTradeoff(Collection<SelfHealingScenario> affects,
			Collection<SelfHealingScenario> breaks) {
		super();
		this.affects = affects;
		this.breaks = breaks;
	}

	public Collection<SelfHealingScenario> getAffects() {
		return this.affects;
	}

	public Collection<SelfHealingScenario> getBreaks() {
		return this.breaks;
	}
}
