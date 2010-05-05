package ar.uba.dc.thesis.rainbow.constraint;

import org.sa.rainbow.scenario.model.RainbowModelWithScenarios;

/**
 * Extends the super interface in order to express the need to have a getter for the property involved in the Constraint
 * 
 */
public interface SinglePropertyInvolvedConstraint extends Constraint {

	String getFullyQualifiedPropertyName();

	boolean holds(RainbowModelWithScenarios rainbowModelWithScenarios, double concernDiffAfterStrategy);

}
