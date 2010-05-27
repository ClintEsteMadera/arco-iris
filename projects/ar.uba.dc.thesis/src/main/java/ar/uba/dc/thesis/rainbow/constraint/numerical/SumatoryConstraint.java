package ar.uba.dc.thesis.rainbow.constraint.numerical;

import java.util.Set;

import org.acmestudio.acme.element.IAcmeElementInstance;
import org.sa.rainbow.scenario.model.RainbowModelWithScenarios;
import org.sa.rainbow.stitch.lib.Model;

import ar.uba.dc.thesis.atam.Artifact;
import ar.uba.dc.thesis.rainbow.constraint.Constraint;

public class SumatoryConstraint extends BaseSinglePropertyInvolvedConstraint {

	private final Constraint constraint;

	public SumatoryConstraint(Artifact artifact, String property, Constraint constraint) {
		super(artifact, property);
		this.constraint = constraint;

		validate();
	}

	@Override
	public final String getFullyQualifiedPropertyName() {
		return this.constraint.getFullyQualifiedPropertyName();
	}

	public boolean holds(RainbowModelWithScenarios rainbowModelWithScenarios) {
		String typeName = this.getArtifact().getName();

		Set<IAcmeElementInstance<?, ?>> typeMatchingComponents = getTypeMatchingComponents(typeName,
				rainbowModelWithScenarios);

		return this.constraint.holds(Model.sumOverProperty(this.getProperty(), typeMatchingComponents));
	}

	public boolean holds(RainbowModelWithScenarios rainbowModelWithScenarios, double concernDiffAfterStrategy) {
		String typeName = this.getArtifact().getName();
		Set<IAcmeElementInstance<?, ?>> typeMatchingComponents = this.getTypeMatchingComponents(typeName,
				rainbowModelWithScenarios);

		double concernDiffSumatory = typeMatchingComponents.size() * concernDiffAfterStrategy;
		return this.constraint.holds(Model.sumOverProperty(this.getProperty(), typeMatchingComponents)
				+ concernDiffSumatory);
	}

	public boolean holds(Number value) {
		throw new RuntimeException("Method not yet implemented!!");
	}

	@Override
	public void validate() {
		if (this.getArtifact().getName() == null) {
			throw new RuntimeException("The type name for the constraint cannot be null");
		}

		if (this.constraint == null) {
			throw new RuntimeException("The constraint to apply cannot be null");
		}
	}

	public boolean holds4AllInstances(RainbowModelWithScenarios rainbowModelWithScenarios) {
		throw new RuntimeException("Implementar!!!!!!!!!!!!");
	}
}
