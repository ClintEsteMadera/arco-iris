package ar.uba.dc.thesis.qa;

public enum QualityAttribute {
	PERFORMANCE("Performance");

	private final String description;

	QualityAttribute(String description) {
		this.description = description;
	}

	@Override
	public String toString() {
		return this.description;
	}
}