package ar.uba.dc.thesis.rainbow.constraint.instance;

import org.acmestudio.acme.model.IAcmeModel;
import org.acmestudio.basicmodel.core.AcmeFloatValue;
import org.acmestudio.basicmodel.core.AcmeIntValue;
import org.acmestudio.basicmodel.core.AcmePropertyValue;
import org.acmestudio.basicmodel.element.property.AcmeProperty;
import org.apache.commons.lang.StringUtils;

import ar.uba.dc.thesis.common.ThesisPojo;
import ar.uba.dc.thesis.rainbow.constraint.SinglePropertyInvolvedConstraint;
import ar.uba.dc.thesis.rainbow.constraint.operator.NumericBinaryOperator;

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

	private Number getPropertyValueFrom(IAcmeModel acmeModel, String propertyName) {
		AcmeProperty property = (AcmeProperty) acmeModel.findNamedObject(acmeModel, propertyName);
		if (property == null) {
			throw new RuntimeException("Could not find in the model the property '" + propertyName + "'");
		} else {
			AcmePropertyValue propertyValue = property.getValue();

			if (propertyValue instanceof AcmeIntValue) {
				return ((AcmeIntValue) propertyValue).getValue();
			} else if (propertyValue instanceof AcmeFloatValue) {
				return ((AcmeFloatValue) propertyValue).getValue();
			} else {
				throw new RuntimeException("The type " + propertyValue.getClass().getName()
						+ "in not a valid numeric type");
			}
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
