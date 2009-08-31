package ar.uba.dc.thesis.qa;

public enum QualityAttribute {

	PERFORMANCE("Performance");

	QualityAttribute(String description) {
		this.description = description;
	}

	@Override
	public String toString() {
		return this.description;
	}

	private final String description;

}