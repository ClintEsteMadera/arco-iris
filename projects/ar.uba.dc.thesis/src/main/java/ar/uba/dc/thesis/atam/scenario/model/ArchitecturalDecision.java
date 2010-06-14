package ar.uba.dc.thesis.atam.scenario.model;

import java.util.Collection;

import ar.uba.dc.thesis.common.ThesisPojo;

public class ArchitecturalDecision extends ThesisPojo {

	private final String description;

	private final Collection<String> sensitivityPoints;

	private final Collection<String> tradeoffs;

	private final Collection<String> risks;

	private final Collection<String> nonRisks;

	public ArchitecturalDecision(String description, Collection<String> sensitivityPoints,
			Collection<String> tradeoffs, Collection<String> risks, Collection<String> nonRisks) {
		super();
		this.description = description;
		this.sensitivityPoints = sensitivityPoints;
		this.tradeoffs = tradeoffs;
		this.risks = risks;
		this.nonRisks = nonRisks;

		this.validate();
	}

	public String getDescription() {
		return this.description;
	}

	public Collection<String> getSensitivityPoints() {
		return this.sensitivityPoints;
	}

	public Collection<String> getTradeoffs() {
		return this.tradeoffs;
	}

	public Collection<String> getRisks() {
		return this.risks;
	}

	public Collection<String> getNonRisks() {
		return this.nonRisks;
	}

	public void validate() {
		// Do nothing
	}
}