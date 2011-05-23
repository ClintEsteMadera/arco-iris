package commons.annotations;

public enum ControlType {
	TEXT("Text"), COMB("Combo"), CALE("Calendar"), BOOL("Boolean");

	ControlType(String description) {
		this.description = description;
	}

	public String getDescription() {
		return this.description;
	}

	private String description;

}
