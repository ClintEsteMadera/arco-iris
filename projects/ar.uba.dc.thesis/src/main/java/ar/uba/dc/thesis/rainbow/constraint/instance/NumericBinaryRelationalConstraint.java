package ar.uba.dc.thesis.rainbow.constraint.instance;

import org.acmestudio.acme.model.IAcmeModel;
import org.acmestudio.basicmodel.core.AcmeFloatValue;
import org.acmestudio.basicmodel.core.AcmeIntValue;
import org.acmestudio.basicmodel.element.property.AcmeProperty;
import org.apache.commons.lang.StringUtils;

import ar.uba.dc.thesis.common.ThesisPojo;
import ar.uba.dc.thesis.rainbow.constraint.SinglePropertyInvolvedConstraint;
import ar.uba.dc.thesis.rainbow.constraint.operator.NumericBinaryOperator;

/**
 * TODO: We can contemplate an abstraction for the operands so that we do not force an specific order (i.e. left side =
 * property, right side=literal value)
 */
public class NumericBinaryRelationalConstraint extends ThesisPojo implements SinglePropertyInvolvedConstraint {

	private static final String SPACE = " ";

	private final String property;

	private final NumericBinaryOperator binaryOperator;

	private final long value;

	public NumericBinaryRelationalConstraint(String property, NumericBinaryOperator binaryOperator, long value) {
		super();
		this.property = property;
		this.binaryOperator = binaryOperator;
		this.value = value;

		this.validate();
	}

	public String getProperty() {
		return property;
	}

	public NumericBinaryOperator getBinaryOperator() {
		return binaryOperator;
	}

	public long getValue() {
		return value;
	}

	public boolean holds(IAcmeModel acmeModel) {
		Number propertyValue = this.getPropertyValueFrom(acmeModel, this.getProperty());

		return this.getBinaryOperator().performOperation(propertyValue, this.getValue());
	}

	/**
	 * TODO: Improve the reliability of this method!
	 * 
	 * @param acmeModel
	 * @param propertyName
	 * @return
	 */
	private Number getPropertyValueFrom(IAcmeModel acmeModel, String propertyName) {
		AcmeProperty property = (AcmeProperty) acmeModel.findNamedObject(acmeModel, propertyName);
		if (property.getValue() instanceof AcmeIntValue) {
			AcmeIntValue propertyValue = (AcmeIntValue) property.getValue();
			return propertyValue.getValue();
		} else if (property.getValue() instanceof AcmeFloatValue) {
			AcmeFloatValue propertyValue = (AcmeFloatValue) property.getValue();
			return propertyValue.getValue();
		} else {
			throw new RuntimeException("Cannot determine the type of value corresponding to the '" + propertyName
					+ "' property");
		}
	}

	public void validate() {
		if (StringUtils.isBlank(this.getProperty())) {
			throw new IllegalArgumentException("The property involved in the comparison cannot be blank");
		}
		this.getBinaryOperator().validate();
	}

	@Override
	public String toString() {
		return new StringBuffer().append("(").append(this.getProperty()).append(SPACE).append(this.getBinaryOperator())
				.append(SPACE).append(this.getValue()).append(")").toString();
	}
}
