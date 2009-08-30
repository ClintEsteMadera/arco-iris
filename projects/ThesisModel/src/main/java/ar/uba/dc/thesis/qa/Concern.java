package ar.uba.dc.thesis.qa;

public enum Concern {

	RESPONSE_TIME(QualityAttribute.PERFORMANCE);

	Concern(QualityAttribute qualityAttribute) {
		this.qualityAttribute = qualityAttribute;
	}

	public QualityAttribute getQualityAttribute() {
		return this.qualityAttribute;
	}

	private QualityAttribute qualityAttribute;
}