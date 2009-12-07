package sba.common.pais;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * <b>NOTA</b>: Se usa el estándar "ISO 3166-1 alfa-2" (el usado por los dominios de Internet.)
 * @author H. Adrián Uribe
 */
public enum Country {
	// TODO: Ante necesidad de i18n, habría que tomar las descripciones de un ResourceBundle.
	AF("Afganistán"),
	AL("Albania"),
	DE("Alemania"),
	AD("Andorra"),
	AO("Angola"),
	AI("Anguilla"),
	AQ("Antártida"),
	AG("Antigua y Barbuda"),
	AN("Antillas Neerlandesas"),
	SA("Arabia Saudita"),
	DZ("Argelia"),
	AR("Argentina"),
	AM("Armenia"),
	AW("Aruba"),
	MK("ARY Macedonia"),
	AU("Australia"),
	AT("Austria"),
	AZ("Azerbaiyán"),
	BS("Bahamas"),
	BH("Bahréin"),
	BD("Bangladesh"),
	BB("Barbados"),
	BE("Bélgica"),
	BZ("Belice"),
	BJ("Benin"),
	BM("Bermudas"),
	BT("Bhután"),
	BY("Bielorrusia"),
	BO("Bolivia"),
	BA("Bosnia-Herzegovina"),
	BW("Botsuana"),
	BR("Brasil"),
	BN("Brunéi"),
	BG("Bulgaria"),
	BF("Burkina Faso"),
	BI("Burundi"),
	CV("Cabo Verde"),
	KH("Camboya"),
	CM("Camerún"),
	CA("Canadá"),
	TD("Chad"),
	CL("Chile"),
	CN("China"),
	CY("Chipre"),
	VA("Ciudad del Vaticano"),
	CO("Colombia"),
	KM("Comoras"),
	KP("Corea del Norte"),
	KR("Corea del Sur"),
	CI("Costa de Marfil"),
	CR("Costa Rica"),
	HR("Croacia"),
	CU("Cuba"),
	DK("Dinamarca"),
	DM("Dominica"),
	EC("Ecuador"),
	EG("Egipto"),
	SV("El Salvador"),
	AE("Emiratos Árabes Unidos"),
	ER("Eritrea"),
	SK("Eslovaquia"),
	SI("Eslovenia"),
	ES("España"),
	US("Estados Unidos"),
	EE("Estonia"),
	ET("Etiopía"),
	PH("Filipinas"),
	FI("Finlandia"),
	FJ("Fiyi"),
	FR("Francia"),
	GA("Gabón"),
	GM("Gambia"),
	GE("Georgia"),
	GH("Ghana"),
	GI("Gibraltar"),
	GD("Granada"),
	GR("Grecia"),
	GL("Groenlandia"),
	GP("Guadalupe"),
	GU("Guam"),
	GT("Guatemala"),
	GF("Guayana Francesa"),
	GG("Guernesey"),
	GW("Guinea-Bissau"),
	GQ("Guinea Ecuatorial"),
	GN("Guinea"),
	GY("Guyana"),
	HT("Haití"),
	HN("Honduras"),
	HK("Hong Kong"),
	HU("Hungría"),
	IN("India"),
	ID("Indonesia"),
	IR("Irán"),
	IQ("Iraq"),
	IE("Irlanda"),
	BV("Isla Bouvet"),
	IM("Isla de Man"),
	CX("Isla de Navidad"),
	IS("Islandia"),
	NF("Isla Norfolk"),
	AX("Islas Åland"),
	KY("Islas Caimán"),
	CC("Islas Cocos"),
	CK("Islas Cook"),
	FO("Islas Feroe"),
	GS("Islas Georgias del Sur y Sandwich del Sur"),
	HM("Islas Heard y McDonald"),
	FK("Islas Malvinas"),
	MP("Islas Marianas del Norte"),
	MH("Islas Marshall"),
	PN("Islas Pitcairn"),
	SB("Islas Salomón"),
	TC("Islas Turcas y Caicos"),
	UM("Islas ultramarinas de Estados Unidos"),
	VG("Islas Vírgenes Británicas"),
	VI("Islas Vírgenes de los Estados Unidos"),
	IL("Israel"),
	IT("Italia"),
	JM("Jamaica"),
	JP("Japón"),
	JE("Jersey"),
	JO("Jordania"),
	KZ("Kazajstán"),
	KE("Kenia"),
	KG("Kirguistán"),
	KI("Kiribati"),
	KW("Kuwait"),
	LA("Laos"),
	LS("Lesotho"),
	LV("Letonia"),
	LB("Líbano"),
	LR("Liberia"),
	LY("Libia"),
	LI("Liechtenstein"),
	LT("Lituania"),
	LU("Luxemburgo"),
	MO("Macao"),
	MG("Madagascar"),
	MY("Malasia"),
	MW("Malawi"),
	MV("Maldivas"),
	ML("Malí"),
	MT("Malta"),
	MA("Marruecos"),
	MQ("Martinica"),
	MU("Mauricio"),
	MR("Mauritania"),
	YT("Mayotte"),
	MX("México"),
	FM("Micronesia"),
	MD("Moldavia"),
	MC("Mónaco"),
	MN("Mongolia"),
	ME("Montenegro"),
	MS("Montserrat"),
	MZ("Mozambique"),
	MM("Myanmar"),
	NA("Namibia"),
	NR("Nauru"),
	NP("Nepal"),
	NI("Nicaragua"),
	NG("Nigeria"),
	NE("Níger"),
	NU("Niue"),
	NO("Noruega"),
	NC("Nueva Caledonia"),
	NZ("Nueva Zelanda"),
	OM("Omán"),
	NL("Países Bajos"),
	PK("Pakistán"),
	PW("Palau"),
	PS("Palestina"),
	PA("Panamá"),
	PG("Papúa Nueva Guinea"),
	PY("Paraguay"),
	PE("Perú"),
	PF("Polinesia Francesa"),
	PL("Polonia"),
	PT("Portugal"),
	PR("Puerto Rico"),
	QA("Qatar"),
	GB("Reino Unido"),
	CF("República Centroafricana"),
	CZ("República Checa"),
	CG("República del Congo"),
	CD("República Democrática del Congo"),
	DO("República Dominicana"),
	RE("Reunión"),
	RW("Ruanda"),
	RO("Rumania"),
	RU("Rusia"),
	EH("Sahara Occidental"),
	AS("Samoa Americana"),
	WS("Samoa"),
	KN("San Cristóbal y Nevis"),
	SM("San Marino"),
	PM("San Pedro y Miquelón"),
	SH("Santa Helena"),
	LC("Santa Lucía"),
	ST("Santo Tomé y Príncipe"),
	VC("San Vicente y las Granadinas"),
	SN("Senegal"),
	RS("Serbia"),
	SC("Seychelles"),
	SL("Sierra Leona"),
	SG("Singapur"),
	SY("Siria"),
	SO("Somalia"),
	LK("Sri Lanka"),
	SZ("Suazilandia"),
	ZA("Sudáfrica"),
	SD("Sudán"),
	SE("Suecia"),
	CH("Suiza"),
	SR("Surinam"),
	SJ("Svalbard y Jan Mayen"),
	TH("Tailandia"),
	TW("Taiwán"),
	TZ("Tanzania"),
	TJ("Tayikistán"),
	IO("Territorio Británico del Océano Índico"),
	TF("Territorios Australes Franceses"),
	TL("Timor Oriental"),
	TG("Togo"),
	TK("Tokelau"),
	TO("Tonga"),
	TT("Trinidad y Tobago"),
	TN("Túnez"),
	TM("Turkmenistán"),
	TR("Turquía"),
	TV("Tuvalu"),
	UA("Ucrania"),
	UG("Uganda"),
	UY("Uruguay"),
	UZ("Uzbekistán"),
	VU("Vanuatu"),
	VE("Venezuela"),
	VN("Vietnam"),
	WF("Wallis y Futuna"),
	YE("Yemen"),
	DJ("Yibuti"),
	ZM("Zambia"),
	ZW("Zimbabue");

	@Override
	public String toString() {
		return description;
	}

	public synchronized Enum[] getSubdivisions() {
		if (subdivisions == null) {
			subdivisions = loadSubdivisions();
			if (subdivisions == null) {
				// NOTA: Evita la recarga si el país no tiene subdivisiones.
				subdivisions = NO_SUBDIVISIONS;
			}
		}
		return subdivisions;
	}

	Country(String description) {
		this.description = description;
	}

	/**
	 * Carga dinámicamente la clase Enum de la Subdivisión (en caso que exista).
	 * <p>
	 * El nombre de la clase se determina así: SUBDIVISION_BASE_CLASS_NAME + name().
	 */
	@SuppressWarnings("unchecked")
	private Enum[] loadSubdivisions() {
		try {
			Class enumClass = Class.forName(SUBDIVISION_BASE_CLASS_NAME + name());
			Method valuesMethod = enumClass.getMethod("values", (Class[]) null);
			return (Enum[]) valuesMethod.invoke(null, (Object[]) null);
		} catch (ClassNotFoundException e) {
			// NOTA: Esto es normal, no están definidas las subdivisiones de todos los países.
			return null;
		} catch (NoSuchMethodException exc) {
			throw (InternalError) new InternalError().initCause(exc);
		} catch (IllegalAccessException exc) {
			throw (InternalError) new InternalError().initCause(exc);
		} catch (InvocationTargetException exc) {
			throw (InternalError) new InternalError().initCause(exc.getCause());
		}
	}

	private final String description;

	private Enum[] subdivisions;

	private static final Enum[] NO_SUBDIVISIONS = {};

	private static final String SUBDIVISION_BASE_CLASS_NAME = Country.class.getPackage().getName()
			+ ".CountrySubdivision_";
}
