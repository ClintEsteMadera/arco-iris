package ar.uba.dc.thesis.rainbow.constraint;

import org.sa.rainbow.scenario.model.RainbowModelWithScenarios;

import ar.uba.dc.thesis.common.Validatable;

public interface Constraint extends Validatable {

	boolean holds(Number value);

	boolean holds4Scoring(RainbowModelWithScenarios rainbowModelWithScenarios);

	boolean holds4Scoring(RainbowModelWithScenarios rainbowModelWithScenarios, double concernDiffAfterStrategy);

	boolean holdsConsideringAllInstances(RainbowModelWithScenarios rainbowModelWithScenarios);

	String getFullyQualifiedPropertyName();

}
