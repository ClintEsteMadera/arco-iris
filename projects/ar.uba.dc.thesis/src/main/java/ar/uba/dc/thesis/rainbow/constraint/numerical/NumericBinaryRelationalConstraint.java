package ar.uba.dc.thesis.rainbow.constraint.numerical;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Level;
import org.sa.rainbow.core.Oracle;
import org.sa.rainbow.model.RainbowModel;
import org.sa.rainbow.scenario.model.RainbowModelWithScenarios;
import org.sa.rainbow.util.RainbowLogger;
import org.sa.rainbow.util.RainbowLoggerFactory;

import ar.uba.dc.thesis.atam.scenario.model.Artifact;
import ar.uba.dc.thesis.rainbow.constraint.operator.NumericBinaryOperator;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamOmitField;

@XStreamAlias("numericBinaryRelationalConstraint")
public class NumericBinaryRelationalConstraint extends BaseSinglePropertyInvolvedConstraint {

	private static final long serialVersionUID = 1L;

	private static final String SPACE = " ";

	private static final RainbowLogger logger = RainbowLoggerFactory.logger(NumericBinaryRelationalConstraint.class);

	private NumericBinaryOperator binaryOperator;

	private Number constantToCompareThePropertyWith;

	@XStreamOmitField
	private String eAvgPropertyName;

	@XStreamAsAttribute
	private boolean sum;

	public NumericBinaryRelationalConstraint() {
		super();
		this.restoreToDefaultValues();
	}

	public NumericBinaryRelationalConstraint(Artifact artifact, String property, NumericBinaryOperator binaryOperator,
			Number constantToCompareThePropertyWith) {
		this(artifact, property, binaryOperator, constantToCompareThePropertyWith, false);
	}

	public NumericBinaryRelationalConstraint(Artifact artifact, String property, NumericBinaryOperator binaryOperator,
			Number constantToCompareThePropertyWith, boolean sum) {
		super(artifact, property);
		this.binaryOperator = binaryOperator;
		this.constantToCompareThePropertyWith = constantToCompareThePropertyWith;
		this.sum = sum;
		this.validate();
	}

	@Override
	public void restoreToDefaultValues() {
		this.constantToCompareThePropertyWith = Double.valueOf(0.0);
	}

	public boolean isSum() {
		return sum;
	}

	public void setSum(boolean sum) {
		this.sum = sum;
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

	public boolean holds(Number eavg) {
		boolean holds = this.binaryOperator.performOperation(eavg, this.constantToCompareThePropertyWith);
		log(Level.DEBUG, "Holds " + getFullyQualifiedPropertyName() + " for EAvg " + eavg + "? " + holds + "!!!!");
		return holds;
	}

	public String getEAvgPropertyName() {
		if (this.eAvgPropertyName == null) {
			String expPropPrefix = sum ? RainbowModelWithScenarios.EXP_SUM_KEY : RainbowModel.EXP_AVG_KEY;
			this.eAvgPropertyName = expPropPrefix + getArtifact().getName() + "." + getProperty();
		}
		return eAvgPropertyName;
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
		return new StringBuffer().append(this.getFullyQualifiedPropertyName()).append(SPACE)
				.append(this.binaryOperator).append(SPACE).append(this.constantToCompareThePropertyWith).toString();
	}

	protected void log(Level level, String txt, Throwable... t) {
		Oracle.instance().writeEvaluatorPanel(logger, level, txt, t);
	}
}