package ar.uba.dc.thesis.qa;

public enum QualityAttribute {

	PERFORMANCE("Performance"),
	COST("Cost"),
	USABILITY("Usability");

	QualityAttribute(String description) {
		this.description = description;
	}

	@Override
	public String toString() {
		return this.description;
	}

	private final String description;

}