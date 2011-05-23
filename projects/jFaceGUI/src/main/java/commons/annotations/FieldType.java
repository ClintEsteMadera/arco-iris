package commons.annotations;

public enum FieldType {
	ALF("Alphabetic"), AFN("Alphanumeric"), NUM("Numeric"), CAL("Calendar"), DEFAULT("Default");

	FieldType(String description) {
		this.description = description;
	}

	public String getDescription() {
		return this.description;
	}

	private String description;

}
