package ar.uba.dc.thesis.qa;

public enum Concern {

	RESPONSE_TIME("Response time", QualityAttribute.PERFORMANCE);

	Concern(String description, QualityAttribute qualityAttribute) {
		this.description = description;
		this.qualityAttribute = qualityAttribute;
	}

	public QualityAttribute getQualityAttribute() {
		return this.qualityAttribute;
	}

	@Override
	public String toString() {
		return this.description;
	}

	private final String description;

	private final QualityAttribute qualityAttribute;
}