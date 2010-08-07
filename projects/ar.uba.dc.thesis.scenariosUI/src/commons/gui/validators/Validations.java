package commons.gui.validators;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import commons.exception.ValidationException;
import commons.validation.EMailValidator;

/**
 * 
 * 
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
		if (!StringUtils.isEmpty(texto)) {
			int textoIngresado = Integer.parseInt(texto);
			if (textoIngresado < infimo || textoIngresado > supremo) {
				result.add("El texto ingresado no está entre " + infimo + " y " + supremo);
			}
		}
		return result;
	}
}