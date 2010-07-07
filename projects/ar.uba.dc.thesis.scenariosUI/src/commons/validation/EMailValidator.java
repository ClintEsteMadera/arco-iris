package commons.validation;

import java.io.Serializable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import commons.exception.ValidationException;

/**
 * Validador para textos que representan direcciones de e-mail.
 * 
 * @author Jonathan Chiocchio
 * @version $Revision: 1.4 $ - $Date: 2008/03/17 14:51:52 $
 */
public abstract class EMailValidator implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * Valida si un texto es válido para denotar una dirección de e-mail.
	 * 
	 * @param email
	 *            texto a validar
	 */
	public static boolean validate(String email, ValidationResult result) {
		Matcher m = validateEmail(email);
		if (!m.matches()) {
			result.addError(CommonValidationMessages.INVALID_EMAIL, email);
			return false;
		}
		return true;
	}

	/**
	 * Valida si un texto es válido para denotar una dirección de e-mail.
	 * 
	 * @param email
	 *            texto a validar
	 */
	public static void validateEmailValido(String email) {
		Matcher m = validateEmail(email);

		// check whether match is found
		if (!m.matches()) {
			throw new ValidationException(new ValidationError(CommonValidationMessages.INVALID_EMAIL, email));
		}
	}

	public static boolean emailValido(String email) {
		Matcher m = validateEmail(email);
		// check whether match is found
		return m.matches();
	}

	private static Matcher validateEmail(String email) {
		// Normalize
		String normalizedEmail = email.toLowerCase();

		Pattern p = Pattern.compile(MAIL_COMPONENT + "@" + MAIL_COMPONENT);
		return p.matcher(normalizedEmail);
	}

	private final static String MAIL_COMPONENT = "[a-z0-9_%\\-]+(\\.[a-z0-9_%\\-]+)*";
}