package ar.uba.dc.thesis.rainbow.constraint.numerical;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.sa.rainbow.core.Oracle;
import org.sa.rainbow.model.RainbowModel;
import org.sa.rainbow.scenario.model.RainbowModelWithScenarios;
import org.sa.rainbow.util.RainbowLogger;
import org.sa.rainbow.util.RainbowLoggerFactory;

import ar.uba.dc.thesis.atam.scenario.model.Artifact;
import ar.uba.dc.thesis.rainbow.constraint.Quantifier;
import ar.uba.dc.thesis.rainbow.constraint.operator.NumericBinaryOperator;

public class NumericBinaryRelationalConstraint extends BaseSinglePropertyInvolvedConstraint {

	private static final String SPACE = " ";

	private final RainbowLogger logger = RainbowLoggerFactory.logger(NumericBinaryRelationalConstraint.class);

	private final NumericBinaryOperator binaryOperator;

	private final Number constantToCompareThePropertyWith;

	private final String eAvgPropertyName;

	public NumericBinaryRelationalConstraint(Quantifier quantifier, Artifact artifact, String property,
			NumericBinaryOperator binaryOperator, Number constantToCompareThePropertyWith) {
		super(quantifier, artifact, property);
		this.binaryOperator = binaryOperator;
		this.constantToCompareThePropertyWith = constantToCompareThePropertyWith;
		this.eAvgPropertyName = RainbowModel.EXP_AVG_KEY + artifact.getName() + "." + property;

		this.validate();
	}

	/**
	 * In this implementation, we always use the exponential average of the historical values (this is managed by the
	 * Rainbow Model).
	 * 
	 * @param rainbowModelWithScenarios
	 *            the Rainbow Model to take the value of the underlying property.
	 * 
	 * @see RainbowModelWithScenarios#getProperty(String) when called with a property with prefix equals to "[EAvg]"
	 */
	public boolean holds4Scoring(RainbowModelWithScenarios rainbowModelWithScenarios) {
		return this.holds4Scoring(rainbowModelWithScenarios, 0);
	}

	public boolean holds4Scoring(RainbowModelWithScenarios rainbowModelWithScenarios, double concernDiffAfterStrategy) {
		double propertyValue = this.getPropertyValueFrom(rainbowModelWithScenarios);
		double propertyValueAfterStrategy = propertyValue + concernDiffAfterStrategy;

		Oracle.instance().writeEvaluatorPanel(logger, this.eAvgPropertyName + ": " + propertyValueAfterStrategy);

		return this.holds(propertyValueAfterStrategy);
	}

	public boolean holdsConsideringAllInstances(RainbowModelWithScenarios rainbowModelWithScenarios) {
		List<Number> values = rainbowModelWithScenarios.getAllInstancesPropertyValues(getArtifact().getSystemName(),
				getArtifact().getName(), this.getProperty());

		return this.getQuantifier().holds(this, values);
	}

	public boolean holds(Number propertyValue) {
		return this.binaryOperator.performOperation(propertyValue, this.constantToCompareThePropertyWith);
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

	private double getPropertyValueFrom(RainbowModelWithScenarios rainbowModelWithScenarios) {
		Double propertyValue = (Double) rainbowModelWithScenarios.getProperty(this.eAvgPropertyName);

		return propertyValue != null ? propertyValue : NumberUtils.DOUBLE_ZERO;
	}
}