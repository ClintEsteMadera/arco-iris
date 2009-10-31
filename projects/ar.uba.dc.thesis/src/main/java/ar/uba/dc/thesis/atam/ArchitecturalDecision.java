package ar.uba.dc.thesis.atam;

import java.util.Collection;

public class ArchitecturalDecision {

	private String description;

	private Collection<String> sensitivityPoints;

	private Collection<String> tradeoffs;

	private Collection<String> risks;

	private Collection<String> nonRisks;

	public ArchitecturalDecision(String description,
			Collection<String> sensitivityPoints, Collection<String> tradeoffs,
			Collection<String> risks, Collection<String> nonRisks) {
		super();
		this.description = description;
		this.sensitivityPoints = sensitivityPoints;
		this.tradeoffs = tradeoffs;
		this.risks = risks;
		this.nonRisks = nonRisks;
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
}