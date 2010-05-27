package ar.uba.dc.thesis.rainbow.constraint;

import org.sa.rainbow.scenario.model.RainbowModelWithScenarios;

import ar.uba.dc.thesis.common.Validatable;

public interface Constraint extends Validatable {

	boolean holds(Number value);

	boolean holds(RainbowModelWithScenarios rainbowModelWithScenarios);

	boolean holds(RainbowModelWithScenarios rainbowModelWithScenarios, double concernDiffAfterStrategy);

	String getFullyQualifiedPropertyName();

	boolean holds4AllInstances(RainbowModelWithScenarios rainbowModelWithScenarios);
}
