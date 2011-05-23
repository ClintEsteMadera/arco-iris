package org.acegisecurity.providers.ldap.authenticator;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.acegisecurity.ldap.InitialDirContextFactory;

import commons.security.exception.AuthenticationException;

/**
 * An authenticator that parses Oracle's LDAP error code and throws a specific exception according to the specific
 * problem found.
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
		// By default, this exception is returned, unless the cause contains more detail...
		AuthenticationException result = new AuthenticationException("Either username or password is invalid.", cause);
		String msg = cause.getMessage();
		Matcher matcher = oidErrorMsgPattern.matcher(msg);

		if (matcher.matches()) {
			String code = matcher.group(2);
			try {
				OracleLdapExceptionCode oracleLdapExceptionCode = OracleLdapExceptionCode.valueOf("CODE_" + code);
				result = oracleLdapExceptionCode.getAuthenticationException(cause);
			} catch (IllegalArgumentException e) {
				// error code is not defined in OracleLdapExceptionCode
			}
		}
		return result;
	}
}