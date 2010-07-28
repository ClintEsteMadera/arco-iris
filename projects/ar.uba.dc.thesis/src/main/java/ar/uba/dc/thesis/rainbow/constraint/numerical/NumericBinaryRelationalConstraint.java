package ar.uba.dc.thesis.rainbow.constraint.numerical;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Level;
import org.sa.rainbow.core.Oracle;
import org.sa.rainbow.util.RainbowLogger;
import org.sa.rainbow.util.RainbowLoggerFactory;

import ar.uba.dc.thesis.atam.scenario.model.Artifact;
import ar.uba.dc.thesis.rainbow.constraint.Quantifier;
import ar.uba.dc.thesis.rainbow.constraint.operator.NumericBinaryOperator;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("numericBinaryRelationalConstraint")
public class NumericBinaryRelationalConstraint extends BaseSinglePropertyInvolvedConstraint {

	private static final long serialVersionUID = 1L;

	private static final String SPACE = " ";

	private static final RainbowLogger logger = RainbowLoggerFactory.logger(NumericBinaryRelationalConstraint.class);

	private final NumericBinaryOperator binaryOperator;

	private final Number constantToCompareThePropertyWith;

	public NumericBinaryRelationalConstraint(Quantifier quantifier, Artifact artifact, String property,
			NumericBinaryOperator binaryOperator, Number constantToCompareThePropertyWith) {
		super(quantifier, artifact, property);
		this.binaryOperator = binaryOperator;
		this.constantToCompareThePropertyWith = constantToCompareThePropertyWith;
		this.validate();
	}

	public Number getConstantToCompareThePropertyWith() {
		return constantToCompareThePropertyWith;
	}

	public boolean holds(Number eavg) {
		boolean holds = this.binaryOperator.performOperation(eavg, this.constantToCompareThePropertyWith);
		log(Level.DEBUG, "Holds " + getFullyQualifiedPropertyName() + " for EAvg " + eavg + "? " + holds + "!!!!");
		return holds;
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

	protected void log(Level level, String txt, Throwable... t) {
		Oracle.instance().writeEvaluatorPanel(logger, level, txt, t);
	}
}