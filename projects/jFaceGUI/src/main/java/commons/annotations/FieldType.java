package commons.annotations;

public enum FieldType {
	ALF("Alfabetico"), AFN("Alfanum�rico"), NUM("Num�rico"), CAL("Calendar"), DEFAULT("Default");

	FieldType(String descripcion) {
		this.descripcion = descripcion;
	}

	public String getDescripcion() {
		return this.descripcion;
	}

	private String descripcion;

}
