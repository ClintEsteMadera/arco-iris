package ar.uba.dc.thesis.rainbow.constraint.instance;

import org.apache.commons.lang.StringUtils;
import org.sa.rainbow.scenario.model.RainbowModelWithScenarios;

import ar.uba.dc.thesis.atam.Artifact;
import ar.uba.dc.thesis.atam.Environment.HeuristicType;
import ar.uba.dc.thesis.rainbow.constraint.operator.NumericBinaryOperator;

public class NumericBinaryRelationalConstraint extends BaseSinglePropertyInvolvedConstraint {

	private static final String SPACE = " ";

	private final NumericBinaryOperator binaryOperator;

	private final long value;

	private final String avgPropertyName;

	public NumericBinaryRelationalConstraint(Artifact artifact, String property, NumericBinaryOperator binaryOperator,
			long value) {
		super(artifact, property);
		this.binaryOperator = binaryOperator;
		this.value = value;
		this.avgPropertyName = "[EAvg]" + artifact.getName() + "." + property;
		this.validate();
	}

	public NumericBinaryOperator getBinaryOperator() {
		return binaryOperator;
	}

	public long getValue() {
		return value;
	}

	public boolean holds(RainbowModelWithScenarios rainbowModelWithScenarios, HeuristicType most) {
		Number propertyValue = this.getPropertyValueFrom(rainbowModelWithScenarios);
		return this.getBinaryOperator().performOperation(propertyValue, this.getValue());
	}

	public boolean holds(RainbowModelWithScenarios rainbowModelWithScenarios, double concernDiffAfterStrategy) {
		double propertyValue = this.getPropertyValueFrom(rainbowModelWithScenarios);
		double propertyValueAfterStrategy = propertyValue + concernDiffAfterStrategy;
		return this.getBinaryOperator().performOperation(propertyValueAfterStrategy, this.getValue());
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
		this.getBinaryOperator().validate();
	}

	@Override
	public String toString() {
		return new StringBuffer().append("(").append(this.getFullyQualifiedPropertyName()).append(SPACE).append(
				this.getBinaryOperator()).append(SPACE).append(this.getValue()).append(")").toString();
	}

}