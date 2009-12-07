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
 * $Id: Validations.java,v 1.4 2007/12/21 20:16:38 cvschioc Exp $
 */
package commons.gui.validators;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import sba.common.exception.ValidationException;
import sba.common.validation.EMailValidator;

/**
 *
 * @author Gabriel Tursi
 * @version $Revision: 1.4 $ $Date: 2007/12/21 20:16:38 $
 */
public abstract class Validations {

    public static List<String> email(String email) {
    	List<String> result = new ArrayList<String>();
		try {
			EMailValidator.validateEmailValido(email);
		} catch (ValidationException ex) {
			result = Arrays.asList(ex.getValidationMessages());
		}
		return result;
	}

    public static List<String> rango(String texto, int infimo, int supremo) {
    	List<String> result = new ArrayList<String>();
    	if(!StringUtils.isEmpty(texto)) {
	    	int textoIngresado = Integer.parseInt(texto);
	    	if(textoIngresado < infimo || textoIngresado > supremo) {
	        	result.add("El texto ingresado no está entre " + infimo + " y " + supremo);
	    	}
    	}
    	return result;
    }
}