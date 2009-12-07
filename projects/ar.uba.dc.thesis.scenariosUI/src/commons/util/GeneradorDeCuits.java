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
 * $Id: GeneradorDeCuits.java,v 1.6 2008/04/24 20:04:04 cvschioc Exp $
 */
package commons.util;

import java.util.Calendar;

import org.eclipse.jface.dialogs.MessageDialog;

import sba.common.exception.ValidationException;
import sba.common.validation.AfipValidator;

import commons.gui.util.TextJfaceUtils;

/**
 * @author Gabriel Tursi
 * @version $Revision: 1.6 $ $Date: 2008/04/24 20:04:04 $
 */
public abstract class GeneradorDeCuits {

	public static String generarCuitValido() {

		String prefijo = "30";
		String time = String.valueOf(Calendar.getInstance().getTimeInMillis());
		String documento = time.substring(time.length() - 9, time.length() - 1);
		String digitoVerificador = "";
		String cuit = "";
		boolean cuitValido = false;
		int dig = 0;
		while (!cuitValido && dig < 10) {
			digitoVerificador = String.valueOf(dig);
			cuit = prefijo + documento + digitoVerificador;
			try {
				AfipValidator.isValidCuitCuil(Long.parseLong(cuit));
				cuitValido = true;
				TextJfaceUtils.copyToClipboard(cuit);
				GeneradorDeCuits.notificar(cuit);
			} catch (ValidationException e) {
				dig++;
			}
		}
		return cuit;
	}

	private static void notificar(String cuit) {
		MessageDialog.openInformation(null, "Cuit Generado", "El CUIT " + cuit
				+ " ha sido copiado al portapapeles");
	}
}