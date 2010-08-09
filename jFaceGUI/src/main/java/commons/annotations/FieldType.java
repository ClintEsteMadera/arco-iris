package commons.annotations;

public enum FieldType {
	ALF("Alfabetico"), AFN("Alfanumérico"), NUM("Numérico"), CAL("Calendar"), DEFAULT("Default");

	FieldType(String descripcion) {
		this.descripcion = descripcion;
	}

	public String getDescripcion() {
		return this.descripcion;
	}

	private String descripcion;

}
