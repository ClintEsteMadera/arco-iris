package sba.common.pais;

/**
 * Subdivisiones del pa�s "Argentina". Tambi�n conocidas como "provincias".
 * <b>NOTA</b>: Se usa el est�ndar "ISO 3166-2".
 * @author H. Adri�n Uribe
 */
public enum CountrySubdivision_AR implements CountrySubdivision {
	// TODO: Ante necesidad de i18n, habr�a que tomar las descripciones de un ResourceBundle.
	CABA("Ciudad Aut�noma de Buenos Aires", "C"),
	BUENOS_AIRES("Buenos Aires", "B"),
	CATAMARCA("Catamarca", "K"),
	CORDOBA("C�rdoba", "X"),
	CORRIENTES("Corrientes", "W"),
	CHACO("Chaco", "H"),
	CHUBUT("Chubut", "U"),
	ENTRE_RIOS("Entre R�os", "E"),
	FORMOSA("Formosa", "P"),
	JUJUY("Jujuy", "Y"),
	LA_PAMPA("La Pampa", "L"),
	LA_RIOJA("La Rioja", "F"),
	MENDOZA("Mendoza", "M"),
	MISIONES("Misiones", "N"),
	NEUQUEN("Neuqu�n", "Q"),
	RIO_NEGRO("R�o Negro", "R"),
	SALTA("Salta", "A"),
	SAN_JUAN("San Juan", "J"),
	SAN_LUIS("San Luis", "D"),
	SANTA_CRUZ("Santa Cruz", "Z"),
	SANTA_FE("Santa Fe", "S"),
	SANTIAGO_DEL_ESTERO("Santiago del Estero", "G"),
	TIERRA_DEL_FUEGO("Tierra del Fuego", "V"),
	TUCUMAN("Tucum�n", "T");

	CountrySubdivision_AR(String description, String isoCode) {
		this.description = description;
		this.isoCode = isoCode;
	}
	
	@Override
	public String toString() {
		return description;
	}

	/**
	 * Provee el c�digo ISO 3166-2 correspondiente
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
