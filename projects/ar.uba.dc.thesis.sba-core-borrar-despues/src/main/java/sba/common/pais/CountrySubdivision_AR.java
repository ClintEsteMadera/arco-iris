package sba.common.pais;

/**
 * Subdivisiones del país "Argentina". También conocidas como "provincias".
 * <b>NOTA</b>: Se usa el estándar "ISO 3166-2".
 * @author H. Adrián Uribe
 */
public enum CountrySubdivision_AR implements CountrySubdivision {
	// TODO: Ante necesidad de i18n, habría que tomar las descripciones de un ResourceBundle.
	CABA("Ciudad Autónoma de Buenos Aires", "C"),
	BUENOS_AIRES("Buenos Aires", "B"),
	CATAMARCA("Catamarca", "K"),
	CORDOBA("Córdoba", "X"),
	CORRIENTES("Corrientes", "W"),
	CHACO("Chaco", "H"),
	CHUBUT("Chubut", "U"),
	ENTRE_RIOS("Entre Ríos", "E"),
	FORMOSA("Formosa", "P"),
	JUJUY("Jujuy", "Y"),
	LA_PAMPA("La Pampa", "L"),
	LA_RIOJA("La Rioja", "F"),
	MENDOZA("Mendoza", "M"),
	MISIONES("Misiones", "N"),
	NEUQUEN("Neuquén", "Q"),
	RIO_NEGRO("Río Negro", "R"),
	SALTA("Salta", "A"),
	SAN_JUAN("San Juan", "J"),
	SAN_LUIS("San Luis", "D"),
	SANTA_CRUZ("Santa Cruz", "Z"),
	SANTA_FE("Santa Fe", "S"),
	SANTIAGO_DEL_ESTERO("Santiago del Estero", "G"),
	TIERRA_DEL_FUEGO("Tierra del Fuego", "V"),
	TUCUMAN("Tucumán", "T");

	CountrySubdivision_AR(String description, String isoCode) {
		this.description = description;
		this.isoCode = isoCode;
	}
	
	@Override
	public String toString() {
		return description;
	}

	/**
	 * Provee el código ISO 3166-2 correspondiente
	 * @return
	 */
	public String getISOCode() {
		return isoCode;
	}

	public String descripcion() {
		return this.description;
	}
	
	private final String description;
	
	private String isoCode;
}
