/*
 * Licencia de Caja de Valores S.A., Versión 1.0
 *
 * Copyright (c) 2006 Caja de Valores S.A.
 * 25 de Mayo 362, Ciudad Autónoma de Buenos Aires, República Argentina
 * Todos los derechos reservados.
 *
 * Este software es información confidencial y propietaria de Caja de Valores S.A. ("Información
 * Confidencial"). Usted no divulgará tal Información Confidencial y la usará solamente de acuerdo a
 * los términos del acuerdo de licencia que posee con Caja de Valores S.A.
 */

/*
 * $Id: CommonDataTypes.java,v 1.9 2008/05/13 19:25:13 cvsmvera Exp $
 */

package sba.common.validation;

import sba.common.validation.string.CharacterSet;


/**
 * 
 * @author Pablo Pastorino
 * @version $Revision: 1.9 $ $Date: 2008/05/13 19:25:13 $
 */

public class CommonDataTypes extends DataTypeDictionary {

	public static final String DOMICILIO_CALLE = "commons.domicilio.calle";
	
	public static final String DOMICILIO_NUMERO = "commons.domicilio.numero";
	
	public static final String DOMICILIO_PISO = "commons.domicilio.piso";
	
	public static final String DOMICILIO_DEPTO = "commons.domicilio.depto";
	
	public static final String DOMICILIO_LOCALIDAD = "commons.domicilio.localidad";

	public static final String DOMICILIO_COD_POSTAL = "commons.domicilio.codPostal";
	
	public static final String DOMICILIO_SIMPLE = "commons.domiciliosimple.domicilio";

	public static final String PJ_RPC = "commons.persona.rpc";
	
	public static final String PJ_NUM = "commons.persona.numero";

	public static final String CONTACTO_DENOMINACION = "commons.contacto.denominacion";

	public static final String CONTACTO_DESCRIPCION = "commons.contacto.descripcion";

	public static final String OBSERVACIONES = "commons.observaciones";
	
	public static final String DESCRIPCION = "commons.descripcion";
	
	
	public static CommonDataTypes getCommonTypes(){
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
		
		this.putStringType(PJ_NUM, new StringConstraints(PJ_NUM_MIN_SIZE, PJ_NUM_MAX_SIZE,
				CharacterSet.ALPHA_NUM));
		
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