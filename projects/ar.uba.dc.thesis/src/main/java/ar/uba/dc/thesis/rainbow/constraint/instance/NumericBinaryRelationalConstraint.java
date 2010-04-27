package ar.uba.dc.thesis.rainbow.constraint.instance;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.acmestudio.acme.model.IAcmeModel;
import org.acmestudio.basicmodel.core.AcmeFloatValue;
import org.acmestudio.basicmodel.core.AcmeIntValue;
import org.acmestudio.basicmodel.core.AcmePropertyValue;
import org.acmestudio.basicmodel.element.property.AcmeProperty;
import org.apache.commons.lang.StringUtils;
import org.sa.rainbow.scenario.model.RainbowModelWithScenarios;

import ar.uba.dc.thesis.atam.Artifact;
import ar.uba.dc.thesis.atam.Environment.HeuristicType;
import ar.uba.dc.thesis.rainbow.constraint.operator.NumericBinaryOperator;

public class NumericBinaryRelationalConstraint extends BaseSinglePropertyInvolvedConstraint {

	private static final String ARTIFACT_INSTANCE_NAME_TOKEN = "${instanceName}";

	private static final String SPACE = " ";

	private final NumericBinaryOperator binaryOperator;

	private final long value;

	private final String propertyInstancePath;

	public NumericBinaryRelationalConstraint(Artifact artifact, String property, NumericBinaryOperator binaryOperator,
			long value) {
		super(artifact, property);
		this.binaryOperator = binaryOperator;
		this.value = value;
		this.propertyInstancePath = artifact.getSystemName() + "." + ARTIFACT_INSTANCE_NAME_TOKEN + "." + property;
		this.validate();
	}

	public NumericBinaryOperator getBinaryOperator() {
		return binaryOperator;
	}

	public long getValue() {
		return value;
	}

	public boolean holds(IAcmeModel acmeModel, String involvedArtifactName) {
		Number propertyValue = this.getPropertyValueFrom(acmeModel, involvedArtifactName);

		return this.getBinaryOperator().performOperation(propertyValue, this.getValue());
	}

	public boolean holds(IAcmeModel acmeModel, HeuristicType heuristicType) {
		boolean result = true;

		// collect all instances of artifact type
		Set<String> instanceQualifiedProps = RainbowModelWithScenarios.collectInstanceProps(getArtifact()
				.getSystemName(), getArtifact().getName(), getFullyQualifiedPropertyName(), acmeModel);
		List<Boolean> history = new ArrayList<Boolean>();
		for (String instanceQualifiedProp : instanceQualifiedProps) {
			Number propertyValue = this.getPropertyValueFrom(acmeModel, instanceQualifiedProp);
			history.add(this.getBinaryOperator().performOperation(propertyValue, this.getValue()));
		}
		boolean currentConstraintsEvaluation = true;// FIXME que significa currentConstraintsEvaluation?
		result = heuristicType.run(currentConstraintsEvaluation, history);
		return result;
	}

	private Number getPropertyValueFrom(IAcmeModel acmeModel, String involvedArtifactName) {
		String lookupProperty = propertyInstancePath.replace(ARTIFACT_INSTANCE_NAME_TOKEN, involvedArtifactName);
		AcmeProperty property = this.findAcmePropertyInAcme(acmeModel, lookupProperty);

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