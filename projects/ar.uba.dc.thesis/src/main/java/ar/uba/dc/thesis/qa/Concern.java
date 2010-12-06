package ar.uba.dc.thesis.qa;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("concern")
public enum Concern {

	// TODO In order to move these concerns to SelfHealingConfiguration we may need to determine first how are we going
	// to deal with the problem of retrieving ALL of the instances when dealing with Environment's weights, and how we
	// would deal with updated and removed Concerns and their relationship with those weight maps.

	RESPONSE_TIME("Response time", QualityAttribute.PERFORMANCE, "uR"),

	SERVER_COST("Server Cost", QualityAttribute.COST, "uC"),

	CONTENT_FIDELITY("Content Fidelity", QualityAttribute.USABILITY, "uF");

	Concern(String description, QualityAttribute qualityAttribute, String rainbowName) {
		this.description = description;
		this.qualityAttribute = qualityAttribute;
		this.rainbowName = rainbowName;
	}

	public QualityAttribute getQualityAttribute() {
		return this.qualityAttribute;
	}

	public String getRainbowName() {
		return rainbowName;
	}

	@Override
	public String toString() {
		return this.description;
	}

	private final String description;

	private final QualityAttribute qualityAttribute;

	private final String rainbowName;

}