package org.acegisecurity.providers.ldap.authenticator;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.acegisecurity.ldap.InitialDirContextFactory;

import commons.security.exception.AuthenticationException;

/**
 * Autenticador que parsea el c�digo de error del LDAP de Oracle y se encarga de arrojar una excepci�n espec�fica para
 * cada uno de los posibles errores.
 */
public class OracleIDBindAuthenticator extends BindAuthenticator {

	private static final Pattern oidErrorMsgPattern = Pattern
			.compile("^\\[LDAP: error code ([0-9]+) - .*:([0-9]{4}):.*");

	protected OracleIDBindAuthenticator(InitialDirContextFactory initialDirContextFactory) {
		super(initialDirContextFactory);
	}

	@Override
	protected void handleBindException(String userDn, String username, Throwable exception) {
		// Just debug log the exception
		super.handleBindException(userDn, username, exception);

		AuthenticationException authenticationException = getAuthenticationException(exception);
		throw authenticationException;
	}

	/**
	 * Attempts to parse the error code from the exception message returned by OID.
	 */
	private AuthenticationException getAuthenticationException(Throwable cause) {
		// Por defecto, se devuelve esta excepci�n, salvo que la causa contenga m�s detalle.
		AuthenticationException result = new AuthenticationException("El usuario o la contrase�a son inv�lidos.", cause);
		String msg = cause.getMessage();
		Matcher matcher = oidErrorMsgPattern.matcher(msg);

		if (matcher.matches()) {
			String code = matcher.group(2);
			try {
				OracleLdapExceptionCode oracleLdapExceptionCode = OracleLdapExceptionCode.valueOf("CODE_" + code);
				result = oracleLdapExceptionCode.getAuthenticationException(cause);
			} catch (IllegalArgumentException e) {
				// en este caso, el c�digo de error no est� contemplado en OracleLdapExceptionCode
			}
		}
		return result;
	}
}