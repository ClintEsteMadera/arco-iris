package ar.uba.dc.thesis.qa;

public enum Concern {

	RESPONSE_TIME("Response time", QualityAttribute.PERFORMANCE, "uR"),

	SERVER_COST("Number of active servers", QualityAttribute.COST, "uC"),

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