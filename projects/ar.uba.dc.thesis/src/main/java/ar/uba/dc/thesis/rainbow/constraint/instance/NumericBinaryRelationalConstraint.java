package ar.uba.dc.thesis.rainbow.constraint.instance;

import org.acmestudio.acme.model.IAcmeModel;
import org.acmestudio.basicmodel.core.AcmeFloatValue;
import org.acmestudio.basicmodel.core.AcmeIntValue;
import org.acmestudio.basicmodel.core.AcmePropertyValue;
import org.acmestudio.basicmodel.element.property.AcmeProperty;
import org.apache.commons.lang.StringUtils;

import ar.uba.dc.thesis.atam.Artifact;
import ar.uba.dc.thesis.rainbow.constraint.operator.NumericBinaryOperator;

public class NumericBinaryRelationalConstraint extends BaseSinglePropertyInvolvedConstraint {

	private static final String SPACE = " ";

	private final NumericBinaryOperator binaryOperator;

	private final long value;

	public NumericBinaryRelationalConstraint(Artifact artifact, String property, NumericBinaryOperator binaryOperator,
			long value) {
		super(artifact, property);
		this.binaryOperator = binaryOperator;
		this.value = value;

		this.validate();
	}

	public NumericBinaryOperator getBinaryOperator() {
		return binaryOperator;
	}

	public long getValue() {
		return value;
	}

	public boolean holds(IAcmeModel acmeModel) {
		Number propertyValue = this.getPropertyValueFrom(acmeModel, this.getFullyQualifiedPropertyName());

		return this.getBinaryOperator().performOperation(propertyValue, this.getValue());
	}

	private Number getPropertyValueFrom(IAcmeModel acmeModel, String propertyName) {
		AcmeProperty property = this.findAcmePropertyInAcme(acmeModel);

		AcmePropertyValue propertyValue = property.getValue();

		if (propertyValue instanceof AcmeIntValue) {
			return ((AcmeIntValue) propertyValue).getValue();
		} else if (propertyValue instanceof AcmeFloatValue) {
			return ((AcmeFloatValue) propertyValue).getValue();
		} else {
			throw new RuntimeException("The type " + propertyValue.getClass().getName() + "in not a valid numeric type");
		}
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
