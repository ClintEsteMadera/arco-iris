package commons.validation;

import commons.validation.string.CharacterSet;

public class CommonDataTypes extends DataTypeDictionary {

	public static final String DOMICILIO_CALLE = "jface.gui.domicilio.calle";

	public static final String DOMICILIO_NUMERO = "jface.gui.domicilio.numero";

	public static final String DOMICILIO_PISO = "jface.gui.domicilio.piso";

	public static final String DOMICILIO_DEPTO = "jface.gui.domicilio.depto";

	public static final String DOMICILIO_LOCALIDAD = "jface.gui.domicilio.localidad";

	public static final String DOMICILIO_COD_POSTAL = "jface.gui.domicilio.codPostal";

	public static final String DOMICILIO_SIMPLE = "jface.gui.domiciliosimple.domicilio";

	public static final String PJ_RPC = "jface.gui.persona.rpc";

	public static final String PJ_NUM = "jface.gui.persona.numero";

	public static final String CONTACTO_DENOMINACION = "jface.gui.contacto.denominacion";

	public static final String CONTACTO_DESCRIPCION = "jface.gui.contacto.descripcion";

	public static final String OBSERVACIONES = "jface.gui.observaciones";

	public static final String DESCRIPCION = "jface.gui.descripcion";

	public static CommonDataTypes getCommonTypes() {
		return instance;
	}

	private CommonDataTypes() {
		super();
		// Domicilio
		this.putStringType(DOMICILIO_CALLE, new StringConstraints(DOMICILIO_CALLE_MAX_SIZE));

		this.putStringType(DOMICILIO_PISO, new StringConstraints(DOMICILIO_PISO_MAX_SIZE));

		this.putStringType(DOMICILIO_NUMERO, new StringConstraints(DOMICILIO_NUMERO_MAX_SIZE));

		this.putStringType(DOMICILIO_DEPTO, new StringConstraints(DOMICILIO_DEPTO_MAX_SIZE));

		this.putStringType(DOMICILIO_LOCALIDAD, new StringConstraints(DOMICILIO_LOCALIDAD_MAX_SIZE));

		this.putStringType(DOMICILIO_COD_POSTAL, new StringConstraints(DOMICILIO_COD_POSTAL_MAX_SIZE));

		this.putStringType(DOMICILIO_SIMPLE, new StringConstraints(DOMICILIO_SIMPLE_MAX_SIZE));

		// Persona
		this.putStringType(PJ_RPC, new StringConstraints(PJ_RPC_MIN_MAX_SIZE, PJ_RPC_MIN_MAX_SIZE,
				CharacterSet.ALPHA_NUM));

		this.putStringType(PJ_NUM, new StringConstraints(PJ_NUM_MIN_SIZE, PJ_NUM_MAX_SIZE, CharacterSet.ALPHA_NUM));

		// Contacto
		this.putStringType(CONTACTO_DENOMINACION, new StringConstraints(CONTACTO_DENOMINACION_MAX_SIZE));

		this.putStringType(CONTACTO_DESCRIPCION, new StringConstraints(CONTACTO_DESCRIPCION_MAX_SIZE));

		// Common
		this.putStringType(OBSERVACIONES, new StringConstraints(OBSERVACIONES_MAX_SIZE));

		this.putStringType(DESCRIPCION, new StringConstraints(DESCRIPCION_MAX_SIZE));
	}

	private static final CommonDataTypes instance = new CommonDataTypes();

	// Domicilio
	private static final int DOMICILIO_CALLE_MAX_SIZE = 255;

	private static final int DOMICILIO_LOCALIDAD_MAX_SIZE = 255;

	private static final int DOMICILIO_PISO_MAX_SIZE = 20;

	private static final int DOMICILIO_NUMERO_MAX_SIZE = 20;

	private static final int DOMICILIO_DEPTO_MAX_SIZE = 20;

	private static final int DOMICILIO_COD_POSTAL_MAX_SIZE = 50;

	private static final int DOMICILIO_SIMPLE_MAX_SIZE = 500;

	// Persona
	private static final int PJ_RPC_MIN_MAX_SIZE = 13;

	// Contacto
	private static final int CONTACTO_DENOMINACION_MAX_SIZE = 255;

	private static final int CONTACTO_DESCRIPCION_MAX_SIZE = 255;

	// Common
	private static final int DESCRIPCION_MAX_SIZE = 1000;

	private static final int OBSERVACIONES_MAX_SIZE = 2;

	private static final int PJ_NUM_MIN_SIZE = 0;

	private static final int PJ_NUM_MAX_SIZE = 50;
}