package ar.uba.dc.thesis.qa;

public enum Concern {

	RESPONSE_TIME("Response time", QualityAttribute.PERFORMANCE),
	SERVER_LOAD("Server Load", QualityAttribute.PERFORMANCE),
	CONNECTION_BANDWIDTH("Connection Bandwidth", QualityAttribute.PERFORMANCE),
	
	NUMBER_OF_ACTIVE_SERVERS("Number of active servers", QualityAttribute.COST),
	
	CONTENT_FIDELITY("Content Fidelity", QualityAttribute.USABILITY);
	

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