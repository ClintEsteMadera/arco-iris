package commons.annotations;

public enum ControlType {
	TEXT("Text"), COMB("Combo"), CALE("Calendar"), BOOL("Booleano");

	ControlType(String descripcion) {
		this.descripcion = descripcion;
	}

	public String getDescripcion() {
		return this.descripcion;
	}

	private String descripcion;

}
