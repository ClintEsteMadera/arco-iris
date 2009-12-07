package sba.common.pais;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * <b>NOTA</b>: Se usa el est�ndar "ISO 3166-1 alfa-2" (el usado por los dominios de Internet.)
 * @author H. Adri�n Uribe
 */
public enum Country {
	// TODO: Ante necesidad de i18n, habr�a que tomar las descripciones de un ResourceBundle.
	AF("Afganist�n"),
	AL("Albania"),
	DE("Alemania"),
	AD("Andorra"),
	AO("Angola"),
	AI("Anguilla"),
	AQ("Ant�rtida"),
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
	AZ("Azerbaiy�n"),
	BS("Bahamas"),
	BH("Bahr�in"),
	BD("Bangladesh"),
	BB("Barbados"),
	BE("B�lgica"),
	BZ("Belice"),
	BJ("Benin"),
	BM("Bermudas"),
	BT("Bhut�n"),
	BY("Bielorrusia"),
	BO("Bolivia"),
	BA("Bosnia-Herzegovina"),
	BW("Botsuana"),
	BR("Brasil"),
	BN("Brun�i"),
	BG("Bulgaria"),
	BF("Burkina Faso"),
	BI("Burundi"),
	CV("Cabo Verde"),
	KH("Camboya"),
	CM("Camer�n"),
	CA("Canad�"),
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
	AE("Emiratos �rabes Unidos"),
	ER("Eritrea"),
	SK("Eslovaquia"),
	SI("Eslovenia"),
	ES("Espa�a"),
	US("Estados Unidos"),
	EE("Estonia"),
	ET("Etiop�a"),
	PH("Filipinas"),
	FI("Finlandia"),
	FJ("Fiyi"),
	FR("Francia"),
	GA("Gab�n"),
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
	HT("Hait�"),
	HN("Honduras"),
	HK("Hong Kong"),
	HU("Hungr�a"),
	IN("India"),
	ID("Indonesia"),
	IR("Ir�n"),
	IQ("Iraq"),
	IE("Irlanda"),
	BV("Isla Bouvet"),
	IM("Isla de Man"),
	CX("Isla de Navidad"),
	IS("Islandia"),
	NF("Isla Norfolk"),
	AX("Islas �land"),
	KY("Islas Caim�n"),
	CC("Islas Cocos"),
	CK("Islas Cook"),
	FO("Islas Feroe"),
	GS("Islas Georgias del Sur y Sandwich del Sur"),
	HM("Islas Heard y McDonald"),
	FK("Islas Malvinas"),
	MP("Islas Marianas del Norte"),
	MH("Islas Marshall"),
	PN("Islas Pitcairn"),
	SB("Islas Salom�n"),
	TC("Islas Turcas y Caicos"),
	UM("Islas ultramarinas de Estados Unidos"),
	VG("Islas V�rgenes Brit�nicas"),
	VI("Islas V�rgenes de los Estados Unidos"),
	IL("Israel"),
	IT("Italia"),
	JM("Jamaica"),
	JP("Jap�n"),
	JE("Jersey"),
	JO("Jordania"),
	KZ("Kazajst�n"),
	KE("Kenia"),
	KG("Kirguist�n"),
	KI("Kiribati"),
	KW("Kuwait"),
	LA("Laos"),
	LS("Lesotho"),
	LV("Letonia"),
	LB("L�bano"),
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
	ML("Mal�"),
	MT("Malta"),
	MA("Marruecos"),
	MQ("Martinica"),
	MU("Mauricio"),
	MR("Mauritania"),
	YT("Mayotte"),
	MX("M�xico"),
	FM("Micronesia"),
	MD("Moldavia"),
	MC("M�naco"),
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
	NE("N�ger"),
	NU("Niue"),
	NO("Noruega"),
	NC("Nueva Caledonia"),
	NZ("Nueva Zelanda"),
	OM("Om�n"),
	NL("Pa�ses Bajos"),
	PK("Pakist�n"),
	PW("Palau"),
	PS("Palestina"),
	PA("Panam�"),
	PG("Pap�a Nueva Guinea"),
	PY("Paraguay"),
	PE("Per�"),
	PF("Polinesia Francesa"),
	PL("Polonia"),
	PT("Portugal"),
	PR("Puerto Rico"),
	QA("Qatar"),
	GB("Reino Unido"),
	CF("Rep�blica Centroafricana"),
	CZ("Rep�blica Checa"),
	CG("Rep�blica del Congo"),
	CD("Rep�blica Democr�tica del Congo"),
	DO("Rep�blica Dominicana"),
	RE("Reuni�n"),
	RW("Ruanda"),
	RO("Rumania"),
	RU("Rusia"),
	EH("Sahara Occidental"),
	AS("Samoa Americana"),
	WS("Samoa"),
	KN("San Crist�bal y Nevis"),
	SM("San Marino"),
	PM("San Pedro y Miquel�n"),
	SH("Santa Helena"),
	LC("Santa Luc�a"),
	ST("Santo Tom� y Pr�ncipe"),
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
	ZA("Sud�frica"),
	SD("Sud�n"),
	SE("Suecia"),
	CH("Suiza"),
	SR("Surinam"),
	SJ("Svalbard y Jan Mayen"),
	TH("Tailandia"),
	TW("Taiw�n"),
	TZ("Tanzania"),
	TJ("Tayikist�n"),
	IO("Territorio Brit�nico del Oc�ano �ndico"),
	TF("Territorios Australes Franceses"),
	TL("Timor Oriental"),
	TG("Togo"),
	TK("Tokelau"),
	TO("Tonga"),
	TT("Trinidad y Tobago"),
	TN("T�nez"),
	TM("Turkmenist�n"),
	TR("Turqu�a"),
	TV("Tuvalu"),
	UA("Ucrania"),
	UG("Uganda"),
	UY("Uruguay"),
	UZ("Uzbekist�n"),
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
				// NOTA: Evita la recarga si el pa�s no tiene subdivisiones.
				subdivisions = NO_SUBDIVISIONS;
			}
		}
		return subdivisions;
	}

	Country(String description) {
		this.description = description;
	}

	/**
	 * Carga din�micamente la clase Enum de la Subdivisi�n (en caso que exista).
	 * <p>
	 * El nombre de la clase se determina as�: SUBDIVISION_BASE_CLASS_NAME + name().
	 */
	@SuppressWarnings("unchecked")
	private Enum[] loadSubdivisions() {
		try {
			Class enumClass = Class.forName(SUBDIVISION_BASE_CLASS_NAME + name());
			Method valuesMethod = enumClass.getMethod("values", (Class[]) null);
			return (Enum[]) valuesMethod.invoke(null, (Object[]) null);
		} catch (ClassNotFoundException e) {
			// NOTA: Esto es normal, no est�n definidas las subdivisiones de todos los pa�ses.
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
