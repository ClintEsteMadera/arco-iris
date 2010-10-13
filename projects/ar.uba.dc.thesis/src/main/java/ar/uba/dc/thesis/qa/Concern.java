package ar.uba.dc.thesis.qa;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("concern")
public enum Concern {

	// TODO We can extract the Concerns to the XML in order to increase flexibility (low priority)

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