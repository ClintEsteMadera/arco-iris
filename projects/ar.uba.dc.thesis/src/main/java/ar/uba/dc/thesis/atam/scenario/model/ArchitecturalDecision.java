package ar.uba.dc.thesis.atam.scenario.model;

import java.util.Set;

import ar.uba.dc.thesis.common.ThesisPojo;

import com.thoughtworks.xstream.annotations.XStreamAlias;

//TODO Decidir si tiene sentido incluír esto o no
@XStreamAlias("architecturalDecision")
public class ArchitecturalDecision extends ThesisPojo {

	private static final long serialVersionUID = 1L;

	private final String description;

	@XStreamAlias("sensitivityPoints")
	private final Set<String> sensitivityPoints;

	@XStreamAlias("tradeoffs")
	private final Set<String> tradeoffs;

	@XStreamAlias("risk")
	private final Set<String> risks;

	@XStreamAlias("nonRisk")
	private final Set<String> nonRisks;

	public ArchitecturalDecision(String description, Set<String> sensitivityPoints, Set<String> tradeoffs,
			Set<String> risks, Set<String> nonRisks) {
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

	public Set<String> getSensitivityPoints() {
		return this.sensitivityPoints;
	}

	public Set<String> getTradeoffs() {
		return this.tradeoffs;
	}

	public Set<String> getRisks() {
		return this.risks;
	}

	public Set<String> getNonRisks() {
		return this.nonRisks;
	}

	@Override
	public void validate() {
		// Do nothing
	}
}