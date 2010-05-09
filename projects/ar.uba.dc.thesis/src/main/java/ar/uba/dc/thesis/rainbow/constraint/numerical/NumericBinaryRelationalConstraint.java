package ar.uba.dc.thesis.rainbow.constraint.numerical;

import org.apache.commons.lang.StringUtils;
import org.sa.rainbow.scenario.model.RainbowModelWithScenarios;

import ar.uba.dc.thesis.atam.Artifact;
import ar.uba.dc.thesis.rainbow.constraint.operator.NumericBinaryOperator;

public class NumericBinaryRelationalConstraint extends BaseSinglePropertyInvolvedConstraint {

	private static final String SPACE = " ";

	private final NumericBinaryOperator binaryOperator;

	private final Number constantToCompareThePropertyWith;

	private final String avgPropertyName;

	public NumericBinaryRelationalConstraint(Artifact artifact, String property, NumericBinaryOperator binaryOperator,
			Number constantToCompareThePropertyWith) {
		super(artifact, property);
		this.binaryOperator = binaryOperator;
		this.constantToCompareThePropertyWith = constantToCompareThePropertyWith;
		this.avgPropertyName = "[EAvg]" + artifact.getName() + "." + property;
		this.validate();
	}

	/**
	 * In this implementation, we always use the average of the historical values (this is managed by the Rainbow
	 * Model). Thus, the param <code>heuristic</code> is unused.
	 * 
	 * @param rainbowModelWithScenarios
	 *            the Rainbow Model to take the value of the underlying property.
	 * @param heuristic
	 *            <b>this parameter is not used. we always use the average but we do not invoke the heuristic to weight
	 *            the answer: Rainbow already does this.</b>
	 * 
	 * @see RainbowModelWithScenarios#getProperty(String) when called with a property with prefix equals to "[EAvg]"
	 */
	public boolean holds(RainbowModelWithScenarios rainbowModelWithScenarios) {
		Number propertyValue = this.getPropertyValueFrom(rainbowModelWithScenarios);

		return this.holds(propertyValue);
	}

	public boolean holds(RainbowModelWithScenarios rainbowModelWithScenarios, double concernDiffAfterStrategy) {
		double propertyValue = this.getPropertyValueFrom(rainbowModelWithScenarios);
		double propertyValueAfterStrategy = propertyValue + concernDiffAfterStrategy;

		return this.holds(propertyValueAfterStrategy);
	}

	public boolean holds(Number propertyValue) {
		return this.binaryOperator.performOperation(propertyValue, this.constantToCompareThePropertyWith);
	}

	private double getPropertyValueFrom(RainbowModelWithScenarios rainbowModelWithScenarios) {
		return (Double) this.findAcmePropertyInAcme(rainbowModelWithScenarios, this.avgPropertyName);
	}

	@Override
	public void validate() {
		super.validate();

		if (StringUtils.isBlank(this.getFullyQualifiedPropertyName())) {
			throw new IllegalArgumentException("The property involved in the comparison cannot be blank");
		}
		this.binaryOperator.validate();
	}

	@Override
	public String toString() {
		return new StringBuffer().append("(").append(this.getFullyQualifiedPropertyName()).append(SPACE).append(
				this.binaryOperator).append(SPACE).append(this.constantToCompareThePropertyWith).append(")").toString();
	}
}