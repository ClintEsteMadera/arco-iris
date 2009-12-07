/*
 * $Id: AfipValidator.java,v 1.2 2008/01/10 19:37:48 cvsmarco Exp $
 *
 * Copyright (c) 2005 Caja de Valores S.A.
 * 25 de Mayo 362, Buenos Aires, República Argentina.
 * Todos los derechos reservados.
 *
 * Este software es información confidencial y propietaria de Caja de Valores
 * S.A. ("Información Confidencial"). Usted no divulgará tal Información
 * Confidencial y solamente la usará conforme a los terminos del Acuerdo que Ud.
 * posee con Caja de Valores S.A.
 */
package sba.common.validation;

import java.io.Serializable;

import sba.common.properties.EnumProperty;

/**
 * Validador de identificadores de la AFIP.
 * @author Ignacio Manzano
 * @author Miguel Díaz
 * @author Jonathan Chiocchio
 * @version 3.0 (16 Diciembre 2007)
 */
public abstract class AfipValidator implements Serializable {

	/**
	 * Verifica si un número de CUIT es válido.
	 * @param sNumero
	 *            número de CUIT a verificar
	 * @param result
	 *            el resultado de la validación al cual agregar errores (en caso de haberlos)
	 * @return <code>true</code> si el número es un número válido de CUIT, o <code>false</code>
	 *         en caso contrario
	 */
	public static boolean isValidCuit(String sNumero, ValidationResult result) {
		boolean isValid;
		try {
			long numero = Long.parseLong(sNumero);
			isValid = isValidCuitCuil(numero, result, CommonValidationMessages.INVALID_CUIT);
		} catch (NumberFormatException e) {
			result.addError(CommonValidationMessages.INVALID_CUIT, sNumero);
			isValid = false;
		}
		return isValid;
	}

	/**
	 * Verifica si un número de CUIT es válido.
	 * @param numero
	 *            número de CUIT a verificar
	 * @param result
	 *            el resultado de la validación al cual agregar errores (en caso de haberlos)
	 * @return <code>true</code> si el número es un número válido de CUIT, o <code>false</code>
	 *         en caso contrario
	 */
	public static boolean isValidCuit(long numero, ValidationResult result) {
		return isValidCuitCuil(numero, result, CommonValidationMessages.INVALID_CUIT);
	}

	/**
	 * Verifica si un número de CUIL es válido.
	 * @param sNumero
	 *            número de CUIL a verificar
	 * @param result
	 *            el resultado de la validación al cual agregar errores (en caso de haberlos)
	 * @return <code>true</code> si el número es un número válido de CUIL, o <code>false</code>
	 *         en caso contrario
	 */
	public static boolean isValidCuil(String sNumero, ValidationResult result) {
		boolean isValid;
		try {
			long numero = Long.parseLong(sNumero);
			isValid = isValidCuitCuil(numero, result, CommonValidationMessages.INVALID_CUIL);
		} catch (NumberFormatException e) {
			result.addError(CommonValidationMessages.INVALID_CUIL, sNumero);
			isValid = false;
		}
		return isValid;
	}

	/**
	 * Verifica si un número de CUIL es válido.
	 * @param numero
	 *            número de CUIL a verificar
	 * @param result
	 *            el resultado de la validación al cual agregar errores (en caso de haberlos)
	 * @return <code>true</code> si el número es un número válido de CUIL, o <code>false</code>
	 *         en caso contrario
	 */
	public static boolean isValidCuil(long numero, ValidationResult result) {
		return isValidCuitCuil(numero, result, CommonValidationMessages.INVALID_CUIL);
	}

	/**
	 * Verifica si un número de CUIT / CUIL es válido.
	 * @param numero
	 *            número de CUIT / CUIL a verificar
	 * @return <code>true</code> si el número es un número válido de CUIT / CUIL, o
	 *         <code>false</code> en caso contrario
	 */
	public static boolean isValidCuitCuil(long numero) {
		int identificador = getIdentificador(numero);
		int doc = getDocumento(numero);
		return isValidIdentificador(identificador) && isValidDocumento(doc)	&& isValidDigito(numero);
	}
	
	private static boolean isValidCuitCuil(long numero, ValidationResult result,
			EnumProperty valueDescription) {
		boolean isValid = isValidCuitCuil(numero);
		if (!isValid) {
			result.addError(valueDescription, String.valueOf(numero));
		}
		return isValid;
	}

	private static boolean isValidIdentificador(int id) {
		int DOS = 2;
		int TRES = 3;
		int n = (int) (id / DIEZ);
		return ((n == DOS) || (n == TRES));
	}

	private static boolean isValidDocumento(int dc) {
		return ((dc > CERO) && (dc < MILMILLONES));
	}

	private static boolean isValidDigito(long numero) {
		final int ONCE = 11;
		int resto = (sumaDigito(numero, ONCE)) % ONCE;
		return (resto == CERO);
	}

	private static int sumaDigito(long nm, int n) {
		int s = 0;
		int[] valor = { 5, 4, 3, 2, 7, 6, 5, 4, 3, 2, 1 };

		if (n > 0) {
			s = (int) (valor[n - 1] * (nm % DIEZ) + sumaDigito((nm / DIEZ), (n - 1)));
		}
		return s;
	}

	private static int getIdentificador(long numero) {
		return ((int) ((numero - (numero % MILMILLONES)) / MILMILLONES));
	}

	private static int getDocumento(long numero) {
		return ((int) ((numero % MILMILLONES) / DIEZ));
	}

	private static final long MILMILLONES = 1000000000L;

	private static final long DIEZ = 10L;

	private static final int CERO = 0;
}