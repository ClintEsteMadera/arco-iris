package ar.uba.dc.arcoiris.rainbow.constraint.numerical;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Level;
import org.sa.rainbow.core.Oracle;
import org.sa.rainbow.util.RainbowLogger;
import org.sa.rainbow.util.RainbowLoggerFactory;

import ar.uba.dc.arcoiris.atam.scenario.model.Artifact;
import ar.uba.dc.arcoiris.common.validation.Assert;
import ar.uba.dc.arcoiris.common.validation.ValidationError;
import ar.uba.dc.arcoiris.rainbow.constraint.operator.NumericBinaryOperator;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamOmitField;

@XStreamAlias("numericBinaryRelationalConstraint")
public class NumericBinaryRelationalConstraint extends BaseSinglePropertyInvolvedConstraint {

	private static final String PROPERTY_MAPPING_FORMAT = "[%s]%s.%s";

	private static final long serialVersionUID = 1L;

	private static final String VALIDATION_MSG_QUANTIFIER = "The quantifier cannot be not empty";

	private static final String VALIDATION_MSG_BINARY_OPERATOR = "The binary operator cannot be not empty";

	private static final String VALIDATION_MSG_CONSTANT_TO_COMPARE_PROP_WITH = "The constant to compare the property with, cannot be not empty";

	private static final String SPACE = " ";

	private static final RainbowLogger logger = RainbowLoggerFactory.logger(NumericBinaryRelationalConstraint.class);

	private Quantifier quantifier;

	private NumericBinaryOperator binaryOperator;

	private Number constantToCompareThePropertyWith;

	@XStreamOmitField
	private String exponentialPropertyName;

	public NumericBinaryRelationalConstraint() {
		super();
		this.constantToCompareThePropertyWith = Double.valueOf(0.0);
		this.quantifier = Quantifier.IN_AVERAGE;
	}

	/**
	 * Returns the fully qualified property name prepending the System and Type name of it.
	 */
	public String getPropertyMapping() {
		return String.format(PROPERTY_MAPPING_FORMAT, getQuantifier().getExpPropertyPrefix(), getArtifact().getName(),
				getProperty());
	}

	public NumericBinaryRelationalConstraint(Artifact artifact, String property, NumericBinaryOperator binaryOperator,
			Number constantToCompareThePropertyWith) {
		this(Quantifier.IN_AVERAGE, artifact, property, binaryOperator, constantToCompareThePropertyWith);
	}

	public NumericBinaryRelationalConstraint(Quantifier quantifier, Artifact artifact, String property,
			NumericBinaryOperator binaryOperator, Number constantToCompareThePropertyWith) {
		super(artifact, property);
		this.quantifier = quantifier;
		this.binaryOperator = binaryOperator;
		this.constantToCompareThePropertyWith = constantToCompareThePropertyWith;

		this.validate();
	}

	public Quantifier getQuantifier() {
		return quantifier;
	}

	public void setQuantifier(Quantifier quantifier) {
		this.quantifier = quantifier;
	}

	public Number getConstantToCompareThePropertyWith() {
		return constantToCompareThePropertyWith;
	}

	public void setConstantToCompareThePropertyWith(Number constantToCompareThePropertyWith) {
		this.constantToCompareThePropertyWith = constantToCompareThePropertyWith;
	}

	public NumericBinaryOperator getBinaryOperator() {
		return binaryOperator;
	}

	public void setBinaryOperator(NumericBinaryOperator binaryOperator) {
		this.binaryOperator = binaryOperator;
	}

	public String getExponentialPropertyName() {
		if (this.exponentialPropertyName == null) {
			this.exponentialPropertyName = quantifier.getExpPropertyPrefix() + getArtifact().getName() + "."
					+ getProperty();
		}
		return this.exponentialPropertyName;
	}

	public boolean holds(Number expValue) {
		boolean holds = this.binaryOperator.performOperation(expValue, this.constantToCompareThePropertyWith);
		log(Level.DEBUG, "Holds " + getFullyQualifiedPropertyName() + " for " + this.quantifier.getExpPropertyPrefix()
				+ expValue + "? " + holds + "!!!!");
		return holds;
	}

	@Override
	protected List<ValidationError> collectValidationErrors() {
		List<ValidationError> validationErrors = new ArrayList<ValidationError>();

		String constraintId = this.getDescriptiveIdForReporting();

		Assert.notNull(this.quantifier, constraintId + VALIDATION_MSG_QUANTIFIER, validationErrors);

		Assert.notNull(this.binaryOperator, constraintId + VALIDATION_MSG_BINARY_OPERATOR, validationErrors);

		Assert.notNull(this.constantToCompareThePropertyWith, constraintId
				+ VALIDATION_MSG_CONSTANT_TO_COMPARE_PROP_WITH, validationErrors);

		return validationErrors;
	}

	@Override
	public String toString() {
		return new StringBuffer().append(this.quantifier.toString()).append(SPACE)
				.append(this.getFullyQualifiedPropertyName()).append(SPACE).append(this.binaryOperator).append(SPACE)
				.append(this.constantToCompareThePropertyWith).toString();
	}

	protected void log(Level level, String txt, Throwable... t) {
		Oracle.instance().writeEvaluatorPanel(logger, level, txt, t);
	}
}