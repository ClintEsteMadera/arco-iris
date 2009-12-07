/*
 * Licencia de Caja de Valores S.A., Versi�n 1.0
 *
 * Copyright (c) 2006 Caja de Valores S.A.
 * 25 de Mayo 362, Ciudad Aut�noma de Buenos Aires, Rep�blica Argentina
 * Todos los derechos reservados.
 *
 * Este software es informaci�n confidencial y propietaria de Caja de Valores S.A. ("Informaci�n
 * Confidencial"). Usted no divulgar� tal Informaci�n Confidencial y la usar� solamente de acuerdo a
 * los t�rminos del acuerdo de licencia que posee con Caja de Valores S.A.
 */

/*
 * $Id: DomicilioValidator.java,v 1.14 2008/04/22 20:39:51 cvschioc Exp $
 */

package sba.common.validation;

import static sba.common.validation.CommonDataTypes.DOMICILIO_CALLE;
import static sba.common.validation.CommonDataTypes.DOMICILIO_COD_POSTAL;
import static sba.common.validation.CommonDataTypes.DOMICILIO_DEPTO;
import static sba.common.validation.CommonDataTypes.DOMICILIO_LOCALIDAD;
import static sba.common.validation.CommonDataTypes.DOMICILIO_NUMERO;
import static sba.common.validation.CommonDataTypes.DOMICILIO_PISO;
import static sba.common.validation.CommonDataTypes.getCommonTypes;

import java.io.Serializable;

import org.apache.commons.lang.StringUtils;

import sba.common.domicilio.Domicilio;
import sba.common.domicilio.DomicilioSimple;
import sba.common.pais.Country;

/**
 * Valida un objeto Domicilio.
 * 
 * @author Jonathan Chiocchio
 * @version $Revision: 1.14 $ $Date: 2008/04/22 20:39:51 $
 */

public abstract class DomicilioValidator implements Serializable {

	/**
	 * Verifica que se cumplan TODAS estas condiciones:
	 * <ul>
	 * <li> Calle � Obligatorio � 255 caracteres
	 * <li> Numero � Obligatorio � 20 caracteres alfanum�ricos
	 * <li> Piso � No Obligatorio � 20 caracteres alfanum�ricos
	 * <li> Departamento � No Obligatorio � 20 caracteres alfanum�ricos
	 * <li> Localidad � Obligatoria � 255 caracteres
	 * <li> C�digo Postal � Obligatorio � 50 caracteres alfanum�ricos
	 * <li> Provincia � Obligatoria � seleccionar de la lista
	 * <li> Observaciones � No Obligatorio � 1000 caracteres
	 * </ul>
	 * 
	 * @param domicilio
	 *            el domicilio a validar
	 * @param result
	 *            el objeto al cual agregar errores (en caso de haberlos)
	 * @param nacionalidadDomicilio
	 *            indica si el Domicilio es NACIONAL (el pa�s s�lo puede ser Argentina), NO_NACIONAL
	 *            (el pa�s NO puede ser Argentina) o SIN_RESTRICCIONES de pa�s
	 * @see NacionalidadDomicilio
	 */
	public static boolean isValid(Domicilio domicilio, ValidationResult result,
			NacionalidadDomicilio nacionalidadDomicilio) {
		// NOTA IMPORTANTE: NO cambiar el orden de evaluaci�n de los "&&"!!
		boolean isValid = ValidationUtils.isMandatory(domicilio.getCalle(), result,
				getCommonTypes(), DOMICILIO_CALLE, DOMICILIO, CALLE);

		isValid = ValidationUtils.isMandatory(domicilio.getNroCalle(), result, getCommonTypes(),
				DOMICILIO_NUMERO, DOMICILIO, NUMERO)
				& isValid;

		isValid = ValidationUtils.isNotMandatory(domicilio.getPiso(), result, getCommonTypes(),
				DOMICILIO_PISO, DOMICILIO, PISO)
				& isValid;

		isValid = ValidationUtils.isNotMandatory(domicilio.getDepto(), result, getCommonTypes(),
				DOMICILIO_DEPTO, DOMICILIO, DEPTO)
				& isValid;

		isValid = ValidationUtils.isMandatory(domicilio.getLocalidad(), result, getCommonTypes(),
				DOMICILIO_LOCALIDAD, DOMICILIO, LOCALIDAD)
				& isValid;

		isValid = ValidationUtils.isMandatory(domicilio.getCodigoPostal(), result,
				getCommonTypes(), DOMICILIO_COD_POSTAL, DOMICILIO, COD_POSTAL)
				& isValid;

		if (Country.AR.equals(domicilio.getPais())) {
			isValid = ValidationUtils.isNotNull(result, domicilio.getProvincia(), DOMICILIO,
					PROVINCIA)
					& isValid;
		}
		isValid = ValidationUtils.isNotNull(result, domicilio.getPais(), DOMICILIO, PAIS)
				& nacionalidadDomicilio.validate(domicilio.getPais(), result) && isValid;

		isValid = ValidationUtils.isNotMandatory(domicilio.getObservaciones(), result,
				CommonDataTypes.getCommonTypes(), CommonDataTypes.OBSERVACIONES,
				DOMICILIO, OBSERVACIONES)
				& isValid;

		return isValid;
	}

	/**
	 * Verifica que se cumplan TODAS estas condiciones:
	 * <ul>
	 * <li> Domicilio � Obligatorio � 500 caracteres
	 * <li> C�digo Postal � Obligatorio � 50 caracteres alfanum�ricos
	 * <li> Pa�s � Obligatorio
	 * </ul>
	 * 
	 * @param domicilio
	 *            el domicilio a validar
	 * @param result
	 *            el objeto al cual agregar errores (en caso de haberlos)
	 */
	public static boolean isValid(DomicilioSimple domicilio, ValidationResult result) {
		boolean isValid = ValidationUtils.isMandatory(domicilio.getDomicilio(), result,
				CommonDataTypes.getCommonTypes(), CommonDataTypes.DOMICILIO_SIMPLE, DOMICILIO,
				StringUtils.capitalize(DOMICILIO));

		isValid = ValidationUtils.isMandatory(domicilio.getCodigoPostal(), result, CommonDataTypes
				.getCommonTypes(), CommonDataTypes.DOMICILIO_COD_POSTAL, DOMICILIO, COD_POSTAL)
				&& isValid;

		boolean paisIsNotNull = ValidationUtils.isNotNull(result, domicilio.getPais(), DOMICILIO,
				PAIS)
				&& isValid;

		isValid = paisIsNotNull && isValid;

		if (paisIsNotNull) {
			isValid = ValidationUtils.isNotArgentina(domicilio.getPais(), result, DOMICILIO)
					&& isValid;
		}
		return isValid;
	}

	private static final String DOMICILIO = "domicilio";

	private static final String CALLE = "Calle";

	private static final String NUMERO = "N�mero";

	private static final String PISO = "Piso";

	private static final String DEPTO = "Departamento";

	private static final String LOCALIDAD = "Localidad";

	private static final String COD_POSTAL = "C�digo Postal";

	private static final String PROVINCIA = "Provincia";

	private static final String PAIS = "Pa�s";

	private static final String OBSERVACIONES = "Observaciones";

	/**
	 * Peque�o Enum que resuelve polim�rficamente la validaci�n sobre si el pa�s del Domicilio debe
	 * ser Argentina, no puede ser Argentina o no hay restricciones definidas para el pa�s del
	 * Domicilio.
	 * 
	 * @author Jonathan Chiocchio
	 */
	public enum NacionalidadDomicilio {
		NACIONAL {
			@Override
			public boolean validate(Country pais, ValidationResult result) {
				return ValidationUtils.isArgentina(pais, result, DOMICILIO.toString());
			}
		},
		NO_NACIONAL {
			@Override
			public boolean validate(Country pais, ValidationResult result) {
				return ValidationUtils.isNotArgentina(pais, result, DOMICILIO.toString());
			}
		},
		SIN_RESTRICCIONES {
			@Override
			public boolean validate(Country pais, ValidationResult result) {
				return true;
			}
		};

		public abstract boolean validate(Country pais, ValidationResult result);
	}
}