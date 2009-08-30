package ar.uba.dc.thesis.atam;

public class ResponseMeasure {

	private String description;

	private String invariant;

	public ResponseMeasure(String description, String invariant) {
		super();
		this.description = description;
		this.invariant = invariant;
	}

	public String getDescription() {
		return this.description;
	}

	public String getInvariant() {
		return this.invariant;
	}
}